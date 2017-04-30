package com.railwayservice.order.vo;

import java.util.List;

/**
 * 下单参数vo类
 *
 * @author lid
 * @date 2017.3.1
 */
public class OrderParamVo {
    /**
     * 商户id
     */
    private String merchantId;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 电话号码
     */
    private String customerPhoneNo;

    /**
     * 送餐方式
     */
    private Integer deliverType;

    /**
     * 收货位置
     */
    private String deliverAddress;

    /**
     * 最晚送达时间
     */
    private String latestServiceTime;
    /**
     * 产品id以及数量s
     */
    private List<ProductInputParamVo> listProduct;

    public String getLatestServiceTime() {
		return latestServiceTime;
	}

	public void setLatestServiceTime(String latestServiceTime) {
		this.latestServiceTime = latestServiceTime;
	}

	public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNo() {
        return customerPhoneNo;
    }

    public void setCustomerPhoneNo(String customerPhoneNo) {
        this.customerPhoneNo = customerPhoneNo;
    }

    public Integer getDeliverType() {
        return deliverType;
    }

    public void setDeliverType(Integer deliverType) {
        this.deliverType = deliverType;
    }

    public String getDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
    }

    public List<ProductInputParamVo> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<ProductInputParamVo> listProduct) {
        this.listProduct = listProduct;
    }

}
