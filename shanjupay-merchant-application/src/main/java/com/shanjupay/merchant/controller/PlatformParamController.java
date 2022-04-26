package com.shanjupay.merchant.controller;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.merchant.common.utils.SecurityUtil;
import com.shanjupay.transaction.api.PayChannelService;
import com.shanjupay.transaction.api.dto.PayChannelDTO;
import com.shanjupay.transaction.api.dto.PayChannelParamDTO;
import com.shanjupay.transaction.api.dto.PlatformChannelDTO;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@Api(value = "商户平台‐渠道和支付参数相关", tags = "商户平台‐渠道和支付参数", description = "商户平台‐渠道和支付参数相关")
@RestController
public class PlatformParamController {
    @Reference
    private PayChannelService payChannelService;

    /**
     * 获取平台服务类型
     *
     * @return
     */
    @ApiOperation(value = "获取商户平台服务类型")
    @GetMapping("/my/platform-channels")
    public List<PlatformChannelDTO> queryPlatformChannel() {
        return payChannelService.queryPlatformChannel();
    }

    @PostMapping(value = "/my/apps/{appId}/platform-channels")
    @ApiOperation("绑定服务类型")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "应用id", name = "appId", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(value = "服务类型code", name = "platformChannelCodes", required = true, dataType = "String", paramType = "query")
    })
    void bindPlatformForApp(@PathVariable("appId") String appId, @RequestParam("platformChannelCodes") String platformChannelCodes) {
        payChannelService.bindPlatformChannelForApp(appId, platformChannelCodes);
    }

    @ApiOperation(value = "查询应用是否绑定了某个服务类型")
    @GetMapping("/my/merchant/apps/platformChannels")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用AppId", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "platformChannelCodes", value = "服务类型", required = true, dataType = "String", paramType = "query")
    })
    public int queryAppBindPlatformChannel(@PathParam("appId") String appId, @PathParam("platformChannelCodes") String platformChannelCodes) {
        return payChannelService.queryAppBindPlatformChannel(appId, platformChannelCodes);
    }


    @ApiOperation(value = "根据平台服务类型获取支付渠道")
    @GetMapping("/my/pay‐channels/platform‐channel/{platformChannelCode}")
    @ApiImplicitParam(name = "platformChannelCode", value = "服务类型编码", required = true, dataType = "String", paramType = "query")
    public List<PayChannelDTO> queryPayChannelByPlatformChannel(@PathVariable("platformChannelCode") String platformChannelCode) {
        return payChannelService.queryPayChannelByPlatformChannel(platformChannelCode);
    }

    @ApiOperation(value = "商户配置支付渠道参数")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "payChannelParamDTO", value = "商户配置支付渠道参数",
                    required = true, dataType = "PayChannelParamDTO", paramType = "body")
    )
    @RequestMapping(value = "/my/pay‐channel‐params", method = {RequestMethod.POST, RequestMethod.PUT})
    public void createPayChannelParam(@RequestBody PayChannelParamDTO payChannelParamDTO) {
        Long merchantId = SecurityUtil.getMerchantId();
        payChannelParamDTO.setMerchantId(merchantId);
        payChannelService.savePayChannelParam(payChannelParamDTO);
    }

    @ApiOperation(value = "获取指定应用指定服务类型下所包含的原始支付渠道参数列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "platformChannel", value = "服务类型", required = true, dataType = "String", paramType = "path")
    })
    @GetMapping("/my/pay‐channel‐params/apps/{appId}/platform‐channels/{platformChannel}")
    public List<PayChannelParamDTO> queryPayChannelParam(@PathVariable("appId") String appId, @PathVariable("platformChannel") String platformChannel) throws BusinessException {
        return payChannelService.queryPayChannelParamByAppAndPlatform(appId, platformChannel);
    }

    @ApiOperation("获取指定应用指定服务类型下所包含的某个原始支付参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "platformChannel", value = "平台支付渠道编码", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "payChannel", value = "实际支付渠道编码", required = true, dataType = "String", paramType = "path")})
    @GetMapping(value = "/my/pay‐channel‐params/apps/{appId}/platform‐channels/{platformChannel}/pay‐channels/{payChannel}")
    public PayChannelParamDTO queryPayChannelParam(@PathVariable String appId, @PathVariable String platformChannel, @PathVariable String payChannel) {
        return payChannelService.queryParamByAppPlatformAndPayChannel(appId, platformChannel, payChannel);
    }

}
