package com.railwayservice.authority.web;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.interceptor.Authorize;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.authority.entity.Authority;
import com.railwayservice.authority.service.AuthorityService;
import com.railwayservice.authority.vo.Menu;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 权限模块请求控制器。
 *
 * @author lid
 * @date 2017.2.8
 */
@Controller
@RequestMapping(value = "/authority", produces = {"application/json;charset=UTF-8"})
@Api(value = "权限模块请求控制器", description = "权限模块请求控制器")
@Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_AUTHORITY_MANAGE"})
public class AuthorityController {
    private final Logger logger = LoggerFactory.getLogger(AuthorityController.class);

    private AuthorityService authorityService;

    @Autowired
    public void setAuthorityService(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * 新增权限信息
     */
    @ResponseBody
    @RequestMapping("/add")
    public ResultMessage addAuthority(Authority authority) {
        logger.info("权限控制层：新增权限信息：");
        try {
            authorityService.addAuthority(authority);
            return ResultMessage.newSuccess("新增权限成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("新增权限异常：" , e);
            return ResultMessage.newFailure("新增权限异常！");
        }
    }

    /**
     * 更新权限信息。
     */
    @ResponseBody
    @RequestMapping("/update")
    public ResultMessage updateAuthority(Authority authority) {
        logger.info("权限控制层：更新权限信息：");
        try {
            authorityService.updateAuthority(authority);
            return ResultMessage.newSuccess("修改权限成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("修改权限异常：" , e);
            return ResultMessage.newFailure("修改权限异常！");
        }
    }

    /**
     * 删除权限
     */
    @ResponseBody
    @RequestMapping("/delete")
    public ResultMessage deleteAuthority( String authorityId) {
        logger.info("权限控制层：删除权限：");
        try {
            authorityService.deleteAuthority(authorityId);
            return ResultMessage.newSuccess("删除权限成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("删除权限异常：" , e);
            return ResultMessage.newFailure("删除权限异常！");
        }
    }

    /**
     * 查询所有权限。
     */
    @ResponseBody
    @RequestMapping("/query")
    public ResultMessage queryAuthority(PageParam param, @Param("name") String name, @Param("description") String description) {
        logger.info("权限控制层：查询所有权限：");
        try {
            PageData pageData = authorityService.queryAuthorityPage(param, name, description);
            return ResultMessage.newSuccess("获取权限列表成功！").setData(pageData);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取权限异常：" , e);
            e.printStackTrace();
            return ResultMessage.newFailure("获取权限异常！");
        }
    }

    /**
     * 查询所有权限。
     */
    @ResponseBody
    @RequestMapping("/queryAuthorityTree")
    public ResultMessage getAllAuthorityTree() {
        logger.info("权限控制层：查询所有权限：");
        try {
            List<Authority> list = authorityService.getAllAuthority();
            List<Menu> result = authorityService.convertToMenu(list);
            return ResultMessage.newSuccess("获取权限列表成功！").setData(result);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取权限异常：" , e);
            e.printStackTrace();
            return ResultMessage.newFailure("获取权限异常！");
        }
    }

    /**
     * 查询单条权限。
     */
    @ResponseBody
    @RequestMapping("/get")
    public ResultMessage getAuthority(String id) {
        logger.info("权限控制层：查询单条权限：");
        try {
            Authority authority = authorityService.findAuthorityById(id);
            if (null == authority) {
                return ResultMessage.newFailure("未找到相关权限记录！");
            }
            return ResultMessage.newSuccess("获取权限成功！").setData(authority);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取权限异常：" , e);
            return ResultMessage.newFailure("获取权限异常！");
        }
    }

    /**
     * 根据name、url查找权限
     */
    @ResponseBody
    @RequestMapping("/getParentAuthority")
    public ResultMessage getParentAuthority() {
        logger.info("权限控制层：根据name、url查找权限：");
        try {
            List<Authority> listAuthority = authorityService.getAuthorityByType(0);
            return ResultMessage.newSuccess("获取权限成功！").setData(listAuthority);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取权限异常：" , e);
            return ResultMessage.newFailure("获取权限异常！");
        }
    }
}
