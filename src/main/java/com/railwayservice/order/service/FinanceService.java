package com.railwayservice.order.service;

import com.railwayservice.order.vo.FinanceMerchantVo;
import com.railwayservice.order.vo.FinanceServiceProviderVo;

import java.util.Date;
import java.util.List;

/**
 * 财务查询
 *
 * @author xuyu
 */
public interface FinanceService {

    List<FinanceMerchantVo> getMerchantOrders(String stationId, String merchantName, Date beginDate, Date endDate);

    List<FinanceServiceProviderVo> findServiceProvider(String stationId, String serviceProviderName, Date beginDate, Date endDate);

}
