package com.chenxinzhi.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenxinzhi.common.enums.AppHttpCodeEnum;
import com.chenxinzhi.common.vos.ResponseResult;
import com.chenxinzhi.file.dto.FileCopyAndMoveDto;
import com.chenxinzhi.file.dto.FileDto;
import com.chenxinzhi.file.dto.FileUpdateDto;
import com.chenxinzhi.file.pojo.File_;
import com.chenxinzhi.file.service.FileStorageService;
import com.chenxinzhi.mapper.FileMapper;
import com.chenxinzhi.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileMapper, File_> implements FileService {
    private final FileStorageService fileStorageService;

    public FileServiceImpl(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Override
    public ResponseResult<?> upload(MultipartFile file, Long id) throws IOException {
        //判断是否上传了文件
        if (file == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NOT_DATA);
        }
        //判断是否传入了文件夹id
        if (id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NOT_FOlDER);
        }

        String originalFilename = file.getOriginalFilename();//获取文件的名称
        assert originalFilename != null;
        boolean isTrue = check_name(originalFilename);
        if (!isTrue) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NAME_LONG_ERROR);
        }
        //根据文件名称和文件夹去进行查询，如果存在该相同名称的文件则直接进行失败处理不进行上传
        File_ fileOne = getOne(new LambdaQueryWrapper<File_>().eq(File_::getParentFolderId, id).eq(File_::getFileName, originalFilename));
        if (fileOne != null) {
            //存在相同名称的文件直接报错
            return ResponseResult.errorResult(AppHttpCodeEnum.FILE_ALREADY_EXISTS);
        }
        String suffix = getSuffix(originalFilename);
        String fileName = UUID.randomUUID() + suffix;
        String url = fileStorageService.uploadFile("", fileName, file.getInputStream());
        //将文件的信息存入数据库
        File_ file_ = new File_() {{
            setFileName(originalFilename);
            setFileSize(file.getSize() + "字节");
            setIsFile(true);
            setUrl(url);
            setParentFolderId(id);
        }};
        this.save(file_);
        return ResponseResult.ok(AppHttpCodeEnum.FILE_UPLOAD_SUCCESS);
    }

    private static String getSuffix(String name) {
        String suffix = "";
        if (StringUtils.isNotEmpty(name)) {//进行文件名称非空判断
            try {
                suffix = name.substring(name.lastIndexOf("."));//获取文件的后缀
            } catch (Exception e) {
                suffix = "";
            }
        }
        return suffix;
    }

    @Override
    public ResponseResult<?> add_folder(String name, Long parent_id) {
        if (name == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NAME_ERROR);
        }
        if (parent_id == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NOT_FOlDER);

        }
        boolean isTrue = check_name(name);
        if (!isTrue) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NAME_LONG_ERROR);
        }
        //判断文件夹是否已经存在
        File_ one = this.getOne(new LambdaQueryWrapper<>(File_.class).eq(File_::getFileName, name));
        if (one != null) {
            //存在相同名称的文件直接报错
            return ResponseResult.errorResult(AppHttpCodeEnum.DIR_ALREADY_EXISTS);
        }
        File_ file_ = new File_();
        file_.setFileName(name);
        file_.setIsFile(false);
        file_.setParentFolderId(parent_id);
        this.save(file_);
        return ResponseResult.okResult(null);
    }

    private boolean check_name(String name) {
        //校验名称
        return name.length() <= 250;
    }

    @Override
    public ResponseResult<?> list_(FileDto fileDto) {
        fileDto.check();
        Long parentFolderId = fileDto.getParentFolderId();
        if (parentFolderId == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NOT_FOlDER);

        }
        Page<File_> file_pages = this.page(new Page<>(fileDto.getPage(), fileDto.getPageSize()),
                new LambdaQueryWrapper<File_>().eq(File_::getParentFolderId, parentFolderId));

        return ResponseResult.okResult(file_pages);
    }

    @Override
    public ResponseResult<?> update_(FileUpdateDto updateDto) {
        if (StringUtils.isNotEmpty(updateDto.getNewName())
                && updateDto.getId() != null && updateDto.getId() != 0) {
            LambdaUpdateWrapper<File_> lambdaUpdateWrapper = new LambdaUpdateWrapper<File_>().eq(File_::getId, updateDto.getId())
                    .set(File_::getFileName, updateDto.getNewName());
            update(lambdaUpdateWrapper);
            return ResponseResult.okResult(null);

        }
        return ResponseResult.errorResult(AppHttpCodeEnum.CHANGE_ERROR);
    }

    @Override
    public ResponseResult<?> copy(FileCopyAndMoveDto dto) {
        File_ file_ = verify_copyOrMove(dto);
        if (file_ == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.COPY_FILE_ERROR);
        }
        //查询目的路径是否以及有同名的文件
        File_ one = getOne(new LambdaQueryWrapper<>(File_.class).eq(File_::getParentFolderId, dto.getTo()).eq(File_::getFileName, file_.getFileName()));
        if (one != null) {
            //存在相同的文件，进行名称拼接
            if (file_.getFileName().length() > 250) {
                //文件名称太长不进行拼接直接报错
                return ResponseResult.errorResult(AppHttpCodeEnum.COPY_FILE_ERROR);
            }
            //或者文件后缀进行拼接
            String suffix = getSuffix(file_.getFileName());
            file_.setFileName(file_.getFileName().substring(0, file_.getFileName().indexOf(".")) + "的副本" + suffix);
        }
        //重新设置文件
        file_.setId(null);
        file_.setCreatedTime(null);
        file_.setUpdatedTime(null);
        file_.setParentFolderId(dto.getTo());
        save(file_);
        return ResponseResult.okResult(null);
    }

    @Override
    public ResponseResult<?> move(FileCopyAndMoveDto dto) {
        File_ file_ = verify_copyOrMove(dto);
        if (file_ == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.MOVE_FILE_ERROR);
        }
        update(new LambdaUpdateWrapper<>(File_.class).eq(File_::getId, dto.getId()).set(File_::getParentFolderId, dto.getTo()));
        return ResponseResult.okResult(null);
    }

    @Override
    public ResponseResult<?> delete(Long id) {
        if (id == null || id == 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DEL_ERROR);
        }
        boolean remove = remove(new LambdaQueryWrapper<>(File_.class).eq(File_::getId, id).or().eq(File_::getParentFolderId, id));//删除该文件或者文件夹，如果是文件夹则会删除所有的文件
        if (!remove) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DEL_ERROR);

        }
        return ResponseResult.okResult(null);
    }

    public File_ verify_copyOrMove(FileCopyAndMoveDto dto) {
        Long id = dto.getId();
        Long to = dto.getTo();
        if (id == null && to == null) {
            return null;
        }
        //查询需要复制的文件
        return getOne(new LambdaQueryWrapper<>(File_.class).eq(File_::getId, dto.getId()));
    }


}

