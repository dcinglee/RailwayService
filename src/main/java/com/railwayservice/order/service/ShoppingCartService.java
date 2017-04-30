package com.railwayservice.order.service;

import com.railwayservice.order.entity.ShoppingCart;

import java.util.List;

/**
 * @author lid
 * @date 2017.3.2
 * @describe 购物车服务类接口
 */
public interface ShoppingCartService {
    /**
     * 根据用户id和商户Id查找购物车信息
     *
     * @param UserId
     * @param merchantId
     * @author lid
     */
    List<ShoppingCart> findByUserIdAndMerchantId(String userId, String merchantId);

    /**
     * 根据用户id和商户Id清空购物车
     *
     * @param UserId
     * @param merchantId
     * @author lid
     */
    void cleanShoppingCarts(String UserId, String merchantId);

    /**
     * 根据用户id和产品id查找购物车记录
     *
     * @param productId
     */
    ShoppingCart findByProductId(String UserId, String productId);

    /**
     * 添加购物车记录
     *
     * @param shoppingCart
     */
    ShoppingCart addShoppingCart(ShoppingCart shoppingCart);

    /**
     * 删除购物车记录
     */
    void deleteShoppingCart(String cartId);
}
