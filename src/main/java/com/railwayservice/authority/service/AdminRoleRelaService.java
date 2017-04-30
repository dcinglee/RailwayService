package com.railwayservice.authority.service;

import com.railwayservice.authority.entity.Admin;
import com.railwayservice.authority.entity.AdminRoleRela;
import com.railwayservice.authority.entity.Role;

import java.util.List;

/**
 * 管理员与角色关联关系服务接口
 *
 * @author lid
 * @date 2017.2.8
 */

public interface AdminRoleRelaService {

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
     * 根据Admin信息查找所有的Role信息
     *
     * @param admin
     * @return List<Role>
     * @author lid
     * @date 2017.2.8
     */
    List<Role> findRoleByAdmin(Admin admin);

    /**
     * 添加管理员与角色关联关系
     *
     * @param adminRoleRela
     * @return authority
     * @author lid
     * @date 2017.2.8
     */
    AdminRoleRela addAdminRoleRela(AdminRoleRela adminRoleRela);

    /**
     * 删除管理员与角色关联关系
     *
     * @param adminRoleRela
     * @return void
     * @author lid
     * @date 2017.2.8
     */
    void deleteAdminRoleRela(AdminRoleRela adminRoleRela);

    /**
     * 删除管理员与角色关联关系
     *
     * @param adminId
     * @return void
     * @author lid
     * @date 2017.2.8
     */
    void deleteAdminRoleRelaByAdminId(String adminId);

    /**
     * 修改管理员与角色关联关系
     *
     * @param adminRoleRela
     * @return AdminRoleRela
     * @author lid
     * @date 2017.2.8
     */
    AdminRoleRela updateAdminRoleRela(AdminRoleRela adminRoleRela);

    /**
     * 查找所有的管理员与角色关联关系
     *
     * @return Page
     * @author lid
     * @date 2017.2.13
     */
    List<AdminRoleRela> getAllAdminRoleRela();

}
