package com.railwayservice.merchantmanage.vo;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.order.entity.SubOrder;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 配送员查看的订单内容。
 *
 * @author Ewing
 * @date 2017/3/3
 */
public class MerchantMainOrder {
    // 主订单ID
    private String mainOrderId;

    // 商家ID
    private String merchantId;

    // 下单用户ID
    private String userId;

    // 订单状态
    private Integer orderStatus;

    // 订单取消状态
    private Integer orderCancelStatus;

    // 订单号
    private String orderNo;

    // 顾客姓名
    private String customerName;

    // 顾客电话 手机号
    private String customerPhoneNo;

    // 支付时间、下单时间
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    private Date payDate;

    // 最新变化时间
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    private Date updateDate;

    // 配送方式  见字典表
    private Integer deliverType;

    // 送货地址
    private String deliverAddress;

    // 服务人员ID
    private String serviceProviderId;

    // 服务人员姓名
    private String serviceProviderName;

    // 服务人员电话
    private String serviceProviderPhoneNo;

    // 发车时间
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    private Date aboardTime;

    // 最晚服务（送达）时间、用户取货时间
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    private Date latestServiceTime;

    // 订单总价
    private BigDecimal orderTotalPrice;

    // 取消原因
    private String cancelReason;

    // 订单拒绝原因
    private String refuseReason;

    // 子订单ID
    private String subOrderId;

    // 产品Id
    private String productId;

    // 产品名称
    private String productName;

    // 产品价格
    private BigDecimal productPrice;

    // 产品数量
    private Integer productCount;

    private List<SubOrder> subOrders;

    public String getMainOrderId() {
        return mainOrderId;
    }

    public void setMainOrderId(String mainOrderId) {
        this.mainOrderId = mainOrderId;
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

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderCancelStatus() {
        return orderCancelStatus;
    }

    public void setOrderCancelStatus(Integer orderCancelStatus) {
        this.orderCancelStatus = orderCancelStatus;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getDeliverType() {
        return deliverType;
    }

    public void setDeliverType(Integer deliverType) {
        this.deliverType = deliverType;
    }

    public String getDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public String getServiceProviderName() {
        return serviceProviderName;
    }

    public void setServiceProviderName(String serviceProviderName) {
        this.serviceProviderName = serviceProviderName;
    }

    public String getServiceProviderPhoneNo() {
        return serviceProviderPhoneNo;
    }

    public void setServiceProviderPhoneNo(String serviceProviderPhoneNo) {
        this.serviceProviderPhoneNo = serviceProviderPhoneNo;
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

    public BigDecimal getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(BigDecimal orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public List<SubOrder> getSubOrders() {
        return subOrders;
    }

    public void setSubOrders(List<SubOrder> subOrders) {
        this.subOrders = subOrders;
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MerchantMainOrder that = (MerchantMainOrder) o;

        return mainOrderId != null ? mainOrderId.equals(that.mainOrderId) : that.mainOrderId == null;
    }

    @Override
    public int hashCode() {
        return mainOrderId != null ? mainOrderId.hashCode() : 0;
    }
}
