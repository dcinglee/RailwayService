package com.railwayservice.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 用户历史联系人实体类
 *
 * @author lid
 * @date 2017.2.6
 */
@Entity
public class HistoryContactPerson {
    /**
     * 用户历史联系人ID
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String historyContactPersonId;

    /**
     * 当前用户
     */
    private String userId;

    /**
     * 联系人姓名
     */
    private String contactPersonName;

    /**
     * 联系人电话
     */
    private String contactPersonTel;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public HistoryContactPerson() {

    }

    public String getHistoryContactPersonId() {
        return historyContactPersonId;
    }

    public void setHistoryContactPersonId(String historyContactPersonId) {
        this.historyContactPersonId = historyContactPersonId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String user) {
        this.userId = user;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonTel() {
        return contactPersonTel;
    }

    public void setContactPersonTel(String contactPersonTel) {
        this.contactPersonTel = contactPersonTel;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
