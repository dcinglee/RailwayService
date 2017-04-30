package com.railwayservice.merchantmanage.vo;

/**
 * 说明。
 *
 * @author Ewing
 * @date 2017/3/14
 */
public class Merchantmen {

    /**
     * 商户id
     */
    private String merchantId;

    /**
     * 商户名称
     */
    private String name;

    /**
     * 商户联系电话号码
     */
    private String phoneNo;

    /**
     * 地址
     */
    private String address;

    /**
     * 展示图片URL
     */
    private String imageUrl;

    /**
     * 公告
     */
    private String announcement;

    /**
     * 营业开始时间 日期格式 hhmmss 直接转成整数
     */
    private Integer startTime;

    /**
     * 营业结束时间 日期格式 hhmmss 直接转成整数
     */
    private Integer stopTime;
    
    private Integer status;

    public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getStopTime() {
        return stopTime;
    }

    public void setStopTime(Integer stopTime) {
        this.stopTime = stopTime;
    }
}
