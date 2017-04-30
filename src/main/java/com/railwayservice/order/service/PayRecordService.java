package com.railwayservice.order.service;

import com.railwayservice.order.entity.PayRecord;

/**
 * 支付记录服务接口
 * @author lid
 * @date 2017.3.14
 */
public interface PayRecordService {
	
	/**
	 * 添加支付记录
	 * @param payRecord
	 * @return
	 */
	PayRecord addPayRecord(PayRecord payRecord);
}
