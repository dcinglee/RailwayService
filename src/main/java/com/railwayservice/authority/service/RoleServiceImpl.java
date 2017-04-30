package com.railwayservice.authority.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.authority.dao.AdminRoleRelaDao;
import com.railwayservice.authority.dao.AuthorityDao;
import com.railwayservice.authority.dao.RoleAuthorityRelaDao;
import com.railwayservice.authority.dao.RoleDao;
import com.railwayservice.authority.entity.AdminRoleRela;
import com.railwayservice.authority.entity.Authority;
import com.railwayservice.authority.entity.Role;
import com.railwayservice.authority.entity.RoleAuthorityRela;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * 角色管理服务实现类
 *
 * @author lid
 * @date 2017.2.8
 */
@Service
public class RoleServiceImpl implements RoleService {
    private final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    private RoleDao roleDao;

    private AuthorityDao authorityDao;

    private RoleAuthorityRelaDao roleAuthorityRelaDao;

    private AdminRoleRelaDao adminRoleRelaDao;

    @Autowired
    public void setAdminRoleRelaDao(AdminRoleRelaDao adminRoleRelaDao) {
        this.adminRoleRelaDao = adminRoleRelaDao;
    }

    @Autowired
    public void setRoleAuthorityRelaDao(RoleAuthorityRelaDao roleAuthorityRelaDao) {
        this.roleAuthorityRelaDao = roleAuthorityRelaDao;
    }

    @Autowired
    public void setAuthorityDao(AuthorityDao authorityDao) {
        this.authorityDao = authorityDao;
    }

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role findByRoleId(String id) {
        if (!StringUtils.hasText(id)) {
            throw new AppException("缺少id参数！");
        }
        logger.info("角色管理服务层：角色查询：角色ID：" + id);
        return roleDao.findByRoleId(id);
    }

    @Override
    public List<Role> findByName(String name) {
        if (null == name) {
            throw new AppException("缺少roleName参数！");
        }
        logger.info("角色管理服务层：角色查询：角色名称：" + name);

        // JPA标准查询接口，使用Lambda表达式。root以商户类为根对象。
        Specification<Role> specification = (root, query, builder) -> {
            // 创建组合条件，默认1=1。
            Predicate predicate = builder.conjunction();
            // 如果名称不为空，添加到查询条件。
            if (StringUtils.hasText(name)) {
                predicate = builder.and(predicate, builder.like(root.get("name"), "%" + name + "%"));
            }
            return predicate;
        };
        // 调用JPA标准查询接口查询数据。
        return roleDao.findAll(specification);
    }

    @Override
    public List<Role> getAllRole() {
        logger.info("角色管理服务层：所有角色查询：");
        return roleDao.findAll();
    }

    @Override
    @Transactional
    public Role addRole(String name, String description, String[] listAuthorityId) {
        if (!StringUtils.hasText(name)) {
            throw new AppException("角色名称不能为空！");
        }
        logger.info("角色管理服务层：角色添加：角色名称：" + name + "角色描述：" + description);

        //防止重复添加角色
        if (roleDao.countByName(name) > 0)
            throw new AppException("此角色已存在");

        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        role.setCreateDate(new Date());
        Role newRole = roleDao.save(role);

        //如果listAuthorityId不为空，则添加角色与权限的关系
        if ((null != listAuthorityId) && (0 < listAuthorityId.length)) {
            //查找所有权限，并保存权限与角色的关联关系
            for (int index = 0; index < listAuthorityId.length; index++) {
                Authority authority = authorityDao.findByAuthorityId(listAuthorityId[index]);
                if (null == authority) {
                    continue;
                }
                RoleAuthorityRela roleAuthorityRela = new RoleAuthorityRela();
                roleAuthorityRela.setAuthority(authority);
                roleAuthorityRela.setRole(role);
                roleAuthorityRelaDao.save(roleAuthorityRela);
            }

        }
        return newRole;
    }

    @Override
    @Transactional
    public void deleteRole(String roleId) {
        if (!StringUtils.hasText(roleId)) {
            throw new AppException("roleId参数为空");
        }
        logger.info("角色管理服务层：角色删除：角色ID：" + roleId);

        Role role = roleDao.findByRoleId(roleId);
        if (null == role) {
            throw new AppException("未找到对应的角色记录");
        }

        //删除角色与权限的关联关系
        List<RoleAuthorityRela> listRoleAuthorityRela = roleAuthorityRelaDao.findRoleAuthorityRelaByRole(role);
        for (int index = 0; index < listRoleAuthorityRela.size(); index++) {
            roleAuthorityRelaDao.delete(listRoleAuthorityRela.get(index));
        }

        //删除角色与管理员的关联关系
        List<AdminRoleRela> listAdminRoleRela = adminRoleRelaDao.getAdminRoleRelaByRole(role);
        for (int index = 0; index < listAdminRoleRela.size(); index++) {
            adminRoleRelaDao.delete(listAdminRoleRela.get(index));
        }

        //最后删除角色
        roleDao.delete(role);
    }

    @Override
    @Transactional
    public Role updateRole(String roleId, String name, String description, String[] listAuthorityId) {
        if (!StringUtils.hasText(roleId)) {
            throw new AppException("roleId参数为空");
        }
        logger.info("角色管理服务层：角色更新：角色ID：" + roleId + "角色名称：" + name + "角色描述：" + description);

        Role role = roleDao.findByRoleId(roleId);
        if (null == role) {
            throw new AppException("未找到对应的角色记录");
        }

        if (StringUtils.hasText(name)) {
            role.setName(name);
        }
        if (StringUtils.hasText(description)) {
            role.setDescription(description);
        }
        Role newRole = roleDao.save(role);
        //修改角色与权限的关联关系, 当listAuthorityId不为空时，先删除关联关系再添加
        List<RoleAuthorityRela> listRoleAuthorityRela = roleAuthorityRelaDao.findRoleAuthorityRelaByRole(role);
        for (int index = 0; index < listRoleAuthorityRela.size(); index++) {
            roleAuthorityRelaDao.delete(listRoleAuthorityRela.get(index));
        }

        if ((null != listAuthorityId) && (0 != listAuthorityId.length)) {
            for (int index = 0; index < listAuthorityId.length; index++) {
                Authority authority = authorityDao.findByAuthorityId(listAuthorityId[index]);
                if (null == authority) {
                    continue;
                }
                RoleAuthorityRela roleAuthorityRela = new RoleAuthorityRela();
                roleAuthorityRela.setAuthority(authority);
                roleAuthorityRela.setRole(role);
                roleAuthorityRelaDao.save(roleAuthorityRela);
            }
        }
        return newRole;
    }
}
