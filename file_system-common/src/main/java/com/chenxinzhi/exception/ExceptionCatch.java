package com.chenxinzhi.exception;


import com.chenxinzhi.common.enums.AppHttpCodeEnum;
import com.chenxinzhi.common.vos.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@ControllerAdvice  //控制器增强类
@Slf4j
public class ExceptionCatch {


    /**
     * 处理可控异常  自定义异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult<?> exception(CustomException e) {
        log.error("捕获到了一个自定义异常:{}", e.getAppHttpCodeEnum());
        return ResponseResult.errorResult(e.getAppHttpCodeEnum());
    }

    /**
     * 处理不可控异常
     *
     * @param e
     * @return
     */

    @ExceptionHandler({Exception.class,})
    @ResponseBody
    public ResponseResult<?> exception(Exception e) {
        e.printStackTrace();
        log.error("捕获到了一个其他的异常:{}", e.getMessage());

        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

}
