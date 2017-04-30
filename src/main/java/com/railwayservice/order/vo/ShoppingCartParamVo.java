package com.railwayservice.order.vo;

import java.util.List;

/**
 * 修改购物车信息vo类
 *
 * @author lid
 * @date2017.3.4
 */
public class ShoppingCartParamVo {
    /**
     * 商户id
     */
    private String merchantId;

    /**
     * 商品信息链表
     */
    private List<CartProductParamVo> productList;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public List<CartProductParamVo> getProductList() {
        return productList;
    }

    public void setProductList(List<CartProductParamVo> productList) {
        this.productList = productList;
    }

}
