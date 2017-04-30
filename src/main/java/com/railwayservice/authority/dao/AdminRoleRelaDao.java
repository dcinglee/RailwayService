package com.railwayservice.authority.dao;

import com.railwayservice.authority.entity.Admin;
import com.railwayservice.authority.entity.AdminRoleRela;
import com.railwayservice.authority.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 管理员与角色关联关系数据库访问接口
 *
 * @author lid
 * @date 2017.2.8
 */
public interface AdminRoleRelaDao extends JpaRepository<AdminRoleRela, String>, JpaSpecificationExecutor<AdminRoleRela> {
    /**
     * 根据admin信息获取role列表
     *
     * @param admin
     * @return List<AdminRoleRela>
     * @author lid
     * @date 2017.2.13
     */
    List<AdminRoleRela> findAdminRoleRelaByAdmin(Admin admin);

    /**
     * 根据relaId查找对应的AdminRoleRela记录
     *
     * @param relaId
     * @return AdminRoleRela
     * @author lid
     * @date 2017.2.13
     */
    AdminRoleRela getAdminRoleRelaByRelaId(String relaId);

    /**
     * 根据角色查找所有的AdminRoleRela记录
     *
     * @param role
     * @return List<AdminRoleRela>
     * @author lid
     * @date 2017.2.13
     */
    List<AdminRoleRela> getAdminRoleRelaByRole(Role role);

    /**
     * 根据管理员ID删除管理员与角色的关联
     *
     * @param adminId
     * @return
     */
    @Modifying
    @Query("delete from AdminRoleRela arr where arr.admin.adminId=:adminId")
    Integer deleteAdminRoleRelaOfAdmin(@Param("adminId") String adminId);

    /**
     * 根据管理员对象获取管理员角色关联
     *
     * @param admin 管理员对象
     * @return 管理员角色关联集
     */
    List<AdminRoleRela> getAdminRoleRelaByAdmin(Admin admin);
}
