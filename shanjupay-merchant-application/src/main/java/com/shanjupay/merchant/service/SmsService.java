package com.shanjupay.merchant.service;

import com.shanjupay.common.domain.BusinessException;

public interface SmsService {
    /**
     * 发送手机验证码
     * @return  验证码对应的key
     */
    String sendMsg(String phone) throws BusinessException;

    /**
     *  检验验证码，抛出异常则检验无效
     * @param verifiyKey  验证码的key
     * @param verifiyCode   验证码
     */
    void checkVerifiyCode(String verifiyKey,String verifiyCode) throws BusinessException;
}
