package com.railwayservice.user.service;

import com.railwayservice.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 用户服务类
 *
 * @author lid
 * @date 2017.2.4
 */
public interface UserService {
    /**
     * 查找所有的用户
     *
     * @return list
     * @author lid
     * @date 2017.2.4
     */
    List<User> getAllUser();

    /**
     * 根据id查找用户信息
     *
     * @param id
     * @return User
     * @author lid
     * @date 2017.2.9
     */
    User getUserByUserId(String id);

    /**
     * 根据openid查找用户信息
     *
     * @param String openid
     * @return User
     * @author lid
     * @date 2017.2.9
     */
    User getUserByOpenid(String openid);

    /**
     * 添加用户
     *
     * @param user
     * @return User
     * @author lid
     * @date 2017.2.4
     */
    User addUser(User user);

    /**
     * 修改用户
     *
     * @param user
     * @return user
     * @author lid
     * @date 2017.2.4
     */
    User updateUser(User user);

    /**
     * 查找所有的用户
     *
     * @author lidx
     * @date 2017.2.4
     */
    Page<User> queryAllUser(String nickName, String phoneNo, Pageable pageable);

}
