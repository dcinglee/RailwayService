package com.railwayservice.authority.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.util.EncodeUtil;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.authority.dao.AdminDao;
import com.railwayservice.authority.dao.AdminRoleRelaDao;
import com.railwayservice.authority.dao.AuthorityDao;
import com.railwayservice.authority.dao.RoleDao;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.authority.entity.AdminRoleRela;
import com.railwayservice.authority.entity.Authority;
import com.railwayservice.authority.entity.Role;
import com.railwayservice.authority.vo.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * @author lidx
 * @date 2017年2月8日
 * @describe 管理员服务类接口实现类
 */
@Service
public class AdminServiceImpl implements AdminService {
    private final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    private AdminDao adminDao;

    private AuthorityDao authorityDao;

    private AdminRoleRelaDao adminRoleRelaDao;

    private Admin admin;

    private AdminRoleRelaService adminRoleRelaService;

    private RoleService roleService;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    public void setAdminDao(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Autowired
    public void setAuthorityDao(AuthorityDao authorityDao) {
        this.authorityDao = authorityDao;
    }

    @Autowired
    public void setAdminRoleRelaService(AdminRoleRelaService adminRoleRelaService) {
        this.adminRoleRelaService = adminRoleRelaService;
    }

    @Autowired
    public void setroleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setAdminRoleRelaDao(AdminRoleRelaDao adminRoleRelaDao) {
        this.adminRoleRelaDao = adminRoleRelaDao;
    }

    /**
     * 管理员登录
     */
    @Override
    public ResultMessage loginByAccount(String account, String password) {
        if (!StringUtils.hasText(account) || !StringUtils.hasText(password))
            throw new AppException("管理员用户名或密码不能为空！");
        logger.info("管理员服务层：管理员登陆：账号：" + account);

        //利用MD5进行加密
        password = EncodeUtil.encodePassword(password, account);
        Admin admin = adminDao.findByAccountAndPassword(account, password);

        if (admin != null) {
            admin.setAuthoritys(authorityDao.findByAdminId(admin.getAdminId()));
            return ResultMessage.newSuccess("登陆成功！").setData(admin);
        } else {
            return ResultMessage.newFailure("管理员用户名或密码错误");
        }
    }

    /**
     * 管理员密码重置
     */
    @Override
    @Transactional
    public Admin resetPassWord(String adminId, String oldPassword, String newPassword) {
        // 参数防空判断。
        if (!StringUtils.hasText(adminId)) {
            throw new AppException("管理员ID不能为空");
        }
        if (!StringUtils.hasText(oldPassword)) {
            throw new AppException("密码不能为空");
        }
        if (!StringUtils.hasText(newPassword)) {
            throw new AppException("新密码不能为空");
        }
        if (oldPassword.equals(newPassword)) {
            throw new AppException("新密码不能和旧密码相同");
        }
        logger.info("管理员服务层：管理员密码重置：账号：" + adminId);

        admin = adminDao.findOne(adminId);
        if (admin == null) {
            throw new AppException("未找到用户信息");
        }
        //对旧密码进行MD5加密
        oldPassword = EncodeUtil.encodePassword(oldPassword, admin.getAccount());

        if (!oldPassword.equals(admin.getPassword())) {
            throw new AppException("旧密码不正确");
        }

        //对新密码进行MD5加密
        newPassword = EncodeUtil.encodePassword(newPassword, admin.getAccount());

        if (newPassword.equals(oldPassword)) {
            throw new AppException("新密码不能和旧密码相同");
        }

        admin.setPassword(newPassword);

        return adminDao.save(admin);
    }

    /**
     * 获取菜单
     */
    @Override
    public List<Menu> getAdminMenu(Admin admin) {

        List<Menu> topMenu = new ArrayList<Menu>();
        if (admin != null) {
            List<Authority> authoritys = admin.getAuthoritys();
            Map<String, Menu> menuMap = new HashMap<String, Menu>();
            if (authoritys != null && authoritys.size() > 0) {
                //构造一树目录树,放在map备用，以及找出顶级目录
                for (int i = 0; i < authoritys.size(); i++) {
                    Authority auth = authoritys.get(i);
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
                    if (!StringUtils.hasText(auth.getParentId())) {
                        topMenu.add(menu);
                    }
                }
                //构造一树目录树 构造父子关系
                for (int i = 0; i < authoritys.size(); i++) {
                    Authority auth = authoritys.get(i);
                    if (auth.getType() != null && 0 != auth.getType().intValue()) {
                        continue;
                    }
                    if (StringUtils.hasText(auth.getParentId())) {

                        Menu child = menuMap.get(auth.getAuthorityId());
                        Menu parent = menuMap.get(auth.getParentId());
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
     * 通过用户名查询
     */
    @Override
    public Admin findByAccount(String account) {

        return adminDao.findByAccount(account);
    }

    /**
     * 通过用户id查询
     */
    @Override
    public Admin findByAdminId(String adminId) {

        return adminDao.findOne(adminId);
    }

    /**
     * 添加管理员
     */
    @Override
    @Transactional
    public Admin addAdmin(Admin currentAdmin, Admin admin, String[] listroleId) {

        //有所属车站的管理员只能新增该车站的管理员
        if (currentAdmin != null && currentAdmin.getBelongId() != null && !"".equals(currentAdmin.getBelongId().trim())) {
            admin.setBelongId(currentAdmin.getBelongId());
        }
        if (admin == null)
            throw new AppException("管理员对象不能为空");
        if (!StringUtils.hasText(admin.getName()))
            throw new AppException("管理员名称不能为空");
        if (!StringUtils.hasText(admin.getAccount()))
            throw new AppException("管理员账号不能为空");
        if (!StringUtils.hasText(admin.getPassword()))
            throw new AppException("管理员密码不能为空");

        //防止重复添加管理员用户
        if (adminDao.countByAccount(admin.getAccount()) > 0)
            throw new AppException("此管理员帐号已存在！");

        logger.info("管理员服务层 ：添加管理员：所属管理员：" + currentAdmin.getName() + "要添加的管理员：" + admin.getName());
        // 缺省属性给默认值。
        admin.setCreateDate(new Date());
        String password = EncodeUtil.encodePassword(admin.getPassword(), admin.getAccount());

        //利用MD5进行加密
        admin.setPassword(password);

        //如果listroleId不为空，则添加管理员与角色的关系
        if (listroleId.length == 0) {
            throw new AppException("未指定权限");
        }
        //保存实体对象
        admin = adminDao.save(admin);

        //查找所有角色，并保存角色与管理员的关联关系
        for (int index = 0; index < listroleId.length; index++) {
            Role role = roleService.findByRoleId(listroleId[index]);
            if (role == null) {
                throw new AppException("未授权");
            }

            AdminRoleRela adminRoleRela = new AdminRoleRela();
            adminRoleRela.setAdmin(admin);
            adminRoleRela.setRole(role);
            adminRoleRelaService.addAdminRoleRela(adminRoleRela);
        }
        return admin;
    }

    /**
     * 查找管理员
     */
    @Override
    public List<Admin> findAdminByRoleId(Admin currentAdmin, Admin admin, String roleId) {
        //判断管理员名称,角色ID是否为空
        if (admin == null && !StringUtils.hasText(roleId)) {
            return adminDao.findAll();
        }
        //有所属车站的管理员只能新增该车站的管理员
        if (currentAdmin != null && currentAdmin.getBelongId() != null && !"".equals(currentAdmin.getBelongId().trim())) {
            admin.setBelongId(currentAdmin.getBelongId());
        }
        logger.info("管理员服务层 ：查询管理员列表：所属管理员：" + currentAdmin.getName() + "要查找的管理员：" + admin.getName());
        return adminDao.findByRoleId(admin.getName(), roleId);

    }

    @Override
    public Page<Admin> findAdminByNameOrRoleId(Admin currentAdmin, String name, String order, Pageable pageable) {
        if (currentAdmin == null) {
            throw new AppException("当前用户未登陆");
        }
        logger.info("管理员服务层 ：查询管理员列表：所属管理员：" + currentAdmin.getName() + "要查找的管理员：" + name);

        // JPA标准查询接口，使用Lambda表达式。root以商户类为根对象。
        Specification<Admin> specification = (root, query, builder) -> {
            // 创建组合条件，默认1=1。
            Predicate predicate = builder.conjunction();

            if (currentAdmin.getBelongId() != null && !"".equals(currentAdmin.getBelongId().trim())) {
                predicate = builder.and(predicate, builder.equal(root.get("belongId"), currentAdmin.getBelongId()));
            }
            // 如果名称不为空，添加到查询条件。
            if (StringUtils.hasText(name)) {
                predicate = builder.and(predicate, builder.like(root.get("name"), "%" + name + "%"));
            }
            if ("asc".equals(order)) {
                query.orderBy(builder.asc(root.get("createDate")));
            } else {
                query.orderBy(builder.desc(root.get("createDate")));
            }
            return predicate;
        };
        // 调用JPA标准查询接口查询数据。
        Page<Admin> admins = adminDao.findAll(specification, pageable);

        Iterator<Admin> iter = admins != null ? admins.iterator() : null;//.forEach(action);
        while (iter != null && iter.hasNext()) {
            Admin sp = iter.next();
            sp.setRoles(roleDao.getRoleByAdminId(sp.getAdminId()));
        }
        return admins;
    }

    @Override
    @Transactional
    public Admin updateAdmin(Admin currentAdmin, Admin admin, String[] listroleId) {
        if (admin == null) {
            throw new AppException("选择需要修改的管理员");
        }
        if (admin.getAccount() == null || admin.getAdminId() == null) {
            throw new AppException("选择需要修改的管理员");
        }
        logger.info("管理员服务层 ：更新管理员：所属管理员：" + currentAdmin.getName() + "要修改的管理员：" + admin.getName());

        //有所属车站的管理员只能新增该车站的管理员
        if (currentAdmin != null && currentAdmin.getBelongId() != null && !"".equals(currentAdmin.getBelongId().trim())) {
//    		admin.setBelongId(currentAdmin.getBelongId());
            if (admin.getBelongId() == null || !admin.getBelongId().equals(currentAdmin.getBelongId())) {
                throw new AppException("你不允许修改其它车站的管理员");
            }
        }

        Admin oldAdmin = adminDao.findByAdminId(admin.getAdminId());
        if (oldAdmin == null) {
            throw new AppException("该管理员并不存在");
        }

        if (oldAdmin.getAccount() != null && !oldAdmin.getAccount().equals(admin.getAccount())) {
            throw new AppException("管理员帐号不允许进行修改");
        }

        //更新需要修改的字段
        admin.setCreateDate(oldAdmin.getCreateDate());
        admin.setPassword(oldAdmin.getPassword());

        //删除管理员角色关系，再新增管理员角色关系
        adminRoleRelaService.deleteAdminRoleRelaByAdminId(admin.getAdminId());

        //查找所有角色，并保存角色与管理员的关联关系
        for (int index = 0; listroleId != null && index < listroleId.length; index++) {
            Role role = roleService.findByRoleId(listroleId[index]);
            if (role == null) {
                throw new AppException("未授权");
            }

            AdminRoleRela adminRoleRela = new AdminRoleRela();
            adminRoleRela.setAdmin(admin);
            adminRoleRela.setRole(role);
            adminRoleRelaService.addAdminRoleRela(adminRoleRela);
        }

        //保存更新的对象
        return adminDao.save(admin);
    }

    @Override
    @Transactional
    public void deleteAdmin(Admin currentAdmin, Admin admin) {
        if (currentAdmin == null) {
            throw new AppException("请登陆");
        }

        if (admin == null || !StringUtils.hasText(admin.getAdminId())) {
            throw new AppException("选择需要删除的管理员");
        }
        logger.info("管理员服务层 ：删除管理员：所属管理员：" + currentAdmin.getName() + "要删除的管理员：" + admin.getName());

        //车站管理人员不允许删除其它车站的管理人员
        if (currentAdmin.getBelongId() != null) {
            admin = adminDao.findOne(admin.getAdminId());
            if (!currentAdmin.getBelongId().equals(admin.getBelongId())) {
                throw new AppException("你不允许删除其它车站的管理人员 ");
            }
        }

        //解除管理员与角色的关联
        adminRoleRelaDao.deleteAdminRoleRelaOfAdmin(admin.getAdminId());

        //删除管理员对象
        adminDao.deleteByAdminId(admin.getAdminId());
    }

}
