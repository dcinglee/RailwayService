package com.railwayservice.stationmanage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 高铁线路信息，保存线路所有停靠站点，停靠时间等信息
 * 同一条线路的lineNo字段值（车次）相同，stationNo字段为停靠的顺序。
 *
 * @author lid
 * @date 2017.2.6
 */
@Entity
public class LineStation {
    /**
     * 线路ID
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String lineStationId;

    /**
     * 车次信息   如G205
     */
    private String lineNo;

    /**
     * 到站时间
     */
    private String arriveTime;

    /**
     * 站点
     */
    private String station;

    /**
     *
     */
    private String type;

    /**
     * 出发时间
     */
    private String departTime;

    /**
     * 停靠时间
     */
    private String stopTime;

    /**
     * 车站在本次列车中排序
     */
    private Integer sortNo;

    /**
     * 本次列车在系统应用中的开始时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date startDate;

    /**
     * 本次列车在系统应用中的结束时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date endDate;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public LineStation() {

    }

    public String getLineStationId() {
        return lineStationId;
    }

    public void setLineStationId(String lineStationId) {
        this.lineStationId = lineStationId;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String startTime) {
        this.departTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopOverTime) {
        this.stopTime = stopOverTime;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer stationNo) {
        this.sortNo = stationNo;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
