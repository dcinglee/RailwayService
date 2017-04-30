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
 * 用户历史输入信息
 *
 * @author lid
 * @date 2017.2.6
 */
@Entity
public class UserHistoryInput {
    /**
     * 历史输入id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String UserHistoryInputUuid;

    /**
     * 用户
     */
    private String userId;

    /**
     * 输入信息
     */
    private String historyInput;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public String getUserHistoryInputUuid() {
        return UserHistoryInputUuid;
    }

    public void setUserHistoryInputUuid(String userHistoryInputUuid) {
        UserHistoryInputUuid = userHistoryInputUuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String user) {
        this.userId = user;
    }

    public String getHistoryInput() {
        return historyInput;
    }

    public void setHistoryInput(String historyInput) {
        this.historyInput = historyInput;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
