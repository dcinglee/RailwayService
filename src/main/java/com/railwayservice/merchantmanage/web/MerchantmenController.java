package com.railwayservice.merchantmanage.web;

import com.railwayservice.application.cache.MerchantSessionCache;
import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.util.TimeUtil;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.merchantmanage.service.MerchantService;
import com.railwayservice.merchantmanage.service.MerchantmenService;
import com.railwayservice.merchantmanage.vo.MerchantAchievement;
import com.railwayservice.merchantmanage.vo.MerchantMainOrder;
import com.railwayservice.merchantmanage.vo.Merchantmen;
import com.railwayservice.messages.service.CommentService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 商户模块请求控制器。
 *
 * @author Ewing
 */
@Controller
@RequestMapping(value = "/merchantmen", produces = {"application/json;charset=UTF-8"})
@Api(value = "商户模块请求控制器", description = "商户模块的相关操作")
public class MerchantmenController {
    private final Logger logger = LoggerFactory.getLogger(MerchantmenController.class);

    private MerchantSessionCache merchantSessionCache;

    private MerchantService merchantService;
    private MerchantmenService merchantmenService;
    private CommentService commentService;

    @Autowired
    public void setMerchantService(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @Autowired
    public void setMerchantmenService(MerchantmenService merchantmenService) {
        this.merchantmenService = merchantmenService;
    }

    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @Autowired
    public void setMerchantSessionCache(MerchantSessionCache merchantSessionCache) {
        this.merchantSessionCache = merchantSessionCache;
    }

    /**
     * 查询待处理的订单。
     */
    @ResponseBody
    @RequestMapping("/queryWaitOrders")
    public ResultMessage queryWaitDealOrders(HttpServletRequest request) {
        logger.info("商户人员控制器：查询待处理的订单");
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            Merchant merchant = merchantSessionCache.get(token);
            List<MerchantMainOrder> merchantMainOrders = merchantmenService.queryWaitDealOrders(merchant);
            return ResultMessage.newSuccess().setData(merchantMainOrders);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("查询待处理的订单异常：", e);
            return ResultMessage.newFailure("查询待处理的订单！");
        }
    }

    /**
     * 查询待取货的订单。
     */
    @ResponseBody
    @RequestMapping("/queryAcceptOrders")
    public ResultMessage queryWaitUserOrders(HttpServletRequest request) {
        logger.info("商户人员控制器：查询待取货的订单");
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            Merchant merchant = merchantSessionCache.get(token);
            List<MerchantMainOrder> merchantMainOrders = merchantmenService.queryWaitUserOrders(merchant);
            return ResultMessage.newSuccess().setData(merchantMainOrders);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("查询待处理的订单异常：", e);
            return ResultMessage.newFailure("查询待处理的订单！");
        }
    }

    /**
     * 处理订单：确认接单、拒绝订单、顾客已到店取货、同意取消订单、拒绝取消订单。
     */
    @ResponseBody
    @RequestMapping("/dealOrderByStatus")
    public ResultMessage dealOrderByStatus(String orderId, Integer newStatus, String reason, HttpServletRequest request) {
        logger.info("商户人员控制器：处理订单：订单ID:" + orderId + "新订单状态:" + newStatus + "处理原因：" + reason);
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            Merchant merchant = merchantSessionCache.get(token);
            merchantmenService.dealOrderByStatus(merchant, orderId, newStatus, reason);
            return ResultMessage.newSuccess().setData(orderId);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("处理订单异常：", e);
            return ResultMessage.newFailure("处理订单异常！");
        }
    }

    /**
     * 商户更新自己的状态：营业中、休息中。
     */
    @ResponseBody
    @RequestMapping("/updateMerchantStatus")
    public ResultMessage updateMerchantStatus(Integer status, HttpServletRequest request) {
        logger.info("商户人员控制器：商户更新自己的状态:商家运营状态：" + status);
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            Merchant merchant = merchantSessionCache.get(token);
            merchantmenService.updateMerchantStatus(merchant, status);
            return ResultMessage.newSuccess();
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("商户更新自己的状态异常：", e);
            return ResultMessage.newFailure("商户更新自己的状态异常！");
        }
    }

    /**
     * 商户查询用户评论，默认只显示最新一页。
     */
    @ResponseBody
    @RequestMapping("/queryComment")
    public ResultMessage queryComment(PageParam pageParam, HttpServletRequest request) {
        logger.info("商户人员控制器：商户查询用户评论");
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            Merchant merchant = merchantSessionCache.get(token);
            PageData pageData = commentService.listComment(pageParam, merchant.getMerchantId());
            return ResultMessage.newSuccess().setData(pageData);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("商户查询用户评论异常：", e);
            return ResultMessage.newFailure("商户查询用户评论异常！");
        }
    }

    /**
     * 商户统计自己的营业额。
     */
    @ResponseBody
    @RequestMapping("/achievement")
    public ResultMessage achievement(PageParam pageParam, HttpServletRequest request) {
        logger.info("商户人员控制器：商户统计自己的营业额");
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            Merchant merchant = merchantSessionCache.get(token);
            MerchantAchievement achievement = merchantmenService.achievement(pageParam, merchant);
            return ResultMessage.newSuccess().setData(achievement);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("商户统计自己的营业额异常：", e);
            return ResultMessage.newFailure("商户统计自己的营业额异常！");
        }
    }

    /**
     * 商户获取自己的资料信息。
     */
    @ResponseBody
    @RequestMapping("/getMerchantInfo")
    public ResultMessage getMerchantInfo(HttpServletRequest request) {
        logger.info("商户人员控制器：商户获取自己的资料信息");
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            Merchant merchant = merchantSessionCache.get(token);
            Merchantmen merchantmen = merchantmenService.getMerchantInfo(merchant);
            return ResultMessage.newSuccess().setData(merchantmen);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("商户获取自己的资料信息：", e);
            return ResultMessage.newFailure("商户获取自己的资料信息！");
        }
    }

    /**
     * 商户更新自己的信息。
     */
    @ResponseBody
    @RequestMapping("/updateMerchant")
    public ResultMessage updateMerchant(Merchant merchant, HttpServletRequest request) {
        logger.info("商户人员控制器：商户更新自己的信息：商户名称：" + merchant.getName());
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            Merchant merchantmen = merchantSessionCache.get(token);
            merchantmenService.updateMerchant(merchantmen, merchant);
            return ResultMessage.newSuccess("商户更新自己的信息成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("商户更新自己的信息异常：", e);
            return ResultMessage.newFailure("商户更新自己的信息异常！");
        }
    }

    /**
     * 更新商户图片信息。
     */
    @ResponseBody
    @RequestMapping("/updateImage")
    public ResultMessage updateImage(@RequestParam("imageFile") MultipartFile imageFile, HttpServletRequest request) {
        logger.info("商户控制器：新增图片");
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            Merchant merchant = merchantSessionCache.get(token);
            merchantService.updateImage(merchant.getMerchantId(), imageFile.getInputStream());
            return ResultMessage.newSuccess("更新商户图片成功！");
        } catch (Exception e) {
            logger.error("更新商户图片异常：", e);
            e.printStackTrace();
            return ResultMessage.newFailure("更新商户图片失败！");
        }
    }

    /**
     * 按时间类型和订单状态查询商户历史订单。
     */
    @ResponseBody
    @RequestMapping("/queryHistoryOrders")
    public ResultMessage queryHistoryOrders(String dateRangeType, Integer orderStatus, HttpServletRequest request) {
        logger.info("商户人员控制器：按时间和状态查询商户历史订单: 订单状态：" + orderStatus);
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            Merchant merchant = merchantSessionCache.get(token);
            Date endDate = null;
            Date startDate = TimeUtil.getCurrentDate();
            if ("1".equals(dateRangeType)) { //当日
                endDate = TimeUtil.addDays(startDate, 1);
            } else if ("2".equals(dateRangeType)) { //近1个月
                endDate = TimeUtil.addDays(startDate, 1);
                startDate = TimeUtil.addMonths(startDate, -1);
            } else if ("3".equals(dateRangeType)) { //近3个月
                endDate = TimeUtil.addDays(startDate, 1);
                startDate = TimeUtil.addMonths(startDate, -3);
            } else if ("4".equals(dateRangeType)) { //近1年
                endDate = TimeUtil.addDays(startDate, 1);
                startDate = TimeUtil.addMonths(startDate, -12);
            }
            //如果前台传的是空值或者0，作为全局进行查询
            if (orderStatus != null && orderStatus == 0) {
                orderStatus = null;
            }
            List<MerchantMainOrder> merchantMainOrders = merchantmenService.queryHistoryOrders(merchant, startDate, endDate, orderStatus);
            return ResultMessage.newSuccess().setData(merchantMainOrders);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("商户查询历史订单异常：", e);
            return ResultMessage.newFailure("商户查询历史订单异常！");
        }
    }

}
