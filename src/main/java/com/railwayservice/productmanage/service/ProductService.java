package com.railwayservice.productmanage.service;

import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.productmanage.entity.Product;
import com.railwayservice.productmanage.vo.ProductVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.InputStream;
import java.util.List;

/**
 * 商品服务接口。
 *
 * @author Ewing
 */
public interface ProductService {

    /**
     * 校验并添加单个商品。
     *
     * @param merchant
     * @param product  商品
     * @return 添加成功的商品。
     */
    Product addProduct(Merchant merchant, Product product);

    /**
     * 校验并更新单个商品。
     *
     * @param merchant
     * @param product  商品。
     * @return 更新成功的商品。
     */
    Product updateProduct(Merchant merchant, Product product);

    /**
     * 查询所有商品。
     *
     * @param name     商品名称。
     * @param pageable 分页参数。
     * @return 分页数据。
     */
    Page<Product> queryProducts(String name, Pageable pageable);

    /**
     * 删除单个商品。
     *
     * @param productId 商品ID。
     */
    void deleteProduct(String productId);

    /**
     * 根据商品ID获取商品信息。
     *
     * @param productId 商品ID。
     * @return 商品。
     */
    Product findProductById(String productId);

    /**
     * 更新商品图片信息。
     */
    Product updateImage(Merchant merchant, String productId, InputStream inputStream);

    /**
     * 查询热门商品
     *
     * @author lid
     * @date 2017.3.11
     */
    List<Product> findHotProductByRecommend(String stationId);

    /**
     * 查询已推荐的商品
     */
    List<Product> findProductByRecommend(String stationId);

    /**
     * 查询所属商户商品
     */
    List<Product> findProductByMerchantId(String merchantId);

    /**
     * 根据商品分类查找所有商品
     *
     * @param productCategoryId
     * @return
     * @author lid
     * @date 2017.3.1
     */
    List<Product> findByProductCategoryId(String productCategoryId);

    /**
     * 商户查询自己的所有商品分类及商品。
     *
     * @param merchant 商户信息。
     * @return 商户信息带商品分类及商品。
     */
    Merchant getCategoryProducts(Merchant merchant);

    /**
     * 联合查询站点的商品信息以及所属商家和车站
     *
     * @param stationId
     * @param serviceType
     * @param merchantName
     * @param productName
     * @param recommend
     * @return List
     */
    List<ProductVo> findAllProduct(String stationId, String serviceType, String merchantName, String productName, int recommend);

    /**
     * 更新站点的商品信息，主要是推荐类型
     *
     * @param productvo
     * @return
     */
    Product updateProductRecommend(ProductVo productvo);

    /**
     * 商户更新商品状态，开售、停售、下架等。
     */
    Product updateProductStatus(Merchant merchant, Product product);

}
