package com.railwayservice.authority.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * 管理员角色
 *
 * @author lid
 * @date 2017.2.3
 */
@Entity
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 角色id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String roleId;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public Role() {

    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
