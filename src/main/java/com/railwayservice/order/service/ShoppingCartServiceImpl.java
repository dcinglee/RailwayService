package com.railwayservice.order.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.order.dao.ShoppingCartDao;
import com.railwayservice.order.entity.ShoppingCart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author lid
 * @date 2017.3.2
 * @describe 购物车服务类接口实现
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final Logger logger = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    private ShoppingCartDao shoppingCartDao;

    @Autowired
    public void setShoppingCartDao(ShoppingCartDao shoppingCartDao) {
        this.shoppingCartDao = shoppingCartDao;
    }

    @Override
    public List<ShoppingCart> findByUserIdAndMerchantId(String userId, String merchantId) {
        if ((!StringUtils.hasText(userId))
                || (!StringUtils.hasText(merchantId))) {
            throw new AppException("UserId参数或者merchantId参数为空！");
        }
        logger.info("购物车服务层：购物车列表：用户ID：" + userId + "商户ID：" + merchantId);

        return shoppingCartDao.findByUserIdAndMerchantId(userId, merchantId);
    }

    @Override
    public void cleanShoppingCarts(String userId, String merchantId) {
        if ((!StringUtils.hasText(userId))
                || (!StringUtils.hasText(merchantId))) {
            throw new AppException("UserId参数或者merchantId参数为空！");
        }
        logger.info("购物车服务层：取消购物车：用户ID：" + userId + "商户ID：" + merchantId);

        List<ShoppingCart> listShoppingCart = shoppingCartDao.findByUserIdAndMerchantId(userId, merchantId);
        if (0 == listShoppingCart.size()) {
            return;
        }

        shoppingCartDao.delete(listShoppingCart);
    }

    @Override
    public ShoppingCart findByProductId(String userId, String productId) {
        if (!StringUtils.hasText(productId)) {
            throw new AppException("productId参数为空！");
        }
        logger.info("购物车服务层：查询购物车：用户ID：" + userId + "商品ID：" + productId);

        return shoppingCartDao.findByUserIdAndProductId(userId, productId);
    }

    @Override
    public ShoppingCart addShoppingCart(ShoppingCart shoppingCart) {
        if (null == shoppingCart) {
            throw new AppException("addShoppingCart参数为空！");
        }

        if ((null == shoppingCart.getCount())
                || (null == shoppingCart.getMerchantId())
                || (null == shoppingCart.getPrice())
                || (null == shoppingCart.getProductId())
                || (null == shoppingCart.getProductName())
                || (null == shoppingCart.getUserId())) {
            throw new AppException("addShoppingCart参数错误！");
        }
        logger.info("购物车服务层：添加购物车：商品名称：" + shoppingCart.getProductName());

        shoppingCart.setCreateDate(new Date());
        return shoppingCartDao.save(shoppingCart);
    }

    @Override
    public void deleteShoppingCart(String cartId) {
        if (null == cartId) {
            throw new AppException("cartId参数为空！");
        }
        logger.info("购物车服务层：删除购物车：购物车ID：" + cartId);

        shoppingCartDao.delete(cartId);
    }
}
