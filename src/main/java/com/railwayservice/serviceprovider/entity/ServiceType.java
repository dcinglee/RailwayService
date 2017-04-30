package com.railwayservice.serviceprovider.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 服务类型，包括服务名称，服务介绍，服务价格，所属站点以及服务的时间区间
 *
 * @author lid
 * @date 2017.2.3
 */
@Entity
public class ServiceType {
    /**
     * 服务类型id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String typeId;

    /**
     * 服务名称
     */
    private String name;

    /**
     * 服务介绍
     */
    private String introduction;

    /**
     * logo图片
     */
    private String imageId;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    /**
     * 配送费用
     */
    @Column(name = "distributionCosts", precision = 12, scale = 2)
    private BigDecimal distributionCosts;

    /**
     * 服务状态
     */
    private Integer status;

    public ServiceType() {

    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getDistributionCosts() {
        return distributionCosts;
    }

    public void setDistributionCosts(BigDecimal distributionCosts) {
        this.distributionCosts = distributionCosts;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

}
