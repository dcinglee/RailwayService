package com.railwayservice.authority.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.authority.vo.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author lidx
 * @date 2017年2月8日
 * @describe 管理员服务类接口
 */
public interface AdminService {
    /**
     * 管理员登录
     *
     * @param account  管理员账号
     * @param password 管理员密码
     * @return ResultMessage
     * @author lidx
     * @date 2017.2.8
     */
    ResultMessage loginByAccount(String account, String password);

    /**
     * 管理员密码重置
     *
     * @param adminId     管理员用户id
     * @param oldPassword 管理员原密码
     * @param newPassword 管理员新密码
     * @return Admin对象
     * @author lixs
     * @date 2017.2.8
     */
    Admin resetPassWord(String adminId, String oldPassword, String newPassword);

    /**
     * 通过管理员用户名查找管理员对象
     *
     * @param account 管理员账号
     * @return 查成功的管理员对象
     */
    Admin findByAccount(String account);

    /**
     * @param admin 管理员对象
     * @return String
     * @throws JsonProcessingException
     */
    List<Menu> getAdminMenu(Admin admin);

    /**
     * 通过管理员ID查找管理员对象
     *
     * @param adminId 管理员ID
     * @return Admin管理员对象
     */
    Admin findByAdminId(String adminId);

    /**
     * 添加管理员对象（车站管理、运营管理）
     *
     * @param currentAdmin 当前操作人员
     * @param newAdmin     被新增的管理员对象
     * @return Admin
     */
    Admin addAdmin(Admin currentAdmin, Admin newAdmin, String[] listroleId);

    /**
     * 查找管理员列表
     *
     * @param currentAdmin 所属管理员
     * @param name         要查的管理员名称
     * @param order
     * @param pageable
     * @return
     */
    Page<Admin> findAdminByNameOrRoleId(Admin currentAdmin, String name, String order, Pageable pageable);

    /**
     * @param currentAdmin 当前操作用户
     * @param admin        被删除用户
     */
    void deleteAdmin(Admin currentAdmin, Admin admin);

    /**
     * 查询管理员信息
     *
     * @param admin  管理员名称
     * @param roleId 角色ID
     * @return 成功查到的管理员集合
     */
    List<Admin> findAdminByRoleId(Admin currentAdmin, Admin admin, String roleId);

    /**
     * 修改管理员信息
     *
     * @param currentAdmin 当前登陆操作人员
     * @param admin        待修改管理对象
     * @return Admin
     */
    Admin updateAdmin(Admin currentAdmin, Admin admin, String[] listroleId);
}
