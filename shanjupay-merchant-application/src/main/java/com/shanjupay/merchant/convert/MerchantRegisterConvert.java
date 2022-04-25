package com.shanjupay.merchant.convert;

import com.shanjupay.merchant.VO.MerchantRegisterVO;
import com.shanjupay.merchant.api.dto.MerchantDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * MerchantRegisterVO类转MerchantDTO
 * MerchantDTO类转MerchantRegisterVO
 */
@Mapper
public interface MerchantRegisterConvert {
    MerchantRegisterConvert INSTANCE = Mappers.getMapper(MerchantRegisterConvert.class);

    MerchantDTO vo2dto(MerchantRegisterVO merchantRegisterVO);

    MerchantRegisterVO dto2vo(MerchantDTO merchantDTO);
}
