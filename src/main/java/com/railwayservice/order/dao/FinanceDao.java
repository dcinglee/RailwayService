package com.railwayservice.order.dao;

import com.railwayservice.order.vo.FinanceMerchantVo;
import com.railwayservice.order.vo.FinanceServiceProviderVo;

import java.util.Date;
import java.util.List;

public interface FinanceDao {

    List<FinanceMerchantVo> findMerchantMainOrder(String stationId, String merchantName, Date beginDate, Date endDate);

    List<FinanceServiceProviderVo> findServiceProvider(String stationId, String serviceProviderName, Date beginDate, Date endDate);

}
