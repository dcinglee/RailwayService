package com.railwayservice.grabticket.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.util.AESOperator;
import com.railwayservice.application.util.RSAPKCS1Public;
import com.railwayservice.grabticket.dao.KyfwInUserRelaDao;
import com.railwayservice.grabticket.dao.KyfwUserDao;
import com.railwayservice.grabticket.dao.PassengerDao;
import com.railwayservice.grabticket.entity.KyfwInUserRela;
import com.railwayservice.grabticket.entity.KyfwUser;
import com.railwayservice.grabticket.entity.Passenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 乘车人信息服务类。
 *
 * @author Ewing
 */
@Service
public class PassengerServiceImpl implements PassengerService {
    private final Logger logger = LoggerFactory.getLogger(PassengerServiceImpl.class);

    private PassengerDao passengerDao;

    private KyfwUserDao kyfwUserDao;

    private KyfwInUserRelaDao kyfwInUserRelaDao;

    private KyfwInUserRelaService kyfwInUserRelaService;

    private PassengerService passengerService;

    @Autowired
    public void setKyfwInUserRelaDao(KyfwInUserRelaDao kyfwInUserRelaDao) {
        this.kyfwInUserRelaDao = kyfwInUserRelaDao;
    }

    @Autowired
    public void setPassengerService(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @Autowired
    public void setKyfwUserDao(KyfwUserDao kyfwUserDao) {
        this.kyfwUserDao = kyfwUserDao;
    }

    @Autowired
    public void setKyfwInUserRelaService(KyfwInUserRelaService kyfwInUserRelaService) {
        this.kyfwInUserRelaService = kyfwInUserRelaService;
    }

    @Autowired
    public void setPassengerDao(PassengerDao passengerDao) {
        this.passengerDao = passengerDao;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public List<Passenger> getPassengers(Map<String, Object> stringObjectMap) {

        ArrayList<Map<String, Object>> passengerArrayList = (ArrayList<Map<String, Object>>) ((Map<String, Object>) stringObjectMap.get("data")).get("normal_passengers");

        //12306账号用户ID
        String kyfwUserId = stringObjectMap.get("kyfwUserId").toString();

        //当前乘客所属的用户id
        String userId = stringObjectMap.get("userId").toString();

        logger.info("获取用户乘车人信息：12306账号用户ID：" + kyfwUserId + "当前乘客所属的用户id:" + userId);

        //用于保存Passenger对象
        List<Passenger> passengerList = new ArrayList<Passenger>();

        for (int i = 0; i < passengerArrayList.size(); i++) {
            Passenger passenger = new Passenger();

            stringObjectMap = (Map<String, Object>) passengerArrayList.get(i);

            //12306账号id
            passenger.setKyfwUserId(kyfwUserId);

            //当前乘客所属的用户id
            passenger.setUserId(userId);

            //姓名
            passenger.setPassengerName(stringObjectMap.get("passenger_name").toString());

            //性别
            passenger.setGender(stringObjectMap.get("sex_name").toString());

            //国家/地区
            passenger.setCountryCode(stringObjectMap.get("country_code").toString());

            //证件类型
            passenger.setCardType(stringObjectMap.get("passenger_id_type_name").toString());

            //证件号码
            passenger.setIdentityCardNo(stringObjectMap.get("passenger_id_no").toString());

            //手机号码
            passenger.setMobilePhoneNo(stringObjectMap.get("mobile_no").toString());

            //固定电话
            passenger.setPhoneNo(stringObjectMap.get("phone_no").toString());

            //email
            passenger.setEmail(stringObjectMap.get("email").toString());

            //地址
            passenger.setAddress(stringObjectMap.get("address").toString());

            //邮编
            passenger.setPostalCode(stringObjectMap.get("postalcode").toString());

            //旅客类型
            passenger.setPassengerType(stringObjectMap.get("passenger_type_name").toString());

            //是否购票
            passenger.setIsOrderTicket(0);

            //创建时间
            passenger.setCreateDate(new Date());

            //保存到实体类
            passenger = passengerDao.save(passenger);

            //添加到passengerList集合
            passengerList.add(passenger);
        }
        return passengerList;
    }

    @Override
    @Transactional
    public Passenger addPassenger(Map<String, Object> stringObjectMap) {

        //12306账号用户ID
        String kyfwUserId = stringObjectMap.get("kyfwUserId").toString();

        //当前乘客所属的用户id
        String userId = stringObjectMap.get("userId").toString();

        stringObjectMap = (Map<String, Object>) ((Map<String, Object>) stringObjectMap.get("data")).get("normal_passengers");

        logger.info("添加的乘车人信息: 12306账号用户ID：" + kyfwUserId + "当前乘客所属的用户id" + userId);

        if (!StringUtils.hasText(userId)) {
            throw new AppException("用户不存在");
        }

        if (!StringUtils.hasText(kyfwUserId)) {
            throw new AppException("12306账号不存在");
        }
        if (kyfwInUserRelaDao.countByKyfwUserId(kyfwUserId) == 0) {
            throw new AppException("暂未绑定12306账号");
        }

        Passenger passenger = new Passenger();

        //12306账号id
        passenger.setKyfwUserId(kyfwUserId);

        //当前乘客所属的用户id
        passenger.setUserId(userId);

        //姓名
        passenger.setPassengerName(stringObjectMap.get("passenger_name").toString());

        //性别
        passenger.setGender(stringObjectMap.get("sex_name").toString());

        //国家/地区
        passenger.setCountryCode(stringObjectMap.get("country_code").toString());

        //证件类型
        passenger.setCardType(stringObjectMap.get("passenger_id_type_name").toString());

        //证件号码
        passenger.setIdentityCardNo(stringObjectMap.get("passenger_id_no").toString());

        //手机号码
        passenger.setMobilePhoneNo(stringObjectMap.get("mobile_no").toString());

        //固定电话
        passenger.setPhoneNo(stringObjectMap.get("phone_no").toString());

        //email
        passenger.setEmail(stringObjectMap.get("email").toString());

        //地址
        passenger.setAddress(stringObjectMap.get("address").toString());

        //邮编
        passenger.setPostalCode(stringObjectMap.get("postalcode").toString());

        //旅客类型
        passenger.setPassengerType(stringObjectMap.get("passenger_type_name").toString());

        //是否购票
        passenger.setIsOrderTicket(0);

        //创建时间
        passenger.setCreateDate(new Date());

        //保存到实体类
        return passengerDao.save(passenger);
    }

    @Override
    @Transactional
    public List<Passenger> freshPassengers(Map<String, Object> stringObjectMap) {

        //当前乘客所属的用户id
        String userId = stringObjectMap.get("userId").toString();

        logger.info("刷新乘车人信息：用户ID：" + userId);
        List<Passenger> passengers = passengerDao.findByUserId(userId);
        //迭代清楚已有乘车人信息记录
        Iterator<Passenger> it = passengers.iterator();
        while (it.hasNext()) {
            passengerDao.delete(it.next());
            it.remove();
        }

        //获取乘车人信息列表
        List<Passenger> passengerList = passengerService.getPassengers(stringObjectMap);
        return passengerList;
    }

    @Override
    @Transactional
    public boolean bindingKyfwByUser(String userId, String userName, String passWord) {
        if (!StringUtils.hasText(userId)) {
            throw new AppException("userId不能为空");
        }
        if (!StringUtils.hasText(userName)) {
            throw new AppException("12306账号不能为空");
        }
        if (!StringUtils.hasText(passWord)) {
            throw new AppException("12306密码不能为空");
        }
        logger.info("绑定12306账号：用户ID：" + userId + "12306账号：" + userName + "密码" + passWord);

        //若已绑定12306账号，先解除关联
        KyfwInUserRela kyfwInUserRela = kyfwInUserRelaService.findByUserId(userId);
        if (null != kyfwInUserRela) {
            kyfwInUserRelaDao.delete(kyfwInUserRela);
        }

        KyfwUser kyfwUser = null;

        //若账号不存在则创建并添加账号信息
        if (kyfwUserDao.countByUserName(userName) == 0) {
            kyfwUser = new KyfwUser();

            kyfwUser.setUserName(userName);

            //对12306密码进行加密存储
            String enString = RSAPKCS1Public.publicEncryptString(passWord);
            kyfwUser.setPassWord(enString);

            //添加默认字段值
            kyfwUser.setCreateDate(new Date());

            //保存实体类
            kyfwUser = kyfwUserDao.save(kyfwUser);
        } else {
            //若账号已存在则为已有账号添加关联
            kyfwUser = kyfwUserDao.findByUserName(userName);
        }

        //添加关联关系
        KyfwInUserRela newKyfwInUserRela = new KyfwInUserRela();
        newKyfwInUserRela.setUserId(userId);
        newKyfwInUserRela.setKyfwUserId(kyfwUser.getKyfwUserId());
        kyfwInUserRelaService.addKyfwInUserRela(newKyfwInUserRela);

        return true;
    }

    @Override
    @Transactional
    public Map<String, Object> getKyfwByUser(String userId) {

        //返回结果保存在Map中
        Map<String, Object> resultMap = new HashMap<String, Object>();

        if (!StringUtils.hasText(userId)) {
            throw new AppException("用户ID不能为空！");
        }
        logger.info("获取12306账号：用户ID：" + userId);

        KyfwInUserRela kyfwInUserRela = kyfwInUserRelaService.findByUserId(userId);
        if (null == kyfwInUserRela) {
            throw new AppException("该用户暂未绑定12306账号");
        }

        KyfwUser kyfwUser = kyfwUserDao.findOne(kyfwInUserRela.getKyfwUserId());

        //解密12306密码
//        kyfwUser.setPassWord(AESOperator.getInstance().decrypt(kyfwUser.getPassWord()));

        List<Passenger> passengers = passengerDao.findByUserId(userId);

        resultMap.put("kyfwUser", kyfwUser);
        resultMap.put("passengers", passengers);

        return resultMap;
    }
}
