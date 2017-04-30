package com.railwayservice.productmanage;

import com.railwayservice.application.util.RandomString;
import com.railwayservice.common.dao.ImageInfoDao;
import com.railwayservice.common.entity.ImageInfo;
import com.railwayservice.merchantmanage.dao.MerchantDao;
import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.productmanage.dao.ProductCategoryDao;
import com.railwayservice.productmanage.dao.ProductCategoryRelaDao;
import com.railwayservice.productmanage.dao.ProductDao;
import com.railwayservice.productmanage.entity.Product;
import com.railwayservice.productmanage.entity.ProductCategory;
import com.railwayservice.productmanage.entity.ProductCategoryRela;
import com.railwayservice.productmanage.service.ProductService;
import com.railwayservice.productmanage.service.ProductStatic;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;

/**
 * 商户管理服务测试类。
 *
 * @author Ewing
 * @date 2017/2/15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestProduct {
    private Logger logger = LoggerFactory.getLogger(TestProduct.class);

    private ProductService productService;
    private ProductDao productDao;
    private ImageInfoDao imageInfoDao;
    private MerchantDao merchantDao;
    private ProductCategoryDao productCategoryDao;
    private ProductCategoryRelaDao productCategoryRelaDao;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Autowired
    public void setImageInfoDao(ImageInfoDao imageInfoDao) {
        this.imageInfoDao = imageInfoDao;
    }

    @Autowired
    public void setMerchantDao(MerchantDao merchantDao) {
        this.merchantDao = merchantDao;
    }

    @Autowired
    public void setProductCategoryDao(ProductCategoryDao productCategoryDao) {
        this.productCategoryDao = productCategoryDao;
    }

    @Autowired
    public void setProductCategoryRelaDao(ProductCategoryRelaDao productCategoryRelaDao) {
        this.productCategoryRelaDao = productCategoryRelaDao;
    }

    public ImageInfo createImageInfo() {
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setUrl("/images/default.jpg");
        return imageInfoDao.save(imageInfo);
    }

    private Merchant createMerchant() {
        Merchant merchant = new Merchant();
        merchant.setName("星巴克" + RandomString.randomString(5));
        merchant = merchantDao.save(merchant);
        return merchant;
    }

    public Product createProduct(Merchant merchant, ImageInfo imageInfo) {
        Product product = new Product();
        product.setName("一杯咖啡" + RandomString.randomString(5));
        product.setMerchantId(merchant.getMerchantId());
        product.setImageId(imageInfo.getImageId());
        product.setRecommend(ProductStatic.Product_Recommend_Hot);
        return productDao.save(product);
    }

    public ProductCategory createProductCategory(Merchant merchant) {
        ProductCategory category = new ProductCategory();
        category.setName("饮料");
        category.setMerchantId(merchant.getMerchantId());
        return productCategoryDao.save(category);
    }

    public ProductCategoryRela createProductCategoryRela(Product product, ProductCategory category) {
        ProductCategoryRela productCategoryRela = new ProductCategoryRela();
        productCategoryRela.setProductId(product.getProductId());
        productCategoryRela.setProductCategoryId(category.getProductCategoryId());
        return productCategoryRelaDao.save(productCategoryRela);
    }

    /**
     * 传多少删多少，不限顺序，不限个数，支持需要的几种类型。。
     */
    public void cleanData(Object... objects) {
        for (Object object : objects) {
            if (object != null) {
                if (object.getClass().equals(ImageInfo.class))
                    imageInfoDao.delete((ImageInfo) object);

                else if (object.getClass().equals(Merchant.class))
                    merchantDao.delete((Merchant) object);

                else if (object.getClass().equals(Product.class))
                    productDao.delete((Product) object);

                else if (object.getClass().equals(ProductCategory.class))
                    productCategoryDao.delete((ProductCategory) object);

                else if (object.getClass().equals(ProductCategoryRela.class))
                    productCategoryRelaDao.delete((ProductCategoryRela) object);
            }
        }
    }

    @Test
    public void addProductTest() {
        Product productAdd = new Product();
        productAdd.setName("一杯咖啡875265");
        productAdd = productService.addProduct(null, productAdd);

        cleanData(productAdd);

        Assert.assertTrue(StringUtils.hasText(productAdd.getProductId()));
    }

    @Test
    public void updateProductTest() {
        String name = "两杯咖啡8755454";
        Product product = createProduct(new Merchant(), new ImageInfo());
        product.setName(name);
        product = productService.updateProduct(null, product);

        cleanData(product);

        Assert.assertTrue(name.equals(product.getName()));
    }

    @Test
    public void queryProductTest() {
        Product product = createProduct(new Merchant(), new ImageInfo());

        Pageable pageable = new PageRequest(0, 10);
        Page products = productService.queryProducts(product.getName(), pageable);

        cleanData(product);

        Assert.assertTrue(products.getTotalElements() > 0);
    }

    @Test
    public void deleteProductTest() {
        Product product = createProduct(new Merchant(), new ImageInfo());

        productService.deleteProduct(product.getProductId());
        Product productResult = productService.findProductById(product.getProductId());

        cleanData(product);

        Assert.assertNull(productResult);
    }

    @Test
    public void queryProductMerchantTest() {
        Merchant merchant = createMerchant();
        ImageInfo imageInfo = createImageInfo();
        Product product = createProduct(merchant, imageInfo);
        ProductCategory productCategory = createProductCategory(merchant);
        ProductCategoryRela productCategoryRela = createProductCategoryRela(product, productCategory);

        productService.getCategoryProducts(merchant);

        cleanData(merchant, imageInfo, product, productCategory, productCategoryRela);

        Assert.assertTrue(merchant.getCategories().size() > 0 && merchant.getCategories().get(0).getProducts().size() > 0);
    }

}
