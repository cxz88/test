package com.chenxinzhi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenxinzhi.common.vos.ResponseResult;
import com.chenxinzhi.file.dto.FileCopyAndMoveDto;
import com.chenxinzhi.file.dto.FileDto;
import com.chenxinzhi.file.dto.FileUpdateDto;
import com.chenxinzhi.file.pojo.File_;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService extends IService<File_> {



    ResponseResult<?> upload(MultipartFile file,Long id) throws IOException;

    ResponseResult<?> add_folder(String name, Long parent_id);

    ResponseResult<?> list_(FileDto fileDto);

    ResponseResult<?> update_(FileUpdateDto updateDto);

    ResponseResult<?> copy(FileCopyAndMoveDto dto);

    ResponseResult<?> move(FileCopyAndMoveDto dto);

    ResponseResult<?> delete(Long id);
}
