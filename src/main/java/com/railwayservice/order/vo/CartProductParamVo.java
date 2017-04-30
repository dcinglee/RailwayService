package com.railwayservice.order.vo;

/**
 * 购物车中商品信息VO类
 * 包括商品id，商品数量
 *
 * @author Administrator
 */
public class CartProductParamVo {
    /**
     * 商品id
     */
    private String productId;

    /**
     * 商品数量
     */
    private Integer count;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
