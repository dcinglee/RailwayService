package com.railwayservice.operatemanage.entity;

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
 * 广告
 *
 * @author lid
 * @date 2017.2.3
 */
@Entity
public class AdBanner {
    /**
     * 广告id
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String adBannerId;

    /**
     * 广告类型
     */
    private Integer adType;

    /**
     * 广告标题
     */
    private String title;

    /**
     * 广告文本内容
     */
    private String content;

    /**
     * 广告链接
     */
    private String linkUrl;

    /**
     * 广告展放位置
     * 具体形式待定
     */
    private Integer adPosition;

    /**
     * 广告图片
     */
    private String imageId;
    
    /**
     * 广告图片地址
     */
    @Transient
    private String imageUrl;

    public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
     * 广告权重   用于确定广告排序
     */
    private Integer adWeight;

    /**
     * 广告播放时间
     */
    private Integer duration;

    /**
     * 点击量
     */
    private Long hits;

    /**
     * 广告展示开始时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date startDate;

    /**
     * 广告展示结束时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date endDate;

    /**
     * 发布广告的管理员
     */
    private String adminId;

    
    @Transient
    private String adminName;

    public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = AppConfig.DATE_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public AdBanner() {

    }

    public String getAdBannerId() {
        return adBannerId;
    }

    public void setAdBannerId(String adBannerId) {
        this.adBannerId = adBannerId;
    }

    public Integer getAdType() {
        return adType;
    }

    public void setAdType(Integer adType) {
        this.adType = adType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public Integer getAdPosition() {
        return adPosition;
    }

    public void setAdPosition(Integer adPosition) {
        this.adPosition = adPosition;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Integer getAdWeight() {
        return adWeight;
    }

    public void setAdWeight(Integer adWeight) {
        this.adWeight = adWeight;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
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

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
