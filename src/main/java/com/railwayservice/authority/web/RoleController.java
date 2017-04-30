package com.railwayservice.authority.web;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.interceptor.Authorize;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.authority.entity.Authority;
import com.railwayservice.authority.entity.Role;
import com.railwayservice.authority.service.RoleAuthorityRelaService;
import com.railwayservice.authority.service.RoleService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

/**
 * 角色请求控制器。
 *
 * @author lid
 * @date 2017.2.8
 */
@Controller
@RequestMapping(value = "/role", produces = {"application/json;charset=UTF-8"})
@Api(value = "角色请求控制器", description = "角色请求控制器")
@Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_ROLE_MANAGE"})
public class RoleController {
    private final Logger logger = LoggerFactory.getLogger(RoleController.class);

    private RoleService roleService;

    private RoleAuthorityRelaService roleAuthorityRelaService;

    @Autowired
    public void setRoleAuthorityRelaService(RoleAuthorityRelaService roleAuthorityRelaService) {
        this.roleAuthorityRelaService = roleAuthorityRelaService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 新增角色信息
     * 将权限列表传入，添加权限与角色的关联关系
     *
     * @param name
     * @param description
     * @param listAuthorityId
     * @return ResultMessage
     * @author lid
     * @date 2017.2.16
     */
    @ResponseBody
    @RequestMapping("/add")
    public ResultMessage addRole(String name, String description, @RequestParam(value = "listAuthorityId[]") String[] listAuthorityId) {
        logger.info("角色控制层：添加角色：角色名称：" + name + ",角色描述：" + description + ",listAuthorityId.length():" + listAuthorityId.length
                + ",listAuthorityId:" + Arrays.toString(listAuthorityId));
        try {
            roleService.addRole(name, description, listAuthorityId);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("新增角色异常：", e);
            return ResultMessage.newFailure("新增角色异常！");
        }
        return ResultMessage.newSuccess("新增角色成功！");
    }

    /**
     * 更新角色信息。传入角色id、角色与权限的关联关系id list
     *
     * @param roleId
     * @return ResultMessage
     * @author lid
     * @date 2017.2.16
     */
    @ResponseBody
    @RequestMapping("/update")
    public ResultMessage updateRole(String roleId, String name, String description, @RequestParam(value = "listAuthorityId[]") String[] listAuthorityId) {
        logger.info("角色控制层：更新角色信息：角色Id：" + roleId + ",角色名称：" + name + ",角色描述：" + description + ",listAuthorityId:" + Arrays.toString(listAuthorityId));

        try {
            roleService.updateRole(roleId, name, description, listAuthorityId);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("修改角色异常：", e);
            return ResultMessage.newFailure("修改角色异常！");
        }

        return ResultMessage.newSuccess("修改角色成功！");
    }

    /**
     * 删除角色,先删除角色与权限的关联关系以及角色与管理员的关联关系
     *
     * @param roleId
     * @return
     */
    @ResponseBody
    @RequestMapping("/delete")
    public ResultMessage deleteRole(String roleId) {
        logger.info("角色控制层：删除角色：角色Id" + roleId);
        //最后删除角色
        try {
            roleService.deleteRole(roleId);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("删除角色异常：", e);
            return ResultMessage.newFailure("删除角色异常！");
        }

        return ResultMessage.newSuccess("删除角色成功！");
    }

    /**
     * 查询所有角色。
     */
    @ResponseBody
    @RequestMapping("/query")
    public ResultMessage getAllRole() {
        logger.info("角色控制层：查询所有角色：");
        try {
            List<Role> list = roleService.getAllRole();
            return ResultMessage.newSuccess("获取角色成功！").setData(list);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取角色异常：", e);
            return ResultMessage.newFailure("获取角色异常！");
        }
    }

    /**
     * 根据角色名称查找角色
     */
    @ResponseBody
    @RequestMapping("/getRoleByName")
    public ResultMessage getRoleByName(String name) {
        logger.info("角色控制层：根据角色名称查找角色：角色名称：" + name);
        try {
            List<Role> listRole = roleService.findByName(name);
            return ResultMessage.newSuccess("获取角色成功！").setData(listRole);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取角色异常：", e);
            return ResultMessage.newFailure("获取角色异常！");
        }
    }

    /**
     * 查询单条角色。
     */
    @ResponseBody
    @RequestMapping("/get")
    public ResultMessage getRole(String roleId) {
        logger.info("角色控制层：查询单条角色：角色ID：" + roleId);
        try {
            Role role = roleService.findByRoleId(roleId);
            if (null == role) {
                return ResultMessage.newFailure("请输入查询条件！");
            }
            return ResultMessage.newSuccess("获取角色信息成功！").setData(role);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取角色异常：", e);
            return ResultMessage.newFailure("获取角色异常！");
        }
    }

    /**
     * 根据角色id查找所有的权限
     *
     * @param roleId
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAuthorityByRoleId")
    public ResultMessage getAuthorityByRoleId(String roleId) {
        // TODO 业务逻辑都放入Service
        logger.info("角色控制层：根据角色id查找所有的权限：角色ID：" + roleId);
        if (null == roleId) {
            return ResultMessage.newFailure("请输入查询条件！");
        }
        try {
            Role role = roleService.findByRoleId(roleId);
            if (null == role) {
                return ResultMessage.newFailure("未找到角色的权限信息记录！");
            }
            List<Authority> listAuthority = roleAuthorityRelaService.findAuthorityByRole(role);
            return ResultMessage.newSuccess("获取角色的权限信息成功！").setData(listAuthority);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取角色的权限异常：", e);
            return ResultMessage.newFailure("获取角色的权限异常！");
        }
    }

}
