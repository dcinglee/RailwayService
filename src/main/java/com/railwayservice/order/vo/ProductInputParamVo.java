package com.railwayservice.order.vo;

/**
 * 封装订单中的产品Id以及产品数量
 *
 * @author lid
 * @date 2017.3.1
 */
public class ProductInputParamVo {
    /**
     * 产品id
     */
    private String productId;

    /**
     * 产品数量
     */
    private Integer quantity;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
