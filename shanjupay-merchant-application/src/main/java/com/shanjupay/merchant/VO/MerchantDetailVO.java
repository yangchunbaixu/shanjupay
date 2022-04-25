package com.shanjupay.merchant.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@ApiModel(value = "MerchantDetailVO", description = "商户资质申请")
@Data
public class MerchantDetailVO {
    @ApiModelProperty("企业名称")
    private String merchantName;
    @ApiModelProperty("企业编号")
    private String merchantNo;
    @ApiModelProperty("企业地址")
    private String merchantAddress;
    @ApiModelProperty("行业类型")
    private String merchantType;
    @ApiModelProperty("营业执照")
    private String businessLicensesImg;
    @ApiModelProperty("法人身份证正面")
    private String idCardFrontImg;
    @ApiModelProperty("法人身份证反面")
    private String idCardAfterImg;
    @ApiModelProperty("联系人")
    private String username;
    @ApiModelProperty("联系人地址")
    private String contactsAddress;
}
