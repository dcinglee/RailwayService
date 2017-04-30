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
 * 车票订单实体类。
 *
 * @author lid
 * @date 2017.4.5
 */
@Entity
public class TicketOrder {
    /**
     * 车票订单ID
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String ticketOrderId;
    
    /**
     * 订单号
     */
    private String orderId;

    /**
     * 订单类型（抢票还是订票）
     */
    private Integer orderType;

    /**
     * 购买人Id
     */
    private String userId;
    
    /**
     * 购买者的12306账号
     */
    private String userName;

    /**
     * 密码
     */
    private String passWord;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 订单异常返回代码
     */
    private String abnormalCode;

    /**
     * 订单异常描述
     */
    private String abnormalDescribe;

    /**
     * 出发地（不一定是出发车站，存在出发地选长沙，但是出发站是长沙南或者长沙火车站）
     */
    private String aboardPlace;

    /**
     * 到达地
     */
    private String arrivedPlace;

    /**
     * 预计出发日期（订票时用户输入的时间，并不一定和真实出发时间一致）
     */
    private String estimatedAboardTime;

    /**
     * 备选出发日期(备选日期最多4个,以字符串的形式保存在字段中)
     */
    private String alternativeAboardTime;
    
    /**
     * 所选车次的出发时间
     */
    private String aboardTimeFromStation;
    
    /**
     * 所选车次的到达时间
     */
    private String arrivedTimeToStation;

    /**
     * 出发车站
     */
    private String aboardStation;

    /**
     * 到达车站
     */
    private String arrivedStation;

    /**
     * 出发时间
     */
    private String aboardTime;

    /**
     * 到达时间
     */
    private String arrivedTime;

    /**
     * 车次
     */
    private String lineNo;

    /**
     * 备选车次(备选车次最多10个,以字符串的形式保存)
     */
    private String estimatedLineNo;

    /**
     * 列车类型
     * 如：高铁，动车，特快，普快等
     */
    private String trainType;

    /**
     * 坐席类型
     */
    private String seatType;
    
    /**
     * 坐席描述
     */
    private String seatDetail;

    /**
     * 备选坐席
     */
    private String estimatedSeatType;
    
    /**
     * 票数
     */
    private Integer ticketNumber;

    /**
     * 备选坐席描述
     */
    private String estimatedSeatDetail;

	/**
     * 订单总价
     */
    private BigDecimal totalPrice;

    /**
     * 创建日期
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    /**
     * 是否允许购买中间车站的车票
     */
    private boolean middleStation;

    /**
     * 支付状态
     * (保留字段)
     */
    private Integer payStatus;

    /**
     * 通知手机号码
     */
    private String noticePhoneNo;

    /**
     * 通知状态
     */
    private Integer noticeStatus;

    /**
     * 历史删除状态
     * 0为未删除，1为已删除
     */
    private Integer isHistoryDelete = 0;

    /**
     * 订单删除状态
     * 0为未删除，1为已删除
     */
    private Integer isDelete = 0;
    
    /**
     * 取票号
     */
    private String sequenceNo;
    
    /**
     * 订单乘客信息
     */
    private String passengerIds;
    
    public String getSequenceNo() {
		return sequenceNo;
	}

	public void setSequenceNo(String sequenceNo) {
		this.sequenceNo = sequenceNo;
	}

	public String getSeatDetail() {
		return seatDetail;
	}

	public void setSeatDetail(String seatDetail) {
		this.seatDetail = seatDetail;
	}

	public String getEstimatedSeatDetail() {
		return estimatedSeatDetail;
	}

	public void setEstimatedSeatDetail(String estimatedSeatDetail) {
		this.estimatedSeatDetail = estimatedSeatDetail;
	}

    public String getTicketOrderId() {
        return ticketOrderId;
    }

    public void setTicketOrderId(String ticketOrderId) {
        this.ticketOrderId = ticketOrderId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
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

    public String getAbnormalCode() {
        return abnormalCode;
    }

    public void setAbnormalCode(String abnormalCode) {
        this.abnormalCode = abnormalCode;
    }

    public String getAbnormalDescribe() {
        return abnormalDescribe;
    }

    public void setAbnormalDescribe(String abnormalDescribe) {
        this.abnormalDescribe = abnormalDescribe;
    }

    public String getAboardPlace() {
        return aboardPlace;
    }

    public void setAboardPlace(String aboardPlace) {
        this.aboardPlace = aboardPlace;
    }

    public String getArrivedPlace() {
        return arrivedPlace;
    }

    public void setArrivedPlace(String arrivedPlace) {
        this.arrivedPlace = arrivedPlace;
    }

    public String getEstimatedAboardTime() {
        return estimatedAboardTime;
    }

    public void setEstimatedAboardTime(String estimatedAboardTime) {
        this.estimatedAboardTime = estimatedAboardTime;
    }

    public String getAlternativeAboardTime() {
        return alternativeAboardTime;
    }

    public void setAlternativeAboardTime(String alternativeAboardTime) {
        this.alternativeAboardTime = alternativeAboardTime;
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

    public String getAboardTime() {
        return aboardTime;
    }

    public void setAboardTime(String aboardTime) {
        this.aboardTime = aboardTime;
    }

    public String getArrivedTime() {
        return arrivedTime;
    }

    public void setArrivedTime(String arrivedTime) {
        this.arrivedTime = arrivedTime;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getEstimatedLineNo() {
        return estimatedLineNo;
    }

    public void setEstimatedLineNo(String estimatedLineNo) {
        this.estimatedLineNo = estimatedLineNo;
    }

    public String getTrainType() {
		return trainType;
	}

	public void setTrainType(String trainType) {
		this.trainType = trainType;
	}

    public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public boolean isMiddleStation() {
        return middleStation;
    }

    public void setMiddleStation(boolean middleStation) {
        this.middleStation = middleStation;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getNoticeStatus() {
        return noticeStatus;
    }

    public void setNoticeStatus(Integer noticeStatus) {
        this.noticeStatus = noticeStatus;
    }

    public Integer getIsHistoryDelete() {
        return isHistoryDelete;
    }

    public void setIsHistoryDelete(Integer isHistoryDelete) {
        this.isHistoryDelete = isHistoryDelete;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

	public String getNoticePhoneNo() {
		return noticePhoneNo;
	}

	public void setNoticePhoneNo(String noticePhoneNo) {
		this.noticePhoneNo = noticePhoneNo;
	}

	public String getEstimatedSeatType() {
		return estimatedSeatType;
	}

	public void setEstimatedSeatType(String estimatedSeatType) {
		this.estimatedSeatType = estimatedSeatType;
	}

	public Integer getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(Integer ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getPassengerIds() {
		return passengerIds;
	}

	public void setPassengerIds(String passengerIds) {
		this.passengerIds = passengerIds;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getAboardTimeFromStation() {
		return aboardTimeFromStation;
	}

	public void setAboardTimeFromStation(String aboardTimeFromStation) {
		this.aboardTimeFromStation = aboardTimeFromStation;
	}

	public String getArrivedTimeToStation() {
		return arrivedTimeToStation;
	}

	public void setArrivedTimeToStation(String arrivedTimeToStation) {
		this.arrivedTimeToStation = arrivedTimeToStation;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
