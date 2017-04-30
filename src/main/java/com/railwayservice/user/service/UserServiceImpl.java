package com.railwayservice.user.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.user.dao.UserDao;
import com.railwayservice.user.entity.User;
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
import java.util.List;

/**
 * ����Ա�����ࡣ
 *
 * @author lid
 * @date 2017.2.4
 */
@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public User addUser(User user) {
    	if (null == user) {
            throw new AppException("没有用户信息！");
        }
        logger.info("用户服务层->添加用户->用户名：" + user.getName());
        if (null != userDao.findByOpenid(user.getOpenid())) {
            throw new AppException("用户已存在！");
        }
        user.setCreateDate(new Date());
        return userDao.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
    	if (null == user) {
            throw new AppException("没有用户信息");
        }

        logger.info("用户服务层->修改用户->用户名：" + user.getName());
        
        User oldUser = userDao.findOne(user.getUserId());
        if (null == oldUser) {
            throw new AppException("未找到需要修改信息的用户");
        }

        oldUser.setCity(user.getCity());
        oldUser.setCountry(user.getCity());
        oldUser.setGroupId(user.getGroupId());
        oldUser.setHeadimgUrl(user.getHeadimgUrl());
        oldUser.setLanguage(user.getLanguage());
        oldUser.setName(user.getName());
        oldUser.setNickName(user.getNickName());
//      用户使用公众号登录，不需要password  
//      oldUser.setPassWord(user.getPassWord());
        oldUser.setPhoneNo(user.getPhoneNo());
        oldUser.setProvince(user.getProvince());
        oldUser.setGender(user.getGender());
        //Unionid应该不允许自行修改       
//      oldUser.setUnionid(user.getUnionid());
//        oldUser.setUserName(user.getUserName());

        return userDao.save(oldUser);
    }

    @Override
    public Page<User> queryAllUser(String nickName, String phoneNo, Pageable pageable) {
        logger.info("用户服务层->查询用户->用户昵称：" + nickName);
        // JPA标准查询接口，使用Lambda表达式。root以商户类为根对象。
        Specification<User> specification = (root, query, builder) -> {
            // 创建组合条件，默认1=1。
            Predicate predicate = builder.conjunction();
            // 如果名称不为空，添加到查询条件。
            if (StringUtils.hasText(nickName)) {
                predicate = builder.and(predicate, builder.like(root.get("nickName"), "%" + nickName + "%"));
            }
            // 如果电话号码不为空，添加到查询条件。
            if (StringUtils.hasText(phoneNo)) {
                predicate = builder.and(predicate, builder.like(root.get("phoneNo"), "%" + phoneNo + "%"));
            }
            query.orderBy(builder.desc(root.get("createDate")));
            return predicate;
        };
        // 调用JPA标准查询接口查询数据。
        return userDao.findAll(specification, pageable);
    }

    @Override
    public List<User> getAllUser() {
        logger.info("用户服务层->查询所有用户");
        return userDao.findAll();
    }

    @Override
    public User getUserByUserId(String id) {
        logger.info("用户服务层->查询用户->用户ID：" + id);
        if (null == id) {
            throw new AppException("没有用户信息");
        }
        return userDao.findUserByUserId(id);
    }

    @Override
    public User getUserByOpenid(String openid) {
        if (null == openid) {
            throw new AppException("没有用户信息");
        }
        logger.info("用户服务层->查询用户->用户openid：" + openid);

        User user = userDao.findByOpenid(openid);
        return user;
    }

}
