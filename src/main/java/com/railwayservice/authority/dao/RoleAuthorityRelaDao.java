package com.railwayservice.authority.dao;

import com.railwayservice.authority.entity.Role;
import com.railwayservice.authority.entity.RoleAuthorityRela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 权限数据库访问接口
 *
 * @author lid
 * @date 2017.2.8
 */
public interface RoleAuthorityRelaDao extends JpaRepository<RoleAuthorityRela, String>, JpaSpecificationExecutor<RoleAuthorityRela> {
    /**
     * 根据id查找对应的角色与权限关联信息
     *
     * @return RoleAuthorityRela
     * @author lid
     * @date 2017.2.8
     */
    RoleAuthorityRela findRoleAuthorityRelaByRelaId(String relaId);

    /**
     * 根据角色id查找对应的角色与权限关联信息
     *
     * @param role
     * @return RoleAuthorityRela
     * @author lid
     * @date 2017.2.8
     */
    List<RoleAuthorityRela> findRoleAuthorityRelaByRole(Role role);

    /**
     * 根据角色id查找对应的角色与权限关联信息
     *
     * @param authority
     * @return RoleAuthorityRela
     * @author lid
     * @date 2017.2.8
     */
    RoleAuthorityRela findRoleAuthorityRelaByAuthority(String authority);
}
