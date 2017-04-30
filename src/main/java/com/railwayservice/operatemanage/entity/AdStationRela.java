package com.railwayservice.operatemanage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import com.railwayservice.stationmanage.entity.RailwayStation;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 广告与站点的关系
 *
 * @author lid
 * @date 2017.2.3
 */
@Entity
public class AdStationRela {
    /**
     * 广告与站点的关系id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String relaId;

    /**
     * 广告
     */
    @ManyToOne
    @JoinColumn(name = "adBanner")
    private AdBanner adBanner;

    /**
     * 站点
     */
    @ManyToOne
    @JoinColumn(name = "station")
    private RailwayStation station;

    /**
     * 广告展示开始时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public AdStationRela() {

    }

    public String getRelaId() {
        return relaId;
    }

    public void setRelaId(String adStationRelId) {
        this.relaId = adStationRelId;
    }

    public AdBanner getAdBanner() {
        return adBanner;
    }

    public void setAdBanner(AdBanner adBanner) {
        this.adBanner = adBanner;
    }

    public RailwayStation getStation() {
        return station;
    }

    public void setStation(RailwayStation station) {
        this.station = station;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
