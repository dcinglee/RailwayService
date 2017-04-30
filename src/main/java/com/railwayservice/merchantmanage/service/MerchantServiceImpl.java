package com.railwayservice.merchantmanage.service;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.util.EncodeUtil;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.common.entity.ImageInfo;
import com.railwayservice.common.service.CommonService;
import com.railwayservice.merchantmanage.dao.MerchantDao;
import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.productmanage.entity.Product;
import com.railwayservice.productmanage.entity.ProductCategory;
import com.railwayservice.productmanage.service.ProductCategoryService;
import com.railwayservice.productmanage.service.ProductService;
import com.railwayservice.serviceprovider.entity.ServiceType;
import com.railwayservice.serviceprovider.service.ServiceTypeService;
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
import java.util.*;

/**
 * 管理员服务类。
 *
 * @author Ewing
 */
@Service
public class MerchantServiceImpl implements MerchantService {
    private final Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);

    private CommonService commonService;

    private MerchantDao merchantDao;

    private ProductCategoryService productCategoryService;

    private ProductService productService;

    private ServiceTypeService serviceTypeService;

    @Autowired
    public void setServiceTypeService(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
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
    public void setProductCategoryService(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 校验商户的合法性。
     *
     * @param merchant 商户。
     */
    private void validateMerchant(Merchant merchant) {
        logger.info("进入服务层：校验商户。");
        if (merchant == null)
            throw new AppException("商户对象不能为空！");
        if (!StringUtils.hasText(merchant.getStationId()))
            throw new AppException("所属车站ID不能为空！");
        if (!StringUtils.hasText(merchant.getName()))
            throw new AppException("商户名称不能为空！");
        if (!StringUtils.hasText(merchant.getAccount()))
            throw new AppException("商户账号不能为空！");
        if (!StringUtils.hasText(merchant.getPassword()))
            throw new AppException("商户密码不能为空！");
        if (!StringUtils.hasText(merchant.getLinkman()))
            throw new AppException("商户联系人不能为空！");
        if (!StringUtils.hasText(merchant.getPhoneNo()))
            throw new AppException("商户电话号码不能为空！");
        if (!StringUtils.hasText(merchant.getAddress()))
            throw new AppException("商户地址不能为空！");
    }

    @Override
    @Transactional
    public Merchant addMerchant(Admin currentAdmin, Merchant merchant) {
        // 校验传入参数合法性。
        validateMerchant(merchant);
        logger.info("进入服务层：新增商户：所属管理员：" + currentAdmin.getName() + "商户名称：" + merchant.getName());

        //有所属车站的管理员只能新增该车站的管理员
        if (currentAdmin != null && currentAdmin.getBelongId() != null && !"".equals(currentAdmin.getBelongId().trim())) {
            merchant.setStationId(currentAdmin.getBelongId());
        }
        // 防止与已有的对象重复。
        if (merchantDao.countByAccount(merchant.getAccount()) > 0)
            throw new AppException("商户账号已被使用！");
        if (merchantDao.countByNameAndAddress(merchant.getName(), merchant.getAddress()) > 0)
            throw new AppException("商户已存在！");
        if (merchantDao.countByPhoneNo(merchant.getPhoneNo()) > 0)
            throw new AppException("该手机号已存在！");
        if (merchant.getEvaluate() > 5)
            throw new AppException("商户评价不可超过5！");

        // 密码存储前需要加密，使用不变的账号作为盐。
        merchant.setPassword(EncodeUtil.encodePassword(merchant.getPassword(), merchant.getAccount()));



        // 缺省属性给默认值。
        merchant.setCreateDate(new Date());
        // 默认营业时间8点到22点
        merchant.setStartTime(80000);
        merchant.setStopTime(220000);

        // 保存实体对象。
        merchantDao.save(merchant);

        ProductCategory productCategory = new ProductCategory();
        productCategory.setMerchantId(merchant.getMerchantId());
        productCategory.setSorter(1);
        productCategory.setName("热销商品");
        productCategory.setCategory("当下最热门畅销的商品！");
        productCategoryService.addProductCategory(merchant,productCategory);

        return merchant;
    }

    @Override
    @Transactional
    public Merchant updateMerchant(Admin currentAdmin, Merchant merchant) {
        // 校验传入参数合法性。
        validateMerchant(merchant);

        if (!StringUtils.hasText(merchant.getMerchantId()))
            throw new AppException("请选择要更新的商户！");
        logger.info("进入服务层：更新商户：所属管理员：" + currentAdmin.getName() + "商户名称：" + merchant.getName());

        // 商户是否存在。
        Merchant merchantOld = merchantDao.findOne(merchant.getMerchantId());
        if (merchantOld == null)
            throw new AppException("该商户不存在或已删除！");
        if (merchantDao.countByPhoneNo(merchantOld.getPhoneNo()) > 1)
            throw new AppException("该手机号已存在！");
        if (merchant.getEvaluate() > 5)
            throw new AppException("商户评价不可超过5！");

        //有所属车站的管理员只能新增该车站的管理员
        if (currentAdmin != null && currentAdmin.getBelongId() != null && !"".equals(currentAdmin.getBelongId().trim())) {
//    		admin.setBelongId(currentAdmin.getBelongId());
            if (merchant.getStationId() == null || !merchant.getStationId().equals(currentAdmin.getBelongId())) {
                throw new AppException("你不允许修改其它车站的管理员");
            }
        }

        // 如果旧密码和新密码不同，则密码被修改，加密保存新密码。
        String password = merchant.getPassword();
        if (!password.equals(merchantOld.getPassword()))
            merchantOld.setPassword(EncodeUtil.encodePassword(password, merchantOld.getAccount()));
        // 更新需要修改的字段。
        merchantOld.setName(merchant.getName());
        merchantOld.setPhoneNo(merchant.getPhoneNo());
        merchantOld.setAddress(merchant.getAddress());
        merchantOld.setIntroduction(merchant.getIntroduction());
        merchantOld.setLinkman(merchant.getLinkman());
        merchantOld.setEmail(merchant.getEmail());
        merchantOld.setServiceTypeId(merchant.getServiceTypeId());
        merchantOld.setStationId(merchant.getStationId());
        merchantOld.setEvaluate(merchant.getEvaluate());
        merchantOld.setSailsInMonth(merchant.getSailsInMonth());
        merchantOld.setStatus(merchant.getStatus());
        // 保存已存在的实体对象。
        return merchantDao.save(merchantOld);
    }

    @Override
    public Page<Merchant> queryMerchants(Admin currentAdmin, String name, String phoneNo, Pageable pageable) {
        logger.info("进入服务层：查询商户。");
        // JPA标准查询接口，使用Lambda表达式。root以商户类为根对象。
        Specification<Merchant> specification = (root, query, builder) -> {
            // 创建组合条件，默认1=1。
            Predicate predicate = builder.conjunction();

            if (currentAdmin.getBelongId() != null && !"".equals(currentAdmin.getBelongId().trim())) {
                predicate = builder.and(predicate, builder.equal(root.get("stationId"), currentAdmin.getBelongId()));
            }
            // 如果名称不为空，添加到查询条件。
            if (StringUtils.hasText(name)) {
                predicate = builder.and(predicate, builder.like(root.get("name"), "%" + name + "%"));
            }
            // 如果电话号码不为空，添加到查询条件。
            if (StringUtils.hasText(phoneNo)) {
                predicate = builder.and(predicate, builder.like(root.get("phoneNo"), "%" + phoneNo + "%"));
            }
            return predicate;
        };
        // 调用JPA标准查询接口查询数据。
        return merchantDao.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public void deleteMerchant(Admin currentAdmin, String merchantId) {
        if (!StringUtils.hasText(merchantId))
            throw new AppException("请选择要删除的商户！");
        logger.info("进入服务层：删除商户：所属管理员：" + currentAdmin.getName() + "商户id：" + merchantId);

        //车站管理人员不允许删除其它车站的管理人员
        if (currentAdmin.getBelongId() != null) {
            Merchant merchant = merchantDao.findOne(merchantId);
            if (!currentAdmin.getBelongId().equals(merchant.getStationId())) {
                throw new AppException("你不允许删除其它车站的管理人员 ");
            }
        }
        merchantDao.delete(merchantId);
    }

    @Override
    public ResultMessage loginByPhoneNo(String phoneNo, String password) {
        // 校验用户名和密码不能为空。
        if (!StringUtils.hasText(phoneNo))
            return ResultMessage.newFailure("商户账号不能为空！");
        if (!StringUtils.hasText(password))
            return ResultMessage.newFailure("商户密码不能为空！");
        logger.info("商户服务层：商户登陆：电话号码：" + phoneNo + " 明文密码：不显示");

        Merchant merchant = merchantDao.findByPhoneNo(phoneNo);
        if (merchant == null)
            return ResultMessage.newFailure("商户不存在或已删除！");
        // 传入的是明文密码，转换成数据库存储的加密形式。
        password = EncodeUtil.encodePassword(password, merchant.getAccount());
        if (password.equals(merchant.getPassword())) {
            return ResultMessage.newSuccess("商户登陆成功！").setData(merchant);
        } else {
            return ResultMessage.newFailure("账号或者密码不正确！");
        }
    }

    @Override
    public boolean hasPhoneNo(String phoneNo) {
        long count = merchantDao.countByPhoneNo(phoneNo);
        return count > 0;
    }

    @Override
    public Merchant findByPhoneNo(String phoneNo) {
        return merchantDao.findByPhoneNo(phoneNo);
    }

    @Override
    public Merchant changePasswordByPhoneNo(String phoneNo, String newPassword) {
        if (!StringUtils.hasText(phoneNo) || !StringUtils.hasText(newPassword))
            throw new AppException("电话号码和密码不能为空！");
        if (!newPassword.matches(AppConfig.PASSWORD_PATTEN))
            throw new AppException("密码不符合要求，请检查是否含有特殊符号和满足长度。");
        logger.info("商户服务层：商户密码重置：电话号码：" + phoneNo + " 明文密码：不显示");

        // 查询帐号。
        Merchant merchant = merchantDao.findByPhoneNo(phoneNo);
        if (merchant == null)
            throw new AppException("服务人员不存在或已删除！");
        // 密码加密。
        newPassword = EncodeUtil.encodePassword(newPassword, merchant.getAccount());
        merchant.setPassword(newPassword);
        return merchantDao.save(merchant);
    }

    @Override
    public Merchant findMerchantById(String merchantId) {
        if (!StringUtils.hasText(merchantId))
            throw new AppException("请选择要查询的商户！");
        logger.info("进入服务层：获取商户：商户ID：" + merchantId);
        return merchantDao.findOne(merchantId);
    }

    @Override
    @Transactional
    public Merchant updateImage(String merchantId, InputStream inputStream) {
        // 校验传入参数合法性。
        if (!StringUtils.hasText(merchantId))
            throw new AppException("请选择要修改的图片！");
        logger.info("进入服务层：给商户添加图片: 商户名称：" + merchantId);

        // 商户是否存在。
        Merchant merchantOld = merchantDao.findOne(merchantId);
        if (merchantOld == null)
            throw new AppException("该商户不存在或已删除！");

        // 删除旧的图片。
        if (StringUtils.hasText(merchantOld.getImageId()))
            commonService.deleteImage(merchantOld.getImageId());

        // 保存图片信息。
        ImageInfo imageInfo = commonService.addImage(inputStream);

        // 更新商户的图片ID。
        merchantOld.setImageId(imageInfo.getImageId());
        return merchantDao.save(merchantOld);
    }

    @Override
    public Map<String, Object> getProductsByMerchant(String merchantId) {
        if (!StringUtils.hasText(merchantId)) {
            throw new AppException("merchantId不能为空！");
        }
        logger.info("根据商户id获取所有的商品分类以及商品。merchantId=" + merchantId);

        //先获取商户信息
        Merchant merchant = merchantDao.findOne(merchantId);
        if (merchant == null) {
            logger.info("商户信息不存在。");
            throw new AppException("商户信息不存在！");
        }

        if (StringUtils.hasText(merchant.getImageId())) {
            ImageInfo image = commonService.getImageInfoById(merchant.getImageId());
            if (null != image) {
                merchant.setImageUrl(image.getUrl());
            }
        }

        ServiceType serviceType = serviceTypeService.findServiceType(merchant.getServiceTypeId());
        if (null == serviceType) {
            throw new AppException("商户的服务类型为空！");
        }

        //将获取的数据保存在Map中
        Map<String, Object> mapResult = new HashMap<String, Object>();

        //保存商户信息
        mapResult.put("merchant", merchant);

        //保存配送费用
        mapResult.put("distributionCosts", serviceType.getDistributionCosts());

        //获取该商家所有的分类
        List<ProductCategory> listProductCategory = productCategoryService.findByMerchantId(merchantId);
        if (0 == listProductCategory.size()) {
            logger.info("商户无商品分类。");
            return mapResult;
        }

        logger.info("listProductCategory.size():" + listProductCategory.size());

        //获取每个分类的商品
        List<Object> listMenu = new ArrayList<Object>();
        for (ProductCategory productCategory : listProductCategory) {
            logger.info("保存商品分类。");
            //查找该分类的所有商品，如果当前分类没有商品则跳过
            List<Product> listProduct = productService.findByProductCategoryId(productCategory.getProductCategoryId());
            logger.info("listProduct.size()    :" + listProduct.size());
            if (0 < listProduct.size()) {
                logger.info("获取当前分类的商品。");
                Map<String, Object> mapCategory = new HashMap<>();
                mapCategory.put("category", productCategory);
                mapCategory.put("goods", listProduct);
                listMenu.add(mapCategory);
            }
        }
        mapResult.put("menu", listMenu);
        return mapResult;
    }

    @Override
    public List<Merchant> findByStationIdAndServiceType(String stationId, String serviceType) {
        if (!StringUtils.hasText(stationId)) {
            throw new AppException("StationId不能为空！");
        }
        logger.info("查询商户列表,车站ID:" + stationId);
        return merchantDao.getByStationIdAndServiceTypeId(stationId, serviceType);
    }

}
