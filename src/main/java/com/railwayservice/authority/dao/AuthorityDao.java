package com.railwayservice.authority.dao;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.authority.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 权限数据库访问接口
 *
 * @author lid
 * @date 2017.2.8
 */
public interface AuthorityDao extends JpaRepository<Authority, String>, JpaSpecificationExecutor<Authority> {
    /**
     * 根据id查找对应的权限
     *
     * @author lid
     * @date 2017.2.4
     */
    Authority findByAuthorityId(String authorityId);

    /**
     * 查找指定管理员的所有权限
     *
     * @param adminId
     * @return
     */
    @Query("select distinct a FROM Authority a,RoleAuthorityRela ru,AdminRoleRela arr where arr.admin.adminId = ?1" +
            " and arr.role = ru.role and ru.authority = a.authorityId and a.status=0 order by a.orderNo asc")
    List<Authority> findByAdminId(String adminId);

    /**
     * 根据权限名称查找权限
     *
     * @param name
     * @return
     */
    List<Authority> findByName(String name);

    /**
     * 根据权限类型获取权限
     *
     * @param type
     * @return
     */
    List<Authority> findAuthorityByType(Integer type);

    /**
     * 根据名称和URL查询权限并分页。
     */
    PageData queryAuthorityPage(PageParam param, String name, String description);
}
