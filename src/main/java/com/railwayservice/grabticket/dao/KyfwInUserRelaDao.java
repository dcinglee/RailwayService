package com.railwayservice.grabticket.dao;

import com.railwayservice.grabticket.entity.KyfwInUserRela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 12306账号，用户账号关联接口。
 * 该接口使用了Spring Data JPA提供的方法。
 *
 * @author Ewing
 */
public interface KyfwInUserRelaDao extends JpaRepository<KyfwInUserRela, String>, JpaSpecificationExecutor<KyfwInUserRela> {

    /**
     * 通过userID查询乘车人信息列表
     *
     * @param userId
     * @return
     */
    KyfwInUserRela findByUserId(String userId);

    Integer countByKyfwUserId(String kyfwUserId);
}
