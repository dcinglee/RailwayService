package com.railwayservice.operatemanage.web;

import com.railwayservice.operatemanage.entity.FinanceRecord;
import com.railwayservice.operatemanage.service.FinanceRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 财务模块请求控制器。
 *
 * @author lid
 * @date 2017.2.7
 */
@Controller
@RequestMapping("/financeRecordController")
public class FinanceRecordController {
    private final Logger logger = LoggerFactory.getLogger(FinanceRecordController.class);

    private FinanceRecordService financeRecordService;

    /**
     * 导出excel表格的文件名称  按照时间格式生成
     *
     * @return String
     * @author lid
     * @date 2017.2.7
     */
    private static String getFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = sdf.format(new Date());
        return "财务统计报表" + fileName + ".xls";
    }

    /**
     * 财务统计报表excel属性值
     */
    public static String[] createFinanceRecordExpProp() {
        return new String[]{
                "createDate", "fee"
        };
    }

    /**
     * 财务统计报表excel标题值
     */
    public static String[] createFinanceRecordExpHead() {
        return new String[]{
                "创建时间", "金额"
        };
    }

    @Autowired
    public void setFinanceRecordService(FinanceRecordService financeRecordService) {
        this.financeRecordService = financeRecordService;
    }

    /**
     * 导出财务记录excel表格
     */
    @ResponseBody
    @RequestMapping("/expAllFinanceRecord")
    public void expAllFinanceRecord(HttpSession session) {
        List<FinanceRecord> listAllFinanceRecord = financeRecordService.getAllFinanceRecord();
        String filename = getFileName();
        String[] props = createFinanceRecordExpProp();
        String[] titles = createFinanceRecordExpHead();
        //return new ModelAndView(new ExcelView(filename, "sheet1", props, titles, null, null, listAllFinanceRecord, null), model);
        System.out.println("listAllFinanceRecord.size():" + listAllFinanceRecord.size());
    }

}
