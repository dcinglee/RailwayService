package com.railwayservice.stationmanage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 车站实体类
 *
 * @author lid
 * @date 2017.2.6
 */
@Entity
public class RailwayStation {
    /**
     * 站点ID
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String stationId;

    /**
     * 站点名称
     */
    private String stationName;

    /**
     * 站点全拼
     */
    private String spell;

    /**
     * 站点名称缩写
     */
    private String stationNameAbbr;

    /**
     * 站点所在省份
     */
    private String province;

    /**
     * 站点所在城市
     */
    private String city;

    /**
     * 启用状态
     */
    private Integer status;

    /**
     * 城市编码
     */
    private String cityId;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;

    /**
     * 车站示意图id
     */
    @Transient
    private String imageId;

    /**
     * 车站示意图URL
     */
    @Transient
    private String imgUrl;
    /**
     * 创建日期
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public RailwayStation() {
    }

    public RailwayStation(String stationId) {
        this.stationId = stationId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationNameAbbr() {
        return stationNameAbbr;
    }

    public void setStationNameAbbr(String stationNameAbbr) {
        this.stationNameAbbr = stationNameAbbr;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
