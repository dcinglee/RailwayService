package com.railwayservice.authority.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
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
 * 管理员信息
 *
 * @author lid
 * @date 2017.2.3
 */
@Entity
public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 管理员id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String adminId;

    /**
     * 管理员用户名
     */
    private String name;

    /**
     * 登陆账号
     */
    private String account;

    /**
     * 管理员密码
     */
    private String password;

    /**
     * 所属类型
     */
    private Integer belongType;

    /**
     * 所属组织ID
     */
    private String belongId;

    /**
     * 性别
     */
    private Integer gender;
    /**
     * 管理员状态
     */
    private Integer adminState;
    @Transient
    private List<Authority> authoritys;
    @Transient
    private List<Role> roles;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public Admin() {

    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
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

    public Integer getBelongType() {
        return belongType;
    }

    public void setBelongType(Integer belongType) {
        this.belongType = belongType;
    }

    public String getBelongId() {
        return belongId;
    }

    public void setBelongId(String belongId) {
        this.belongId = belongId;
    }

    public Integer getAdminState() {
        return adminState;
    }

    public void setAdminState(Integer adminState) {
        this.adminState = adminState;
    }

    public List<Authority> getAuthoritys() {
        return authoritys;
    }

    public void setAuthoritys(List<Authority> authoritys) {
        this.authoritys = authoritys;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}
