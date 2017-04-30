package com.railwayservice.order.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.order.dao.PayRecordDao;
import com.railwayservice.order.entity.PayRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PayRecordServiceImpl implements PayRecordService {

    private final Logger logger = LoggerFactory.getLogger(PayRecordServiceImpl.class);

    private PayRecordDao payRecordDao;

    @Autowired
    public void setPayRecordDao(PayRecordDao payRecordDao) {
        this.payRecordDao = payRecordDao;
    }


    @Override
    public PayRecord addPayRecord(PayRecord payRecord) {
        logger.info("添加支付记录:支付状态：" + payRecord.getPayStatus());
        if (null == payRecord) {
            throw new AppException("payRecord参数为空");
        }
        payRecord.setCreateDate(new Date());
        return payRecordDao.save(payRecord);
    }

}
