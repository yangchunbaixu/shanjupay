package com.shanjupay.merchant.convert;

import com.shanjupay.merchant.api.dto.AppDTO;
import com.shanjupay.merchant.entity.App;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AppConvert {
    AppConvert INSTANCE = Mappers.getMapper(AppConvert.class);

    AppDTO entity2dto (App entity);

    App dto2entity(AppDTO appDTO);

    List<AppDTO> listEntity2dto(List<App> app);
}
