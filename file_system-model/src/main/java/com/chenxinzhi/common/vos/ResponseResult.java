package com.chenxinzhi.common.vos;

import com.chenxinzhi.common.enums.AppHttpCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用的结果返回类
 *
 * @param <T>
 */
@Data
@ApiModel("响应数据")
public class ResponseResult<T> implements Serializable {
    @ApiModelProperty("响应状态码")
    private Integer code;
    @ApiModelProperty("响应消息")
    private String message;
    @ApiModelProperty("响应内容")
    private T data;

    public ResponseResult() {
        this.code = 200;
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public static <K> ResponseResult<K> errorResult(int code, String msg) {
        ResponseResult<K> result = new ResponseResult<>();
        return result.error(code, msg);
    }

    public static <K> ResponseResult<K> okResult(int code, String msg) {
        ResponseResult<K> result = new ResponseResult<>();
        return result.ok(code, null, msg);
    }

    public static <K> ResponseResult<K> okResult(K data) {
        ResponseResult<K> result = setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS, AppHttpCodeEnum.SUCCESS.getErrorMessage());
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    public static <K> ResponseResult<K> errorResult(AppHttpCodeEnum enums) {
        return setAppHttpCodeEnum(enums, enums.getErrorMessage());
    }

    public static <K> ResponseResult<K> errorResult(AppHttpCodeEnum enums, String errorMessage) {
        return setAppHttpCodeEnum(enums, errorMessage);
    }

    public static <K> ResponseResult<K> setAppHttpCodeEnum(AppHttpCodeEnum enums) {
        return okResult(enums.getCode(), enums.getErrorMessage());
    }

    private static <K> ResponseResult<K> setAppHttpCodeEnum(AppHttpCodeEnum enums, String errorMessage) {
        return okResult(enums.getCode(), errorMessage);
    }

    public ResponseResult<T> error(Integer code, String msg) {
        this.code = code;
        this.message = msg;
        return this;
    }

    public ResponseResult<T> ok(Integer code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }

    public ResponseResult<T> ok(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.message = msg;
        return this;
    }

    public ResponseResult<T> ok(T data) {
        this.data = data;
        return this;
    }
    public static <T>ResponseResult<T> ok(AppHttpCodeEnum enums){
        return okResult(enums.getCode(), enums.getErrorMessage());
    }


}
