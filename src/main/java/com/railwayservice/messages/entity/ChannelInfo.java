package com.railwayservice.messages.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

import com.railwayservice.application.config.AppConfig;

/**
 * 用户消息信道控制，放入缓存。
 *
 * @author Ewing
 * @date 2017/3/2
 */
@Entity
public class ChannelInfo {
	
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String channelInfoId;
    
    
	public String getChannelInfoId() {
		return channelInfoId;
	}

	public void setChannelInfoId(String channelInfoId) {
		this.channelInfoId = channelInfoId;
	}

	// 用户ID、可以是商户ID、服务员ID
    private String userId;
    
    // 通信频道
    private String channelId;

    // 设备类型
    private String deviceType;

    // 设备ID号
    private String deviceIdNo;

    // 状态：服务中、休息中
    private Integer status;

    // 最后在线时间（毫秒）
    private Long lastTime;

   

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
	
//    public ChannelInfoId getChannelInfoId() {
//		return channelInfoId;
//	}
//
//	public void setChannelInfoId(ChannelInfoId channelInfoId) {
//		this.channelInfoId = channelInfoId;
//	}

	public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceIdNo() {
        return deviceIdNo;
    }

    public void setDeviceIdNo(String deviceIdNo) {
        this.deviceIdNo = deviceIdNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getLastTime() {
        return lastTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }
}
