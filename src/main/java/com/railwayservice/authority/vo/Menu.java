package com.railwayservice.authority.vo;

import java.io.Serializable;
import java.util.List;

public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;

    private String authorityId;

    /**
     * 权限类型 0:url 1：按键；2，其他
     */
    private Integer type;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 权限码，根据模块、功能等命名
     */
    private String code;

    /**
     * 子菜单
     */
    private List<Menu> subMenus;

    /**
     * 菜单显示顺序
     * 具体实现后续修改
     */
    private Integer orderNo;

    /**
     * 菜单url
     */
    private String menuUrl;

    /**
     * 图标
     */
    private String icon;

    public String getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Menu> getSubMenus() {
        return subMenus;
    }

    public void setSubMenus(List<Menu> subMenus) {
        this.subMenus = subMenus;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
