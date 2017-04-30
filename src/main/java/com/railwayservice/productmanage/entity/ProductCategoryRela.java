package com.railwayservice.productmanage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 商品与商品分类关联信息表
 * <p>
 * 一个商品分类下可有多个商品
 *
 * @author lid
 * @date 2017.2.13
 */
@Entity
public class ProductCategoryRela {
    /**
     * 关联id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String relaId;

    /**
     * 商品id
     */
    private String productId;

    /**
     * 商品分类id
     */
    private String productCategoryId;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public ProductCategoryRela() {
    }

    public String getRelaId() {
        return relaId;
    }

    public void setRelaId(String relaId) {
        this.relaId = relaId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
