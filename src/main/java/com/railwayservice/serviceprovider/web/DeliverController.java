package com.railwayservice.serviceprovider.web;

import com.railwayservice.application.cache.ServiceProviderSessionCache;
import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.serviceprovider.entity.ServiceProvider;
import com.railwayservice.serviceprovider.service.DeliverService;
import com.railwayservice.serviceprovider.service.ServiceProviderService;
import com.railwayservice.serviceprovider.vo.CountOrdersVO;
import com.railwayservice.serviceprovider.vo.DeliverOrderVO;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 送货模块请求控制器。
 *
 * @author Ewing
 */
@Controller
@RequestMapping(value = "/deliver", produces = {"application/json;charset=UTF-8"})
@Api(value = "送货模块请求控制器", description = "送货模块的相关操作")
public class DeliverController {
    private final Logger logger = LoggerFactory.getLogger(DeliverController.class);

    private ServiceProviderSessionCache serviceProviderSessionCache;

    private DeliverService deliverService;

    private ServiceProviderService serviceProviderService;

    @Autowired
    public void setDeliverService(DeliverService deliverService) {
        this.deliverService = deliverService;
    }

    @Autowired
    public void setServiceProviderService(ServiceProviderService serviceProviderService) {
        this.serviceProviderService = serviceProviderService;
    }

    @Autowired
    public void setServiceProviderSessionCache(ServiceProviderSessionCache serviceProviderSessionCache) {
        this.serviceProviderSessionCache = serviceProviderSessionCache;
    }

    /**
     * 查询需要送货的订单。
     *
     * @param pageParam   分页参数。
     * @param orderStatus 订单状态。
     * @return 订单数据。
     */
    @ResponseBody
    @RequestMapping("/queryOrders")
    public ResultMessage queryOrders(HttpServletRequest request, PageParam pageParam,
                                     @RequestParam("orderStatus") Integer orderStatus) {
        logger.info("配送控制器：查询订单：订单状态：" + orderStatus);
        try {
            ServiceProvider serviceProvider = serviceProviderSessionCache.get(request.getHeader(AppConfig.TOKEN_KEY));
            if (serviceProvider == null || !StringUtils.hasText(serviceProvider.getStationId()))
                return ResultMessage.newFailure("当前服务人员无效！");
            PageData pageData = deliverService.queryOrders(pageParam, serviceProvider.getServiceProviderId(), serviceProvider.getStationId(), orderStatus);
            return ResultMessage.newSuccess().setData(pageData);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("查询订单异常：", e);
            return ResultMessage.newFailure("查询订单失败！");
        }
    }

    /**
     * 配送员抢单。
     *
     * @param orderId 订单ID。
     * @return 订单数据。
     */
    @ResponseBody
    @RequestMapping("/acceptOrder")
    public ResultMessage acceptOrder(String orderId, HttpServletRequest request) {
        logger.info("配送控制器：配送员抢单：订单ID：" + orderId);
        try {
            ServiceProvider serviceProvider = serviceProviderSessionCache.get(request.getHeader(AppConfig.TOKEN_KEY));
            if (!StringUtils.hasText(orderId))
                return ResultMessage.newFailure("订单ID不能为空！");
            deliverService.acceptOrder(serviceProvider, orderId);
            return ResultMessage.newSuccess();
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("配送员抢单异常：", e);
            return ResultMessage.newFailure("抢单失败，请刷新再试。");
        }
    }

    /**
     * 服务员对订单取货。
     *
     * @param orderId 订单ID。
     * @return 订单数据。
     */
    @ResponseBody
    @RequestMapping("/getOrderGoods")
    public ResultMessage getOrderGoods(String orderId, HttpServletRequest request) {
        logger.info("配送控制器：从订单取货：订单ID：" + orderId);
        try {
            ServiceProvider serviceProvider = serviceProviderSessionCache.get(request.getHeader(AppConfig.TOKEN_KEY));
            if (!StringUtils.hasText(orderId))
                return ResultMessage.newFailure("订单ID不能为空！");
            deliverService.getOrderGoods(serviceProvider, orderId);
            return ResultMessage.newSuccess();
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("订单取货失败：", e);
            return ResultMessage.newFailure("订单取货失败，请刷新再试。");
        }
    }

    /**
     * 服务员通知用户取货。
     *
     * @param orderId 订单ID。
     * @return 订单数据。
     */
    @ResponseBody
    @RequestMapping("/noticeUserGet")
    public ResultMessage noticeUserGet(String orderId, HttpServletRequest request) {
        logger.info("配送控制器：通知用户取货：订单ID：" + orderId);
        try {
            ServiceProvider serviceProvider = serviceProviderSessionCache.get(request.getHeader(AppConfig.TOKEN_KEY));
            if (!StringUtils.hasText(orderId))
                return ResultMessage.newFailure("订单ID不能为空！");
            deliverService.noticeUserGet(serviceProvider, orderId);
            return ResultMessage.newSuccess();
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("通知用户取货失败：", e);
            return ResultMessage.newFailure("通知用户取货失败，请刷新再试。");
        }
    }

    /**
     * 服务员将商品已送达。
     *
     * @param orderId 订单ID。
     * @return 订单数据。
     */
    @ResponseBody
    @RequestMapping("/deliveredGoods")
    public ResultMessage deliveredGoods(String orderId, HttpServletRequest request) {
        logger.info("配送控制器：商品送达客户：订单ID：" + orderId);
        try {
            ServiceProvider serviceProvider = serviceProviderSessionCache.get(request.getHeader(AppConfig.TOKEN_KEY));
            if (!StringUtils.hasText(orderId))
                return ResultMessage.newFailure("订单ID不能为空！");
            deliverService.deliveredGoods(serviceProvider, orderId);
            return ResultMessage.newSuccess();
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("商品送达异常：", e);
            return ResultMessage.newFailure("商品送达失败，请刷新再试。");
        }
    }

    /**
     * 统计今天完成的订单。
     *
     * @return 订单数据。
     */
    @ResponseBody
    @RequestMapping("/countOrdersToday")
    public ResultMessage countOrdersToday(HttpServletRequest request) {
        logger.info("配送控制器：统计今天完成的订单");
        try {
            ServiceProvider serviceProvider = serviceProviderSessionCache.get(request.getHeader(AppConfig.TOKEN_KEY));
            CountOrdersVO countOrdersVO = deliverService.countOrdersToday(serviceProvider.getServiceProviderId());
            serviceProvider = serviceProviderService.findByServiceProviderId(serviceProvider.getServiceProviderId());
            if (serviceProvider != null) {
                countOrdersVO.setStatus(serviceProvider.getStatus());
                countOrdersVO.setServiceProviderId(serviceProvider.getServiceProviderId());
            }
            return ResultMessage.newSuccess().setData(countOrdersVO);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("统计今天完成的订单异常：", e);
            return ResultMessage.newFailure("统计今天完成的订单失败！");
        }
    }

    /**
     * 查询今天已完成的订单。
     */
    @ResponseBody
    @RequestMapping("/queryCompleteOrdersToday")
    public ResultMessage queryCompleteOrdersToday(HttpServletRequest request) {
        logger.info("配送控制器：查询需要已完成的订单");
        try {
            ServiceProvider serviceProvider = serviceProviderSessionCache.get(request.getHeader(AppConfig.TOKEN_KEY));
            List<DeliverOrderVO> orderVOs = deliverService.queryCompleteOrdersToday(serviceProvider);
            return ResultMessage.newSuccess().setData(orderVOs);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("查询今天已完成的订单异常：", e);
            return ResultMessage.newFailure("查询今天已完成的订单失败！");
        }
    }

    /**
     * 查询今天被取消的订单。
     */
    @ResponseBody
    @RequestMapping("/queryCancelOrdersToday")
    public ResultMessage queryCancelOrdersToday(HttpServletRequest request) {
        logger.info("配送控制器：查询需要被取消的订单");
        try {
            ServiceProvider serviceProvider = serviceProviderSessionCache.get(request.getHeader(AppConfig.TOKEN_KEY));
            List<DeliverOrderVO> orderVOs = deliverService.queryCancelOrdersToday(serviceProvider);
            return ResultMessage.newSuccess().setData(orderVOs);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("查询今天被取消的订单异常：", e);
            return ResultMessage.newFailure("查询今天被取消的订单失败！");
        }
    }

}
