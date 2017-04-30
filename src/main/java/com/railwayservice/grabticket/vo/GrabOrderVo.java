package com.railwayservice.grabticket.vo;

/**
 * 抢票vo类
 * @author lid
 * @date 2017.4.11
 */
public class GrabOrderVo {
    /**
     * 出发地（不一定是出发车站，存在出发地选长沙，但是出发站是长沙南或者长沙火车站）
     */
    private String aboardPlace;
    
    /**
     * 到达地
     */
    private String arrivedPlace;
    
    /**
     * 预计出发日期（订票是用户输入的时间，并不一定和真是出发时间一致）
     */
    private String estimatedAboardTime;
    
    /**
     * 备选出发日期(备选日期最多4个,以字符串的形式保存在字段中)
     */
    private String alternativeAboardTime;
   
    /**
     * 出发车站
     */
    private String aboardStation;

    /**
     * 到达车站
     */
    private String arrivedStation;    
    
    /**
     * 车次
     */
    private String lineNo;
    
    /**
     * 所选车次的出发时间
     */
    private String aboardTimeFromStation;
    
    /**
     * 所选车次的到达时间
     */
    private String arrivedTimeToStation;
    
    /**
     * 备选车次(备选车次最多10个,以字符串的形式保存)
     */
    private String estimatedLineNo;
    
    /**
     * 坐席类型
     */
    private String seatType;
    
    /**
     * 备选坐席
     */
    private String estimatedSeatType;
    
    /**
     * 12306账号
     */
    private String userName;
    
    /**
     * 12306密码
     */
    private String passWord;
    
    /**
     * 通知手机号码 
     */
    private String noticePhoneNo;
    
    /**
     * 乘客身份证号码链表
     */
    private String passengerIdentityCardNo[];
    
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

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}

	public String getEstimatedSeatType() {
		return estimatedSeatType;
	}

	public void setEstimatedSeatType(String estimatedSeatType) {
		this.estimatedSeatType = estimatedSeatType;
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

	public String getNoticePhoneNo() {
		return noticePhoneNo;
	}

	public void setNoticePhoneNo(String noticePhoneNo) {
		this.noticePhoneNo = noticePhoneNo;
	}

	public String[] getPassengerIdentityCardNo() {
		return passengerIdentityCardNo;
	}

	public void setPassengerIdentityCardNo(String[] passengerIdentityCardNo) {
		this.passengerIdentityCardNo = passengerIdentityCardNo;
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
	
}
