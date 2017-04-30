package com.railwayservice.authority.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.authority.dao.AdminRoleRelaDao;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.authority.entity.AdminRoleRela;
import com.railwayservice.authority.entity.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 管理员和角色关联记录服务实现类
 *
 * @author lid
 * @date 2017.2.13
 */
@Service
public class AdminRoleRelaServiceImpl implements AdminRoleRelaService {

    private final Logger logger = LoggerFactory.getLogger(AdminRoleRelaServiceImpl.class);

    private AdminRoleRelaDao adminRoleRelaDao;

    @Autowired
    public void setAdminRoleRelaDao(AdminRoleRelaDao adminRoleRelaDao) {
        this.adminRoleRelaDao = adminRoleRelaDao;
    }

    @Override
    public List<Role> findRoleByAdmin(Admin admin) {
        if (null == admin) {
            throw new AppException("管理员对象不能为空！");
        }
        logger.info("管理员和角色关联记录服务层：查询角色列表：管理员ID：" + admin.getAdminId());

        List<AdminRoleRela> listAdminRoleRela = adminRoleRelaDao.findAdminRoleRelaByAdmin(admin);
        if (0 == listAdminRoleRela.size()) {
            return null;
        }

        List<Role> listRole = new ArrayList<Role>(listAdminRoleRela.size());
        for (int index = 0; index < listAdminRoleRela.size(); index++) {
            listRole.add(listAdminRoleRela.get(index).getRole());
        }
        return listRole;
    }

    @Override
    @Transactional
    public AdminRoleRela addAdminRoleRela(AdminRoleRela adminRoleRela) {
        if (null == adminRoleRela) {
            throw new AppException("未授权！");
        }
        logger.info("管理员和角色关联记录服务层：添加管理员和角色关联记录服务：管理员角色关联ID：" + adminRoleRela.getRelaId());
        adminRoleRela.setCreateDate(new Date());
        return adminRoleRelaDao.save(adminRoleRela);
    }

    @Override
    @Transactional
    public void deleteAdminRoleRela(AdminRoleRela adminRoleRela) {
        if (null == adminRoleRela) {
            throw new AppException("未授权！");
        }
        logger.info("管理员和角色关联记录服务层：删除管理员和角色关联记录服务：管理员角色关联ID" + adminRoleRela.getRelaId());
        adminRoleRelaDao.delete(adminRoleRela);
    }

    /**
     * 删除管理员与角色关联关系
     *
     * @param adminId
     * @return void
     * @author lid
     * @date 2017.2.8
     */
    @Override
    @Transactional
    public void deleteAdminRoleRelaByAdminId(String adminId) {
        adminRoleRelaDao.deleteAdminRoleRelaOfAdmin(adminId);
    }

    @Override
    @Transactional
    public AdminRoleRela updateAdminRoleRela(AdminRoleRela adminRoleRela) {
        if (null == adminRoleRela) {
            throw new AppException("未授权！");
        }
        logger.info("管理员和角色关联记录服务层：更新管理员和角色关联记录服务：管理员角色关联ID" + adminRoleRela.getRelaId());
        AdminRoleRela oldAdminRoleRela = adminRoleRelaDao.findOne(adminRoleRela.getRelaId());
        if (null == oldAdminRoleRela) {
            throw new AppException("暂无已授权的管理员！");
        }
        oldAdminRoleRela.setAdmin(adminRoleRela.getAdmin());
        oldAdminRoleRela.setRole(adminRoleRela.getRole());
        return adminRoleRelaDao.save(oldAdminRoleRela);
    }

    @Override
    public List<AdminRoleRela> getAllAdminRoleRela() {
        return adminRoleRelaDao.findAll();
    }

    @Override
    public List<AdminRoleRela> getAdminRoleRelaByRole(Role role) {
    	if (null == role) {
            throw new AppException("角色不能为空！");
        }
        logger.info("管理员和角色关联记录服务层：查询管理员和角色关联记录列表：角色ID" + role.getRoleId());
        return adminRoleRelaDao.getAdminRoleRelaByRole(role);
    }

}
