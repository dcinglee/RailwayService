package com.railwayservice.serviceprovider.dao;

import com.railwayservice.serviceprovider.entity.ServiceType;
import com.railwayservice.serviceprovider.service.ServiceTypeStatic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author lixs
 * @date 2017年2月17日
 * @describe 服务管理数据库访问接口。
 * 该接口使用了Spring Data JPA提供的方法。
 */

public interface ServiceTypeDao extends JpaRepository<ServiceType, String>, JpaSpecificationExecutor<ServiceType> {

    /**
     * 根据name查找对应的管理员
     *
     * @param name 服务名
     * @return ServiceType  服务对象
     * @author lixs
     * @date 2017.2.17
     */
    List<ServiceType> findByName(String name);

    /**
     * 查找所有类型
     *
     * @return
     */
    @Query("select distinct a from ServiceType a where a.status = " + ServiceTypeStatic.SERVICE_TYPE_NORMAL)
    List<ServiceType> findAllServiceTypeByNormalStatus();

    /**
     * 查找所有类型
     *
     * @param serviceProviderId
     * @return
     */
    @Query("select distinct a FROM ServiceType a,ServiceByProviderRela rela  where rela.serviceTypeId=a.typeId and rela.serviceProvider = ?1")
    List<ServiceType> findAllServiceTypeByProviderId(String serviceProviderId);

    Integer countByName(String name);

}
