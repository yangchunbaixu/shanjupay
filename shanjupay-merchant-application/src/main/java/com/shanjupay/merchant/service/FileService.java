package com.shanjupay.merchant.service;

import com.shanjupay.common.domain.BusinessException;
import org.springframework.stereotype.Service;

public interface FileService {
    /**
     * 上传文件
     * @param bytes  文件的字节数组
     * @param fileName  文件名
     * @return  文件的访问路径名
     * @throws BusinessException
     */
    public String upload(byte[] bytes,String fileName)throws BusinessException;
}
