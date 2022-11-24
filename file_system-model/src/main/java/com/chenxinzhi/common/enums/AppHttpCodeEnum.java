package com.chenxinzhi.common.enums;

public enum AppHttpCodeEnum {

    // 成功段0
    SUCCESS(200,"操作成功"),
    DEL_ERROR(433,"删除失败"),
    FILE_UPLOAD_SUCCESS(200,"文件上传成功"),
    NOT_DATA(458,"你没有提交任何数据"), SERVER_ERROR(500, "服务器内部错误"),
    NOT_FOlDER(450,"没有传入文件夹"),
    FILE_NOT_FOUND(450,"该文件不存在"),
    COPY_FILE_ERROR(450,"文件复制出错"),
    MOVE_FILE_ERROR(450,"文件移动出错"),
    CHANGE_ERROR(450,"修改错误"),
    NAME_ERROR(457,"文件名称错误"),
    NAME_LONG_ERROR(457,"名称太长"),
    FILE_ALREADY_EXISTS(444,"该文件已经存在"),
    DIR_ALREADY_EXISTS(444,"该文件夹已经存在"),
    ;

    final int code;
    final String errorMessage;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
