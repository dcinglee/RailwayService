package com.railwayservice.serviceprovider.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.serviceprovider.dao.ServiceTypeDao;
import com.railwayservice.serviceprovider.entity.ServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author lixs
 * @date 2017年2月14日
 * @describe 服务管理类接口实现
 */

@Service
public class ServiceTypeServiceImpl implements ServiceTypeService {
    private final Logger logger = LoggerFactory.getLogger(ServiceTypeService.class);

    @Autowired
    private ServiceTypeDao serviceTypeDao;

    public void setServiceTypeDao(ServiceTypeDao serviceTypeDao) {
        this.serviceTypeDao = serviceTypeDao;
    }

    /**
     * 添加服务
     */
    @Override
    @Transactional
    public ServiceType addServiceType(ServiceType serviceType) {
        if (null == serviceType) {
            throw new AppException("没有服务信息");
        }
        logger.info("服务管理服务层->添加服务->服务名称：" + serviceType.getName());

        if (!StringUtils.hasText(serviceType.getName())) {
            throw new AppException("服务名称不能为空");
        }
        if (serviceTypeDao.countByName(serviceType.getName()) > 0) {
            throw new AppException("已有当前服务");
        }
        serviceType.setCreateDate(new Date());
        serviceTypeDao.save(serviceType);
        return serviceType;
    }

    /**
     * 删除服务
     */
    @Override
    @Transactional
    public void deleteServiceType(String typeId) {
        logger.info("服务管理服务层->删除服务->服务id：" + typeId);
        if (!StringUtils.hasText(typeId)) {
            throw new AppException("请选择要删除的服务");
        }
        serviceTypeDao.delete(typeId);
    }

    /**
     * 更新服务
     */
    @Override
    @Transactional
    public ServiceType updateServiceType(ServiceType serviceType) {
        if (serviceType == null) {
            throw new AppException("没有服务信息");
        }
        logger.info("服务管理服务层->更新服务->服务id：" + serviceType.getTypeId());
        serviceType.setIntroduction(serviceType.getIntroduction());
        serviceType.setName(serviceType.getName());
        serviceType.setStatus(serviceType.getStatus());
        serviceType.setDistributionCosts(serviceType.getDistributionCosts());
        serviceType.setCreateDate(new Date());
        serviceTypeDao.save(serviceType);
        return serviceType;
    }

    /**
     * 查找服务
     */
    @Override
    public ServiceType findServiceType(String id) {
        logger.info("服务管理服务层->查找服务->服务id：" + id);
        if (!StringUtils.hasText(id)) {
            throw new AppException("未输入服务id");
        }
        ServiceType serviceType = serviceTypeDao.findOne(id);
        if (serviceType == null) {
            throw new AppException("没有服务信息");
        }
        return serviceType;
    }

    @Override
    public List<ServiceType> findAllServiceType(String order) {
        logger.info("服务管理服务层->查找服务列表：按时间排序");
        List<ServiceType> serviceType;
        if ("desc".equals(order)) {
            serviceType = serviceTypeDao.findAll(new Sort(Sort.Direction.DESC, "createDate"));
        } else {
            serviceType = serviceTypeDao.findAll(new Sort(Sort.Direction.ASC, "createDate"));
        }
        return serviceType;
    }

    @Override
    public List<ServiceType> findAllServiceTypeByNormalStatus() {
        logger.info("服务管理服务层->查找服务");
        return serviceTypeDao.findAllServiceTypeByNormalStatus();
    }

    @Override
    public List<ServiceType> findByName(String name) {
        logger.info("服务管理服务层->查找服务->服务名称：" + name);
        if (!StringUtils.hasText(name))
            throw new AppException("服务名称不能为空");
        return serviceTypeDao.findByName(name);
    }

    @Override
    public List<ServiceType> findServiceTypeByProvider(String providerId) {
        logger.info("服务管理服务层->查找服务列表");
        return serviceTypeDao.findAllServiceTypeByProviderId(providerId);
    }

}
