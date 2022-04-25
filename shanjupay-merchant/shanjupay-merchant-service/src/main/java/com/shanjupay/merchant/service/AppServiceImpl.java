package com.shanjupay.merchant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.StringUtil;
import com.shanjupay.merchant.api.AppService;
import com.shanjupay.merchant.api.dto.AppDTO;
import com.shanjupay.merchant.convert.AppConvert;
import com.shanjupay.merchant.entity.App;
import com.shanjupay.merchant.entity.Merchant;
import com.shanjupay.merchant.mapper.AppMapper;
import com.shanjupay.merchant.mapper.MerchantMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * dubbo 服务接口，所以用dubbo的Service标记
 */
@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private MerchantMapper merchantMapper;

    /**
     * 创建应用
     *
     * @param merchantId 商户的id
     * @param appDTO     应用的信息
     * @return
     * @throws BusinessException
     */
    @Override
    public AppDTO createApp(Long merchantId, AppDTO appDTO) throws BusinessException {
        // 检验合法性
        if (merchantId == null || appDTO == null || StringUtil.isBlank(appDTO.getAppName())) {
            throw new BusinessException(CommonErrorCode.E_300009);
        }
        // 检验商户资质申请的状态码
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException(CommonErrorCode.E_200002);
        }
        // 取出商户资质申请的状态码
        String auditStatus = merchant.getAuditStatus();
        if (!"2".equals(auditStatus)) {
            throw new BusinessException(CommonErrorCode.E_200003);
        }

        // 检验应用的名称
        String appName = appDTO.getAppName();
        Boolean exitAppName = isExitAppName(appName);
        if (exitAppName){
            throw new BusinessException(CommonErrorCode.E_200004);
        }
        // 生成应用的id
        String appId = UUID.randomUUID().toString();

        App entity = AppConvert.INSTANCE.dto2entity(appDTO);
        entity.setAppId(appId);     // 应用id
        entity.setMerchantId(merchantId);  // 商户id
        // 调用appMapper向app表中添加数据
        appMapper.insert(entity);

        return AppConvert.INSTANCE.entity2dto(entity);
    }

    /**
     *
     * @param merchantId 商户id
     * @return
     * @throws BusinessException
     */
    @Override
    public List<AppDTO> queryAppByMerchant(Long merchantId) throws BusinessException {
        List<App> apps = appMapper.selectList(new QueryWrapper<App>
                ().lambda().eq(App::getMerchantId,merchantId));
        return AppConvert.INSTANCE.listEntity2dto(apps);
    }

    /**
     *
     * @param id 应用id
     * @return
     * @throws BusinessException
     */
    @Override
    public AppDTO getAppById(String id) throws BusinessException {
        App app = appMapper.selectOne(new LambdaQueryWrapper<App>().eq(App::getId, id));
        return AppConvert.INSTANCE.entity2dto(app);
    }

    /**
     * 检验应用名
     *
     * @param appName 应用名
     * @return 返回大于1则存在
     */
    private Boolean isExitAppName(String appName) {
        Integer count = appMapper.selectCount(new QueryWrapper<App>
                ().lambda().eq(App::getAppName, appName));

        return count > 0;
    }
}
