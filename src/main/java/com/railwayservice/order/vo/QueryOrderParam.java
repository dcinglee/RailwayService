package com.railwayservice.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author lidx
 * @date 2017年2月8日
 * @describe
 */
public class QueryOrderParam {

    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 客户手机
     */
    private String customerPhoneNo;
    /**
     * 商户名称
     */
    private String merchantName;

    private String order;
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date startTime;
    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date endTime;

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

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "QueryOrderParam{" +
                "orderNo='" + orderNo + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerPhoneNo='" + customerPhoneNo + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", order='" + order + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
