package com.railwayservice.order.web;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.interceptor.Authorize;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.order.entity.DeliverAddress;
import com.railwayservice.order.service.DeliverAddressService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 配送地址
 *
 * @author xuy
 * @date 2017.3.2
 */
@Controller
@RequestMapping(value = "/deliver", produces = {"application/json;charset=UTF-8"})
@Api(value = "配送地址", description = "配送地址的相关操作")
@Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_ORDER_DELIVER_ADDRESS"})
public class DeliverAddressController {

    private final Logger logger = LoggerFactory.getLogger(DeliverAddressController.class);

    @Autowired
    private DeliverAddressService deliverAddressService;

    public void setDeliverAddressService(DeliverAddressService deliverAddressService) {
        this.deliverAddressService = deliverAddressService;
    }

    /**
     * 送货地址查询
     *
     * @return ResultMessage
     * @author xuy
     * @date 2017.2.8
     */
    @ResponseBody
    @RequestMapping("/query")
    public ResultMessage query(String stationId) {
        logger.info("配送地址控制层: 送货地址查询：车站ID：" + stationId);
        try {
            List<DeliverAddress> result = deliverAddressService.findDeliverAddressByStationId(stationId);
            return ResultMessage.newSuccess().setData(result);
        } catch (Exception e) {
            logger.error("查询送货地址数据异常：", e);
            e.printStackTrace();
            return ResultMessage.newFailure("查询送货地址数据异常！");
        }
    }

    /**
     * 新增送货地址
     *
     * @return ResultMessage
     * @author xuy
     * @date 2017.2.8
     */
    @ResponseBody
    @RequestMapping("/add")
    public ResultMessage add(DeliverAddress deliverAddress) {
        logger.info("配送地址控制层: 新增送货地址：");
        try {
            deliverAddress = deliverAddressService.addDeliverAddress(deliverAddress);
            return ResultMessage.newSuccess().setData(deliverAddress);
        } catch (Exception e) {
            logger.error("新增送货地址数据异常：", e);
            return ResultMessage.newFailure("新增送货地址数据异常！");
        }
    }

    /**
     * 修改送货地址
     *
     * @return ResultMessage
     * @author xuy
     * @date 2017.2.8
     */
    @ResponseBody
    @RequestMapping("/update")
    public ResultMessage update(DeliverAddress deliverAddress) {
        logger.info("配送地址控制层: 修改送货地址：" + deliverAddress);
        try {
            deliverAddress = deliverAddressService.updateDeliverAddress(deliverAddress);
            return ResultMessage.newSuccess().setData(deliverAddress);
        } catch (Exception e) {
            logger.error("修改送货地址数据异常：", e);
            return ResultMessage.newFailure("修改送货地址数据异常！");
        }
    }

    /**
     * 删除送货地址
     *
     * @return ResultMessage
     * @author xuy
     * @date 2017.2.8
     */
    @ResponseBody
    @RequestMapping("/delete")
    public ResultMessage delete(DeliverAddress deliverAddress) {
        logger.info("配送地址控制层: 删除送货地址：" + deliverAddress);
        try {
            deliverAddressService.deleteDeliverAddress(deliverAddress);
            return ResultMessage.newSuccess().setData(deliverAddress);
        } catch (Exception e) {
            logger.error("删除送货地址数据异常：", e);
            return ResultMessage.newFailure("删除送货地址数据异常！");
        }
    }

}
