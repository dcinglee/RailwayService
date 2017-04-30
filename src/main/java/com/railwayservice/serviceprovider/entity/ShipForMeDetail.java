package com.railwayservice.serviceprovider.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 代送订单详情，存在子单的情况，子单保存在该表中
 *
 * @author lid
 * @date 2017.2.7
 */
@Entity
public class ShipForMeDetail {
    /**
     * id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String shipForMeDetailId;

    /**
     * 代购单
     */
    private String shipForMeId;

    /**
     * 代购的产品
     */
    private String productId;

    /**
     * 产品价格
     */
    private BigDecimal productPrice;

    /**
     * 产品数量
     */
    private Integer productCount;

    /**
     * 产品总价
     */
    private BigDecimal productTotalPrice;

    /**
     * 代购的商户
     */
    private String merchantId;

    /**
     * 代购状态
     */
    private Integer shipStatus;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public ShipForMeDetail() {

    }

    public String getShipForMeDetailId() {
        return shipForMeDetailId;
    }

    public void setShipForMeDetailId(String shipForMeDetailId) {
        this.shipForMeDetailId = shipForMeDetailId;
    }

    public String getShipForMeId() {
        return shipForMeId;
    }

    public void setShipForMeId(String shipForMe) {
        this.shipForMeId = shipForMe;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String product) {
        this.productId = product;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchant) {
        this.merchantId = merchant;
    }

    public Integer getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(Integer shipStatus) {
        this.shipStatus = shipStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
