package com.railwayservice.authority.dao;

import com.railwayservice.authority.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 角色数据库访问接口
 *
 * @author lid
 * @date 2017.2.8
 */
public interface RoleDao extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {
    /**
     * 根据id查找对应的角色
     *
     * @author lid
     * @date 2017.2.4
     */
    Role findByRoleId(String id);

    /**
     * 根据角色名称查找对应的角色
     *
     * @author lid
     * @date 2017.2.4
     */
    Role findByName(String name);

    /**
     * 根据名称统计所有的记录数
     *
     * @param name 角色名称
     * @return
     */
    Integer countByName(String name);

    /**
     * 根据管理员ID查询所拥有的角色
     *
     * @param adminId
     * @return
     */
    @Modifying
    @Query("select r from AdminRoleRela arr,Role r where arr.admin.adminId = ?1 and arr.role.roleId = r.roleId")
    List<Role> getRoleByAdminId(String adminId);
}
