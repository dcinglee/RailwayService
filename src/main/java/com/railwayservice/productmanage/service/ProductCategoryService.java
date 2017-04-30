package com.railwayservice.productmanage.service;

import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.productmanage.entity.ProductCategory;

import java.util.List;

/**
 * 商品分类服务接口。
 *
 * @author lid
 * @date 2017.3.1
 */
public interface ProductCategoryService {
    /**
     * 查找商户的所有商品分类
     *
     * @param merchantId
     * @return
     * @author lid
     * @date 2017.3.1
     */
    List<ProductCategory> findByMerchantId(String merchantId);

    /**
     * 校验并添加单个商品分类。
     *
     * @param merchant
     * @param productCategory 商品分类。
     * @return 添加成功的商品。
     */
    ProductCategory addProductCategory(Merchant merchant, ProductCategory productCategory);

    /**
     * 校验并更新单个商品。
     *
     * @param merchant
     * @param productCategory 商品分类。
     * @return 更新成功的商品。
     */
    ProductCategory updateProductCategory(Merchant merchant, ProductCategory productCategory);

    /**
     * 删除商品分类，该分类下的商品移动到新的分类。
     *
     * @param deleteCategoryId 要删除的分类。
     * @param newCategoryId    商品移动到新的分类。
     */
    void deleteProductCategory(Merchant merchant, String deleteCategoryId, String newCategoryId);

    /**
     * 根据商品分类ID查商品分类。
     *
     * @param productCategoryId 商品分类ID。
     * @return 更新成功的商品。
     */
    ProductCategory findProductCategory(String productCategoryId);

    /**
     * 商户交换两个分类的顺序。
     *
     * @param merchant    商户。
     * @param categoryIdA 分类A。
     * @param categoryIdB 分类B。
     */
    void swapProductCategorySort(Merchant merchant, String categoryIdA, String categoryIdB);
}
