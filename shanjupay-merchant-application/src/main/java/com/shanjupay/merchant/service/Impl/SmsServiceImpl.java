package com.shanjupay.merchant.service.Impl;

import com.alibaba.fastjson.JSON;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.merchant.service.SmsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@org.springframework.stereotype.Service  // 供本地使用，所以不用dubbo
public class SmsServiceImpl implements SmsService {
    @Value("${sms.url}")
    private String url;

    @Value("${sms.effectiveTime}")
    private String effectiveTime;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public String sendMsg(String phone) throws BusinessException{
        //向验证码服务发送请求的地址
        String sms_url = url + "/generate?name=sms&effectiveTime=" + effectiveTime;

        //请求体
        Map<String, Object> body = new HashMap<>();
        body.put("mobile", phone);
        //请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        //指定Content-Type: application/json
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        //请求信息,传入body，header
        HttpEntity httpEntity = new HttpEntity(body, httpHeaders);
        //向url请求
        ResponseEntity<Map> exchange = null;

        Map bodyMap = null;
        try {
            exchange = restTemplate.exchange(sms_url, HttpMethod.POST, httpEntity, Map.class);
            log.info("请求验证码服务，得到响应:{}", JSON.toJSONString(exchange));
            bodyMap = exchange.getBody();
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_100100);
            //throw new RuntimeException("发送验证码失败");
        }
        if (bodyMap == null || bodyMap.get("result") == null) {
            throw new BusinessException(CommonErrorCode.E_100100);
            //throw new RuntimeException("发送验证码失败");
        }

        Map result = (Map) bodyMap.get("result");
        String key = (String) result.get("key");
        log.info("得到发送验证码对应的key:{}", key);
        return key;
    }

    @Override
    public void checkVerifiyCode(String verifiyKey, String verifiyCode) throws BusinessException {
        // 实现检验验证码的逻辑
        String sms_url = url + "/verify?name=sms&verificationCode=" + verifiyCode + "&verificationKey=" + verifiyKey;
        Map responseMap = null;
        try {
            // 检验验证码不需要请求头和请求体
            ResponseEntity<Map> exchange = restTemplate.exchange(sms_url, HttpMethod.POST, HttpEntity.EMPTY, Map.class);
            responseMap = exchange.getBody();
            log.info("校验验证码，响应内容：{}", JSON.toJSONString(responseMap));
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage(), e);
            throw  new BusinessException(CommonErrorCode.E_100102);

            //throw new RuntimeException("验证码错误",e);
        }

        if (responseMap == null || responseMap.get("result") == null || !(Boolean) responseMap.get("result") ){
            throw  new BusinessException(CommonErrorCode.E_100102);
            //throw new RuntimeException("验证码错误");
        }

    }
}
