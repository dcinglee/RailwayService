package com.railwayservice.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 支付记录
 *
 * @author lid
 * @date 2017.2.8
 */
@Entity
public class PayRecord {
    /**
     * 记录ID
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String payRecordId;

    /**
     * 订单信息
     */
    private String mainOrderId;

    /**
     * 用户信息
     */
    private String userId;

    /**
     * 支付状态
     */
    private Integer payStatus;
    
    /**
     * 交易单号
     */
    private String transactionId;
    
    /**
     * 订单金额  单位为分
     */
    private Integer totalFee;

    /**
     * 支付时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_TIME_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date payDate;

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
     * 申请退款时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_TIME_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date refundApplyDate;

    /**
     * 退款时间refund_date
     */
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_TIME_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date refundDate;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_TIME_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public PayRecord() {

    }

    public Integer getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Integer totalFee) {
		this.totalFee = totalFee;
	}


	public String getPayRecordId() {
        return payRecordId;
    }

    public void setPayRecordId(String payRecordId) {
        this.payRecordId = payRecordId;
    }

    public String getMainOrderId() {
        return mainOrderId;
    }

    public void setMainOrderId(String mainOrderId) {
        this.mainOrderId = mainOrderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
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

    public Date getRefundApplyDate() {
        return refundApplyDate;
    }

    public void setRefundApplyDate(Date refundApplyTime) {
        this.refundApplyDate = refundApplyTime;
    }

    public Date getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Date refundTime) {
        this.refundDate = refundTime;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createTime) {
        this.createDate = createTime;
    }
}
