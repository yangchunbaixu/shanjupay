package com.shanjupay.merchant.service.Impl;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.QiniuUtils;
import com.shanjupay.merchant.service.FileService;
import org.springframework.beans.factory.annotation.Value;
@org.springframework.stereotype.Service  // 供商户使用的接口
public class FileServiceImpl implements FileService {

    @Value("${oss.qinui.url}")
    private String qiniuUrl;
    @Value("${oss.qinui.accessKey}")
    private String accessKey;
    @Value("${oss.qinui.secretKey}")
    private String secretKey;
    @Value("${oss.qinui.bucket}")
    private String bucket;

    @Override
    public String upload(byte[] bytes, String fileName) throws BusinessException {


        // 调用common下的工具类
        try {
            QiniuUtils.upload2qiniu(accessKey,secretKey,bucket,bytes,fileName);

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_100106);
        }
        //返回文件名称
        return qiniuUrl+fileName;
    }
}
