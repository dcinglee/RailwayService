package com.railwayservice.grabticket.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.railwayservice.application.config.AppConfig;

/**
 * 爬虫爬取的车次票价
 *
 * @author lid
 * @date 2017.4.19
 */
@Entity
public class LineTicket {
	@Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String lineTicketId;
	
    /**
	 * 车次
	 */
	private String lineNo;
	
	/**
	 * 出发车站
	 */
	private String aboardStation;
	
	/**
	 * 到达车站
	 */
	private String arrivedStation;
	
	/**
	 * 无座票价
	 */
	private BigDecimal noSeat;
	
	/**
	 * 硬座票价
	 */
	private BigDecimal hardSeat;
	
	/**
	 * 硬卧票价
	 */
	private BigDecimal hardBerth;
	
	/**
	 * 软座票价
	 */
	private BigDecimal softSeat;
	
	/**
	 * 软卧票价
	 */
	private BigDecimal softBerth;
	
	/**
	 * 高级软卧票价
	 */
	private BigDecimal highSoftBerth;
	
	/**
	 * 二等座票价
	 */
	private BigDecimal secondSeat;
	
	/**
	 * 一等座票价
	 */
	private BigDecimal firstSeat;
	
	/**
	 * 特等座票价
	 */
	private BigDecimal specialSeat;
	
	/**
	 * 商务座票价
	 */
	private BigDecimal businessSeat;
	
	public String getLineTicketId() {
		return lineTicketId;
	}

	public void setLineTicketId(String lineTicketId) {
		this.lineTicketId = lineTicketId;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
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

	public BigDecimal getNoSeat() {
		return noSeat;
	}

	public void setNoSeat(BigDecimal noSeat) {
		this.noSeat = noSeat;
	}

	public BigDecimal getHardSeat() {
		return hardSeat;
	}

	public void setHardSeat(BigDecimal hardSeat) {
		this.hardSeat = hardSeat;
	}

	public BigDecimal getHardBerth() {
		return hardBerth;
	}

	public void setHardBerth(BigDecimal hardBerth) {
		this.hardBerth = hardBerth;
	}

	public BigDecimal getSoftSeat() {
		return softSeat;
	}

	public void setSoftSeat(BigDecimal softSeat) {
		this.softSeat = softSeat;
	}

	public BigDecimal getSoftBerth() {
		return softBerth;
	}

	public void setSoftBerth(BigDecimal softBerth) {
		this.softBerth = softBerth;
	}

	public BigDecimal getHighSoftBerth() {
		return highSoftBerth;
	}

	public void setHighSoftBerth(BigDecimal highSoftBerth) {
		this.highSoftBerth = highSoftBerth;
	}

	public BigDecimal getSecondSeat() {
		return secondSeat;
	}

	public void setSecondSeat(BigDecimal secondSeat) {
		this.secondSeat = secondSeat;
	}

	public BigDecimal getFirstSeat() {
		return firstSeat;
	}

	public void setFirstSeat(BigDecimal firstSeat) {
		this.firstSeat = firstSeat;
	}

	public BigDecimal getSpecialSeat() {
		return specialSeat;
	}

	public void setSpecialSeat(BigDecimal specialSeat) {
		this.specialSeat = specialSeat;
	}

	public BigDecimal getBusinessSeat() {
		return businessSeat;
	}

	public void setBusinessSeat(BigDecimal businessSeat) {
		this.businessSeat = businessSeat;
	}
	
}
