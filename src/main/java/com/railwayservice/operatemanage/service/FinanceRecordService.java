package com.railwayservice.operatemanage.service;

import com.railwayservice.operatemanage.entity.FinanceRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * 财务记录服务类。
 *
 * @author lid
 * @date 2017.2.7
 */
public interface FinanceRecordService {
    /**
     * 添加财务记录
     *
     * @param financeRecord
     * @return financeRecord
     * @author lid
     * @date 2017.2.7
     */
    FinanceRecord addFinanceRecord(FinanceRecord financeRecord);

    /**
     * 分页获取所有的财务记录
     *
     * @param pageable
     * @return Page
     * @author lid
     * @date 2017.2.7
     */
    Page<FinanceRecord> findAllFinanceRecordByPage(Pageable pageable);

    /**
     * 获取所有的财务记录
     *
     * @return List
     * @author lid
     * @date 2017.2.7
     */
    List<FinanceRecord> getAllFinanceRecord();

    /**
     * 按照起始时间分页获取商户财务记录
     *
     * @param startDate
     * @param endDate
     * @param merchantId
     * @param pageable
     * @return page
     * @author lid
     * @date 2017.2.7
     */
    Page<FinanceRecord> findFinanceRecordDuringDate(Date startDate, Date endDate, String merchantId, Pageable pageable);

}
