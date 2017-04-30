package com.railwayservice.order.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.railwayservice.application.config.AppConfig;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * 订单状态记录表
 *
 * @author lid
 * @date 2017.3.1
 */
@Entity
public class OrderStatusRecord {
    /**
     * ID
     */
    @Id
    @GenericGenerator(name = "idGenerator", strategy = AppConfig.UUID_GENERATOR)
    @GeneratedValue(generator = "idGenerator")
    private String orderStatusRecordId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 订单状态（包括取消状态）
     */
    private Integer orderStatus;
    
    /**
     * 备注(包括取消原因、拒绝原因等)
     */
    private String remark;

    /**
     * 创建日期
     */
    @DateTimeFormat(pattern = AppConfig.DATE_TIME_FORMAT)
    @JsonFormat(pattern = AppConfig.DATE_TIME_FORMAT, timezone = AppConfig.TIMEZONE)
    private Date createDate;

    public String getOrderStatusRecordId() {
        return orderStatusRecordId;
    }

    public void setOrderStatusRecordId(String orderStatusRecordId) {
        this.orderStatusRecordId = orderStatusRecordId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
}
