package com.railwayservice.productmanage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * 商品信息
 *
 * @author lid
 * @date 2017.2.6
 */
@Entity
public class Product {
    /**
     * 商品id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String productId;

    /**
     * 商品所属商户
     */
    private String merchantId;

    /**
     * 商品所属车站
     */
    private String stationId;

    /**
     * 商品所属分类，临时属性不保存！
     */
    @Transient
    private String categoryId;

    /**
     * 商品所属服务类型，临时属性不保存！
     */
    @Transient
    private String serviceTypeId;

    /**
     * 商品所属车站，临时属性不保存！
     */
    @Transient
    private String stationName;

    /**
     * 产品编号
     */
    private String seriesNo;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 销售价格
     */
    private BigDecimal price;

    /**
     * 展示图片ID
     */
    private String imageId;

    /**
     * 商品展示图片URL，临时属性不保存。
     */
    @Transient
    private String imageUrl;

    /**
     * 商户logo
     */
    @Transient
    private String logoImageUrl;

    /**
     * 产品单位
     */
    private String unit;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 商品状态（发布、上架、售罄等）
     */
    private Integer state;

    /**
     * 备注
     */
    private String note;

    /**
     * 商品简介
     */
    private String introduction;

    /**
     * 评分
     */
    private Integer score;

    /**
     * 点击量
     */
    private BigInteger hits;

    /**
     * 销量
     */
    private Integer sales;

    /**
     * 产品显示顺序
     */
    private Integer sorter;
    /**
     * 产品推荐指数
     */
    private Integer recommend;
    /**
     * 创建日期
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public Product() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public BigInteger getHits() {
        return hits;
    }

    public void setHits(BigInteger hits) {
        this.hits = hits;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public Integer getSorter() {
        return sorter;
    }

    public void setSorter(Integer sorter) {
        this.sorter = sorter;
    }

    public Integer getRecommend() {
        return recommend;
    }

    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getLogoImageUrl() {
        return logoImageUrl;
    }

    public void setLogoImageUrl(String logoImageUrl) {
        this.logoImageUrl = logoImageUrl;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

}
