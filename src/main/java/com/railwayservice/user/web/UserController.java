package com.railwayservice.user.web;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.user.entity.User;
import com.railwayservice.user.service.UserService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 用户模块请求控制器。
 *
 * @author lid
 * @date 2017.2.4
 */
@Controller
@RequestMapping(value = "/user", produces = {"application/json;charset=UTF-8"})
@Api(value = "用户模块请求控制器", description = "用户模块的相关操作")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 新增用户信息
     */
    @ResponseBody
    @RequestMapping("/add")
    public ResultMessage addUser(User user) {
        logger.info("用户控制层->添加用户->用户名:" + user.getName());
        try {
            userService.addUser(user);
            return ResultMessage.newSuccess("新增用户成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("新增用户异常：", e);
            return ResultMessage.newFailure("新增用户异常！");
        }
    }

    /**
     * 更新用户信息。
     */
    @ResponseBody
    @RequestMapping("/update")
    public ResultMessage updateUser(User user) {
        logger.info("用户控制层->更新用户->用户名:" + user.getName());
        try {
            userService.updateUser(user);
            return ResultMessage.newSuccess("修改用户成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("修改用户异常：", e);
            return ResultMessage.newFailure("修改用户异常！");
        }
    }

    /**
     * 查询所有用户。
     */
    @ResponseBody
    @RequestMapping("/query")
//    @Authorize( type = AppConfig.AUTHORITY_ADMIN,value={"WEB_USER_WXUSER_LIST"})
    public ResultMessage getAllUser(HttpSession session) {
        logger.info("用户控制层->查询所有用户");
        try {
            List<User> list = userService.getAllUser();
            System.out.println("list.size():" + list.size());
            return ResultMessage.newSuccess("获取用户成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取用户异常：", e);
            return ResultMessage.newFailure("获取用户异常！");
        }
    }

    /**
     * 根据id查询单个用户详细信息
     */
    @ResponseBody
    @RequestMapping("/get")
//    @Authorize( type = AppConfig.AUTHORITY_ADMIN,value={"WEB_USER_WXUSER_LIST"})
    public ResultMessage getUserById(String id) {
        logger.info("用户控制层->详细用户信息->用户ID:" + id);
        try {
            User user = userService.getUserByUserId(id);
            if (null == user) {
                return ResultMessage.newFailure("未找到相关用户记录！");
            }
            return ResultMessage.newSuccess("获取用户信息成功！").setData(user);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取用户信息异常：", e);
            return ResultMessage.newFailure("获取用户信息异常！");
        }
    }

    /**
     * 根据openid查询单个用户信息
     */
    @ResponseBody
    @RequestMapping("/getByOpenid")
//    @Authorize( type = AppConfig.AUTHORITY_ADMIN,value={"WEB_USER_WXUSER_LIST"})
    public ResultMessage getUserByOpenid(String openid) {
        logger.info("用户控制层->单个用户信息->openID:" + openid);
        try {
            User user = userService.getUserByOpenid(openid);
            if (null == user) {
                return ResultMessage.newFailure("未找到相关用户记录！");
            }
            return ResultMessage.newSuccess("获取用户信息成功！").setData(user);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取用户信息异常：", e);
            return ResultMessage.newFailure("获取用户信息异常！");
        }
    }

    /**
     * 查询所有用户(分页)
     */
    @ResponseBody
    @RequestMapping("/queryAllUser")
//    @Authorize( type = AppConfig.AUTHORITY_ADMIN,value={"WEB_USER_WXUSER_LIST"})
    public ResultMessage queryAllUser(PageParam param, String nickName, String phoneNo) {
        logger.info("用户控制层->查询所有用户->用户昵称:" + nickName + ", 联系方式：" + phoneNo);
        try {
            Page<User> userPage = userService.queryAllUser(nickName, phoneNo, param.newPageable());
            return ResultMessage.newSuccess("获取用户列表成功！").setData(new PageData(userPage));
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取用户列表异常：", e);
            return ResultMessage.newFailure("获取用户列表异常！");
        }
    }
}
