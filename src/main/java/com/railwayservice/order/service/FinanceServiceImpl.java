package com.railwayservice.order.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.util.TimeUtil;
import com.railwayservice.order.dao.FinanceDao;
import com.railwayservice.order.vo.FinanceMerchantVo;
import com.railwayservice.order.vo.FinanceServiceProviderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FinanceServiceImpl implements FinanceService {

    @Autowired
    private FinanceDao financeDao;

    public FinanceDao getFinanceDao() {
        return financeDao;
    }

    public void setFinanceDao(FinanceDao financeDao) {
        this.financeDao = financeDao;
    }

    @Override
    public List<FinanceMerchantVo> getMerchantOrders(String stationId, String merchantName, Date beginDate, Date endDate) {

        if (beginDate == null) {
            throw new AppException("开始时间不允许为空");
        }

        if (endDate == null) {
            throw new AppException("结束时间不允许为空");
        }

        if (beginDate != null) {
            System.out.println("begin=" + TimeUtil.convert2String(beginDate, "YYYYMMDD HH24"));
            beginDate = TimeUtil.setStartDay(beginDate);
            System.out.println("begin=" + TimeUtil.convert2String(beginDate, "YYYYMMDD HH24"));
        }
        if (endDate != null) {
            System.out.println("endDate=" + TimeUtil.convert2String(endDate, "YYYYMMDD HH24"));
            endDate = TimeUtil.setEndDay(endDate);
            System.out.println("endDate=" + TimeUtil.convert2String(endDate, "YYYYMMDD HH24"));
        }

        return financeDao.findMerchantMainOrder(stationId, merchantName, beginDate, endDate);

    }

    @Override
    public List<FinanceServiceProviderVo> findServiceProvider(String stationId, String serviceProviderName, Date beginDate, Date endDate) {
        if (beginDate == null) {
            throw new AppException("开始时间不允许为空");
        }

        if (endDate == null) {
            throw new AppException("结束时间不允许为空");
        }

        if (beginDate != null) {
//			System.out.println("begin="+TimeUtil.convert2String(beginDate, "YYYYMMDD HH24"));
            beginDate = TimeUtil.setStartDay(beginDate);
//			System.out.println("begin="+TimeUtil.convert2String(beginDate, "YYYYMMDD HH24"));
        }
        if (endDate != null) {
//			System.out.println("endDate="+TimeUtil.convert2String(endDate, "YYYYMMDD HH24"));
            endDate = TimeUtil.setEndDay(endDate);
//			System.out.println("endDate="+TimeUtil.convert2String(endDate, "YYYYMMDD HH24"));
        }

        return financeDao.findServiceProvider(stationId, serviceProviderName, beginDate, endDate);
    }

}
