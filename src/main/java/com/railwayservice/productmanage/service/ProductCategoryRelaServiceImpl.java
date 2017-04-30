package com.railwayservice.productmanage.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.productmanage.dao.ProductCategoryRelaDao;
import com.railwayservice.productmanage.dao.ProductDao;
import com.railwayservice.productmanage.entity.Product;
import com.railwayservice.productmanage.entity.ProductCategory;
import com.railwayservice.productmanage.entity.ProductCategoryRela;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 产品分类服务类。
 *
 * @author lidx
 * @date 2017.3.2
 */
@Service
public class ProductCategoryRelaServiceImpl implements ProductCategoryRelaService {
    private final Logger logger = LoggerFactory.getLogger(ProductCategoryRelaServiceImpl.class);

    private ProductDao productDao;

    private ProductCategoryRelaDao productCategoryRelaDao;

    @Autowired
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Autowired
    public void setProductCategoryRelaDao(ProductCategoryRelaDao productCategoryRelaDao) {
        this.productCategoryRelaDao = productCategoryRelaDao;
    }

    @Override
    @Transactional
    public ProductCategoryRela addProductCategoryRela(ProductCategoryRela productCategoryRela) {
        if (productCategoryRela == null) {
            throw new AppException("商品商品分类关联信息不能为空！");
        }
        logger.info("产品分类服务层：添加产品分类：产品分类id：" + productCategoryRela.getProductCategoryId());

        //添加默认信息
        productCategoryRela.setCreateDate(new Date());

        //保存实体对象
        return productCategoryRelaDao.save(productCategoryRela);
    }

    @Override
    public List<Product> findProuctByProductCategory(String productCategoryId) {
        if (productCategoryId == null) {
            throw new AppException("请指定查询条件");
        }
        logger.info("产品分类服务层：查询产品分类：产品分类id：" + productCategoryId);

        List<ProductCategoryRela> listProductCategoryRela = productCategoryRelaDao.findByProductCategoryId(productCategoryId);
        List<Product> listProduct = new ArrayList<Product>();
        if (0 == listProductCategoryRela.size()) {
            return listProduct;
        }
        for (ProductCategoryRela rela : listProductCategoryRela) {
            Product product = productDao.findProductByProductId(rela.getProductId());
            if (null != product) {
                listProduct.add(product);
            }
        }
        return listProduct;
    }

    @Override
    public List<ProductCategory> findAllProductCategory() {
        return null;
    }

    @Override
    @Transactional
    public void deleteByProduct(@Param("productId") String productId) {
        logger.info("产品分类服务层：删除产品分类：产品ID：" + productId);
        productCategoryRelaDao.deleteByProduct(productId);
    }

    @Override
    @Transactional
    public void updateCategoryOfProduct(String productId, String newCategoryId) {
        logger.info("产品分类服务层：更新产品分类：产品ID：" + productId);
        productCategoryRelaDao.updateCategoryOfProduct(productId, newCategoryId);
    }

}
