package com.railwayservice.order.vo;

import java.util.Date;

/**
 * 定时扫描订单，仅保存订单id和创建时间
 * @author lid
 * @date 2017.3.31
 */
public class OrderQuartzVo {
	
	private String orderId;
	
	private Date createDate;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
