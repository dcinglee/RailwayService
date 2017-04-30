package com.railwayservice.productmanage.web;

import com.railwayservice.application.cache.MerchantSessionCache;
import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.interceptor.Authorize;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.productmanage.entity.Product;
import com.railwayservice.productmanage.service.ProductService;
import com.railwayservice.productmanage.vo.ProductVo;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 商品模块请求控制器。
 *
 * @author Ewing
 */
@Controller
@RequestMapping(value = "/product", produces = {"application/json;charset=UTF-8"})
@Api(value = "商品模块请求控制器", description = "商品模块的相关操作")
public class ProductController {
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private ProductService productService;

    private MerchantSessionCache merchantSessionCache;

    @Autowired
    public void setMerchantSessionCache(MerchantSessionCache merchantSessionCache) {
        this.merchantSessionCache = merchantSessionCache;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 新增商品。
     */
    @ResponseBody
    @RequestMapping("/add")
    public ResultMessage addProduct(Product product, HttpServletRequest request) {
        logger.info("进入控制层：新增商品: 商品名称：" + product.getName());
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            Merchant merchant = merchantSessionCache.get(token);
            Product productNew = productService.addProduct(merchant, product);
            return ResultMessage.newSuccess("新增商品成功！").setData(productNew);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("新增商品异常：", e);
            return ResultMessage.newFailure("新增商品异常！");
        }
    }

    /**
     * 更新商品。
     */
    @ResponseBody
    @RequestMapping("/update")
    public ResultMessage updateProduct(Product product, HttpServletRequest request) {
        logger.info("进入控制层：更新商品 商品名称：" + product.getName());
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            Merchant merchant = merchantSessionCache.get(token);
            productService.updateProduct(merchant, product);
            return ResultMessage.newSuccess("修改商品成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("修改商品异常：", e);
            return ResultMessage.newFailure("修改商品异常！");
        }
    }

    /**
     * 删除商品。
     */
    @ResponseBody
    @RequestMapping("/delete")
    public ResultMessage deleteProduct(String productId) {
        logger.info("进入控制层：删除商品 商品id：" + productId);
        try {
            productService.deleteProduct(productId);
            return ResultMessage.newSuccess("删除商品成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("删除商品异常：", e);
            return ResultMessage.newFailure("删除商品异常！");
        }
    }

    /**
     * 查询商品。
     */
    @ResponseBody
    @RequestMapping("/query")
    public ResultMessage queryProducts(PageParam pageParam, String name) {
        logger.info("进入控制层：查询商品: 商品名称：" + name);
        try {
            Page<Product> dataPage = productService.queryProducts(name, pageParam.newPageable());
            return ResultMessage.newSuccess().setData(new PageData(dataPage));
        } catch (Exception e) {
            logger.error("查询数据异常：", e);
            return ResultMessage.newFailure("查询数据异常！");
        }
    }

    /**
     * 更新商品图片信息。
     */
    @ResponseBody
    @RequestMapping("/updateImage")
    public ResultMessage addImage(String productId, @RequestParam("imageFile") MultipartFile imageFile, HttpServletRequest request) {
        logger.info("进入控制层：更新商品：商品ID：" + productId);
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            Merchant merchant = merchantSessionCache.get(token);
            productService.updateImage(merchant, productId, imageFile.getInputStream());
            return ResultMessage.newSuccess("更新商品图片成功！");
        } catch (Exception e) {
            logger.error("更新商品图片异常：", e);
            return ResultMessage.newFailure("更新商品图片失败！");
        }
    }

    /**
     * 查询当前车站的精选商品。（也就是原形设计中的精选美食）
     * 如果车站id为空，则默认查找深圳北站的热卖商品信息
     *
     * @param stationId
     * @return ResultMessage
     * @author lid
     * @date 2017.3.11
     */
    @ResponseBody
    @RequestMapping("/queryHotProductByRecommend")
    @Authorize(type = AppConfig.AUTHORITY_USER)
    public ResultMessage QueryHotProduct(String stationId) {
        logger.info("进入控制层:查询热卖商品:车站ID：" + stationId);
        try {
            List<Product> listProduct = productService.findHotProductByRecommend(stationId);
            return ResultMessage.newSuccess().setData(listProduct);
        } catch (Exception e) {
            logger.error("查询已推荐商品异常：", e);
            return ResultMessage.newFailure("查询已推荐商品失败！");
        }
    }

    /**
     * 查询该站点已推荐的商品(热门商品)。
     */
    @ResponseBody
    @RequestMapping("/queryProductByRecommend")
    @Authorize(type = AppConfig.AUTHORITY_USER)
    public ResultMessage QueryProduct(String stationId) {
        logger.info("进入控制层:查询已推荐的商品:车站ID：" + stationId);
        try {
            List<Product> listProduct = productService.findProductByRecommend(stationId);
            return ResultMessage.newSuccess().setData(listProduct);
        } catch (Exception e) {
            logger.error("查询已推荐商品异常：", e);
            return ResultMessage.newFailure("查询已推荐商品失败！");
        }
    }

    /**
     * 商户查询自己的所有商品分类及商品。
     */
    @ResponseBody
    @RequestMapping("/merchantCategoryProducts")
    public ResultMessage getCategoryProducts(HttpServletRequest request) {
        logger.info("进入控制层：商户查询自己的所有商品分类及商品。");
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            Merchant merchant = merchantSessionCache.get(token);
            merchant = productService.getCategoryProducts(merchant);
            return ResultMessage.newSuccess().setData(merchant);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("商户查询所有商品分类及商品异常：", e);
            return ResultMessage.newFailure("商户查询所有商品分类及商品异常！");
        }
    }

    /**
     * 商户更新商品状态，开售、停售、下架等。
     */
    @ResponseBody
    @RequestMapping("/updateProductStatus")
    public ResultMessage updateProductStatus(Product product, HttpServletRequest request) {
        logger.info("进入控制层：商户查询自己的所有商品分类及商品:商品状态：" + product.getState());
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            Merchant merchant = merchantSessionCache.get(token);
            productService.updateProductStatus(merchant, product);
            return ResultMessage.newSuccess();
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("商户更新商品状态异常：", e);
            return ResultMessage.newFailure("商户更新商品状态异常！");
        }
    }

    /**
     * 查询该站点所有商品。
     */
    @ResponseBody
    @RequestMapping("/queryAllProduct")
    public ResultMessage findAllProduct(String stationId, String serviceType, String merchantName, String productName, int recommend) {
        logger.info("进入控制层：查询所有关联查询的商品:车站ID：" + stationId + "商户名称：" + merchantName + "商品名称：" + productName);
        try {
            List<ProductVo> listProduct = productService.findAllProduct(stationId, serviceType, merchantName, productName, recommend);
            return ResultMessage.newSuccess().setData(listProduct);
        } catch (Exception e) {
            logger.error("查询该站点所有商品异常：", e);
            return ResultMessage.newFailure("查询该站点所有商品异常！");
        }
    }

    /**
     * 更新该站点所有商品信息。
     */
    @ResponseBody
    @RequestMapping("/updateProductRecommend")
    public ResultMessage updateRecomProduct(ProductVo productvo) {
        logger.info("进入控制层：更新所有关联查询的商品: 商品名称：" + productvo.getProductName());
        try {
            Product product = productService.updateProductRecommend(productvo);
            return ResultMessage.newSuccess().setData(product);
        } catch (Exception e) {
            logger.error("更新该站点所有商品信息异常：", e);
            return ResultMessage.newFailure("更新该站点所有商品信息异常！");
        }
    }
}

