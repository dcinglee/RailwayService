package com.railwayservice.authority.dao;

import com.railwayservice.authority.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author lidx
 * @date 2017年2月8日
 * @describe 管理员数据库访问接口。
 * 该接口使用了Spring Data JPA提供的方法。
 */
public interface AdminDao extends JpaRepository<Admin, String>, JpaSpecificationExecutor<Admin> {

    /**
     * 根据userName和password查找对应的管理员
     *
     * @param account  管理员账号
     * @param password 管理员密码
     * @return Admin  管理员对象
     * @author lidx
     * @date 2017.2.8
     */
    Admin findByAccountAndPassword(String account, String password);

    /**
     * 根据管理员用户名查找管理员
     *
     * @param account 管理员账号
     * @return Admin 管理员对象
     */
    Admin findByAccount(String account);

    /**
     * 统计管理员用户
     *
     * @param account 管理员账号
     * @return long
     */
    long countByAccount(String account);

    /**
     * 查询管理员用户（运营管理员）
     *
     * @param roleId
     * @return 成功查到的管理员集合
     */
    @Query("select arr.admin from AdminRoleRela arr where arr.admin.name like :adminName and arr.role.roleId = :roleId")
    List<Admin> findByRoleId(@Param("adminName") String adminName, @Param("roleId") String roleId);

    /**
     * 根据id查找对应的管理员
     *
     * @author lid
     * @date 2017.2.4
     */
    Admin findByAdminId(String adminId);

    /**
     * 根据id删除对应的管理员
     *
     * @author lid
     * @date 2017.2.4
     */
    Integer deleteByAdminId(String adminId);

}
