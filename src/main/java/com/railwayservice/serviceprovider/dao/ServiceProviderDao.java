package com.railwayservice.serviceprovider.dao;

import com.railwayservice.serviceprovider.entity.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 服务人员数据库访问接口
 *
 * @author lid
 * @date 2017.2.9
 */

public interface ServiceProviderDao extends JpaRepository<ServiceProvider, String>, JpaSpecificationExecutor<ServiceProvider> {
    /**
     * 根据id查找对应的服务员
     *
     * @author lid
     * @date 2017.2.8
     */
    ServiceProvider findByServiceProviderId(String id);

    /**
     * 根据name查找对应的服务员
     *
     * @author lid
     * @date 2017.2.20
     */
    List<ServiceProvider> findByName(String name);

    /**
     * 服务人员登录
     *
     * @param phoneNo  服务人员手机号
     * @param password 服务人员密码
     * @return ServiceProvider
     */
    ServiceProvider findByPhoneNoAndPassword(String phoneNo, String password);

    /**
     * 手机号码是否已存在。
     *
     * @param phoneNo 手机号码。
     * @return 是否存在。
     */
    long countByPhoneNo(String phoneNo);

    /**
     * 根据手机号查询服务员。
     *
     * @param phoneNo 手机号。
     * @return 商户。
     */
    ServiceProvider findByPhoneNo(String phoneNo);
}
