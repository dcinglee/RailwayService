package com.railwayservice.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import com.railwayservice.stationmanage.entity.RailwayStation;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import java.util.Date;
import java.util.List;

/**
 * 用户的行程信息，每一次高铁出行对应一次行程
 *
 * @author lid
 * @date 2017.2.6
 */
@Entity
public class TravelRoute {
    /**
     * 用户的行程ID
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String routeId;

    /**
     * 用户 id
     */
    private String userId;

    /**
     * 旅客姓名
     */
    private String customerName;

    /**
     * 旅客电话
     */
    private String customerPhone;

    /**
     * 出发车站
     */
    private String aboardStation;

    /**
     * 到达车站
     */
    private String arrivedStation;
    
    /**
     * 到达城市id
     */
    private String cityId;
    
    /**
     * 沿途站点  临时数据，不保存
     */
    @Transient
    private List<RailwayStation> listStationOnTheWay;

    /**
     * 出发时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT_MINUTE)
    @JsonFormat(pattern = AppConfig.DATE_TIME_FORMAT_MINUTE, timezone = AppConfig.TIMEZONE)
    private Date aboardTime;

    /**
     * 到达时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT_MINUTE)
    @JsonFormat(pattern = AppConfig.DATE_TIME_FORMAT_MINUTE, timezone = AppConfig.TIMEZONE)
    private Date arrivedTime;

    /**
     * 车次号
     */
    private String lineNo;

    /**
     * 车厢号
     */
    private String carriageNumber;

    /**
     * 座位号
     */
    private String seatNumber;

    /**
     * 创建日期
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public TravelRoute() {
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAboardStation() {
        return aboardStation;
    }

    public void setAboardStation(String aboardStation) {
        this.aboardStation = aboardStation;
    }

    public String getArrivedStation() {
        return arrivedStation;
    }

    public void setArrivedStation(String arrivedStation) {
        this.arrivedStation = arrivedStation;
    }

    public Date getArrivedTime() {
        return arrivedTime;
    }

    public void setArrivedTime(Date arrivedTime) {
        this.arrivedTime = arrivedTime;
    }

    public Date getAboardTime() {
        return aboardTime;
    }

    public void setAboardTime(Date aboardTime) {
        this.aboardTime = aboardTime;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public List<RailwayStation> getListStationOnTheWay() {
		return listStationOnTheWay;
	}

	public void setListStationOnTheWay(List<RailwayStation> listStationOnTheWay) {
		this.listStationOnTheWay = listStationOnTheWay;
	}
    
}
