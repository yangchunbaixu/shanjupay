package com.shanjupay.merchant.convert;

import com.shanjupay.merchant.api.dto.AppDTO;
import com.shanjupay.merchant.entity.App;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-04-25T23:12:22+0800",
    comments = "version: 1.3.0.Final, compiler: javac, environment: Java 1.8.0_281 (Oracle Corporation)"
)
public class AppConvertImpl implements AppConvert {

    @Override
    public AppDTO entity2dto(App entity) {
        if ( entity == null ) {
            return null;
        }

        AppDTO appDTO = new AppDTO();

        appDTO.setId( entity.getId() );
        appDTO.setAppId( entity.getAppId() );
        appDTO.setAppName( entity.getAppName() );
        appDTO.setMerchantId( entity.getMerchantId() );
        appDTO.setPublicKey( entity.getPublicKey() );
        appDTO.setNotifyUrl( entity.getNotifyUrl() );

        return appDTO;
    }

    @Override
    public App dto2entity(AppDTO appDTO) {
        if ( appDTO == null ) {
            return null;
        }

        App app = new App();

        app.setId( appDTO.getId() );
        app.setAppId( appDTO.getAppId() );
        app.setAppName( appDTO.getAppName() );
        app.setMerchantId( appDTO.getMerchantId() );
        app.setPublicKey( appDTO.getPublicKey() );
        app.setNotifyUrl( appDTO.getNotifyUrl() );

        return app;
    }

    @Override
    public List<AppDTO> listEntity2dto(List<App> app) {
        if ( app == null ) {
            return null;
        }

        List<AppDTO> list = new ArrayList<AppDTO>( app.size() );
        for ( App app1 : app ) {
            list.add( entity2dto( app1 ) );
        }

        return list;
    }
}
