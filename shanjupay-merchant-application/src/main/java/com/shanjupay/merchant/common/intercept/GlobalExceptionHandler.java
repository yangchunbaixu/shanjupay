package com.shanjupay.merchant.common.intercept;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.domain.ErrorCode;
import com.shanjupay.common.domain.RestErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
    全局异常处理器
 */
@ControllerAdvice // 与@Exceptionhandler 配合使用实现全局异常处理
public class GlobalExceptionHandler {
    // 打印异常信息
    private static final Logger LOGGER =  LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 捕获Exception异常
    @ExceptionHandler(value = Exception.class)
    @ResponseBody   // 转换成json格式给前端
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 把全部异常标记为500
    public RestErrorResponse processException(HttpServletResponse response ,
                                              HttpServletRequest request,
                                              Exception e){
        // 解析异常信息
        // 如果是系统自定义的异常，则直接取出errorCode和errorMessage
        // instanceof ：表示 e是BusinessException的类型
        if (e instanceof BusinessException){
            LOGGER.info(e.getMessage(),e);
            // 解析系统异常信息
            BusinessException businessException = (BusinessException)e;
            ErrorCode errorCode = businessException.getErrorCode();
            // 错误代码
            int  code = errorCode.getCode();
            // 错误信息
            String Desc = errorCode.getDesc();
            return new RestErrorResponse(String.valueOf(code),Desc);
        }

        // 系统抛出的未知异常
        LOGGER.error("系统异常:",e);

        // 不是系统定义的错误
        // 统一定义为9999999系统未知错误
        return new RestErrorResponse(String.valueOf(CommonErrorCode.UNKNOWN.getCode()),CommonErrorCode.UNKNOWN.getDesc());

    }
}
