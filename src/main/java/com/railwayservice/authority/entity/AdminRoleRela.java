package com.railwayservice.authority.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色与管理员关联信息表
 * <p>
 * 管理员可有多个角色
 *
 * @author lid
 * @date 2017.2.13
 */
@Entity
public class AdminRoleRela implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 关联id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String relaId;

    /**
     * 角色
     */
    @ManyToOne
    @JoinColumn(name = "role")
    private Role role;

    /**
     * 管理员
     */
    @ManyToOne
    @JoinColumn(name = "admin")
    private Admin admin;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public AdminRoleRela() {

    }

    public String getRelaId() {
        return relaId;
    }

    public void setRelaId(String relaId) {
        this.relaId = relaId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
