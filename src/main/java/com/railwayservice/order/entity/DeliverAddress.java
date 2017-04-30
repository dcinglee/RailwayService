package com.railwayservice.order.entity;

import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 送货地址
 *
 * @author xuy
 * @date 2017.2.27
 */
@Entity
public class DeliverAddress {

    /**
     * ID
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String deliverAddressId;

    /**
     * 所属车站
     */
    private String stationId;

    /**
     * 地址
     */
    private String address;

    /**
     * 显示顺序
     */
    private Integer orderNo;

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getDeliverAddressId() {
        return deliverAddressId;
    }

    public void setDeliverAddressId(String deliverAddressId) {
        this.deliverAddressId = deliverAddressId;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
