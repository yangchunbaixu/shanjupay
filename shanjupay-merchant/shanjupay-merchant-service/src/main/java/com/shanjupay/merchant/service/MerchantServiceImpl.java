package com.shanjupay.merchant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.PhoneUtil;
import com.shanjupay.common.util.StringUtil;
import com.shanjupay.merchant.api.MerchantService;
import com.shanjupay.merchant.api.dto.MerchantDTO;
import com.shanjupay.merchant.convert.MerchantConvert;
import com.shanjupay.merchant.entity.Merchant;
import com.shanjupay.merchant.mapper.MerchantMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service   // 必须写上dubbo的Service注解，才能暴露dubbo的服务发现
@Transactional
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private MerchantMapper merchantMapper;

    @Override
    public MerchantDTO queryMerchantById(Long id) {
        Merchant merchant = merchantMapper.selectById(id);
        MerchantDTO dto = new MerchantDTO();
        dto.setId(merchant.getId());
        dto.setMerchantName(merchant.getMerchantName());
        dto.setMerchantAddress(merchant.getMerchantAddress());

        return dto;
    }


    /**
     * @param merchantDTO  商户注册消息
     * @return
     */
    @Override
    public MerchantDTO CreateMerchant(MerchantDTO merchantDTO)throws BusinessException {
        // 校验参数的合法性
        // 参数不为空
        if (merchantDTO == null){
            throw new BusinessException(CommonErrorCode.E_100108);
        }
        // 手机号不为空
        if (StringUtil.isBlank(merchantDTO.getMobile())){
            throw new BusinessException(CommonErrorCode.E_100112);
        }
        // 验证手机号格式
        if (  !PhoneUtil.isMatches(merchantDTO.getMobile())){
            throw new BusinessException(CommonErrorCode.E_100109);
        }
        // 校验手机号的唯一性
        // 根据手机号查询商户表
        int count = merchantMapper.selectCount(new LambdaQueryWrapper<Merchant>().eq(Merchant::getMobile,merchantDTO.getMobile()));
        if (count>1){
            throw new BusinessException(CommonErrorCode.E_100113);
        }


        Merchant entity = MerchantConvert.INSTANCE.dto2entity(merchantDTO);

        // 设置审核状态0‐未申请,1‐已申请待审核,2‐审核通过,3‐审核拒绝
        entity.setAuditStatus("0");

        // 调用mapper向数据库写入信息
        merchantMapper.insert(entity);

        // 向dto中写入新商户的id
        MerchantDTO merchantDTONew = MerchantConvert.INSTANCE.entity2dto(entity);
        return merchantDTONew;
    }

    /**
     * 资质申请接口
     * @param merchantId  商户id
     * @param merchantDTO 资质申请的信息
     * @throws BusinessException
     */
    @Override
    public void applyMerchant(Long merchantId, MerchantDTO merchantDTO) throws BusinessException {
        if (merchantId == null || merchantDTO == null){
            throw new BusinessException(CommonErrorCode.E_300009);
        }
        // 校验 merchantId 的合法性，查询商户表，如果查不到记录，认定为非法
        Merchant merchant = merchantMapper.selectById(merchantId);

        if (merchant == null){
            throw new BusinessException(CommonErrorCode.E_200002);
        }
        //将dto转成entity
        Merchant entity = MerchantConvert.INSTANCE.dto2entity(merchantDTO);
        //将必要的参数设置到entity
        entity.setId(merchant.getId());
        entity.setMobile(merchant.getMobile());//因为资质申请的时候手机号不让改，还使用数据库中原来的手机号
        entity.setAuditStatus("1");//审核状态1-已申请待审核
        entity.setTenantId(merchant.getTenantId());
        // 调用mapper更新商户表
        merchantMapper.updateById(entity);
    }
}
