package com.railwayservice.authority.service;

import com.railwayservice.authority.entity.Authority;
import com.railwayservice.authority.entity.Role;
import com.railwayservice.authority.entity.RoleAuthorityRela;

import java.util.List;

/**
 * 角色与权限关联记录服务接口
 *
 * @author lid
 * @date 2017.2.8
 */
public interface RoleAuthorityRelaService {
    /**
     * 根据id查找对应的角色与权限关联信息
     *
     * @param relaId 角色权限关联ID
     * @return RoleAuthorityRela
     * @author lid
     * @date 2017.2.8
     */
    RoleAuthorityRela findRoleAuthorityRelaById(String relaId);

    /**
     * 查找所有的角色与权限关联记录
     *
     * @return RoleAuthorityRela
     * @author lid
     * @date 2017.2.8
     */
    List<RoleAuthorityRela> getAllRoleAuthorityRela();

    /**
     * 根据角色查找所有的权限关联关系
     *
     * @param role 角色对象
     * @return List
     * @author lid
     * @date 2017.2.16
     */
    List<RoleAuthorityRela> getAllRoleAuthorityRelaByRole(Role role);

    /**
     * 根据角色查找对应的权限列表
     *
     * @param role 角色对象
     * @return RoleAuthorityRela
     * @author lid
     * @date 2017.2.8
     */
    List<Authority> findAuthorityByRole(Role role);

    /**
     * 添加角色与权限关联信息
     *
     * @param roleAuthorityRela 角色权限关联对象
     * @return roleAuthorityRela
     * @author lid
     * @date 2017.2.8
     */
    RoleAuthorityRela addRoleAuthorityRela(RoleAuthorityRela roleAuthorityRela);

    /**
     * 删除权限
     *
     * @param roleAuthorityRela 角色权限关联对象
     * @return void
     * @author lid
     * @date 2017.2.8
     */
    void deleteRoleAuthorityRela(RoleAuthorityRela roleAuthorityRela);

    /**
     * 修改权限
     *
     * @param roleAuthorityRela 角色权限关联对象
     * @return RoleAuthorityRela
     * @author lid
     * @date 2017.2.8
     */
    RoleAuthorityRela updateRoleAuthorityRela(RoleAuthorityRela roleAuthorityRela);
}
