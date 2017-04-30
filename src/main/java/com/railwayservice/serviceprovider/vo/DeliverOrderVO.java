package com.railwayservice.serviceprovider.vo;

import com.railwayservice.application.config.AppConfig;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 配送员查看的订单内容。
 *
 * @author Ewing
 * @date 2017/3/3
 */
public class DeliverOrderVO {
    // 主订单ID
    private String mainOrderId;

    // 订单号
    private String orderNo;

    // 商家ID
    private String merchantId;

    // 下单用户ID
    private String userId;

    // 商家名称
    private String merchantName;

    // 商家图标
    private String merchantLogoUrl;

    // 商家电话号码
    private String merchantPhoneNo;

    // 顾客姓名
    private String customerName;

    // 顾客电话
    private String customerPhoneNo;

    // 取货地址
    private String fromAddress;

    // 送货地址
    private String deliverAddress;

    // 订单更新时间
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    private Date updateDate;
    // 发车时间
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    private Date aboardTime;
    // 最晚服务（送达）时间
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    private Date latestServiceTime;
    // 订单状态
    private Integer orderStatus;
    // 订单取消原因
    private String cancelReason;

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getMainOrderId() {
        return mainOrderId;
    }

    public void setMainOrderId(String mainOrderId) {
        this.mainOrderId = mainOrderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getMerchantPhoneNo() {
        return merchantPhoneNo;
    }

    public void setMerchantPhoneNo(String merchantPhoneNo) {
        this.merchantPhoneNo = merchantPhoneNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNo() {
        return customerPhoneNo;
    }

    public void setCustomerPhoneNo(String customerPhoneNo) {
        this.customerPhoneNo = customerPhoneNo;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
    }

    public Date getAboardTime() {
        return aboardTime;
    }

    public void setAboardTime(Date aboardTime) {
        this.aboardTime = aboardTime;
    }

    public Date getLatestServiceTime() {
        return latestServiceTime;
    }

    public void setLatestServiceTime(Date latestServiceTime) {
        this.latestServiceTime = latestServiceTime;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
