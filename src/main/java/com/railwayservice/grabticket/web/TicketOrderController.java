package com.railwayservice.grabticket.web;


import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.util.ExcelExportUtil;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.grabticket.entity.LineTicket;
import com.railwayservice.grabticket.entity.TicketOrder;
import com.railwayservice.grabticket.service.LineTicketService;
import com.railwayservice.grabticket.service.TicketOrderService;
import com.railwayservice.grabticket.vo.GrabOrderVo;
import com.railwayservice.grabticket.vo.TicketPassengerVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * 车票订单模块请求控制器。
 *
 * @author lid
 * @date 2017.4.10
 */
@Controller
@RequestMapping(value = "/ticketOrder", produces = {"application/json;charset=UTF-8"})
@Api(value = "车票订单模块请求控制器", description = "车票订单模块的相关操作")
public class TicketOrderController {

    private final Logger logger = LoggerFactory.getLogger(TicketOrderController.class);

    private TicketOrderService ticketOrderService;

    private LineTicketService lineTicketService;

    @Autowired
    public void setLineTicketService(LineTicketService lineTicketService) {
        this.lineTicketService = lineTicketService;
    }

    @Autowired
    public void setTicketOrderService(TicketOrderService ticketOrderService) {
        this.ticketOrderService = ticketOrderService;
    }

    /**
     * 后台查询所有车票订单
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryTicketOrders")
    @ApiOperation(notes = "后台查询所有车票订单", value = "后台查询所有车票订单")
    public ResultMessage queryTicketOrders() {
        logger.info("queryTicketOrders!");
        try {
            List<TicketOrder> ticketOrder = ticketOrderService.findAllTicketOrder();
            return ResultMessage.newSuccess("查找车票订单成功！").setData(ticketOrder);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("查找车票订单异常：", e);
            return ResultMessage.newFailure("查找车票订单异常！");
        }
    }

    /**
     * 后台查询单条车票订单
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryTicketOrder")
    @ApiOperation(notes = "后台查询单条车票订单", value = "后台查询单条车票订单")
    public ResultMessage queryTicketOrder(String ticketOrderId) {
        logger.info("queryTicketOrders!");
        try {
            Map<String, Object> map = ticketOrderService.findOneTicketOrder(ticketOrderId);
            return ResultMessage.newSuccess("查找单条车票订单成功！").setData(map);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("查找单条车票订单异常：", e);
            return ResultMessage.newFailure("查找单条车票订单异常！");
        }
    }


    /**
     * 查找历史车次。
     */
    @ResponseBody
    @RequestMapping("/getHistoryTravels")
    @ApiOperation(notes = "查找历史车次", value = "查找历史车次")
    public ResultMessage getHistoryTravels(String userId) {
        logger.info("getHistoryOrders：" + userId);
        try {
            List<TicketOrder> listHistoryTravels = ticketOrderService.getHistoryTravels(userId);
            return ResultMessage.newSuccess("查找历史车次成功！").setData(listHistoryTravels);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("查找历史车次异常：", e);
            return ResultMessage.newFailure("查找历史车次异常！");
        }
    }

    /**
     * 删除历史车次。
     */
    @ResponseBody
    @RequestMapping("/deleteHistoryOrders")
    @ApiOperation(notes = "删除历史车次", value = "删除历史车次")
    public ResultMessage deleteHistoryOrders(String userId) {
        logger.info("deleteHistoryOrders：" + userId);
        try {
            ticketOrderService.deleteHistoryOrders(userId);
            return ResultMessage.newSuccess("删除历史车次成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("删除历史车次异常：", e);
            return ResultMessage.newFailure("删除历史车次异常！");
        }
    }

    /**
     * 查找历史订单。
     */
    @ResponseBody
    @RequestMapping("/getHistoryOrders")
    @ApiOperation(notes = "查找历史订单", value = "查找历史订单")
    public ResultMessage getHistoryOrders(String userId) {
        logger.info("getHistoryOrders：" + userId);
        try {
            List<TicketOrder> listHistoryOrders = ticketOrderService.getHistoryOrders(userId);
            return ResultMessage.newSuccess("查找我的订单成功！").setData(listHistoryOrders);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("查找我的订单异常：", e);
            return ResultMessage.newFailure("查找我的订单异常！");
        }
    }

    /**
     * 用户删除历史订单，用户调用
     * （逻辑删除，将字段isDelete值改为1）
     */
    @ResponseBody
    @RequestMapping("/deleteOrder")
    @ApiOperation(notes = "用户删除历史订单，用户调用", value = "用户删除历史订单，用户调用")
    public ResultMessage deleteOrder(String ticketOrderId) {
        logger.info("deleteOrder：" + ticketOrderId);
        try {
            ticketOrderService.deleteOrder(ticketOrderId);
            return ResultMessage.newSuccess("删除订单成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("删除订单异常：", e);
            return ResultMessage.newFailure("删除订单异常！");
        }
    }

    /**
     * 取消订单，由python端调用，只需修改订单状态为已取消
     *
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping("/cancelOrder")
    @ApiOperation(notes = "取消订单，由python端调用，只需修改订单状态为已取消", value = "取消订单，由python端调用，只需修改订单状态为已取消")
    public ResultMessage cancelOrder(String ticketOrderId) {
        logger.info("cancelOrder：" + ticketOrderId);
        try {
            TicketOrder ticketOrder = ticketOrderService.changeTicketOrderToCancle(ticketOrderId);
            return ResultMessage.newSuccess("取消订单成功！").setData(ticketOrder);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("取消订单异常：", e);
            return ResultMessage.newFailure("取消订单异常！");
        }
    }

    /**
     * 停止抢票，由python端调用，只需修改订单状态为已停止抢票
     *
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping("/stopOrder")
    @ApiOperation(notes = "停止抢票，由python端调用，只需修改订单状态为已停止抢票", value = "停止抢票，由python端调用，只需修改订单状态为已停止抢票")
    public ResultMessage stopOrder(String ticketOrderId) {
        logger.info("cancelOrder：" + ticketOrderId);
        try {
            TicketOrder ticketOrder = ticketOrderService.changeTicketOrderToStop(ticketOrderId);
            return ResultMessage.newSuccess("停止抢票成功！").setData(ticketOrder);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("停止抢票异常：", e);
            return ResultMessage.newFailure("停止抢票异常！");
        }
    }

    /**
     * 订票：订票时用户直接调用python端订票接口，python端订票成功之后回调该方法
     *
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping("/createBookOrder")
    @ApiOperation(notes = "订票：订票时用户直接调用python端订票接口，python端订票成功之后回调该方法", value = "订票：订票时用户直接调用python端订票接口，python端订票成功之后回调该方法")
    public ResultMessage createBookOrder(@RequestBody Map<String, Object> map) {
        logger.info("createBookOrder!map:" + map);
        try {
            TicketOrder ticketOrder = ticketOrderService.createBookOrder(map);
            return ResultMessage.newSuccess("生成订票订单成功！").setData(ticketOrder);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("生成订票订单异常：", e);
            return ResultMessage.newFailure("生成订票订单异常！");
        }
    }

    /**
     * 获取当前车票信息
     *
     * @param ticketOrderId
     * @return
     */
    @ResponseBody
    @RequestMapping("/getBookOrder")
    @ApiOperation(notes = "获取当前车票信息", value = "获取当前车票信息")
    public ResultMessage getBookOrder(String ticketOrderId) {
        logger.info("获取当前车票信息!ticketOrderId:" + ticketOrderId);
        try {
            Map<String, Object> objectMap = ticketOrderService.getBookOrder(ticketOrderId);
            return ResultMessage.newSuccess("获取当前车票信息成功！").setData(objectMap);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取当前车票信息异常：", e);
            return ResultMessage.newFailure("获取当前车票信息异常！");
        }
    }

    /**
     * 抢票：抢票时先创建抢票订单，python从java端读取待抢票数据
     *
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping("/createGrabOrder")
    @ApiOperation(notes = "抢票：抢票时先创建抢票订单，python从java端读取待抢票数据", value = "抢票：抢票时先创建抢票订单，python从java端读取待抢票数据")
    public ResultMessage createGrabOrder(@RequestBody GrabOrderVo vo) {
        logger.info("createGrabOrder!");
        try {
            TicketOrder ticketOrder = ticketOrderService.createGrabOrder(vo, "123");
            return ResultMessage.newSuccess("生成抢票订单成功！").setData(ticketOrder);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("生成抢票订单异常：", e);
            return ResultMessage.newFailure("生成抢票订单异常！");
        }
    }

    /**
     * python端获取待抢票订单
     * all = 1 时获取待抢票和正在抢票的订单。 all = 0 时只获取待抢票的订单
     *
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping("/getGrabOrders")
    @ApiOperation(notes = "python端获取待抢票订单", value = "python端获取待抢票订单")
    public ResultMessage getGrabOrders(String all) {
        logger.info("getGrabOrders!");
        try {
            List<TicketOrder> listTicketOrder = ticketOrderService.getGrabOrders(all);
            return ResultMessage.newSuccess("获取待抢票订单成功！").setData(listTicketOrder);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取待抢票订单异常：", e);
            return ResultMessage.newFailure("获取待抢票订单异常！");
        }
    }

    /**
     * 抢票结果通知（python调用）
     *
     * @param map
     * @return ResultMessage
     * @author lid
     */
    @ResponseBody
    @RequestMapping("/grabOrderResultNotice")
    @ApiOperation(notes = "抢票结果通知（python调用）", value = "抢票结果通知（python调用）")
    public ResultMessage grabOrderResultNotice(@RequestBody Map<String, Object> map) {
        logger.info("grabOrderResultNotice!map:" + map);
        try {
            ticketOrderService.grabOrderResultNotice(map);
            return ResultMessage.newSuccess("接收抢票结果成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("接收抢票结果异常：", e);
            return ResultMessage.newFailure("接收抢票结果异常！");
        }
    }

    /**
     * 修改订单状态
     *
     * @param orderId
     * @param orderStatus
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateOrderStatus")
    @ApiOperation(notes = "修改订单状态", value = "修改订单状态")
    public ResultMessage updateOrderStatus(String ticketOrderId, Integer orderStatus) {
        logger.info("updateGrabOrderStatus!orderId:" + ticketOrderId + ",orderStatus:" + orderStatus);
        try {
            TicketOrder ticketOrder = ticketOrderService.updateTicketOrderStatus(ticketOrderId, orderStatus);
            return ResultMessage.newSuccess("修改订单状态成功！").setData(ticketOrder);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("修改订单状态异常：", e);
            return ResultMessage.newFailure("修改订单状态异常！");
        }
    }

    /**
     * 根据车次以及出发车站、到达车站查询票价情况
     *
     * @param orderId
     * @param orderStatus
     * @return
     */
    @ResponseBody
    @RequestMapping("/getTicketByLineNoAndStation")
    @ApiOperation(notes = "根据车次以及出发车站、到达车站查询票价情况", value = "根据车次以及出发车站、到达车站查询票价情况")
    public ResultMessage getTicketByLineNoAndStation(String lineNo, String aboardStation, String arrivedStation) {
        logger.info("getTicketByLineNoAndStation!");
        try {
            LineTicket lineTicket = lineTicketService.findByLineNoAndStation(lineNo, aboardStation, arrivedStation);
            return ResultMessage.newSuccess("查询票价成功！").setData(lineTicket);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("查询票价异常：", e);
            return ResultMessage.newFailure("查询票价异常！");
        }
    }

    /**
     * 通过相关条件查询车票订单，包括乘车人信息
     *
     * @param pageParam
     * @param ticketPassengerVo
     * @return
     */
    @SuppressWarnings("static-access")
    @ResponseBody
    @RequestMapping("/queryTicketOrderDetail")
    @ApiOperation(notes = "包括乘车人信息", value = "通过相关条件查询车票订单")
    public ResultMessage queryTicketOrderDetail(PageParam pageParam, TicketPassengerVo ticketPassengerVo) {
        logger.info("queryTicketOrder!");
        try {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(ticketPassengerVo.getEndTime());
            calendar.add(calendar.DATE, 1);      //把日期往后增加一天.整数往后推,负数往前移动
            ticketPassengerVo.setEndTime(calendar.getTime());   //这个时间就是日期往后推一天的结果
            PageData TicketPassengerVo = ticketOrderService.queryTicketOrderDetail(pageParam, ticketPassengerVo);
            return ResultMessage.newSuccess("查询订单成功！").setData(TicketPassengerVo);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("查询订单异常：", e);
            return ResultMessage.newFailure("查询订单异常！");
        }
    }

    /**
     * 导出车票订单excel
     *
     * @param ticketPassengerVo
     * @param request
     * @param resp
     */
    @ResponseBody
    @RequestMapping("/downloadServiceProvider")
    @ApiOperation(notes = "导出车票订单excel", value = "导出车票订单excel")
    public void downloadServiceProvider(TicketPassengerVo ticketPassengerVo, HttpServletRequest request, HttpServletResponse resp) {
        logger.info("车票订单控制器: 导出车票订单");

        try {
            request.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/x-download");
            resp.addHeader("Content-Disposition", "attachment;filename=" + "download.xls");
            List<TicketPassengerVo> result = ticketOrderService.findTicketOrder(ticketPassengerVo);
            ExcelExportUtil<TicketPassengerVo> util = new ExcelExportUtil<TicketPassengerVo>();
            String[] title = {"订单编号", "订单日期", "购票类型", "乘客姓名", "通知手机号", "出发站", "目的站", "票数", "订单状态"};
            String[] values = {"ticketOrderId", "createDate", "orderType", "passengerName", "noticePhoneNo", "aboardStation", "arrivedStation", "ticketNumber", "orderStatus"};
            int[] widths = {120, 120, 120, 120, 120, 120, 120, 120, 120};
            util.exportExcel(title, values, widths, result, resp.getOutputStream());
            resp.getOutputStream().close();
//            return ResultMessage.newSuccess().setData(result);
        } catch (Exception e) {
            logger.error("导出车票订单数据异常：", e);
            e.printStackTrace();
//            return ResultMessage.newFailure("查询服务人员财务异常！");
        }
    }


}
