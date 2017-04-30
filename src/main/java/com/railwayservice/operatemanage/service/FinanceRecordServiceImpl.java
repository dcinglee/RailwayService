package com.railwayservice.operatemanage.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.operatemanage.dao.FinanceRecordDao;
import com.railwayservice.operatemanage.entity.FinanceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * 财务管理服务类。
 *
 * @author Ewing
 */
@Service
public class FinanceRecordServiceImpl implements FinanceRecordService {

    private final Logger logger = LoggerFactory.getLogger(FinanceRecordServiceImpl.class);

    private FinanceRecordDao financeRecordDao;

    @Autowired
    public void setFinanceRecordDao(FinanceRecordDao financeRecordDao) {
        this.financeRecordDao = financeRecordDao;
    }

    @Override
    @Transactional
    public FinanceRecord addFinanceRecord(FinanceRecord financeRecord) {
        if (null == financeRecord) {
            throw new AppException("输入的财务记录为空！");
        }
        logger.info("财务服务层：新增财务：所属商户：" + financeRecord.getMerchantId());

        financeRecord.setCreateDate(new Date());
        return financeRecordDao.save(financeRecord);
    }

    @Override
    public Page<FinanceRecord> findFinanceRecordDuringDate(Date startDate, Date endDate, String merchantId,
                                                           Pageable pageable) {
        logger.info("财务服务层：查询财务：商户ID：" + merchantId);
        return null;
    }

    @Override
    public Page<FinanceRecord> findAllFinanceRecordByPage(Pageable pageable) {
        logger.info("财务服务层：查询所有财务：分页查询：");
        return financeRecordDao.findAll(pageable);
    }

    @Override
    public List<FinanceRecord> getAllFinanceRecord() {
        logger.info("财务服务层：查询所有财务：财务列表：");
        return financeRecordDao.findAll();
    }

}
