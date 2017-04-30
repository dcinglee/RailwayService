package com.railwayservice.order.web;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.order.entity.MainOrder;
import com.railwayservice.order.entity.OrderStatusRecord;
import com.railwayservice.order.service.MainOrderService;
import com.railwayservice.order.service.OrderStatusRecordService;
import com.railwayservice.user.entity.User;
import com.railwayserviceWX.config.WeixinConfig;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单状态记录控制层
 */
@Controller
@RequestMapping(value = "/orderStatusRecordController", produces = {"application/json;charset=UTF-8"})
@Api(value = "订单状态记录控制层", description = "订单状态记录的相关操作")
public class OrderStatusRecordController {

    private final Logger logger = LoggerFactory.getLogger(OrderStatusRecordController.class);

    private OrderStatusRecordService orderStatusRecordService;

    private MainOrderService mainOrderService;

    @Autowired
    public void setMainOrderService(MainOrderService mainOrderService) {
        this.mainOrderService = mainOrderService;
    }

    @Autowired
    public void setOrderStatusRecordService(OrderStatusRecordService orderStatusRecordService) {
        this.orderStatusRecordService = orderStatusRecordService;
    }

    @ResponseBody
    @RequestMapping("/queryOrderRecords")
    public ResultMessage queryOrderRecords(HttpServletRequest req, HttpServletResponse res) {
        logger.info("订单状态记录控制层：查询订单状态记录：");
        User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);

        if (null == user) {
        	String jumpUrl = WeixinConfig.HOST + "/entrance/index";
            try {
				res.sendRedirect(jumpUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
            return ResultMessage.newFailure("获取当前用户失败，页面已跳转！");
        }
        logger.info("user.getUserId()   :" + user.getUserId());

        List<OrderStatusRecord> listOrderStatusRecord = new ArrayList<OrderStatusRecord>();

        try {
            List<MainOrder> listMainOrder = mainOrderService.queryOrdersByUser(user.getUserId(), null, null);
            logger.info("listMainOrder.size()   :" + listMainOrder.size());
            for (MainOrder mainOrder : listMainOrder) {
                List<OrderStatusRecord> list = orderStatusRecordService.findByOrderId(mainOrder.getOrderId());
                logger.info("list.size()   :" + list.size());

                listOrderStatusRecord.addAll(list);
            }
            return ResultMessage.newSuccess().setData(listOrderStatusRecord);
        } catch (Exception e) {
            logger.error("查询送货地址数据异常：" , e);
            e.printStackTrace();
            return ResultMessage.newFailure("查询送货地址数据异常！");
        }
    }
}
