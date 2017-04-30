package com.railwayservice.order.dao;

import com.railwayservice.order.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 购物车数据库访问接口
 *
 * @author lid
 * @date 2017.2.10
 */
public interface ShoppingCartDao extends JpaRepository<ShoppingCart, String>, JpaSpecificationExecutor<ShoppingCart> {

    /**
     * 根据用户id和商户Id查找购物车信息
     *
     * @param UserId
     * @param merchantId
     * @return
     */
    List<ShoppingCart> findByUserIdAndMerchantId(String UserId, String merchantId);

    /**
     * 根据用户id和产品id查找购物车记录
     *
     * @param productId
     * @return
     */
    ShoppingCart findByUserIdAndProductId(String userId, String productId);

}
