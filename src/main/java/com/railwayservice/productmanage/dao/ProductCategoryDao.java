package com.railwayservice.productmanage.dao;

import com.railwayservice.productmanage.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 商品分类数据库接口
 *
 * @author lid
 * @date 2017.3.1
 */
public interface ProductCategoryDao extends JpaRepository<ProductCategory, String>, JpaSpecificationExecutor<ProductCategory> {

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
     * 查询商户下的分类的最大序号。
     *
     * @param merchantId 商户ID。
     * @return 最大序号。
     */
    @Query("select max(pc.sorter) from ProductCategory pc where pc.merchantId=?1")
    Integer findMaxSorterOfMerchant(String merchantId);
}
