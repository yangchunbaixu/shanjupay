package com.shanjupay.merchant.convert;


import com.shanjupay.merchant.api.dto.MerchantDTO;
import com.shanjupay.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 定义DTO和Entity之间的转换规则
 */
@Mapper   // 对象属性的映射
public interface MerchantConvert {
    // 对象实例的映射
    MerchantConvert INSTANCE  = Mappers.getMapper(MerchantConvert.class);
    // dto转entity
    MerchantDTO entity2dto(Merchant merchant);
    // entity转dto
    Merchant dto2entity(MerchantDTO merchantDTO);


}
