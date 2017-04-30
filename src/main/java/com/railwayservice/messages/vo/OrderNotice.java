package com.railwayservice.messages.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 订单消息模型。
 *
 * @author Ewing
 * @date 2017/3/21
 */
public class OrderNotice {

    // 主订单ID
    private String mainOrderId;

    // 用户微信OPENID
    private String openid;

    // 服务人员名称
    private String servantName;

    // 商家名称
    private String merchantName;

    // 商家LOGO
    private String merchantLogoUrl;

    // 创建时间
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_TIME_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createTime;

    // 商品详情
    private String productDetail;

    // 订单状态
    private String orderStatusName;

    public OrderNotice() {
    }

    public String getMainOrderId() {
        return mainOrderId;
    }

    public void setMainOrderId(String mainOrderId) {
        this.mainOrderId = mainOrderId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getServantName() {
        return servantName;
    }

    public void setServantName(String servantName) {
        this.servantName = servantName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantLogoUrl() {
        return merchantLogoUrl;
    }

    public void setMerchantLogoUrl(String merchantLogoUrl) {
        this.merchantLogoUrl = merchantLogoUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }
}
