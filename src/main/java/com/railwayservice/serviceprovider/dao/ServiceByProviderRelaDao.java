package com.railwayservice.serviceprovider.dao;

import com.railwayservice.serviceprovider.entity.ServiceByProviderRela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author lixs
 * @date 2017年2月17日
 * @describe 服务管理数据库访问接口。
 * 该接口使用了Spring Data JPA提供的方法。
 */

public interface ServiceByProviderRelaDao extends JpaRepository<ServiceByProviderRela, String>, JpaSpecificationExecutor<ServiceByProviderRela> {

    /**
     * 删除服务人员和服务类型的关系
     *
     * @param serviceProviderId
     * @return
     */
    @Modifying
    @Query("delete from ServiceByProviderRela arr where arr.serviceProvider = ?1")
    Integer deleteByServiceProviderId(String serviceProviderId);
}
