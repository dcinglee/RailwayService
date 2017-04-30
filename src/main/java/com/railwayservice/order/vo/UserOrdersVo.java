package com.railwayservice.order.vo;

import java.math.BigDecimal;
import java.util.List;

import com.railwayservice.order.entity.SubOrder;

/**
 * 用户获取订单记录vo类
 *
 * @author lid
 * @date 2017.3.6
 */
public class UserOrdersVo {
    /**
     * 订单id
     */
    private String orderId;

    /**
     * 商家名称
     */
    private String merchantName;

    /**
     * 商家logo 的url
     */
    private String imageUrl;
    
    /**
     * 订单创建时间
     */
    private String createDate;

    /**
     * 订单状态
     */
    private Integer orderStatus;
    
    /**
     * 订单取消状态
     */
    private Integer orderCancelStatus;
    
    /**
     * 订单总价
     */
    private BigDecimal totalPrice;
    
    /**
     * 子单信息
     */
    private List<SubOrder> listSubOrder;
    
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public List<SubOrder> getListSubOrder() {
		return listSubOrder;
	}

	public void setListSubOrder(List<SubOrder> listSubOrder) {
		this.listSubOrder = listSubOrder;
	}

	public Integer getOrderCancelStatus() {
		return orderCancelStatus;
	}

	public void setOrderCancelStatus(Integer orderCancelStatus) {
		this.orderCancelStatus = orderCancelStatus;
	}

}
