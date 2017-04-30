package com.railwayservice.productmanage.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.productmanage.dao.ProductCategoryDao;
import com.railwayservice.productmanage.dao.ProductCategoryRelaDao;
import com.railwayservice.productmanage.entity.ProductCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 产品分类服务类。
 *
 * @author lid
 * @date 2017.3.1
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final Logger logger = LoggerFactory.getLogger(ProductCategoryServiceImpl.class);

    private ProductCategoryDao productCategoryDao;

    private ProductCategoryRelaDao productCategoryRelaDao;

    @Autowired
    public void setProductCategoryDao(ProductCategoryDao productCategoryDao) {
        this.productCategoryDao = productCategoryDao;
    }

    @Autowired
    public void setProductCategoryRelaDao(ProductCategoryRelaDao productCategoryRelaDao) {
        this.productCategoryRelaDao = productCategoryRelaDao;
    }

    @Override
    public List<ProductCategory> findByMerchantId(String merchantId) {
        if (merchantId == null) {
            throw new AppException("merchantId参数不能为空！");
        }
        logger.info("进入服务层：获取商户的商品分类 ：商户ID：" + merchantId);

        return productCategoryDao.findByMerchantId(merchantId);
    }

    @Override
    @Transactional
    public ProductCategory addProductCategory(Merchant merchant, ProductCategory productCategory) {
        if (merchant == null || !StringUtils.hasText(merchant.getMerchantId()))
            throw new AppException("商户信息不能为空！");
        if (productCategory == null || !StringUtils.hasText(productCategory.getName()))
            throw new AppException("商品分类信息为空！");
        logger.info("进入服务层：添加商户的商品分类: 商户名称：" + merchant.getName());

        productCategory.setMerchantId(merchant.getMerchantId());

        // 如果没有指定顺序，将排在最后。
        if (productCategory.getSorter() == null) {
            Integer sorter = productCategoryDao.findMaxSorterOfMerchant(productCategory.getMerchantId());
            productCategory.setSorter(sorter == null ? 1 : (sorter + 1));
        }

        return productCategoryDao.save(productCategory);
    }

    @Override
    @Transactional
    public ProductCategory updateProductCategory(Merchant merchant, ProductCategory productCategory) {
        if (merchant == null || !StringUtils.hasText(merchant.getMerchantId()))
            throw new AppException("商户信息不能为空！");
        ProductCategory oldProductCategory = productCategoryDao.findOne(productCategory.getProductCategoryId());
        if (null == oldProductCategory)
            throw new AppException("未找到要修改的商品分类！");
        if (!merchant.getMerchantId().equals(oldProductCategory.getMerchantId()))
            throw new AppException("您不可以动别人的商品分类哦！");
        logger.info("进入服务层：修改商户的商品分类: 商户名称：" + merchant.getName());

        // 以下属性不为空时才更新。
        if (StringUtils.hasText(productCategory.getCategory()))
            oldProductCategory.setCategory(productCategory.getCategory());

        if (StringUtils.hasText(productCategory.getName()))
            oldProductCategory.setName(productCategory.getName());

        return productCategoryDao.save(oldProductCategory);
    }

    @Override
    @Transactional
    public void swapProductCategorySort(Merchant merchant, String categoryIdA, String categoryIdB) {
        if (merchant == null || !StringUtils.hasText(merchant.getMerchantId())
                || !StringUtils.hasText(categoryIdA) || !StringUtils.hasText(categoryIdB))
            throw new AppException("商户或分类不正确！");
        logger.info("进入服务层：别人家的分类还是让他自己排吧: 商户名称：" + merchant.getName());

        ProductCategory productCategoryA = productCategoryDao.findOne(categoryIdA);
        ProductCategory productCategoryB = productCategoryDao.findOne(categoryIdB);
        if (!merchant.getMerchantId().equals(productCategoryA.getMerchantId())
                || !merchant.getMerchantId().equals(productCategoryB.getMerchantId()))
            throw new AppException("别人家的分类还是让他自己排吧。");

        // 交换商品分类的顺序。
        Integer sorterA = productCategoryA.getSorter();
        productCategoryA.setSorter(productCategoryB.getSorter());
        productCategoryB.setSorter(sorterA);

        // 保存排序信息。
        productCategoryDao.save(productCategoryA);
        productCategoryDao.save(productCategoryB);
    }

    @Override
    @Transactional
    public void deleteProductCategory(Merchant merchant, String deleteCategoryId, String newCategoryId) {
        if (deleteCategoryId == null) {
            throw new AppException("productCategoryId参数不能为空！");
        }
        if (merchant == null || !StringUtils.hasText(merchant.getMerchantId()))
            throw new AppException("商户信息不能为空！");
        logger.info("进入服务层：删除商户的商品分类 ：商户名称：" + merchant.getName());

        ProductCategory oldProductCategory = productCategoryDao.findOne(deleteCategoryId);
        if (null == oldProductCategory) {
            throw new AppException("未找到要删除的商品分类！");
        }
        if (!merchant.getMerchantId().equals(oldProductCategory.getMerchantId()))
            throw new AppException("您不可以动别人的商品分类哦！");
        // 处理所有关联关系再删除分类。
        if (productCategoryRelaDao.countByProductCategoryId(oldProductCategory.getProductCategoryId()) > 0) {
            if (StringUtils.hasText(newCategoryId)) {
                productCategoryRelaDao.updateToNewCategory(oldProductCategory.getProductCategoryId(), newCategoryId);
            } else {
                throw new AppException("该分类下有商品时要指定商品的新分类才能删除哦。");
            }
        }

        productCategoryDao.delete(oldProductCategory);
    }

    @Override
    public ProductCategory findProductCategory(String productCategoryId) {
        if (!StringUtils.hasText(productCategoryId))
            throw new AppException("未指定查询条件");
        logger.info("进入服务层：查询商品分类 ：商户分类ID：" + productCategoryId);

        return productCategoryDao.findOne(productCategoryId);
    }

}
