package com.railwayservice.grabticket.dao;

import com.railwayservice.grabticket.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 车票数据库访问接口。
 * 该接口使用了Spring Data JPA提供的方法。
 *
 * @author Ewing
 */
public interface PassengerDao extends JpaRepository<Passenger, String>, JpaSpecificationExecutor<Passenger> {

    /**
     * 乘车人信息记录查询（通过用户ID）
     *
     * @param userId 用户ID
     * @return
     */
    List<Passenger> findByUserId(String userId);

    /**
     * 根据证件号码查找乘车人
     * @param identityCardNo
     * @return Passenger
     * @author lid
     * @date 2017.4.24
     */
    Passenger findByIdentityCardNo(String identityCardNo);
}
