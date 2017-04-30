package com.railwayservice.serviceprovider.service;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.util.EncodeUtil;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.messages.entity.ChannelInfo;
import com.railwayservice.messages.service.ChannelInfoService;
import com.railwayservice.serviceprovider.dao.ServiceByProviderRelaDao;
import com.railwayservice.serviceprovider.dao.ServiceProviderDao;
import com.railwayservice.serviceprovider.dao.ServiceTypeDao;
import com.railwayservice.serviceprovider.entity.ServiceByProviderRela;
import com.railwayservice.serviceprovider.entity.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 角色管理服务实现类
 *
 * @author lid
 * @date 2017.2.9
 */
@Service
public class ServiceProviderServiceImpl implements ServiceProviderService {

    private final Logger logger = LoggerFactory.getLogger(ServiceProviderServiceImpl.class);

    private ServiceProviderDao serviceProviderDao;

    private ServiceTypeDao serviceTypeDao;

    private ServiceByProviderRelaDao serviceByProviderRelaDao;

    private ChannelInfoService channelInfoService;

    @Autowired
    public void setChannelInfoService(ChannelInfoService channelInfoService) {
        this.channelInfoService = channelInfoService;
    }

    @Autowired
    public void setServiceByProviderRelaDao(ServiceByProviderRelaDao serviceByProviderRelaDao) {
        this.serviceByProviderRelaDao = serviceByProviderRelaDao;
    }

    @Autowired
    public void setServiceProviderDao(ServiceProviderDao serviceProviderDao) {
        this.serviceProviderDao = serviceProviderDao;
    }

    @Autowired
    public void setServiceTypeDao(ServiceTypeDao serviceTypeDao) {
        this.serviceTypeDao = serviceTypeDao;
    }

    @Override
    public ServiceProvider findByServiceProviderId(String id) {
        logger.info("服务人员服务层->服务人员查询->服务人员ID：" + id);
        if (null == id) {
            throw new AppException("缺少id参数！");
        }
        return serviceProviderDao.findByServiceProviderId(id);
    }

    @Override
    public Page<ServiceProvider> queryServiceProvider(String name, String phoneNo, String identityCardNo, String stationId, String order, Pageable pageable) {
        logger.info("服务人员服务层->服务人员查询->服务人员名称：" + name + "电话：" + phoneNo + "身份证号码：" + identityCardNo);
        // JPA标准查询接口，使用Lambda表达式。root以商户类为根对象。
        Specification<ServiceProvider> specification = (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            // 如果名称不为空，添加到查询条件。
            if (StringUtils.hasText(name)) {
                predicate = builder.and(predicate, builder.like(root.get("name"), "%" + name + "%"));
            }
            // 如果电话号码不为空，添加到查询条件。
            if (StringUtils.hasText(phoneNo)) {
                predicate = builder.and(predicate, builder.like(root.get("phoneNo"), "%" + phoneNo + "%"));
            }

            // 如果身份证号码不为空，添加到查询条件。
            if (StringUtils.hasText(identityCardNo)) {
                predicate = builder.and(predicate, builder.like(root.get("identityCardNo"), "%" + identityCardNo + "%"));
            }

            //如果车站不为空，添加到查询条件
            if (StringUtils.hasText(stationId)) {
                predicate = builder.and(predicate, builder.like(root.get("stationId"), "%" + stationId + "%"));
            }
            if ("asc".equals(order)) {
                query.orderBy(builder.asc(root.get("age")));
            } else {
                query.orderBy(builder.desc(root.get("age")));
            }
            return predicate;
        };
        // 调用JPA标准查询接口查询数据。
//        return serviceProviderDao.findAll(specification, pageable);

        Page<ServiceProvider> result = serviceProviderDao.findAll(specification, pageable);
        Iterator<ServiceProvider> iter = result != null ? result.iterator() : null;//.forEach(action);
        while (iter != null && iter.hasNext()) {
            ServiceProvider sp = iter.next();
            sp.setServiceTypes(serviceTypeDao.findAllServiceTypeByProviderId(sp.getServiceProviderId()));
        }

        //fill with online count.
        iter = result != null ? result.iterator() : null;//.forEach(action);
        ServiceProvider sp = null;
        List<ChannelInfo> onlineCount = null;
        while (iter != null && iter.hasNext()) {
            sp = iter.next();
            if (sp.getStatus() == ServiceProviderStatic.SERVICEPROVIDER_NORMAL) {
                onlineCount = channelInfoService.getOnlineWorkChannelInfoByUserId(sp.getServiceProviderId());
                sp.setOnlineNum((onlineCount != null) ? onlineCount.size() : 0);
            }
//            sp.setServiceTypes(serviceTypeDao.findAllServiceTypeByProviderId(sp.getServiceProviderId()));
        }

//        if(serviceType != null){
//			for(ServiceType st:serviceType){
//				serviceTypeDao.findAllServiceTypeByProviderId
//			}
//		}

        return result;
    }

    @Override
    @Transactional
    public ServiceProvider addServiceProvider(ServiceProvider serviceProvider, String[] typeIds) {
        if (null == serviceProvider) {
            throw new AppException("添加对象为空！");
        }
        logger.info("服务人员服务层->服务人员添加->服务人员名称：" + serviceProvider.getName());
        if (!StringUtils.hasText(serviceProvider.getStationId())) {
            throw new AppException("所属车站不允许为空！");
        }
        if (StringUtils.hasText(serviceProvider.getPhoneNo()) && serviceProviderDao.countByPhoneNo(serviceProvider.getPhoneNo()) > 0) {
            throw new AppException("该电话号码已存在！");
        }

        serviceProvider.setCreateDate(new Date());
        // 密码存储前需要加密，使用不变的账号作为盐。
        serviceProvider.setPassword(EncodeUtil.encodePassword(serviceProvider.getPassword(), serviceProvider.getAccount()));
        serviceProvider = serviceProviderDao.save(serviceProvider);

        //查找所有角色，并保存角色与管理员的关联关系
        for (int index = 0; typeIds != null && index < typeIds.length; index++) {
            ServiceByProviderRela rels = new ServiceByProviderRela();
            rels.setServiceProvider(serviceProvider.getServiceProviderId());
            rels.setServiceTypeId(typeIds[index]);
            rels.setCreateDate(new Date());
            serviceByProviderRelaDao.save(rels);
        }

        return serviceProvider;
    }

    @Override
    @Transactional
    public void deleteServiceByProviderByProviderId(String serviceProviderId) {
        serviceByProviderRelaDao.deleteByServiceProviderId(serviceProviderId);
    }

    @Override
    @Transactional
    public ServiceProvider updateServiceProvider(ServiceProvider serviceProvider, String[] typeIds) {
        if (null == serviceProvider) {
            throw new AppException("未选择要修改的服务人员！");
        }
        logger.info("服务人员服务层->服务人员修改->服务人员名称：" + serviceProvider.getName());

        if (serviceProvider.getServiceProviderId() == null || "".equals(serviceProvider.getServiceProviderId())) {
            throw new AppException("未选择要修改的服务人员！");
        }

        //查找对应的记录  如果没找到抛出异常
        ServiceProvider oldServiceProvider = serviceProviderDao.findByServiceProviderId(serviceProvider.getServiceProviderId());
        if (null == oldServiceProvider) {
            throw new AppException("未找到待修改的服务人员记录！");
        }

        if (serviceProviderDao.countByPhoneNo(oldServiceProvider.getPhoneNo()) > 1) {
            throw new AppException("该手机号已存在！");
        }

        if (serviceProvider.getStationId() == null || "".equals(serviceProvider.getStationId().trim())) {
            throw new AppException("所属车站不允许为空！");
        }

        //先删除
        serviceByProviderRelaDao.deleteByServiceProviderId(serviceProvider.getServiceProviderId());
        //查找所有角色，并保存角色与管理员的关联关系
        for (int index = 0; typeIds != null && index < typeIds.length; index++) {
            ServiceByProviderRela rels = new ServiceByProviderRela();
            rels.setServiceProvider(serviceProvider.getServiceProviderId());
            rels.setServiceTypeId(typeIds[index]);
            rels.setCreateDate(new Date());
            serviceByProviderRelaDao.save(rels);
        }

        // 如果旧密码和新密码不同，则密码被修改，加密保存新密码。
//        String password = serviceProvider.getPassword();
//        if (!password.equals(oldServiceProvider.getPassword()))
//            oldServiceProvider.setPassword(EncodeUtil.encodePassword(password, oldServiceProvider.getAccount()));

        //设置属性值
        oldServiceProvider.setAge(serviceProvider.getAge());
        oldServiceProvider.setGender(serviceProvider.getGender());
        oldServiceProvider.setIdentityCardNo(serviceProvider.getIdentityCardNo());
        oldServiceProvider.setName(serviceProvider.getName());
        oldServiceProvider.setPhoneNo(serviceProvider.getPhoneNo());
        oldServiceProvider.setPhotoId(serviceProvider.getPhotoId());
        oldServiceProvider.setStationId(serviceProvider.getStationId());
        oldServiceProvider.setStatus(serviceProvider.getStatus());
        return serviceProviderDao.save(oldServiceProvider);
    }

    @Override
    @Transactional
    public void deleteServiceProvider(String serviceProviderId) {
        logger.info("服务人员服务层->服务人员删除->服务人员ID：" + serviceProviderId);
        if (null == serviceProviderId) {
            throw new AppException("未选择需要删除的对象！");
        }

        //先删除关联关系
        serviceByProviderRelaDao.deleteByServiceProviderId(serviceProviderId);

        ServiceProvider serviceProvider = serviceProviderDao.findByServiceProviderId(serviceProviderId);
        serviceProviderDao.delete(serviceProvider);
    }

    @Override
    public List<ServiceProvider> findByName(String name) {
        logger.info("服务人员服务层->服务人员查询->服务人员名称：" + name);
        if (null == name) {
            throw new AppException("服务人员名称不能为空！");
        }
        return serviceProviderDao.findByName(name);
    }

    @Override
    public ResultMessage loginByPhoneNo(String phoneNo, String password) {
        logger.info("服务人员服务层：服务人员登陆。");
        // 校验用户名和密码不能为空。
        if (!StringUtils.hasText(phoneNo))
            return ResultMessage.newFailure("服务人员手机号不能为空！");
        if (!StringUtils.hasText(password))
            return ResultMessage.newFailure("服务人员密码不能为空！");
        ServiceProvider serviceProvider = serviceProviderDao.findByPhoneNo(phoneNo);
        if (serviceProvider == null)
            return ResultMessage.newFailure("服务人员不存在或已删除！");
        // 传入的是明文密码，转换成数据库存储的加密形式。
        password = EncodeUtil.encodePassword(password, serviceProvider.getAccount());
        if (password.equals(serviceProvider.getPassword())) {
            return ResultMessage.newSuccess("服务人员登陆成功！").setData(serviceProvider);
        } else {
            return ResultMessage.newFailure("账号或者密码不正确！");
        }
    }

    @Override
    public boolean hasPhoneNo(String phoneNo) {
        long count = serviceProviderDao.countByPhoneNo(phoneNo);
        return count > 0;
    }

    @Override
    public ServiceProvider findByPhoneNo(String phoneNo) {
        return serviceProviderDao.findByPhoneNo(phoneNo);
    }

    @Override
    public ServiceProvider changePasswordByPhoneNo(String phoneNo, String newPassword) {
        logger.info("服务人员服务层->修改密码->服务人员电话：" + phoneNo + "密码：" + newPassword);
        if (!StringUtils.hasText(phoneNo) || !StringUtils.hasText(newPassword))
            throw new AppException("电话号码和密码不能为空！");
        if (!newPassword.matches(AppConfig.PASSWORD_PATTEN))
            throw new AppException("密码不符合要求，请检查是否含有特殊符号和满足长度。");
        // 查询帐号。
        ServiceProvider serviceProvider = serviceProviderDao.findByPhoneNo(phoneNo);
        if (serviceProvider == null)
            throw new AppException("服务人员不存在或已删除！");
        // 密码加密。
        newPassword = EncodeUtil.encodePassword(newPassword, serviceProvider.getAccount());
        serviceProvider.setPassword(newPassword);
        return serviceProviderDao.save(serviceProvider);
    }

    @Override
    public Integer queryOnlineNum(String phoneNo) {

        logger.info("服务人员服务层：查询在线服务人员人数：服务人员电话：" + phoneNo);

        ServiceProvider serviceProvider = serviceProviderDao.findByPhoneNo(phoneNo);

        String userId = serviceProvider.getServiceProviderId();

        List<ChannelInfo> channelInfos = channelInfoService.getOnlineWorkChannelInfoByUserId(userId);
        return channelInfos.size();
    }
}
