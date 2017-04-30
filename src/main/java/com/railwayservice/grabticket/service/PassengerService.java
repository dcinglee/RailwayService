package com.railwayservice.grabticket.service;

import com.railwayservice.grabticket.entity.KyfwUser;
import com.railwayservice.grabticket.entity.Passenger;

import java.util.List;
import java.util.Map;

/**
 * 乘车人信息服务类。
 *
 * @author Ewing
 */
public interface PassengerService {

    /**
     * 获取用户乘车人信息，保存到数据库
     *
     * @param stringObjectMap
     * @return
     */
    List<Passenger> getPassengers(Map<String, Object> stringObjectMap);

    /**
     * 添加用户乘车人信息
     *
     * @param stringObjectMap
     * @return
     */
    Passenger addPassenger(Map<String, Object> stringObjectMap);

    /**
     * 刷新乘车人信息
     *
     * @param stringObjectMap
     * @return
     */
    List<Passenger> freshPassengers(Map<String, Object> stringObjectMap);

    /**
     * 绑定12306账号
     *
     * @param userId   用户ID
     * @param userName 12306账号
     * @param passWord 密码
     * @return
     */
    boolean bindingKyfwByUser(String userId, String userName, String passWord);

    /**
     * 获取12306账号
     *
     * @param userId
     * @return
     */
    Map<String, Object> getKyfwByUser(String userId);
}
