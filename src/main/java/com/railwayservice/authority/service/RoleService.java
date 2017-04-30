package com.railwayservice.authority.service;

import com.railwayservice.authority.entity.Role;

import java.util.List;

/**
 * 角色服务接口
 *
 * @author lid
 * @date 2017.2.8
 */
public interface RoleService {
    /**
     * 根据id查找对应的角色
     *
     * @param id 角色ID
     * @author lid
     * @date 2017.2.4
     */
    Role findByRoleId(String id);

    /**
     * 根据角色名称查找对应的角色
     *
     * @param name 角色名称
     * @author lid
     * @date 2017.2.4
     */
    List<Role> findByName(String name);

    /**
     * 查找所有的角色
     *
     * @return list
     * @author lid
     * @date 2017.2.8
     */
    List<Role> getAllRole();

    /**
     * 添加角色
     *
     * @param name            角色名称
     * @param description     描述
     * @param listAuthorityId 权限数组
     * @return Role
     */
    Role addRole(String name, String description, String[] listAuthorityId);

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @author lid
     * @date 2017.2.8
     */
    void deleteRole(String roleId);

    /**
     * 修改角色
     *
     * @param roleId          角色ID
     * @param name            角色名称
     * @param description     描述
     * @param listAuthorityId 权限数组
     * @return Role
     */
    Role updateRole(String roleId, String name, String description, String[] listAuthorityId);

}
