package com.railwayservice.productmanage.web;

import com.railwayservice.application.cache.MerchantSessionCache;
import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.productmanage.entity.Product;
import com.railwayservice.productmanage.entity.ProductCategory;
import com.railwayservice.productmanage.service.ProductCategoryRelaService;
import com.railwayservice.productmanage.service.ProductCategoryService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 商品分类模块请求控制器。
 *
 * @author Ewing
 */
@Controller
@RequestMapping(value = "/productCategory", produces = {"application/json;charset=UTF-8"})
@Api(value = "商品分类模块请求控制器", description = "商品分类模块的相关操作")
public class ProductCategoryController {
    private final Logger logger = LoggerFactory.getLogger(ProductCategoryController.class);

    private ProductCategoryService productCategoryService;
    private ProductCategoryRelaService productCategoryRelaService;
    private MerchantSessionCache merchantSessionCache;

    @Autowired
    public void setMerchantSessionCache(MerchantSessionCache merchantSessionCache) {
        this.merchantSessionCache = merchantSessionCache;
    }

    @Autowired
    public void setProductCategoryService(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @Autowired
    public void setProductCategoryRelaService(ProductCategoryRelaService productCategoryRelaService) {
        this.productCategoryRelaService = productCategoryRelaService;
    }

    /**
     * 新增商品分类信息
     * 将商品列表传入，添加商品商品分类的关联关系
     *
     * @param productCategory
     * @return ResultMessage
     * @author lid
     * @date 2017.2.16
     */
    @ResponseBody
    @RequestMapping("/addProductCategory")
    public ResultMessage addProductCategory(ProductCategory productCategory, HttpServletRequest request) {
        logger.info("商品分类控制层：新增商品分类信息：商品分类名称：" + productCategory.getName());
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            Merchant merchant = merchantSessionCache.get(token);
            productCategoryService.addProductCategory(merchant, productCategory);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("新增商品分类异常：", e);
            return ResultMessage.newFailure("新增商品分类异常！");
        }
        return ResultMessage.newSuccess("新增商品分类成功！");
    }

    /**
     * 更新商品分类信息。
     *
     * @param productCategory
     * @return ResultMessage
     * @author lid
     * @date 2017.2.16
     */
    @ResponseBody
    @RequestMapping("/updateProductCategory")
    public ResultMessage updateProductCategory(ProductCategory productCategory, HttpServletRequest request) {
        logger.info("商品分类控制层：更新商品分类信息：商品分类名称：" + productCategory.getName());
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            Merchant merchant = merchantSessionCache.get(token);
            productCategoryService.updateProductCategory(merchant, productCategory);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("修改商品分类异常：", e);
            return ResultMessage.newFailure("修改商品分类异常！");
        }

        return ResultMessage.newSuccess("修改商品分类成功！");
    }

    /**
     * 交换商品分类的顺序，用于排序。
     *
     * @return ResultMessage
     */
    @ResponseBody
    @RequestMapping("/swapProductCategorySort")
    public ResultMessage swapProductCategorySort(String categoryIdA, String categoryIdB, HttpServletRequest request) {
        logger.info("商品分类控制器：交换商品分类的顺序：categoryIdA：" + categoryIdA + " categoryIdB：" + categoryIdB);
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            Merchant merchant = merchantSessionCache.get(token);
            productCategoryService.swapProductCategorySort(merchant, categoryIdA, categoryIdB);
            return ResultMessage.newSuccess("交换商品分类的顺序！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("交换商品分类的顺序：", e);
            return ResultMessage.newFailure("交换商品分类的顺序！");
        }
    }

    /**
     * 删除商品分类，该分类下的商品移动到新的分类。
     *
     * @param deleteCategoryId 要删除的分类。
     * @param newCategoryId    商品移动到新的分类。
     */
    @ResponseBody
    @RequestMapping("/deleteProductCategory")
    public ResultMessage deleteProductCategory(String deleteCategoryId, String newCategoryId,
                                               @RequestHeader(AppConfig.TOKEN_KEY) String token) {
        logger.info("商品分类控制层：删除商品分类信息：商品分类id：" + deleteCategoryId);
        //最后删除商品分类
        try {
            Merchant merchant = merchantSessionCache.get(token);
            productCategoryService.deleteProductCategory(merchant, deleteCategoryId, newCategoryId);
            return ResultMessage.newSuccess("删除商品分类成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("删除商品分类异常：", e);
            return ResultMessage.newFailure("删除商品分类异常！");
        }
    }

    /**
     * 根据商品分类ID查找所有商品
     *
     * @param productCategoryId
     * @return
     */
    @ResponseBody
    @RequestMapping("/getProductCategoryId")
    public ResultMessage getProductCategoryId(String productCategoryId) {
        logger.info("商品分类控制层：查询商品分类信息：商品分类id：" + productCategoryId);
        if (null == productCategoryId) {
            return ResultMessage.newFailure("请输入查询条件！");
        }
        try {
            List<Product> listProduct = productCategoryRelaService.findProuctByProductCategory(productCategoryId);
            return ResultMessage.newSuccess("获取商品信息成功！").setData(listProduct);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取商品信息异常：", e);
            return ResultMessage.newFailure("获取商品信息异常！");
        }
    }

}
