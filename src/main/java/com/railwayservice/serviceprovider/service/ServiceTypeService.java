package com.railwayservice.serviceprovider.service;

import com.railwayservice.serviceprovider.entity.ServiceType;

import java.util.List;

/**
 * 服务类型接口。
 *
 * @author lixs
 */

public interface ServiceTypeService {

    /**
     * 添加服务
     *
     * @param serviceType 服务对象
     * @return ServiceType
     * @author lixs
     * @date 2017.2.16
     */
    ServiceType addServiceType(ServiceType serviceType);

    /**
     * 删除服务
     *
     * @param typeId
     * @return ServiceType
     * @author lixs
     * @date 2017.2.16
     */
    void deleteServiceType(String typeId);

    /**
     * 更新服务
     *
     * @param serviceType
     * @return ServiceType
     * @author lixs
     * @date 2017.2.16
     */
    ServiceType updateServiceType(ServiceType serviceType);

    /**
     * 查询服务
     *
     * @param id 服务id
     * @return ServiceType
     * @author lixs
     * @date 2017.2.16
     */
    ServiceType findServiceType(String id);

    /**
     * 查找所有服务
     *
     * @return
     */
    List<ServiceType> findAllServiceType(String order);

    /**
     * 查找某人所能提供的服务
     *
     * @param providerId
     * @return
     */
    List<ServiceType> findServiceTypeByProvider(String providerId);

    /**
     * 查找所有可用的服务
     *
     * @return
     */
    List<ServiceType> findAllServiceTypeByNormalStatus();

    /**
     * 根据服务名称查询服务列表
     *
     * @param name
     * @return
     */
    List<ServiceType> findByName(String name);
}
