package com.railwayservice.order.web;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.interceptor.Authorize;
import com.railwayservice.application.util.ExcelExportUtil;
import com.railwayservice.application.util.TimeUtil;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.order.service.FinanceService;
import com.railwayservice.order.vo.FinanceMerchantVo;
import com.railwayservice.order.vo.FinanceServiceProviderVo;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/finance", produces = {"application/json;charset=UTF-8"})
@Api(value = "财务控制层", description = "财务的相关操作")
public class FinanceController {

    private final Logger logger = LoggerFactory.getLogger(FinanceController.class);

    @Autowired
    private FinanceService financeService;

    public void setFinanceService(FinanceService financeService) {
        this.financeService = financeService;
    }

    /**
     * 查询商务财务
     *
     * @return ResultMessage
     * @author xuy
     * @date 2017.2.8
     */
    @ResponseBody
    @RequestMapping("/queryMerchant")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_ORDER_FINANCE_MERCHANT"})
    public ResultMessage query(String stationId, String merchantName, String beginDate, String endDate) {
        logger.info("商务财务控制器: 查询商务财务：车站ID：" + stationId + ",商户名称：" + merchantName + ",开始时间" + beginDate + ",结束时间" + endDate);
        try {

            Date d1 = TimeUtil.convert2Date(beginDate, "yyyy-MM-dd");
            Date d2 = TimeUtil.convert2Date(endDate, "yyyy-MM-dd");
            List<FinanceMerchantVo> result = financeService.getMerchantOrders(stationId, merchantName, d1, d2);
            return ResultMessage.newSuccess().setData(result);
        } catch (Exception e) {
            logger.error("查询商家财务数据异常：", e);
            e.printStackTrace();
            return ResultMessage.newFailure("查询商家财务异常！");
        }
    }

    /**
     * 查询配送人员财务
     *
     * @return ResultMessage
     * @author xuy
     * @date 2017.2.8
     */
    @ResponseBody
    @RequestMapping("/findServiceProvider")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_ORDER_FINANCE_SERVICE_PROVIDER"})
    public ResultMessage findServiceProvider(String stationId, String serviceProviderName, String beginDate, String endDate) {
        logger.info("商务财务控制器: 查询配送人员财务：车站ID:" + stationId + ",服务人员名称：" + serviceProviderName + ",开始时间" + beginDate + ",结束时间" + endDate);
        try {

            Date d1 = TimeUtil.convert2Date(beginDate, "yyyy-MM-dd");
            Date d2 = TimeUtil.convert2Date(endDate, "yyyy-MM-dd");
            List<FinanceServiceProviderVo> result = financeService.findServiceProvider(stationId, serviceProviderName, d1, d2);
            return ResultMessage.newSuccess().setData(result);
        } catch (Exception e) {
            logger.error("查询服务人员财务数据异常：", e);
            e.printStackTrace();
            return ResultMessage.newFailure("查询服务人员财务异常！");
        }
    }

    /**
     * 导出配送人员财务
     *
     * @return ResultMessage
     * @author xuy
     * @date 2017.2.8
     */
    @ResponseBody
    @RequestMapping("/downloadServiceProvider")
    public void downloadServiceProvider(String stationId, String serviceProviderName, String beginDate, String endDate, HttpServletRequest request, HttpServletResponse resp) {
        logger.info("商务财务控制器: 导出配送人员财务：车站ID:" + stationId + ",服务人员名称：" + serviceProviderName + ",开始时间：" + beginDate + ",结束时间：" + endDate);

        try {
            request.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/x-download");
            resp.addHeader("Content-Disposition", "attachment;filename=" + "download.xls");
            Date d1 = TimeUtil.convert2Date(beginDate, "yyyy-MM-dd");
            Date d2 = TimeUtil.convert2Date(endDate, "yyyy-MM-dd");
            List<FinanceServiceProviderVo> result = financeService.findServiceProvider(stationId, serviceProviderName, d1, d2);
            ExcelExportUtil<FinanceServiceProviderVo> util = new ExcelExportUtil<FinanceServiceProviderVo>();
            String[] title = {"车站名称", "服务人员", "日期", "总订单数", "总金额"};
            String[] values = {"stationName", "serviceProviderName", "createDate", "totalCount", "totalCost"};
            int[] widths = {120, 120, 120, 120, 120};
            util.exportExcel(title, values, widths, result, resp.getOutputStream());
            resp.getOutputStream().close();
//            return ResultMessage.newSuccess().setData(result);
        } catch (Exception e) {
            logger.error("导出服务人员财务数据异常：", e);
            e.printStackTrace();
//            return ResultMessage.newFailure("查询服务人员财务异常！");
        }
    }


    /**
     * 导出商务财务
     *
     * @return ResultMessage
     * @author xuy
     * @date 2017.2.8
     */
    @ResponseBody
    @RequestMapping("/downloadMerchant")
    public void downloadMerchant(String stationId, String merchantName, String beginDate, String endDate, HttpServletRequest request, HttpServletResponse resp) {
        logger.info("商务财务控制器: 导出商务财务：车站ID:" + stationId + ",商户名称：" + merchantName + ",开始时间：" + beginDate + ",结束时间：" + endDate);
        try {
            request.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/x-download");
            resp.addHeader("Content-Disposition", "attachment;filename=" + "download.xls");
            Date d1 = TimeUtil.convert2Date(beginDate, "yyyy-MM-dd");
            Date d2 = TimeUtil.convert2Date(endDate, "yyyy-MM-dd");
            List<FinanceMerchantVo> result = financeService.getMerchantOrders(stationId, merchantName, d1, d2);
//            return ResultMessage.newSuccess().setData(result);

            ExcelExportUtil<FinanceMerchantVo> util = new ExcelExportUtil<FinanceMerchantVo>();
            String[] title = {"车站名称", "商户", "订单日期", "已完成订单", "已完成订单金额", "未完成订单", "未完成订单金额", "取消订单", "取消订单金额", "拒绝订单", "拒绝订单金额", "合计计单", "合计金额"};
            String[] values = {"stationName", "merchantName", "createDate", "completedCount", "completedPrice", "uncompletedCount", "uncompletedPrice", "cancelCount", "cancelPrice", "rejectCount", "rejectPrice", "totalCount", "totalPrice"};
            int[] widths = {120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120};
            util.exportExcel(title, values, widths, result, resp.getOutputStream());
            resp.getOutputStream().close();
        } catch (Exception e) {
            logger.error("导出商家财务数据异常：", e);
            e.printStackTrace();
//            return ResultMessage.newFailure("查询商家财务异常！");
        }
    }

}
