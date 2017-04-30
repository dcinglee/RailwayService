package com.railwayservice.operatemanage.dao;

import com.railwayservice.operatemanage.entity.FinanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 财务记录数据库访问接口。
 *
 * @author lid
 * @date 2017.2.7
 */
public interface FinanceRecordDao extends JpaRepository<FinanceRecord, String>, JpaSpecificationExecutor<FinanceRecord> {

    /**
     * 分页获取商户财务记录
     *//*
    @Query("select * from FinanceRecord f" +
            " where (f.merchant = :merchantId)")
    Page<FinanceRecord> findFinanceRecord(@Param("merchantId") String merchantId, Pageable pageable);
	
	*//**
     * 分页获取某个时间段内的财务记录
     *//*
	@Query("select * from FinanceRecord f" +
            " where (f.merchant = :merchantId and f.createDate >= :startDate and f.createDate <= :endDate)")
	Page<FinanceRecord> findFinanceRecordDuringDate(@Param("startDate") Date startDate,@Param("endDate") Date endDate, String merchantId, Pageable pageable);
	*/

}
