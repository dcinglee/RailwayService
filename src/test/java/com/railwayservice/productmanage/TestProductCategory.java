package com.railwayservice.productmanage;

import com.railwayservice.productmanage.entity.ProductCategory;
import com.railwayservice.productmanage.service.ProductCategoryService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author lidx
 * @date 2017/3/3
 * @describe
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestProductCategory {

    private ProductCategoryService productCategoryService;

    @Autowired
    public void setProductCategoryService(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    private ProductCategory productCategory;

    @Before
    public void before() {
        productCategory = new ProductCategory();
        productCategory.setName("123");
        productCategory.setMerchantId("123");
        productCategory = productCategoryService.addProductCategory(null, productCategory);
    }

    @After
    public void after() {
        if (productCategory != null)
            productCategoryService.deleteProductCategory(null, productCategory.getProductCategoryId(), null);
    }

    @Test
    public void addProductCategory() {
        productCategory = new ProductCategory();
        productCategory.setName("456");
        productCategory.setMerchantId("456");
        productCategory = productCategoryService.addProductCategory(null, productCategory);
        Assert.assertTrue(productCategory != null);
    }

    @Test
    public void deleteProductCategory() {
        productCategoryService.deleteProductCategory(null, productCategory.getProductCategoryId(), null);
        Assert.assertTrue(productCategory.getProductCategoryId() != null);
        productCategory = null;
    }

    @Test
    public void updateProductCategory() {
        productCategory = productCategoryService.updateProductCategory(null, productCategory);
        Assert.assertTrue(productCategory != null);
    }

    @Test
    public void queryProductCategory() {

        productCategory = productCategoryService.findProductCategory(productCategory.getProductCategoryId());
        Assert.assertTrue(productCategory != null);
    }

}

