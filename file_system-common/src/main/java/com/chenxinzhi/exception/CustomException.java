package com.chenxinzhi.exception;

import com.chenxinzhi.common.enums.AppHttpCodeEnum;
import lombok.Data;

@Data
public class CustomException extends RuntimeException {

    private final AppHttpCodeEnum appHttpCodeEnum;

    public CustomException(AppHttpCodeEnum appHttpCodeEnum){
        this.appHttpCodeEnum = appHttpCodeEnum;
    }


}
