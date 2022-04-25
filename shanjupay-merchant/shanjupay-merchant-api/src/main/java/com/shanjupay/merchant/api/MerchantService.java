package com.shanjupay.merchant.api;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.merchant.api.dto.MerchantDTO;

public interface MerchantService {
    // 根据id查询用户
    public MerchantDTO queryMerchantById(Long id);
    // 注册商户服务接口，接收手机号，账号，密码  为了可扩展性，用merchantDto接收数据

    /**
     *
     * @param merchantDTO  商户注册相信
     * @return  注册成功的商户消息
     */
    public MerchantDTO CreateMerchant(MerchantDTO merchantDTO) throws BusinessException;

    /**
     * 资质申请的接口
     * @param merchantId  商户id
     * @param merchantDTO 资质申请的信息
     * @throws BusinessException
     */
    void applyMerchant(Long merchantId,MerchantDTO merchantDTO) throws BusinessException;
}
