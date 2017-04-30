package com.railwayservice.grabticket.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 乘客信息
 *
 * @author lid
 * @date 2017.4.5
 */
@Entity
public class Passenger {
    /**
     * 乘客ID
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String passengerId;

    /**
     * 12306账号id
     */
    private String kyfwUserId;

    /**
     * 当前乘客所属的用户
     */
    private String userId;

    /**
     * 姓名
     */
    private String passengerName;

    /**
     * 性别
     */
    private String gender;

    /**
     * 国家/地区
     */
    private String countryCode;

    /**
     * 证件类型
     */
    private String cardType;

    /**
     * 证件号码
     */
    private String identityCardNo;

    /**
     * 手机号码
     */
    private String mobilePhoneNo;

    /**
     * 固定电话
     */
    private String phoneNo;

    /**
     * email
     */
    private String email;

    /**
     * 地址
     */
    private String address;

    /**
     * 邮编
     */
    private String postalCode;

    /**
     * 旅客类型
     */
    private String passengerType;

    /**
     * 是否购票
     * 0 为不购票
     * 1 为购票
     */
    private Integer isOrderTicket = 0;

    /**
     * 创建日期
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    public String getKyfwUserId() {
        return kyfwUserId;
    }

    public void setKyfwUserId(String kyfwUserId) {
        this.kyfwUserId = kyfwUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getIdentityCardNo() {
        return identityCardNo;
    }

    public void setIdentityCardNo(String identityCardNo) {
        this.identityCardNo = identityCardNo;
    }

    public String getMobilePhoneNo() {
        return mobilePhoneNo;
    }

    public void setMobilePhoneNo(String mobilePhoneNo) {
        this.mobilePhoneNo = mobilePhoneNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public Integer getIsOrderTicket() {
        return isOrderTicket;
    }

    public void setIsOrderTicket(Integer isOrderTicket) {
        this.isOrderTicket = isOrderTicket;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
