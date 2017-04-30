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
 * 12306账号和用户的关联关系表
 *
 * @author lid
 * @date 2017.4.5
 */
@Entity
public class KyfwInUserRela {
    //关联ID
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String kyfwInUserRelaId;

    private String userId;

    private String kyfwUserId;

    /**
     * 创建日期
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public String getKyfwInUserRelaId() {
        return kyfwInUserRelaId;
    }

    public void setKyfwInUserRelaId(String kyfwInUserRelaId) {
        this.kyfwInUserRelaId = kyfwInUserRelaId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getKyfwUserId() {
        return kyfwUserId;
    }

    public void setKyfwUserId(String kyfwUserId) {
        this.kyfwUserId = kyfwUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
