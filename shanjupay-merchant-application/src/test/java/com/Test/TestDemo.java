package com.Test;

import com.alibaba.fastjson.JSON;
import com.shanjupay.merchant.MerchantApplicationBootstrap;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.methods.HttpHead;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = MerchantApplicationBootstrap.class)
@RunWith(SpringRunner.class)
@Log4j2
public class TestDemo {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void Test1(){
        String url = "http://www.baidu.com/";
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        String body = forEntity.getBody();
        System.out.println(body);
    }
    @Test
    public void Test2(){
        String uri = "http://localhost:56085/sailing/generate?effectiveTime=600&name=sms";
        // 请求头
        Map<String,Object> body= new HashMap();
        body.put("mobile","12313");
        // 请求体
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        // 请求消息，传入body和head
        HttpEntity httpEntity = new HttpEntity(body,httpHeaders);
        ResponseEntity<Map> exchange = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, Map.class);
        log.info("请求验证码服务得到响应"+ JSON.toJSONString(exchange));
        Map body1 = exchange.getBody();
        System.out.println(body1);
    }
}
