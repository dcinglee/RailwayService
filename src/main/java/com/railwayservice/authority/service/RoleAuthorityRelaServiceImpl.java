package com.railwayservice.authority.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.authority.dao.RoleAuthorityRelaDao;
import com.railwayservice.authority.entity.Authority;
import com.railwayservice.authority.entity.Role;
import com.railwayservice.authority.entity.RoleAuthorityRela;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 角色与权限关联记录服务实现类
 *
 * @author lid
 * @date 2017.2.8
 */
@Service
public class RoleAuthorityRelaServiceImpl implements RoleAuthorityRelaService {
    private final Logger logger = LoggerFactory.getLogger(RoleAuthorityRelaServiceImpl.class);

    private RoleAuthorityRelaDao roleAuthorityRelaDao;

    @Autowired
    public void setRoleAuthorityRelaDao(RoleAuthorityRelaDao roleAuthorityRelaDao) {
        this.roleAuthorityRelaDao = roleAuthorityRelaDao;
    }

    @Override
    public RoleAuthorityRela findRoleAuthorityRelaById(String relaId) {
        if (!StringUtils.hasText(relaId)) {
            throw new AppException("未选择要操作的记录！");
        }
        logger.info("角色与权限关联记录服务层：查询角色与权限关联记录服务：角色权限ID：" + relaId);
        return roleAuthorityRelaDao.findRoleAuthorityRelaByRelaId(relaId);
    }

    @Override
    public List<RoleAuthorityRela> getAllRoleAuthorityRela() {
        logger.info("角色与权限关联记录服务层：查询角色与权限关联记录服务：");
        return roleAuthorityRelaDao.findAll();
    }

    @Override
    @Transactional
    public RoleAuthorityRela addRoleAuthorityRela(RoleAuthorityRela roleAuthorityRela) {
        if (null == roleAuthorityRela) {
            throw new AppException("添加记录不能为空！");
        }
        logger.info("角色与权限关联记录服务层：查询角色与权限关联记录服务：角色权限ID：" + roleAuthorityRela.getRelaId());
        roleAuthorityRela.setCreateDate(new Date());
        return roleAuthorityRelaDao.save(roleAuthorityRela);
    }

    @Override
    @Transactional
    public void deleteRoleAuthorityRela(RoleAuthorityRela roleAuthorityRela) {
        if (null == roleAuthorityRela) {
            throw new AppException("角色未授权！");
        }
        logger.info("角色与权限关联记录服务层：删除角色与权限关联记录服务：角色权限ID：" + roleAuthorityRela.getRelaId());

        RoleAuthorityRela oldRoleAuthorityRela = roleAuthorityRelaDao.findRoleAuthorityRelaByRelaId(roleAuthorityRela.getRelaId());
        if (null == oldRoleAuthorityRela) {
            throw new AppException("未找到待删除的记录！");
        }
        roleAuthorityRelaDao.delete(oldRoleAuthorityRela);
    }

    @Override
    @Transactional
    public RoleAuthorityRela updateRoleAuthorityRela(RoleAuthorityRela roleAuthorityRela) {
        if (null == roleAuthorityRela) {
            throw new AppException("请选择要修改的记录！");
        }
        logger.info("角色与权限关联记录服务层：更新角色与权限关联记录服务：角色权限ID：" + roleAuthorityRela.getRelaId());
        RoleAuthorityRela oldRoleAuthorityRela = roleAuthorityRelaDao.findRoleAuthorityRelaByRelaId(roleAuthorityRela.getRelaId());

        oldRoleAuthorityRela.setAuthority(roleAuthorityRela.getAuthority());
        oldRoleAuthorityRela.setRole(roleAuthorityRela.getRole());
        return roleAuthorityRelaDao.save(oldRoleAuthorityRela);
    }

    @Override
    public List<Authority> findAuthorityByRole(Role role) {
        if (null == role) {
            throw new AppException("请输入查询条件！");
        }
        logger.info("角色与权限关联记录服务层：查询权限列表：角色名称：" + role.getName());
        List<RoleAuthorityRela> listRoleAuthorityRela = roleAuthorityRelaDao.findRoleAuthorityRelaByRole(role);
        if (0 == listRoleAuthorityRela.size()) {
            return null;
        }

        List<Authority> listAuthority = new ArrayList<Authority>(listRoleAuthorityRela.size());
        for (int index = 0; index < listRoleAuthorityRela.size(); index++) {
            listAuthority.add(listRoleAuthorityRela.get(index).getAuthority());
        }
        return listAuthority;
    }

    @Override
    public List<RoleAuthorityRela> getAllRoleAuthorityRelaByRole(Role role) {
        if (null == role) {
            throw new AppException("请输入查询条件！");
        }
        logger.info("角色与权限关联记录服务层：查询角色与权限列表：角色名称：" + role.getName());
        List<RoleAuthorityRela> listRoleAuthorityRela = roleAuthorityRelaDao.findRoleAuthorityRelaByRole(role);
        return listRoleAuthorityRela;
    }

}
