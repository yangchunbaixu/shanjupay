package com.shanjupay.merchant.convert;

import com.shanjupay.merchant.VO.MerchantDetailVO;
import com.shanjupay.merchant.api.dto.MerchantDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * MerchantDTO类转MerchantDetailVO
 * MerchantDetailVO类转MerchantDTO
 */
@Mapper
public interface MerchantDetailConvert {
    MerchantDetailConvert INSTANCE = Mappers.getMapper(MerchantDetailConvert.class);
    MerchantDTO vo2dto(MerchantDetailVO vo);
    MerchantDetailVO dto2vo (MerchantDTO dto);
}
