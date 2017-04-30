package com.railwayservice.user.dao;

import com.railwayservice.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 用户数据库访问接口
 *
 * @author lid
 * @date 2017.2.4
 */
public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    /**
     * 根据id查找用户信息
     *
     * @param id
     * @return User
     * @author lid
     * @date 2017.2.9
     */
    User findUserByUserId(String id);

    /**
     * 根据openid查找对应的用户
     *
     * @param openid
     * @return User
     * @author lid
     * @date 2017.2.4
     */
    User findByOpenid(String openid);

//    /**
//     * Query测试
//     */
//    
//    @Query("select a from User a where a.city = :city")
//    User findCity(@Param("city") String city);
}
