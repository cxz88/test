package com.chenxinzhi.file.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.chenxinzhi.annotation.IdEncrypt;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文件表
 * </p>
 *
 * @author chenxinzhi
 * @since 2022-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_file")
@ApiModel(value="File对象", description="文件表")
public class File_ implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件名称")
    private String fileName;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @IdEncrypt
    private Long id;

    @ApiModelProperty(value = "文件大小")
    private String fileSize;

    @ApiModelProperty(value = "文件md5")
    private String fileMd5;

    @ApiModelProperty(value = "是否被删除")
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDelete;

    @ApiModelProperty(value = "父级文件夹")
    private Long parentFolderId;

    @ApiModelProperty(value = "是否是文件")
    private Boolean isFile;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createdTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updatedTime;

    @ApiModelProperty(value = "文件类型")
    private String fileType;
    @ApiModelProperty(value = "文件url")
    private String url;


}
