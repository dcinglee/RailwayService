package com.railwayservice.grabticket.entity;

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
 * 用户车票实体类。
 *
 * @author lid
 * @date 2017.4.6
 */
@Entity
public class RailwayTicket {
    /**
     * 车票ID
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String railwayTicketId;

    /**
     * 所属订单
     */
    private String ticketOrderId;

    /**
     * 乘客id
     */
    private String passengerId;

    /**
     * 乘客姓名
     */
    private String passengerName;
    
    /**
     * 证件类型
     */
    private String identityCarType;
    
    /**
     * 乘客身份证号码
     */
    private String identityCardNo;

    /**
     * 车票类型
     */
    private String ticketType;
    
    /**
     * 车次
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
     * 取票号
     */
    private String ticketPickNumber;

    /**
     * 票价
     */
    private BigDecimal ticketPrice;
    
    /**
     * 出发时间
     */
    private String startTime;
    
    /**
     * 到达时间
     */
    private String arrivedTime;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public String getRailwayTicketId() {
        return railwayTicketId;
    }

    public void setRailwayTicketId(String railwayTicketId) {
        this.railwayTicketId = railwayTicketId;
    }

    public String getTicketOrderId() {
        return ticketOrderId;
    }

    public void setTicketOrderId(String ticketOrderId) {
        this.ticketOrderId = ticketOrderId;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
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

    public String getTicketPickNumber() {
        return ticketPickNumber;
    }

    public void setTicketPickNumber(String ticketPickNumber) {
        this.ticketPickNumber = ticketPickNumber;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public String getIdentityCardNo() {
		return identityCardNo;
	}

	public void setIdentityCardNo(String identityCardNo) {
		this.identityCardNo = identityCardNo;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getArrivedTime() {
		return arrivedTime;
	}

	public void setArrivedTime(String arrivedTime) {
		this.arrivedTime = arrivedTime;
	}

	public String getIdentityCarType() {
		return identityCarType;
	}

	public void setIdentityCarType(String identityCarType) {
		this.identityCarType = identityCarType;
	}

	public String getLineNo() {
		return lineNo;
	}

	public void setLineNo(String lineNo) {
		this.lineNo = lineNo;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
}