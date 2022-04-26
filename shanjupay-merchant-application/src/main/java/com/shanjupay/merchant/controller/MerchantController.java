package com.shanjupay.merchant.controller;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.PhoneUtil;
import com.shanjupay.common.util.StringUtil;
import com.shanjupay.merchant.VO.MerchantDetailVO;
import com.shanjupay.merchant.VO.MerchantRegisterVO;
import com.shanjupay.merchant.api.MerchantService;
import com.shanjupay.merchant.api.dto.AppDTO;
import com.shanjupay.merchant.api.dto.MerchantDTO;
import com.shanjupay.merchant.common.utils.SecurityUtil;
import com.shanjupay.merchant.convert.MerchantDetailConvert;
import com.shanjupay.merchant.convert.MerchantRegisterConvert;
import com.shanjupay.merchant.service.FileService;
import com.shanjupay.merchant.service.SmsService;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
@Api(description = "商户平台应用的接口", tags = "商户平台应用的接口")
@Log4j2
public class MerchantController {

    @Reference  // 注入远程dubbo服务里面的Bean
    MerchantService merchantService;   // 把接口生成代理对象

    @Autowired  // 注入本地Bean
    SmsService smsService;

    @Autowired
    private FileService fileService;

    @GetMapping("/merchants/{id}")
    @ApiOperation(value = "根据id查询商户", tags = "根据id查询商户")
    public MerchantDTO queryMerchantById(@PathVariable("id") Long id) {
        return merchantService.queryMerchantById(id);
    }

    @ApiOperation(value = "获取手机验证码")
    @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String", paramType = "query")
    @GetMapping("/sms")
    public String getSMSCode(@RequestParam("phone") String phone) {
        log.info("向手机号:{}发送验证码", phone);
        return smsService.sendMsg(phone);
    }
    /**
     *    value : 中文名，随便定
     *    name : 参数的形参名称
     *    required : 是否必填
     *    dataType : 数据类型
     */
    @ApiOperation(value = "商户注册")
    @ApiImplicitParam(value = "商户注册信息", name = "merchantRegisterVO", required = true, dataType = "merchantRegisterVO")
    @PostMapping("/merchant/register")
    public MerchantRegisterVO RegisterMerchant(@RequestBody MerchantRegisterVO merchantRegisterVO) {
        // 检验商户手机号
        // 实现思路：
        // 检验商户手机号的唯一性，根据商户的手机号查询商户表，如果存在记录则说明有手机号，不唯一则抛出异常
        if (merchantRegisterVO == null) {
            throw new BusinessException(CommonErrorCode.E_100108);
        }
        // 手机号不为空
        if (StringUtil.isBlank(merchantRegisterVO.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100112);
        }
        // 验证手机号格式
        if (!PhoneUtil.isMatches(merchantRegisterVO.getMobile())) {
            throw new BusinessException(CommonErrorCode.E_100109);
        }


        // 检验验证码
        smsService.checkVerifiyCode(merchantRegisterVO.getVerifiyKey(), merchantRegisterVO.getVerifiyCode());
        // 注册商户
        MerchantDTO merchantDTO = MerchantRegisterConvert.INSTANCE.vo2dto(merchantRegisterVO);
        merchantService.CreateMerchant(merchantDTO);
        return merchantRegisterVO;
    }

    // 上传证件照
    @ApiOperation(value = "上传证件照")
    @PostMapping("/upload")
    public String upload(@ApiParam(value = "证件照", required = true)
                         @RequestParam(value = "file") MultipartFile file) {

        //  原始文件名
        String originalFilename = file.getOriginalFilename();
        // 文件后缀
        if(originalFilename == null){
             throw new BusinessException(CommonErrorCode.E_100117);
        }
        String  suffix = originalFilename.substring(originalFilename.lastIndexOf(".") - 1);
        // 文件名称
        // 为保证不重名，则用UUID命名
        String fileName = UUID.randomUUID() + suffix;
        // 上传文件，返回文件下载url
        String fileUrl = null;
        try {
            fileService.upload(file.getBytes(), fileName);
        } catch (IOException e) {
            throw new BusinessException(CommonErrorCode.E_100106);
        } catch (MultipartException m) {
            throw new BusinessException(CommonErrorCode.E_100116);
        }
        return fileUrl;

    }


    @ApiOperation("资质申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "merchantInfo",
                    value = "商户认证资料",
                    required = true,
                    dataType = "MerchantDetailVO",  // 要与参数列表一样
                    paramType = "body")
    })
    @PostMapping("/my/merchant/save")
    public void saveMerchant(@RequestBody MerchantDetailVO merchantDetailVO){
        Long merchantId = SecurityUtil.getMerchantId();
        // 类转换要使用Detail的
        MerchantDTO merchantDTO = MerchantDetailConvert.INSTANCE.vo2dto(merchantDetailVO);

        merchantService.applyMerchant(merchantId,merchantDTO);
    }

}
