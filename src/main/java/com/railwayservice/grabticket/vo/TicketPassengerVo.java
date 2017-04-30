package com.railwayservice.grabticket.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;

/**
 * 车票详情vo类
 * @author lixs
 *
 */
public class TicketPassengerVo {
	
	/**
	 * 乘客姓名
	 */
	private String passengerName;
	
	/**
	 * 通知电话
	 */
	private String noticePhoneNo;
	
	/**
	 * 订单类型
	 */
	private Integer orderType;
	
	/**
	 * 订单状态
	 */
	private Integer orderStatus;
	
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
    
    private String ticketOrderId;
    
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;
    
    /**
     * 出发车站
     */
    private String aboardStation;

    /**
     * 到达车站
     */
    private String arrivedStation;
    
    /**
     * 票数
     */
    private Integer ticketNumber;
	
	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public String getNoticePhoneNo() {
		return noticePhoneNo;
	}

	public void setNoticePhoneNo(String noticePhoneNo) {
		this.noticePhoneNo = noticePhoneNo;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
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

	public String getTicketOrderId() {
		return ticketOrderId;
	}

	public void setTicketOrderId(String ticketOrderId) {
		this.ticketOrderId = ticketOrderId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public Integer getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(Integer ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	
	
	
	

}
