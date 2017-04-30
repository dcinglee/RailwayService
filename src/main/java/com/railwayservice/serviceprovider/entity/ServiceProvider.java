package com.railwayservice.serviceprovider.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * 服务员信息，包含基本信息、所属站点以及服务的时间区间
 *
 * @author lid
 * @date 2017.2.3
 */
@Entity
public class ServiceProvider {
    /**
     * 服务员id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String serviceProviderId;

    /**
     * 服务员姓名
     */
    private String name;

    /**
     * 登陆账号
     */
    private String account;

    /**
     * 登陆密码
     */
    private String password;

    /**
     * 服务员电话号码
     */
    private String phoneNo;

    /**
     * 服务员年龄
     */
    private Integer age;

    /**
     * 服务员性别
     */
    private Integer gender;

    /**
     * 服务员身份证号码
     */
    private String identityCardNo;

    /**
     * 服务员照片
     */
    private String photoId;

    /**
     * 站点
     */
    private String stationId;

    /**
     * 服务人员状态
     */
    private Integer status;

    /**
     * 服务人员在线个数
     */
    @Transient
    private Integer onlineNum;

    /**
     * 最后登陆时间
     */
    @Transient
    private long loginTime;

    @Transient
    private List<ServiceType> serviceTypes;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public ServiceProvider() {

    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getIdentityCardNo() {
        return identityCardNo;
    }

    public void setIdentityCardNo(String identityCardNo) {
        this.identityCardNo = identityCardNo;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer serviceProvidestatus) {
        this.status = serviceProvidestatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Transient
    public List<ServiceType> getServiceTypes() {
        return serviceTypes;
    }

    @Transient
    public void setServiceTypes(List<ServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public Integer getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(Integer onlineNum) {
        this.onlineNum = onlineNum;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }
}
