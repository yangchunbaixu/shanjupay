package com.shanjupay.common.domain;

/**
 * 自定义业务异常信息
 * 1.如果服务层抛出不可预知的异常，则统一定义为9999999
 * 2.当应用层接收到服务层抛出异常则继续向上抛出，应用层也可以抛出自定义异常类型和不可预告异常类型
 * 3.统一异常解析处理器捕获到异常继续解析
 * 4.捕获到之后响应给前端
 */
public class BusinessException extends  RuntimeException{
    // 错误代码
    private ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode){
        super();
        this.errorCode = errorCode;
    }
    public BusinessException(){
        super();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
