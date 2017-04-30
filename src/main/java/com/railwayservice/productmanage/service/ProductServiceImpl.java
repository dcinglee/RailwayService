package com.railwayservice.productmanage.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.common.entity.ImageInfo;
import com.railwayservice.common.service.CommonService;
import com.railwayservice.merchantmanage.dao.MerchantDao;
import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.productmanage.dao.ProductCategoryDao;
import com.railwayservice.productmanage.dao.ProductDao;
import com.railwayservice.productmanage.dao.ProductMerStationDao;
import com.railwayservice.productmanage.entity.Product;
import com.railwayservice.productmanage.entity.ProductCategory;
import com.railwayservice.productmanage.entity.ProductCategoryRela;
import com.railwayservice.productmanage.vo.ProductVo;
import com.railwayservice.stationmanage.entity.RailwayStation;
import com.railwayservice.stationmanage.service.RailwayStationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 管理员服务类。
 *
 * @author Ewing
 */
@Service
public class ProductServiceImpl implements com.railwayservice.productmanage.service.ProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private ProductDao productDao;
    private MerchantDao merchantDao;
    private ProductCategoryDao productCategoryDao;
    private ProductCategoryRelaService productCategoryRelaService;
    private ProductMerStationDao productMerStationDao;
    private CommonService commonService;

    private RailwayStationService railwayStationService;

    @Autowired
    public void setRailwayStationService(RailwayStationService railwayStationService) {
        this.railwayStationService = railwayStationService;
    }

    @Autowired
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Autowired
    public void setProductCategoryDao(ProductCategoryDao productCategoryDao) {
        this.productCategoryDao = productCategoryDao;
    }

    @Autowired
    public void setProductCategoryRelaService(ProductCategoryRelaService productCategoryRelaService) {
        this.productCategoryRelaService = productCategoryRelaService;
    }

    @Autowired
    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    @Autowired
    public void setMerchantDao(MerchantDao merchantDao) {
        this.merchantDao = merchantDao;
    }

    @Autowired
    public void setProductMerStationDao(ProductMerStationDao productMerStationDao) {
        this.productMerStationDao = productMerStationDao;
    }

    @Override
    @Transactional
    public Product addProduct(Merchant merchant, Product product) {
        if (merchant == null || !StringUtils.hasText(merchant.getMerchantId()))
            throw new AppException("商户信息不能为空！");
        // 校验传入参数合法性。
        if (product == null)
            throw new AppException("商品对象不能为空！");
        if (!StringUtils.hasText(product.getCategoryId()))
            throw new AppException("所属分类ID不能为空！");
        if (!StringUtils.hasText(product.getName()))
            throw new AppException("商品名称不能为空！");
        if (!StringUtils.hasText(product.getIntroduction()))
            throw new AppException("商品描述不能为空！");
        if (product.getPrice() == null)
            throw new AppException("商品价格不能为空！");
        logger.info("进入服务层：新增商品: 商品名称：" + product.getName());

        ProductCategory productCategory = productCategoryDao.findOne(product.getCategoryId());
        if (productCategory == null || !merchant.getMerchantId().equals(productCategory.getMerchantId()))
            throw new AppException("商品分类不存在或商品分类不是您的！");

        // 如果没有指定顺序，将排在最后。
        if (product.getSorter() == null) {
            Integer sorter = productDao.findMaxSorterOfCategory(product.getCategoryId());
            product.setSorter(sorter == null ? 1 : (sorter + 1));
        }

        // 缺省属性给默认值。
        product.setState(ProductStatic.STATE_SALE_ON);
        product.setMerchantId(merchant.getMerchantId());
        product.setStationId(merchant.getStationId());
        product.setCreateDate(new Date());
        // 保存实体对象。
        product = productDao.save(product);

        // 保存商品与分类的关联。
        ProductCategoryRela productCategoryRela = new ProductCategoryRela();
        productCategoryRela.setProductId(product.getProductId());
        productCategoryRela.setProductCategoryId(productCategory.getProductCategoryId());
        productCategoryRelaService.addProductCategoryRela(productCategoryRela);

        return product;
    }

    @Override
    @Transactional
    public Product updateProduct(Merchant merchant, Product product) {
        if (merchant == null || !StringUtils.hasText(merchant.getMerchantId()))
            throw new AppException("商户信息不能为空！");
        // 校验传入参数合法性。
        if (product == null || !StringUtils.hasText(product.getProductId()))
            throw new AppException("请选择要更新的商品！");
        logger.info("进入服务层：更新商品： 商品名称：" + product.getName());

        // 商品是否存在。
        Product productOld = productDao.findOne(product.getProductId());
        if (productOld == null)
            throw new AppException("该商品不存在或已删除！");

        if (!merchant.getMerchantId().equals(productOld.getMerchantId()))
            throw new AppException("您不能修改别人的商品哦！");

        // 更新商品分类
        if (StringUtils.hasText(product.getCategoryId())) {
            // 验证新的商品分类。
            ProductCategory productCategory = productCategoryDao.findOne(product.getCategoryId());
            if (productCategory == null || !merchant.getMerchantId().equals(productCategory.getMerchantId()))
                throw new AppException("新分类不存在或新分类不是您的！");
            // 移动该商品到新的分类。
            productCategoryRelaService.updateCategoryOfProduct(product.getProductId(), product.getCategoryId());
        }

        // 更新需要修改的字段。
        if (StringUtils.hasText(product.getName()))
            productOld.setName(product.getName());
        if (product.getPrice() != null)
            productOld.setPrice(product.getPrice());
        if (StringUtils.hasText(product.getUnit()))
            productOld.setUnit(product.getUnit());
        if (product.getStock() != null)
            productOld.setStock(product.getStock());
        if (product.getState() != null)
            productOld.setState(product.getState());
        if (StringUtils.hasText(product.getIntroduction()))
            productOld.setIntroduction(product.getIntroduction());

        // 保存已存在的实体对象。
        return productDao.save(productOld);
    }

    @Override
    public Page<Product> queryProducts(String name, Pageable pageable) {
        logger.info("进入服务层：查询商品 商品名称：" + name);
        // JPA标准查询接口，使用Lambda表达式。root以商品类为根对象。
        Specification<Product> specification = (root, query, builder) -> {
            // 创建组合条件，默认1=1。
            Predicate predicate = builder.conjunction();
            // 如果名称不为空，添加到查询条件。
            if (StringUtils.hasText(name)) {
                predicate = builder.and(predicate, builder.like(root.get("name"), "%" + name + "%"));
            }
            return predicate;
        };
        // 调用JPA标准查询接口查询数据。
        return productDao.findAll(specification, pageable);
    }

    @Override
    @Transactional

    public void deleteProduct(String productId) {
        if (!StringUtils.hasText(productId))
            throw new AppException("请选择要删除的商品！");
        logger.info("进入服务层：删除商品 :商品id ：" + productId);

        // 商品是否存在。
        Product productOld = productDao.findOne(productId);
        if (productOld == null)
            throw new AppException("该商品不存在或已删除！");
        // 删除与分类的关联。
        productCategoryRelaService.deleteByProduct(productOld.getProductId());
        productDao.delete(productId);
    }

    @Override
    public Product findProductById(String productId) {
        if (!StringUtils.hasText(productId))
            throw new AppException("商品ID不能为空！");
        logger.info("进入服务层：获取商品:商品id ：" + productId);

        return productDao.findOne(productId);
    }

    @Override
    @Transactional
    public Product updateImage(Merchant merchant, String productId, InputStream inputStream) {
        // 校验传入参数合法性。
        if (!StringUtils.hasText(productId))
            throw new AppException("商品ID不能为空！");
        logger.info("进入服务层：给商品添加图片:商品id ：" + productId);

        // 商品是否存在。
        Product productOld = productDao.findOne(productId);
        if (productOld == null)
            throw new AppException("该商品不存在或已删除！");
        if (!merchant.getMerchantId().equals(productOld.getMerchantId()))
            throw new AppException("您不能修改别人的商品哦！");

        // 删除旧的图片。
        if (StringUtils.hasText(productOld.getImageId()))
            commonService.deleteImage(productOld.getImageId());

        // 保存图片信息。
        ImageInfo imageInfo = commonService.addImage(inputStream);

        // 更新商品的图片ID。
        productOld.setImageId(imageInfo.getImageId());
        return productDao.save(productOld);
    }

    @Override
    @Transactional
    public List<Product> findHotProductByRecommend(String stationId) {
        logger.info("查询热门商品。findHotProductByRecommend！stationId：" + stationId);
        List<Merchant> listMerchant = merchantDao.getByStationId(stationId);

        //返回的热门商品列表
        List<Product> listProduct = new ArrayList<Product>();

        //查找当前车站的所有商户，如果没有商户则默认查询深圳北站的热卖商品信息
        if (0 == listMerchant.size()) {
            logger.info("0 == listMerchant.size()");

            RailwayStation station = railwayStationService.findByStationName(ProductStatic.SZB);
            if (null == station) {
                logger.info("null == station");
                return null;
            }
            logger.info("null != station!StationName:" + station.getStationName());
            stationId = station.getStationId();

            listMerchant = merchantDao.getByStationId(stationId);
        }

        logger.info("listMerchant.size():" + listMerchant.size());

        //遍历出listMerchant里面的信息
        for (Merchant merchant : listMerchant) {
            List<Product> listSubProduct = productDao.findHotProductByRecommend(merchant.getMerchantId());
            listProduct.addAll(0, listSubProduct);
        }
        return listProduct;
    }

    @Override
    @Transactional
    public List<Product> findProductByRecommend(String stationId) {
        if (!StringUtils.hasText(stationId)) {
            throw new AppException("所属站点不能为空！");
        }
        logger.info("查询已推荐商品:车站id ：" + stationId);

        List<Product> listProduct = new ArrayList<Product>();
        //通过站点ID来查询商户对象
        List<Merchant> listMerchant = merchantDao.getByStationId(stationId);
        if (listMerchant == null) {
            throw new AppException("未查到该站点推荐的商品信息！");
        }
        //遍历出listMerchant里面的信息
        for (Merchant merchant : listMerchant) {
            List<Product> listSubProduct = productDao.findProductByRecommend(merchant.getMerchantId());
            for (Product product : listSubProduct) {
                //设置商品所属商户的logo链接地址
                product.setLogoImageUrl(merchant.getImageUrl());
                product.setServiceTypeId(merchant.getServiceTypeId());
            }
            listProduct.addAll(0, listSubProduct);
        }

        return listProduct;
    }

    @Override
    @Transactional
    public List<Product> findProductByMerchantId(String merchantId) {
        if (!StringUtils.hasText(merchantId)) {
            throw new AppException("商品所属商户ID不能为空！");
        }
        logger.info("查询商户的所有商品: 商户ID：" + merchantId);

        List<Product> product = productDao.findProductByMerchantId(merchantId);
        if (product == null) {
            throw new AppException("未能查到该商品所属商户！");
        }
        return product;
    }

    @Override
    public List<ProductVo> findAllProduct(String stationId, String serviceType, String merchantName, String productName,
                                          int recommend) {
        logger.info("查询所有商品的信息：车站ID：" + stationId + "商户名称：" + merchantName);
        return productMerStationDao.findProduct(stationId, serviceType, merchantName, productName, recommend);
    }

    @Override
    @Transactional
    public Product updateProductStatus(Merchant merchant, Product product) {
        // 校验传入参数合法性。
        if (product == null || !StringUtils.hasText(product.getProductId()))
            throw new AppException("请选择要更新的商品！");
        if (merchant == null || !StringUtils.hasText(merchant.getMerchantId()))
            throw new AppException("商户信息不能为空！");
        logger.info("进入服务层：更新商品状态：" + (product == null ? "" : product.getProductId()));

        // 商品是否存在。
        Product productOld = productDao.findOne(product.getProductId());
        if (productOld == null)
            throw new AppException("该商品不存在或已删除！");

        if (!merchant.getMerchantId().equals(productOld.getMerchantId()))
            throw new AppException("您不能修改别人的商品哦！");

        // 更新需要修改的字段。
        productOld.setState(product.getState());

        // 保存已存在的实体对象。
        return productDao.save(productOld);
    }

    @Override
    public List<Product> findByProductCategoryId(String productCategoryId) {
        if (!StringUtils.hasText(productCategoryId)) {
            throw new AppException("productCategoryId不能为空！");
        }
        logger.info("查询当前分类的所有商品：商品分类ID：" + productCategoryId);

        return productCategoryRelaService.findProuctByProductCategory(productCategoryId);
    }

    /**
     * 商户查询自己的所有商品分类及商品。
     *
     * @param merchant 商户信息。
     * @return 商户信息带商品分类及商品。
     */
    @Override
    public Merchant getCategoryProducts(Merchant merchant) {
        if (merchant == null)
            throw new AppException("商户不能为空。");
        if (!StringUtils.hasText(merchant.getMerchantId()))
            throw new AppException("商户ID不能为空！");
        logger.info("商户查询自己的所有商品分类及商品：: 商户名称：" + merchant.getName() + "商户ID：" + merchant.getMerchantId());

        // 获取商户的所有商品分类
        List<ProductCategory> categories = productCategoryDao.findByMerchantId(merchant.getMerchantId());
        merchant.setCategories(categories);

        // 获取分类下的所有商品
        for (ProductCategory category : categories) {
            List<Product> products = productDao.findByProductCategory(category.getProductCategoryId());
            category.setProducts(products);
        }

        return merchant;
    }

    /**
     * 更新车站商品信息，即商品的推荐位置
     */
    @Override
    public Product updateProductRecommend(ProductVo productvo) {
        if (!StringUtils.hasText(productvo.getProductId())) {
            throw new AppException("产品id不能为空");
        }
        if (productvo == null || productvo.getRecommend() == null) {
            throw new AppException("产品推荐类型不能为空");
        }
        logger.info("更新产品信息: 商品名称：" + productvo.getProductName());

        Product product = productDao.findOne(productvo.getProductId());
        product.setRecommend(productvo.getRecommend());
        product.setScore(productvo.getScore());
        product.setHits(productvo.getHits());
        product.setSales(productvo.getSales());
        return productDao.save(product);
    }

}
