package com.railwayservice.serviceprovider.service;

import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.serviceprovider.entity.ServiceProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServiceProviderService {

    /**
     * 根据id查找对应的服务员
     *
     * @return ServiceProvider
     * @author lid
     * @date 2017.2.8
     */
    ServiceProvider findByServiceProviderId(String id);

    /**
     * 根据name查找对应的服务员
     *
     * @return ServiceProvider
     * @author lid
     * @date 2017.2.20
     */
    List<ServiceProvider> findByName(String name);

    /**
     * 根据name、电话号码、身份证号码、站点查找对应的服务员
     *
     * @return Page
     * @author lid
     * @date 2017.2.8
     */
    Page<ServiceProvider> queryServiceProvider(String name, String phoneNo, String identityCardNo, String stationId, String order, Pageable pageable);

    /**
     * 添加单个服务人员信息
     *
     * @param serviceProvider
     * @return ServiceProvider
     * @author lid
     * @date 2017.2.9
     */
    ServiceProvider addServiceProvider(ServiceProvider serviceProvider, String[] serviceTypeId);

    /**
     * 修改服务人员信息
     *
     * @param serviceProvider
     * @return ServiceProvider
     * @author lid
     * @date 2017.2.9
     */
    ServiceProvider updateServiceProvider(ServiceProvider serviceProvider, String[] typeId);

    /**
     * 删除服务人员信息
     *
     * @param serviceProviderId
     * @author lid
     * @date 2017.2.9
     */
    void deleteServiceProvider(String serviceProviderId);

    /**
     * 删除指定服务人员分类
     *
     * @param serviceProviderId
     */
    void deleteServiceByProviderByProviderId(String serviceProviderId);

    /**
     * 服务人员登录
     *
     * @param account  服务人员账号
     * @param password 服务人员密码
     * @return ResultMessage
     */
    ResultMessage loginByPhoneNo(String account, String password);

    /**
     * 手机号码是否已存在。
     *
     * @param phoneNo 手机号码。
     * @return 是否存在。
     */
    boolean hasPhoneNo(String phoneNo);

    /**
     * 根据手机号查询服务员。
     *
     * @param phoneNo 手机号。
     * @return 商户。
     */
    ServiceProvider findByPhoneNo(String phoneNo);

    /**
     * 根据电话号码修改密码。
     *
     * @param phoneNo     电话号码。
     * @param newPassword 新的密码。
     */
    ServiceProvider changePasswordByPhoneNo(String phoneNo, String newPassword);

    Integer queryOnlineNum(String phoneNo);
}
