package com.shanjupay.transaction.api;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.transaction.api.dto.PayChannelDTO;
import com.shanjupay.transaction.api.dto.PayChannelParamDTO;
import com.shanjupay.transaction.api.dto.PlatformChannelDTO;

import java.util.List;

public interface PayChannelService {
    /**
     * 获取平台服务类型
     * @return 返回平台提供支付渠道
     * @throws BusinessException
     */
    List<PlatformChannelDTO> queryPlatformChannel()throws BusinessException;

    /**
     * 为app绑定平台服务类型
     * @param appId 应用id
     * @param platformChannelCodes 服务类型的code
     * @throws BusinessException
     */
    void bindPlatformChannelForApp(String appId, String platformChannelCodes)throws BusinessException;

    /**
     * 查询应用是否绑定了某个服务类型
     * @param appId
     * @param platformChannelCodes
     * @return  绑定返回1，反之0
     * @throws BusinessException
     */
    int queryAppBindPlatformChannel(String appId,String platformChannelCodes)throws BusinessException;

    /**
     * 根据平台服务类型获取支付渠道列表
     * @param platformChannelCode 服务类型编码
     * @return 支付渠道列表
     * @throws BusinessException
     */
    List<PayChannelDTO> queryPayChannelByPlatformChannel(String platformChannelCode)throws BusinessException;

    /**
     * 保持支付渠道参数
     * @param payChannelParamDTO 支付渠道参数: 包括： 商户id，应用id，服务类型code，支付渠道code，配置名称，配置参数(json数据)
     * @throws BusinessException
     */
    void savePayChannelParam(PayChannelParamDTO payChannelParamDTO)throws BusinessException;

    /**
     * 获取指定应用指定服务类型下所包含的原始支付渠道参数列表
     * @param appId 应用id
     * @param platformChannel 服务类型
     * @return  返回支付渠道配置的参数
     */
    List<PayChannelParamDTO> queryPayChannelParamByAppAndPlatform(String appId,String platformChannel) throws BusinessException;

    /**
     * 获取指定应用指定服务类型下所包含的某个原始支付参数
     * @param appId
     * @param platformChannel
     * @param payChannel
     * @return
     * @throws BusinessException
     */
    PayChannelParamDTO queryParamByAppPlatformAndPayChannel(String appId,String platformChannel,String payChannel) throws BusinessException;
}
