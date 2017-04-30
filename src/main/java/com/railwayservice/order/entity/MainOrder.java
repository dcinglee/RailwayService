package com.railwayservice.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 主订单信息，至少包含一个子订单
 *
 * @author lid
 * @date 2017.2.6
 */
@Entity
public class MainOrder {
    /**
     * 订单ID
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 购买人Id
     */
    private String userId;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 客户联系电话
     */
    private String customerPhoneNo;

    /**
     * 商品总价
     */
    private BigDecimal productTotalPrice;

    /**
     * 订单总价
     * 订单总价=商品总价+配送费用
     */
    private BigDecimal orderTotalPrice;

    /**
     * 订单状态
     * 具体类别见Dictionary表定义
     */
    private Integer orderStatus;

    /**
     * 用户确认送达（收货）码
     */
    private String receiveCode;

    /**
     * 订单取消原因
     */
    private String cancelReason;

    /**
     * 订单拒绝原因
     */
    private String refuseReason;

    /**
     * 订单取消状态
     * 当用户申请取消时，商户同意取消、商户不同意取消，都会更新此字段，便于订单流程的处理
     */
    private Integer orderCancelStatus;

    /**
     * 所属服务Id
     */
    private String serviceTypeId;

    /**
     * 最晚更新时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_TIME_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date updateDate;

    /**
     * 车站ID
     */
    private String stationId;

    /**
     * 商户Id
     */
    private String merchantId;

    /**
     * 服务人员ID
     */
    private String serviceProviderId;

    /**
     * 服务人员联系方式
     */
    @Transient
    private String serviceProviderPhoneNo = null;

    /**
     * 配送费用
     */
    private BigDecimal distributionCosts;

    /**
     * 用户备注
     */
    private String remark;

    /**
     * 最晚送达（服务结束）时间、用户自提时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_TIME_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date latestServiceTime;

    /**
     * 当前车次出发时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_TIME_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date aboardTime;

    /**
     * 支付状态
     * 具体类别见Dictionary表定义
     */
    private Integer payStatus;
    /**
     * 支付单号
     */
    private String transactionId;
    /**
     * 支付账号
     */
    private String payId;
    /**
     * 支付类型
     * 具体类别见Dictionary表定义
     */
    private Integer payType;

    /**
     * 收货位置
     */
    private String deliverAddress;

    /**
     * 配送方式  见字典表
     */
    private Integer deliverType;

    /**
     * 支付时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_TIME_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date payDate;
    /**
     * 创建日期
     */
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_TIME_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;
    /**
     * 退款申请日期
     */
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_TIME_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date refundApplyDate;

    /**
     * 商户退款单号 参考：OrderUtil.GenerateOrderNo()
     */
    private String outRefundNo;

    /**
     * 微信退款单号   退款成功后由微信服务器返回
     */
    private String refundId;

    /**
     * 退款日期
     */
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_TIME_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date refundDate;

    /**
     * 订单评论
     * 每一个订单只能评价一次 0为未评价，1为已评价
     */
    private Integer commentFlag = 0;

    @Version
    private int version;

    public MainOrder() {
    }

    public String getReceiveCode() {
        return receiveCode;
    }

    public void setReceiveCode(String receiveCode) {
        this.receiveCode = receiveCode;
    }

    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public Date getLatestServiceTime() {
        return latestServiceTime;
    }

    public void setLatestServiceTime(Date latestServiceTime) {
        this.latestServiceTime = latestServiceTime;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public BigDecimal getOrderTotalPrice() {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(BigDecimal orderTotalPrice) {
        this.orderTotalPrice = orderTotalPrice;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getRefundApplyDate() {
        return refundApplyDate;
    }

    public void setRefundApplyDate(Date refundApplyDate) {
        this.refundApplyDate = refundApplyDate;
    }

    public Date getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public Integer getOrderCancelStatus() {
        return orderCancelStatus;
    }

    public void setOrderCancelStatus(Integer orderCancelStatus) {
        this.orderCancelStatus = orderCancelStatus;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public BigDecimal getDistributionCosts() {
        return distributionCosts;
    }

    public void setDistributionCosts(BigDecimal distributionCosts) {
        this.distributionCosts = distributionCosts;
    }

    public String getDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
    }

    public Integer getDeliverType() {
        return deliverType;
    }

    public void setDeliverType(Integer deliverType) {
        this.deliverType = deliverType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Date getAboardTime() {
        return aboardTime;
    }

    public void setAboardTime(Date aboardTime) {
        this.aboardTime = aboardTime;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getServiceProviderPhoneNo() {
        return serviceProviderPhoneNo;
    }

    public void setServiceProviderPhoneNo(String serviceProviderPhoneNo) {
        this.serviceProviderPhoneNo = serviceProviderPhoneNo;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Integer getCommentFlag() {
        return commentFlag;
    }

    public void setCommentFlag(Integer commentFlag) {
        this.commentFlag = commentFlag;
    }

}
