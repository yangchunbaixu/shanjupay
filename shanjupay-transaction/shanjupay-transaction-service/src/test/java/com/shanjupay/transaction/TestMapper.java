package com.shanjupay.transaction;

import com.shanjupay.transaction.api.PayChannelService;
import com.shanjupay.transaction.api.dto.PayChannelDTO;
import com.shanjupay.transaction.mapper.PlatformChannelMapper;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest(classes = TransactionBootstrap.class)
@RunWith(SpringRunner.class)
@Slf4j
public class TestMapper {

    @Autowired
    private PayChannelService payChannelService;

    //测试根据服务类型查询支付渠道
    @Test
    public void test1(){
        List<PayChannelDTO> shanju_c2b = payChannelService.queryPayChannelByPlatformChannel("shanju_c2b");
        System.out.println(shanju_c2b);
    }
    @Test
    public void test2(){
        //"{\"appID\": \"wxd2bf2dba2e86a8c7\",\"appSecret\": \"cec1a9185ad435abe1bced4b93f7ef2e\",\"key\":\"95fe355daca50f1ae82f0865c2ce87c8\",\"mchID\":\"1502570431\",\"payKey\":\"95fe355daca50f1ae82f0865c2ce87c8\"}"
    }
}
