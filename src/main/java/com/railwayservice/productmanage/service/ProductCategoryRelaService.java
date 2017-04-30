package com.railwayservice.productmanage.service;

import com.railwayservice.productmanage.entity.Product;
import com.railwayservice.productmanage.entity.ProductCategory;
import com.railwayservice.productmanage.entity.ProductCategoryRela;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品与商品分类关联服务接口。
 *
 * @author lidx
 * @date 2017.3.2
 */
public interface ProductCategoryRelaService {

    /**
     * 添加商品与商品分类关联
     *
     * @param productCategoryRela
     * @return
     */
    ProductCategoryRela addProductCategoryRela(ProductCategoryRela productCategoryRela);

    /**
     * 通过商品分类查询商品列表
     *
     * @param productCategoryId 商品分类
     * @return 成功查到的商品列表
     */
    List<Product> findProuctByProductCategory(String productCategoryId);

    List<ProductCategory> findAllProductCategory();

    /**
     * 解除商品与其所有分类的关联，即删除关联关系。
     *
     * @param productId 商品。
     */
    void deleteByProduct(String productId);

    @Transactional
    void updateCategoryOfProduct(String productId, String newCategoryId);
}
