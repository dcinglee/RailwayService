package com.railwayservice.authority.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色与权限关联信息表
 *
 * @author lid
 * @date 2017.2.6
 */
@Entity
public class RoleAuthorityRela implements Serializable {
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
     * 权限
     */
    @ManyToOne
    @JoinColumn(name = "authority")
    private Authority authority;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public RoleAuthorityRela() {

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

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
