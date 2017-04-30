package com.railwayservice.authority.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.authority.dao.AuthorityDao;
import com.railwayservice.authority.entity.Authority;
import com.railwayservice.authority.vo.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 权限管理服务实现类
 *
 * @author lid
 * @date 2017.2.8
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final Logger logger = LoggerFactory.getLogger(AuthorityServiceImpl.class);

    private AuthorityDao authorityDao;

    @Autowired
    public void setAuthorityDao(AuthorityDao authorityDao) {
        this.authorityDao = authorityDao;
    }

    @Override
    public Authority findAuthorityById(String id) {
        if (!StringUtils.hasText(id)) {
            throw new AppException("未选择记录！");
        }
        logger.info("权限管理服务层：查询权限管理服务：权限Id：" + id);
        return authorityDao.findByAuthorityId(id);
    }

    @Override
    public List<Authority> findAuthorityByAdminId(String id) {
        logger.info("权限管理服务层：查询权限列表：管理员Id：" + id);
        if (!StringUtils.hasText(id)) {
            throw new AppException("未选择要显示记录！");
        }
        return authorityDao.findByAdminId(id);
    }

    @Override
    public List<Authority> getAllAuthority() {
        logger.info("权限管理服务层：查询权限列表：");
        return authorityDao.findAll();
    }

    @Override
    @Transactional
    public Authority addAuthority(Authority authority) {
        if (null == authority) {
            throw new AppException("权限不能为空！");
        }
        if (!StringUtils.hasText(authority.getName())) {
            throw new AppException("权限名不能为空！");
        }
        if (!StringUtils.hasText(authority.getDescription())) {
            throw new AppException("权限描述不能为空！");
        }
        if (!StringUtils.hasText(authority.getMenuUrl())) {
            throw new AppException("权限URL不能为空！");
        }
        if (!StringUtils.hasText(authority.getCode())) {
            throw new AppException("权限码不能为空！");
        }
        logger.info("权限管理服务层：添加权限管理服务：权限名称：" + authority.getName());
        authority.setCreateDate(new Date());
        return authorityDao.save(authority);
    }

    @Override
    @Transactional
    public void deleteAuthority(String authorityId) {
        if (!StringUtils.hasText(authorityId)) {
            throw new AppException("未选择要删除记录！");
        }
        logger.info("权限管理服务层：删除权限管理服务：权限ID：" + authorityId);
        authorityDao.delete(authorityId);
    }

    @Override
    @Transactional
    public Authority updateAuthority(Authority authority) {
        if (null == authority) {
            throw new AppException("未选择要更新的记录！");
        }
        logger.info("权限管理服务层：更新权限管理服务：权限名称："+authority.getName());

        Authority oldAuthority = authorityDao.findByAuthorityId(authority.getAuthorityId());
        if (null == oldAuthority) {
            throw new AppException("未找到修改的权限记录！");
        }

        if (authority.getParentId() != null && authority.getParentId().equals(authority.getAuthorityId())) {
            throw new AppException("自己的父权限不能够是自己！");
        }

        //判断是否循环引用
        List<Authority> authList = authorityDao.findAll();
        if (authList != null) {
            Map<String, String> m = new HashMap<String, String>();
            for (Authority auths : authList) {
                if (auths.getAuthorityId().equals(authority.getAuthorityId())) {
                    auths.setParentId(authority.getParentId());
                }
                m.put(auths.getAuthorityId(), auths.getParentId());
            }
            String parentId = authority.getParentId();
            while (m.containsKey(parentId)) {
                parentId = m.get(parentId);
                if (parentId != null && parentId.equals(authority.getAuthorityId())) {
                    throw new AppException("循环的父权限设置！");
                }
            }
        }

        oldAuthority.setDescription(authority.getDescription());
        oldAuthority.setName(authority.getName());
        oldAuthority.setMenuUrl(authority.getMenuUrl());
        oldAuthority.setOrderNo(authority.getOrderNo());
        oldAuthority.setParentId(authority.getParentId());
        oldAuthority.setStatus(authority.getStatus());
        oldAuthority.setType(authority.getType());
        oldAuthority.setCode(authority.getCode());
        oldAuthority.setIcon(authority.getIcon());
        return authorityDao.save(oldAuthority);
    }

    @Override
    public List<Menu> convertToMenu(List<Authority> authoritys) {
        List<Menu> topMenu = new ArrayList<Menu>();
        Map<String, Menu> menuMap = new HashMap<String, Menu>();
        if (authoritys != null && authoritys.size() > 0) {
            //构造一树目录树,放在map备用，以及找出顶级目录
            for (int i = 0; i < authoritys.size(); i++) {
                Authority auth = authoritys.get(i);
                //0:菜单 ; 1:按钮 ；2:其它
                if (auth.getType() != null && 0 != auth.getType().intValue()) {
                    continue;
                }
                Menu menu = new Menu();
                menu.setAuthorityId(auth.getAuthorityId());
                menu.setCode(auth.getCode());
                menu.setDescription(auth.getDescription());
                menu.setMenuUrl(auth.getMenuUrl());
                menu.setName(auth.getName());
                menu.setOrderNo(auth.getOrderNo());
                menu.setType(auth.getType());
                menu.setIcon(auth.getIcon());
                menuMap.put(menu.getAuthorityId(), menu);
                if (!StringUtils.hasText(auth.getParentId()) || "0".equals(auth.getParentId())) {
                    topMenu.add(menu);
                }
            }
            //构造一树目录树 构造父子关系
            for (int i = 0; i < authoritys.size(); i++) {
                Authority auth = authoritys.get(i);
                if (auth.getType() != null && 0 != auth.getType().intValue()) {
                    continue;
                }
                if (StringUtils.hasText(auth.getParentId()) && !"0".equals(auth.getParentId())) {
                    Menu child = menuMap.get(auth.getAuthorityId());
                    Menu parent = menuMap.get(auth.getParentId());

                    if (parent != null) {
                        if (parent.getSubMenus() == null) {
                            List<Menu> sub = new ArrayList<Menu>();
                            sub.add(child);
                            parent.setSubMenus(sub);
                        } else {
                            List<Menu> sub = parent.getSubMenus();
                            sub.add(child);
                            parent.setSubMenus(sub);
                        }
                    }

                }

            }
        }
        return topMenu;
    }

    /**
     * 获取指定类型的权限
     *
     * @param type
     * @return
     */
    public List<Authority> getAuthorityByType(Integer type) {
        return authorityDao.findAuthorityByType(type);
    }

    /**
     * 根据名称和URL查询权限并分页。
     */
    @Override
    public PageData queryAuthorityPage(PageParam param, String name, String description) {
        return authorityDao.queryAuthorityPage(param, name, description);
    }
}
