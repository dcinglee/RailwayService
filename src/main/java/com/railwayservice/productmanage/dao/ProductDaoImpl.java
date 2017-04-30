package com.railwayservice.productmanage.dao;

import com.railwayservice.common.dao.BaseDaoImpl;
import com.railwayservice.productmanage.entity.Product;
import com.railwayservice.productmanage.service.ProductStatic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

/**
 * 产品数据访问。
 *
 * @author Ewing
 * @date 2017/3/9
 */
public class ProductDaoImpl extends BaseDaoImpl {

    private final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    public List<Product> findHotProductByRecommend(String merchantId) {
        logger.info("findHotProductByRecommend!merchantId:" + merchantId);

        StringBuilder sql = new StringBuilder("select p.*, i.url as imageUrl, t.stationName as stationName, m.serviceTypeId as serviceTypeId " +
                " from Product p left join ImageInfo i on p.imageId = i.imageId"
                + " left join Merchant m on m.merchantId = p.merchantId left join RailwayStation t "
                + " on t.stationId = m.stationId " +
                " where p.recommend = " + ProductStatic.Product_Recommend_True + " and p.state = " + ProductStatic.STATE_SALE_ON + " and p.merchantId = ?");

        return this.getOperations().query(sql.toString(), BeanPropertyRowMapper.newInstance(Product.class), merchantId);
    }

    /**
     * 查询商户的所有热卖商品
     *
     * @param merchantId
     * @return list
     */
    public List<Product> findProductByRecommend(String merchantId) {
        logger.info("findProductByRecommend!merchantId:" + merchantId);

        StringBuilder sql = new StringBuilder("select p.*, i.url as imageUrl" +
                " from Product p left join ImageInfo i on p.imageId = i.imageId" +
                " where p.recommend = " + ProductStatic.Product_Recommend_Hot + " and p.state = " + ProductStatic.STATE_SALE_ON + " and p.merchantId = ?");

        return this.getOperations().query(sql.toString(), BeanPropertyRowMapper.newInstance(Product.class), merchantId);
    }

    /**
     * 查询商户的所有商品信息
     *
     * @param merchantId
     * @return list
     */
    public List<Product> findProductByMerchantId(String merchantId) {
        logger.info("findProductByMerchantId!merchantId:" + merchantId);

        StringBuilder sql = new StringBuilder("select p.*, i.url as imageUrl" +
                " from Product p left join ImageInfo i on p.imageId = i.imageId" +
                " where p.merchantId = ?");

        return this.getOperations().query(sql.toString(), BeanPropertyRowMapper.newInstance(Product.class), merchantId);
    }

    /**
     * 根据商户的商品分类查询分类下的所有商品及商品图片。
     *
     * @param productCategoryId 分类ID。
     * @return 该分类下的所有商品。
     */
    public List<Product> findByProductCategory(String productCategoryId) {
        StringBuilder sql = new StringBuilder("SELECT\n" +
                "  p.productId,\n" +
                "  p.createDate,\n" +
                "  p.hits,\n" +
                "  p.introduction,\n" +
                "  p.sales,\n" +
                "  p.score,\n" +
                "  p.state,\n" +
                "  p.stock,\n" +
                "  p.unit,\n" +
                "  p.imageId,\n" +
                "  p.merchantId,\n" +
                "  p.name,\n" +
                "  p.note,\n" +
                "  p.price,\n" +
                "  p.seriesNo,\n" +
                "  p.sorter,\n" +
                "  p.recommend,\n" +
                "  img.url AS imageUrl\n" +
                "FROM ProductCategoryRela pcr\n" +
                "  JOIN Product p ON pcr.productId = p.productId\n" +
                "  LEFT JOIN ImageInfo img ON img.imageId = p.imageId\n" +
                "WHERE pcr.productCategoryId = ?");
        logger.info("根据商户的商品分类查询分类下的所有商品及商品图片：sql：" + sql);
        return this.getOperations().query(sql.toString(), BeanPropertyRowMapper.newInstance(Product.class), productCategoryId);
    }

    /**
     * 根据ProductId查找商品并赋值图片url
     *
     * @param productId
     * @return Product
     * @author lid
     */
    public Product findProductByProductId(String productId) {
        logger.info("findProductByProductId!productId:" + productId);

        StringBuilder sql = new StringBuilder("select " +
                "  p.productId,\n" +
                "  p.createDate,\n" +
                "  p.hits,\n" +
                "  p.introduction,\n" +
                "  p.sales,\n" +
                "  p.score,\n" +
                "  p.state,\n" +
                "  p.stock,\n" +
                "  p.unit,\n" +
                "  p.imageId,\n" +
                "  p.merchantId,\n" +
                "  p.name,\n" +
                "  p.note,\n" +
                "  p.price,\n" +
                "  p.seriesNo,\n" +
                "  p.sorter,\n" +
                "  p.recommend\n" + ", i.url as imageUrl" +
                " from Product p left join ImageInfo i on p.imageId = i.imageId" +
                " where p.state = " + ProductStatic.STATE_SALE_ON
                + " and p.productId = ?");

        return this.findOneObject(sql.toString(), Product.class, productId);
    }

    public Integer findMaxSorterOfCategory(String categoryId) {
        StringBuilder sql = new StringBuilder("SELECT max(p.sorter) FROM Product p JOIN ProductCategoryRela" +
                " pcr ON  p.productId = pcr.productId WHERE pcr.productCategoryId = ?");
        logger.info("查询商户的分类的商品最大序号：sql：" + sql.toString() + "\n参数：" + categoryId);
        return this.getOperations().queryForObject(sql.toString(), Integer.class, categoryId);
    }

}
