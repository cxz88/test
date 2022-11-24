package com.chenxinzhi.controller.v1;

import com.chenxinzhi.common.vos.ResponseResult;
import com.chenxinzhi.file.dto.FileCopyAndMoveDto;
import com.chenxinzhi.file.dto.FileDto;
import com.chenxinzhi.file.dto.FileUpdateDto;
import com.chenxinzhi.service.FileService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(tags = "文件操作控制器")
@RestController
@RequestMapping("filesApi")
public class FilesController {
    private final FileService fileService;

    public FilesController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("upload")
    @ApiOperation("文件上传")
    public ResponseResult<?> upload(@ApiParam(value = "文件二进制", required = true) @RequestParam(value = "file", required = false) MultipartFile file,
                                    @ApiParam(value = "文件父文件夹的id", required = true) @RequestParam(value = "parent_id", required = false) Long folderId) throws IOException {
        return fileService.upload(file, folderId);

    }

    @GetMapping
    @ApiOperation("查询对应目录下所有的文件以及文件夹")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "parentFolderId", value = "父级文件夹的id"),
                    @ApiImplicitParam(name = "pageSize", value = "每页的大小"),
                    @ApiImplicitParam(name = "page", value = "第几页")}
    )
    public ResponseResult<?> list(@RequestBody FileDto fileDto) {
        return fileService.list_(fileDto);
    }

    @PostMapping("add_folder")
    @ApiOperation("创建文件夹")
    public ResponseResult<?> add_folder(@ApiParam(value = "要创建的文件夹名称", required = true) String name,
                                        @ApiParam(value = "父级文件夹的id", required = true) Long parent_id) {
        return fileService.add_folder(name, parent_id);

    }
    @PutMapping
    @ApiOperation("重命名文件或者文件夹")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "id", value = "要修改的文件id"),
                    @ApiImplicitParam(name = "newName", value = "修改后的名称")}
    )
    public ResponseResult<?> update(@RequestBody FileUpdateDto updateDto){
        return fileService.update_(updateDto);
    }
    @PostMapping("copy")
    @ApiOperation("复制文件")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "id", value = "要复制的文件id"),
                    @ApiImplicitParam(name = "to", value = "复制后的父级文件夹id")}
    )
    public ResponseResult<?> copy(@RequestBody FileCopyAndMoveDto dto){
        return fileService.copy(dto);
    }
    @PostMapping("move")
    @ApiOperation("移动文件或者文件夹")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "id", value = "要移动的文件id"),
                    @ApiImplicitParam(name = "to", value = "移动后的父级文件夹id")}
    )
    public ResponseResult<?> move(@RequestBody FileCopyAndMoveDto dto){
        return fileService.move(dto);
    }
    @DeleteMapping("{id}")
    @ApiOperation("删除文件或者整个文件夹")
    @ApiImplicitParams(
            {@ApiImplicitParam(name = "id", value = "要删除的文件id")}
    )
    //此为逻辑删除，不是真的删除
    public ResponseResult<?> delete(@PathVariable Long id){
        return fileService.delete(id);
    }


}
