package com.railwayservice.grabticket.dao;

import com.railwayservice.grabticket.entity.KyfwUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 12306账号，用户账号关联接口。
 * 该接口使用了Spring Data JPA提供的方法。
 *
 * @author Ewing
 */
public interface KyfwUserDao extends JpaRepository<KyfwUser, String>, JpaSpecificationExecutor<KyfwUser> {


    KyfwUser findByUserName(String userName);

    Integer countByUserName(String userName);
}
