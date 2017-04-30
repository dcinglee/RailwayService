package com.railwayservice.productmanage.dao;

import com.railwayservice.productmanage.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 商品数据库访问接口。
 *
 * @author Ewing
 */
public interface ProductDao extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {

    /**
     * 根据ProductId查找商品并赋值图片url
     *
     * @param productId
     * @return Product
     * @author lid
     */
    Product findProductByProductId(String productId);

    /**
     * 查询商户的热门商品
     *
     * @author lid
     * @date 2017.3.11
     */
    List<Product> findHotProductByRecommend(String merchantId);

    /**
     * 查询商户已推荐商品信息
     *
     * @param recommend
     * @return list
     */
    List<Product> findProductByRecommend(String merchantId);

    /**
     * 查询商品所属商户信息
     *
     * @param merchantId
     * @return list
     */
    List<Product> findProductByMerchantId(String merchantId);

//    @Query("select r.stationId,r.stationName, p.productName,m.merchantName,p.recommend from Product p left join Merchant m on p.merchantId=m.merchantId left join RailwayStation r on m.stationId=r.stationId")
//    List<Productvo> findallList();

    /**
     * 根据商户的商品分类查询分类下的所有商品及商品图片。
     *
     * @param productCategoryId 分类ID。
     * @return 该分类下的所有商品。
     */
    List<Product> findByProductCategory(String productCategoryId);

    /**
     * 查询商户的分类的商品最大序号。
     *
     * @param categoryId 商户ID。
     * @return 最大序号。
     */
    Integer findMaxSorterOfCategory(String categoryId);

}
