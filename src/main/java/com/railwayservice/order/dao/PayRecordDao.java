package com.railwayservice.order.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.railwayservice.order.entity.PayRecord;

public interface PayRecordDao extends JpaRepository<PayRecord, String>, JpaSpecificationExecutor<PayRecord>{

}
