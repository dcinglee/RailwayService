package com.railwayservice.authority.web;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.interceptor.Authorize;
import com.railwayservice.application.util.RSAEncryptUtil;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.authority.service.AdminService;
import com.railwayservice.authority.service.AuthorityService;
import com.railwayservice.authority.vo.Menu;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lidx
 * @date 2017年2月8日
 * @describe 管理员用户模块请求控制器。
 */
@Controller
@RequestMapping(value = "/admin", produces = {"application/json;charset=UTF-8"})
@Api(value = "管理员用户模块请求控制器", description = "管理员用户模块请求控制器")
@Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_ADMIN_MANAGE"})
public class AdminController {
    private final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private AdminService adminService;

    private AuthorityService authorityService;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Autowired
    public void setAuthorityService(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * 获取管理员用户(运营管理员，车站管理员)
     *
     * @param name 管理员用户名称
     * @return ResultMessage
     */
    @ResponseBody
    @RequestMapping("/queryAdmin")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_ADMIN_MANAGE"})
    public ResultMessage queryAdmin(PageParam param, String name, String order, HttpServletRequest request) {
        logger.info("管理员控制层：查询管理员：管理员名称:" + name);
        try {
            Admin adminLogined = (Admin) request.getSession().getAttribute(AppConfig.ADMIN_SESSION_KEY);
            Page<Admin> page = adminService.findAdminByNameOrRoleId(adminLogined, name, order, param.newPageable());
            return ResultMessage.newSuccess("获取管理员列表成功！").setData(new PageData(page));
        } catch (Exception e) {
            logger.error("查询数据异常：", e);
            return ResultMessage.newFailure("查询数据异常！");
        }
    }

    /**
     * 修改管理员用户(运营管理员，车站管理员)
     *
     * @return ResultMessage
     */
    @ResponseBody
    @RequestMapping("/updateAdmin")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_ADMIN_MANAGE"})
    public ResultMessage updateAdmin(Admin admin, @RequestParam(value = "roleId[]") String[] roleId, HttpServletRequest request) {
        logger.info("管理员控制层：修改管理员：管理员名称: " + admin.getName());
        try {
            Admin adminLogined = (Admin) request.getSession().getAttribute(AppConfig.ADMIN_SESSION_KEY);
            adminService.updateAdmin(adminLogined, admin, roleId);
            return ResultMessage.newSuccess("修改管理员成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("修改管理员异常：", e);
            return ResultMessage.newFailure("修改管理员异常！");
        }
    }

    /**
     * 添加管理员用户(运营管理员，车站管理员)
     *
     * @param adminNew 管理员对象
     * @param roleId
     * @return ResultMessage
     */
    @ResponseBody
    @RequestMapping("/addAdmin")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_ADMIN_MANAGE"})
    public ResultMessage addAdmin(Admin adminNew, @RequestParam(value = "roleId[]") String[] roleId, HttpServletRequest request) {
        logger.info("管理员控制层：添加管理员：管理员账号:" + adminNew.getAccount());
        try {
            Admin adminLogined = (Admin) request.getSession().getAttribute(AppConfig.ADMIN_SESSION_KEY);
            adminService.addAdmin(adminLogined, adminNew, roleId);
            return ResultMessage.newSuccess("添加管理员用户成功");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("添加管理员用户异常：", e);
            return ResultMessage.newFailure("添加管理员用户异常！");
        }
    }

    /**
     * 删除管理员用户(运营管理员，车站管理员)
     *
     * @param admin 管理员对象
     * @return ResultMessage
     */
    @ResponseBody
    @RequestMapping("/deleteAdmin")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_ADMIN_MANAGE"})
    public ResultMessage deleteAdmin(Admin admin, HttpServletRequest request) {
        logger.info("管理员控制层：删除管理员：管理员名称：" + admin.getName());
        try {
            Admin adminLogined = (Admin) request.getSession().getAttribute(AppConfig.ADMIN_SESSION_KEY);
            adminService.deleteAdmin(adminLogined, admin);
            return ResultMessage.newSuccess("删除管理员用户成功");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("删除管理员用户异常：", e);
            return ResultMessage.newFailure("删除管理员用户异常！");
        }

    }

    /**
     * 管理员登陆
     *
     * @param account  管理员用户名
     * @param password 管理员密码
     * @param request
     * @return ResultMessage
     * @author lidx
     * @date 2017.2.8
     */
    @ResponseBody
    @RequestMapping("/adminLogin")
    @Authorize(type = AppConfig.AUTHORITY_NONE)
    public ResultMessage adminLogin(@RequestParam("account") String account, @RequestParam("password") String password, HttpServletRequest request) {
        logger.info("管理员控制层：管理员登陆：账号：" + account + " 加密密码：" + password);
        try {
            //使用私钥解密字符串。
            password = RSAEncryptUtil.decryptString(password);
            String salt = password.substring(0, 10);
            Object sessionSalt = request.getSession().getAttribute("sessionSalt");
            request.getSession().removeAttribute("sessionSalt");
            if (!salt.equals(sessionSalt)) {
                return ResultMessage.newFailure("操作过期，请重试！");
            }
            // 去掉附加盐才是真正的密码。
            password = password.substring(10);
            ResultMessage message = adminService.loginByAccount(account, password);
            if (message.isSuccess()) {
                request.getSession().setAttribute(AppConfig.ADMIN_SESSION_KEY, message.getData());
            }
            // 返回给前台用户信息要置空。
            return message.setData("");
        } catch (Exception e) {
            logger.error("管理员登陆异常：", e);
            return ResultMessage.newFailure("管理员登陆异常！");
        }
    }

    /**
     * 获取当前登陆用户名
     *
     * @return ResultMessage
     * @author xuy
     * @date 2017.2.14
     */
    @ResponseBody
    @RequestMapping("/adminGetUserName")
    @Authorize(type = AppConfig.AUTHORITY_NONE)
    public ResultMessage adminGetUserName(HttpSession session) {
        logger.info("管理员控制层：获取当前登陆用户名");
        try {
            Object obj = session.getAttribute(AppConfig.ADMIN_SESSION_KEY);
            if (obj == null) {
                return ResultMessage.newFailure("请登陆");
            }
            String adminName = ((Admin) obj).getName();
            return ResultMessage.newSuccess().setData(adminName);
        } catch (Exception e) {
            logger.error("管理员获取名称异常：", e);
            return ResultMessage.newFailure("管理员获取名称异常！");
        }
    }

    /**
     * 密码重置
     *
     * @param oldPassword 管理员原密码
     * @param newPassword 管理员新密码
     * @param session
     * @return ResultMessage
     * @author lixs
     * @date 2017.2.8
     */
    @ResponseBody
    @RequestMapping("/resetPassWord")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_PASSWORD_RESET"})
    public ResultMessage resetPassWord(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, HttpSession session) {
        logger.info("管理员控制层：密码重置：加密旧密码：" + oldPassword + " 加密新密码：" + newPassword);
        try {
            Object obj = session.getAttribute(AppConfig.ADMIN_SESSION_KEY);
            if (obj == null) {
                return ResultMessage.newFailure("请登陆");
            }

            String adminId = ((Admin) obj).getAdminId();

            adminService.resetPassWord(adminId, oldPassword, newPassword);
            return ResultMessage.newSuccess("密码更新完成");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("密码更新错误：", e);
            return ResultMessage.newFailure("更改密码失败！");
        }
    }

    /**
     * 获取管理员菜单
     *
     * @return ResultMessage
     * @author xuy
     * @date 2017.2.13
     */
    @ResponseBody
    @RequestMapping("/getAdminMenu")
    @Authorize(type = AppConfig.AUTHORITY_NONE)
    public ResultMessage getAdminMenu(HttpSession session) {
        logger.info("管理员控制层：获取管理员菜单");
        try {
            Object obj = session.getAttribute(AppConfig.ADMIN_SESSION_KEY);
            if (obj == null) {
                return ResultMessage.newFailure("请登陆");
            }
            List<Menu> topMenu = new ArrayList<Menu>();
            if (obj instanceof Admin) {
                //topMenu = adminService.getAdminMenu((Admin) obj);
                topMenu = authorityService.convertToMenu(((Admin) obj).getAuthoritys());
            }

            ResultMessage result = ResultMessage.newSuccess();
            result.setData(topMenu);

            return result;
        } catch (Exception e) {
            logger.error("管理员菜单异常：", e);
            return ResultMessage.newFailure("菜单异常！");
        }
    }
}
   
 