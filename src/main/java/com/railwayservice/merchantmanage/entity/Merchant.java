package com.railwayservice.merchantmanage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import com.railwayservice.productmanage.entity.ProductCategory;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商户信息
 *
 * @author lid
 * @date 2017.2.6
 */
@Entity
public class Merchant implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商户id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String merchantId;

    /**
     * 所在车站ID
     */
    private String stationId;

    /**
     * 商户名称
     */
    private String name;

    /**
     * 商户账号
     */
    private String account;

    /**
     * 商户密码
     */
    private String password;

    /**
     * 合同编号
     */
    private String agreementNo;

    /**
     * 合同生效时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date agreementStartTime;

    /**
     * 合同结束时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date agreementEndTime;

    /**
     * 商户联系人
     */
    private String linkman;

    /**
     * 商户联系电话号码
     */
    private String phoneNo;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 地址
     */
    private String address;

    /**
     * logo图片
     */
    private String imageId;

    /**
     * 展示图片URL，临时属性不保存。
     */
    @Transient
    private String imageUrl;

    /**
     * 商户简介
     */
    private String introduction;

    /**
     * 类型：参考ServiceType表
     * 可能为：消息、充电宝等
     */
    private String serviceTypeId;

    /**
     * 公告
     */
    private String announcement;

    /**
     * 评价
     */
    private Float evaluate;

    /**
     * 商家状态
     */
    private Integer status;

    /**
     * 月销量
     */
    private Integer sailsInMonth;

    /**
     * 营业开始时间 日期格式 hhmmss 直接转成整数
     */
    private Integer startTime;

    /**
     * 营业结束时间 日期格式 hhmmss 直接转成整数
     */
    private Integer stopTime;

    /**
     * 创建日期
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    /**
     * 最后一次登陆时间
     */
    @Transient
    private long loginTime;

    /**
     * 商户的商品分类，临时属性不保存。
     */
    @Transient
    private List<ProductCategory> categories;

    public Merchant() {
    }

    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String typeId) {
        this.serviceTypeId = typeId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAgreementNo() {
        return agreementNo;
    }

    public void setAgreementNo(String agreementNo) {
        this.agreementNo = agreementNo;
    }

    public Date getAgreementStartTime() {
        return agreementStartTime;
    }

    public void setAgreementStartTime(Date agreementStartTime) {
        this.agreementStartTime = agreementStartTime;
    }

    public Date getAgreementEndTime() {
        return agreementEndTime;
    }

    public void setAgreementEndTime(Date agreementEndTime) {
        this.agreementEndTime = agreementEndTime;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
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

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public Float getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(Float evaluate) {
        this.evaluate = evaluate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSailsInMonth() {
        return sailsInMonth;
    }

    public void setSailsInMonth(Integer sailsInMonth) {
        this.sailsInMonth = sailsInMonth;
    }

    public List<ProductCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ProductCategory> categories) {
        this.categories = categories;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getStopTime() {
        return stopTime;
    }

    public void setStopTime(Integer stopTime) {
        this.stopTime = stopTime;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }
}
