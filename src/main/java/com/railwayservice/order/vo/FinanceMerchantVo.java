package com.railwayservice.order.vo;

import java.math.BigDecimal;

/**
 * 财务报表
 *
 * @author xuyu
 */
public class FinanceMerchantVo {

    private String stationId;

    private String stationName;

    private String merchantId;

    private String merchantName;

    private Integer totalCount;

    private Integer rejectCount;

    private Integer cancelCount;

    private Integer completedCount;

    private Integer uncompletedCount;

    private BigDecimal totalPrice;

    private BigDecimal cancelPrice;

    private BigDecimal rejectPrice;

    private BigDecimal completedPrice;

    private BigDecimal uncompletedPrice;

    private String createDate;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getUncompletedCount() {
        return uncompletedCount;
    }

    public void setUncompletedCount(Integer uncompletedCount) {
        this.uncompletedCount = uncompletedCount;
    }

    public BigDecimal getUncompletedPrice() {
        return uncompletedPrice;
    }

    public void setUncompletedPrice(BigDecimal uncompletedPrice) {
        this.uncompletedPrice = uncompletedPrice;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getRejectCount() {
        return rejectCount;
    }

    public void setRejectCount(Integer rejectCount) {
        this.rejectCount = rejectCount;
    }

    public Integer getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(Integer cancelCount) {
        this.cancelCount = cancelCount;
    }

    public Integer getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(Integer completedCount) {
        this.completedCount = completedCount;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getCancelPrice() {
        return cancelPrice;
    }

    public void setCancelPrice(BigDecimal cancelPrice) {
        this.cancelPrice = cancelPrice;
    }

    public BigDecimal getRejectPrice() {
        return rejectPrice;
    }

    public void setRejectPrice(BigDecimal rejectPrice) {
        this.rejectPrice = rejectPrice;
    }

    public BigDecimal getCompletedPrice() {
        return completedPrice;
    }

    public void setCompletedPrice(BigDecimal completedPrice) {
        this.completedPrice = completedPrice;
    }

}
