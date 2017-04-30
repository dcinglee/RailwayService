package com.railwayservice.authority.service;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.authority.entity.Authority;
import com.railwayservice.authority.vo.Menu;

import java.util.List;

/**
 * 权限服务接口
 *
 * @author lid
 * @date 2017.2.8
 */

public interface AuthorityService {

    /**
     * 根据id查找权限
     *
     * @param id
     * @return authority
     * @author lid
     * @date 2017.2.8
     */
    Authority findAuthorityById(String id);

    /**
     * 根据adminId查询用户菜单
     *
     * @param id 管理员ID
     * @return
     */
    List<Authority> findAuthorityByAdminId(String id);

    /**
     * 查找所有的权限
     *
     * @return list
     * @author lid
     * @date 2017.2.8
     */
    List<Authority> getAllAuthority();

    /**
     * 添加权限
     *
     * @param authority 权限对象
     * @return authority
     * @author lid
     * @date 2017.2.8
     */
    Authority addAuthority(Authority authority);

    /**
     * 删除权限
     *
     * @param authorityId 权限ID
     * @return void
     * @author lid
     * @date 2017.2.8
     */
    void deleteAuthority(String authorityId);

    /**
     * 修改权限
     *
     * @param authority 权限对象
     * @return Authority
     * @author lid
     * @date 2017.2.8
     */
    Authority updateAuthority(Authority authority);

    /**
     * 将权限 转为有父子关系的menu
     *
     * @param authoritys 权限对象集
     * @return
     */
    List<Menu> convertToMenu(List<Authority> authoritys);

    /**
     * 获取指定类型的权限
     *
     * @param type
     * @return
     */
    List<Authority> getAuthorityByType(Integer type);

    /**
     * 根据名称和URL查询权限并分页。
     *
     * @param param       分页信息
     * @param name        权限名称
     * @param description 权限类型
     * @return
     */
    PageData queryAuthorityPage(PageParam param, String name, String description);
}
