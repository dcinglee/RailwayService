package com.railwayservice.productmanage.dao;

import com.railwayservice.productmanage.entity.ProductCategoryRela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author lidx
 * @date 2017/3/1
 * @describe 商品与商品分类访问数据库接口
 */
public interface ProductCategoryRelaDao extends JpaRepository<ProductCategoryRela, String>, JpaSpecificationExecutor<ProductCategoryRela> {

    /**
     * 根据商品分类获取商品与商品分类关联
     *
     * @param productCategoryId 商品分类
     * @return 商品与商品分类关联列表
     */
    List<ProductCategoryRela> findByProductCategoryId(String productCategoryId);

    /**
     * 根据relaId查找对应的ProductCategoryRela记录
     *
     * @param relaId 商品与商品分类关联ID
     * @return AdminRoleRela
     * @author lid
     * @date 2017.2.13
     */
    ProductCategoryRela findProductCategoryRelaByrelaId(String relaId);

    /**
     * 解除商品与其所有分类的关联，即删除关联关系。
     *
     * @param productId 商品。
     */
    @Modifying
    @Query("delete from ProductCategoryRela  pcr where pcr.productId = :productId")
    void deleteByProduct(@Param("productId") String productId);

    /**
     * 统计该分类下的商品数量，即关联数量。
     *
     * @return 商品与分类关联数量。
     */
    Long countByProductCategoryId(String productCategoryId);

    /**
     * 将该分类下的商品移动到其他分类。
     *
     * @param deleteCategoryId 分类ID。
     */
    @Modifying
    @Query("update ProductCategoryRela  pcr set pcr.productCategoryId" +
            " = :newCategoryId where pcr.productCategoryId = :deleteCategoryId")
    void updateToNewCategory(@Param("deleteCategoryId") String deleteCategoryId, @Param("newCategoryId") String newCategoryId);

    /**
     * 将商品的所属分类更新为新分类。
     *
     * @param productId 商品ID。
     */
    @Modifying
    @Query("update ProductCategoryRela  pcr set pcr.productCategoryId" +
            " = :newCategoryId where pcr.productId = :productId")
    void updateCategoryOfProduct(@Param("productId") String productId, @Param("newCategoryId") String newCategoryId);
}
