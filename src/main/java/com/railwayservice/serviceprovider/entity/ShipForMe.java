package com.railwayservice.serviceprovider.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 代送申请
 *
 * @author lid
 * @date 2017.2.6
 */
@Entity
public class ShipForMe {
    /**
     * id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String shipForMeId;

    /**
     * 申请者
     */
    private String applyUserId;

    /**
     * 代送者
     */
    private String shipUserId;

    /**
     * 站点
     */
    private String stationId;

    /**
     * 车次号
     */
    private String railwayNumber;

    /**
     * 车厢号
     */
    private String carriageNumber;

    /**
     * 座位号
     */
    private String seatNumber;

    /**
     * 行程日期
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date travelDate;

    /**
     * 申请日期
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date applyDate;

    /**
     * 代送日期
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date shipDate;

    /**
     * 代送状态
     */
    private Integer shipStatus;

    /**
     * 服务费用
     */
    private BigDecimal serviceFee;

    /**
     * 产品费用
     */
    private BigDecimal productPrice;

    /**
     * 最终费用
     */
    private BigDecimal finalPrice;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public ShipForMe() {

    }

    public String getShipForMeId() {
        return shipForMeId;
    }

    public void setShipForMeId(String shipForMeId) {
        this.shipForMeId = shipForMeId;
    }

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUser) {
        this.applyUserId = applyUser;
    }

    public String getShipUserId() {
        return shipUserId;
    }

    public void setShipUserId(String shipUser) {
        this.shipUserId = shipUser;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String station) {
        this.stationId = station;
    }

    public String getRailwayNumber() {
        return railwayNumber;
    }

    public void setRailwayNumber(String railwayNumber) {
        this.railwayNumber = railwayNumber;
    }

    public String getCarriageNumber() {
        return carriageNumber;
    }

    public void setCarriageNumber(String carriageNumber) {
        this.carriageNumber = carriageNumber;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Date getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Date travelDate) {
        this.travelDate = travelDate;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public Integer getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(Integer shipStatus) {
        this.shipStatus = shipStatus;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
