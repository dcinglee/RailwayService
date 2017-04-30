package com.railwayservice.serviceprovider.web;

import com.railwayservice.application.cache.LoginSaltCache;
import com.railwayservice.application.cache.ServiceProviderSessionCache;
import com.railwayservice.application.cache.ShortMessageCache;
import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.interceptor.Authorize;
import com.railwayservice.application.util.RSAEncryptUtil;
import com.railwayservice.application.util.RandomString;
import com.railwayservice.application.util.SendMSG;
import com.railwayservice.application.vo.LoginMessage;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.serviceprovider.entity.ServiceProvider;
import com.railwayservice.serviceprovider.service.ServiceProviderService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 服务人员模块请求控制器。
 *
 * @author lid
 * @date 2017.2.9
 */
@Controller
@RequestMapping(value = "/serviceProvider", produces = {"application/json;charset=UTF-8"})
@Api(value = "服务人员模块请求控制器", description = "服务人员模块的相关操作")
public class ServiceProviderController {
    private final Logger logger = LoggerFactory.getLogger(ServiceProviderController.class);

    private LoginSaltCache loginSaltCache;

    private ShortMessageCache shortMessageCache;

    private ServiceProviderSessionCache serviceProviderSessionCache;

    private ServiceProviderService serviceProviderService;

    @Autowired
    public void setServiceProviderService(ServiceProviderService serviceProviderService) {
        this.serviceProviderService = serviceProviderService;
    }

    @Autowired
    public void setLoginSaltCache(LoginSaltCache loginSaltCache) {
        this.loginSaltCache = loginSaltCache;
    }

    @Autowired
    public void setShortMessageCache(ShortMessageCache shortMessageCache) {
        this.shortMessageCache = shortMessageCache;
    }

    @Autowired
    public void setServiceProviderSessionCache(ServiceProviderSessionCache serviceProviderSessionCache) {
        this.serviceProviderSessionCache = serviceProviderSessionCache;
    }

    /**
     * 新增服务人员信息
     */
    @ResponseBody
    @RequestMapping("/add")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_PROVIDER_MANAGE"})
    public ResultMessage addServiceProvider(ServiceProvider serviceProvider, @RequestParam("typeId[]") String[] typeIds) {
        logger.info("服务人员控制层->新增服务人员->服务人员名称：" + serviceProvider.getName());
        try {
            serviceProviderService.addServiceProvider(serviceProvider, typeIds);
            return ResultMessage.newSuccess("新增服务人员成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("新增服务人员异常：", e);
            return ResultMessage.newFailure("新增服务人员异常！");
        }
    }

    /**
     * 更新服务人员信息。
     */
    @ResponseBody
    @RequestMapping("/update")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_PROVIDER_MANAGE"})
    public ResultMessage updateServiceProvider(ServiceProvider serviceProvider, @RequestParam("typeId[]") String[] typeIds) {
        logger.info("服务人员控制层->更新服务人员->服务人员名称：" + serviceProvider.getName());
        try {
            serviceProviderService.updateServiceProvider(serviceProvider, typeIds);
            return ResultMessage.newSuccess("修改服务人员成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("修改服务人员异常：", e);
            return ResultMessage.newFailure("修改服务人员异常！");
        }
    }

    /**
     * 删除服务人员
     */
    @ResponseBody
    @RequestMapping("/delete")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_PROVIDER_MANAGE"})
    public ResultMessage deleteServiceProvider(String serviceProviderId) {
        logger.info("服务人员控制层->删除服务人员->服务人员id：" + serviceProviderId);
        try {
            serviceProviderService.deleteServiceProvider(serviceProviderId);
            return ResultMessage.newSuccess("删除服务人员成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("删除服务人员异常：", e);
            return ResultMessage.newFailure("删除服务人员异常！");
        }
    }

    /**
     * 查询所有服务人员。
     */
    @ResponseBody
    @RequestMapping("/query")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_PROVIDER_MANAGE"})
    public ResultMessage getAllServiceProvider(PageParam param, String name, String phoneNo, String order, String identityCardNo,
                                               String stationId) {
        logger.info("服务人员控制层->查询所有服务人员->服务人员名称：" + name + "电话：" + phoneNo);
        try {
            Page<ServiceProvider> datapage = serviceProviderService.queryServiceProvider(name, phoneNo, identityCardNo, stationId, order, param.newPageable());
            return ResultMessage.newSuccess("获取服务人员列表成功！").setData(new PageData(datapage));
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取服务人员异常：", e);
            e.printStackTrace();
            return ResultMessage.newFailure("获取服务人员异常！");
        }
    }

    /**
     * 通过名称查询服务人员
     */
    @ResponseBody
    @RequestMapping("/getByName")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_PROVIDER_MANAGE"})
    public ResultMessage getServiceProviderByName(String name) {
        logger.info("服务人员控制层->通过名称查询服务人员->服务人员名称：" + name);
        if (null == name) {
            return ResultMessage.newFailure("Name参数为空！");
        }
        try {
            List<ServiceProvider> listServiceProvider = serviceProviderService.findByName(name);
            if (0 == listServiceProvider.size()) {
                return ResultMessage.newFailure("未找到相关服务人员记录！");
            }
            return ResultMessage.newSuccess("获取服务人员成功！").setData(listServiceProvider);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取服务人员异常：", e);
            return ResultMessage.newFailure("获取服务人员异常！");
        }
    }

    /**
     * 查询单条服务人员。
     */
    @ResponseBody
    @RequestMapping("/get")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_PROVIDER_MANAGE"})
    public ResultMessage getServiceProvider(String id) {
        logger.info("服务人员控制层->查询单条服务人员->服务人员id：" + id);
        try {
            ServiceProvider serviceProvider = serviceProviderService.findByServiceProviderId(id);
            if (null == serviceProvider) {
                return ResultMessage.newFailure("未找到相关服务人员记录！");
            }
            return ResultMessage.newSuccess("获取服务人员成功！").setData(serviceProvider);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取服务人员异常：", e);
            return ResultMessage.newFailure("获取服务人员异常！");
        }
    }

    /**
     * 登陆时获取随机盐带令牌。
     */
    @ResponseBody
    @RequestMapping("/tokenSalt")
    public ResultMessage getTokenSalt(HttpServletRequest request) {
        logger.info("登陆前获取加密盐，来自IP：" + request.getRemoteAddr());
        try {
            String salt = RandomString.randomString(10);
            // 使用token记录盐，登陆时需要带上token。
            String token = RandomString.generate64UUID();
            loginSaltCache.put(token + "tokenSalt", salt);
            return ResultMessage.newSuccess().setData(new LoginMessage(salt, token));
        } catch (Exception e) {
            logger.error("获取随机盐异常：", e);
            return ResultMessage.newFailure("获取随机盐异常！");
        }
    }

    /**
     * 服务人员令牌退出登陆。
     *
     * @author Ewing
     * @date 2017.2.14
     */
    @ResponseBody
    @RequestMapping("/tokenLogout")
    public ResultMessage tokenLogout(HttpServletRequest request) {
        logger.info("服务人员控制器：退出登陆");
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            if (StringUtils.hasText(token))
                serviceProviderSessionCache.remove(token);
            return ResultMessage.newSuccess();
        } catch (Exception e) {
            logger.error("服务人员令牌退出登陆异常：", e);
            return ResultMessage.newFailure("服务人员令牌退出登陆异常！");
        }
    }

    /**
     * 获取使用token认证的用户信息。
     */
    @ResponseBody
    @RequestMapping("/tokenUser")
    public ResultMessage tokenUser(HttpServletRequest request) {
        logger.info("获取使用token认证的用户信息");
        try {
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            ServiceProvider loginer = serviceProviderSessionCache.get(token);
            if (loginer == null)
                return ResultMessage.newFailure("当前token已不存在或已过期！");
            else
                return ResultMessage.newSuccess().setData(loginer);
        } catch (Exception e) {
            logger.error("获取使用token认证的用户信息异常：", e);
            return ResultMessage.newFailure("获取用户信息失败！");
        }
    }

    /**
     * 服务人员登陆。
     *
     * @param phoneNo  服务人员手机号。
     * @param password 服务人员密码。
     */
    @ResponseBody
    @RequestMapping("/login")
    public ResultMessage serviceProvideLogin(@RequestParam("phoneNo") String phoneNo,
                                             @RequestParam("password") String password, HttpServletRequest request) {
        logger.info("服务人员控制器：服务人员登陆，手机号：" + phoneNo + "，加密密码：" + password);
        try {
            // 获取加密盐。
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            if (!StringUtils.hasText(token))
                return ResultMessage.newFailure("验证令牌无效，请重试。");
            String sessionSalt = loginSaltCache.get(token + "tokenSalt");
            loginSaltCache.remove(token + "tokenSalt");
            //使用私钥解密字符串。
            password = RSAEncryptUtil.decryptString(password);
            // 取出加密盐与之前给客户端的盐比对。
            if (sessionSalt == null || password == null || password.length() < 10
                    || !password.substring(0, 10).equals(sessionSalt))
                return ResultMessage.newFailure("操作过期，请重试！");
            // 去掉附加盐才是真正的密码。
            password = password.substring(10);
            ResultMessage message = serviceProviderService.loginByPhoneNo(phoneNo, password);
            // 登陆成功把token放入缓存，超时后自动清理。
            if (message.isSuccess()) {
                ServiceProvider serviceProvider = message.getData();
                serviceProviderSessionCache.put(token, serviceProvider);
            }
            return message;
        } catch (Exception e) {
            logger.error("服务人员登陆异常：", e);
            return ResultMessage.newFailure("服务人员登陆失败！");
        }
    }

    /**
     * 发送短信验证码。
     */
    @ResponseBody
    @RequestMapping("/sendShortMessage")
    public ResultMessage sendShortMessage(String phoneNo) {
        logger.info("服务员控制器：发送短信验证码给：" + phoneNo);
        try {
            // 校验手机号是否有效。
            if (phoneNo == null || !phoneNo.matches(AppConfig.PHONE_NO_PATTEN))
                return ResultMessage.newFailure("该手机号码无效！");
            if (!serviceProviderService.hasPhoneNo(phoneNo))
                return ResultMessage.newFailure("该手机号未注册！");

            // 发送短信验证码，并生成对应token令牌。
            String token = RandomString.generate64UUID();
            String key = phoneNo + token;
            String code = RandomString.randomNumberString(6);
            if (SendMSG.sendCheckCode(phoneNo, code)) {
                shortMessageCache.put(key, code);
                return ResultMessage.newSuccess("发送短信验证码成功！")
                        .setData(new LoginMessage(null, token));
            } else {
                return ResultMessage.newFailure("发送短信验证码失败。");
            }
        } catch (Exception e) {
            logger.error("发送短信验证码异常：", e);
            return ResultMessage.newFailure("发送短信验证码失败。");
        }
    }

    /**
     * 短信验证码登陆。
     */
    @ResponseBody
    @RequestMapping("/shortMessageLogin")
    public ResultMessage sendShortMessage(String phoneNo, String code, HttpServletRequest request) {
        logger.info("服务员控制器：短信验证码登陆：号码：" + phoneNo + "验证码：" + code);
        try {
            // 参数及令牌校验。
            if (!StringUtils.hasText(phoneNo) || !StringUtils.hasText(code))
                return ResultMessage.newFailure("手机号和验证码不能为空！");
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            if (!StringUtils.hasText(token))
                return ResultMessage.newFailure("令牌token不能为空！");

            // 验证短信码，短信码可重复验证。
            String key = phoneNo + token;
            String codeSend = shortMessageCache.get(key);
            if (!code.equals(codeSend))
                return ResultMessage.newFailure("短信验证码不正确！");
            // 验证通过移除验证码。
            shortMessageCache.remove(key);

            // 短信验证通过，获取服务员信息。
            ServiceProvider serviceProvider = serviceProviderService.findByPhoneNo(phoneNo);
            // 登陆成功把token放入缓存，超时后自动清理。
            serviceProviderSessionCache.put(token, serviceProvider);

            // 返回token和服务员信息。
            return ResultMessage.newSuccess().setData(serviceProvider);
        } catch (Exception e) {
            logger.error("短信验证码登陆异常：", e);
            return ResultMessage.newFailure("短信验证码登陆失败。");
        }
    }

    /**
     * 短信验证码修改密码。
     */
    @ResponseBody
    @RequestMapping("/changePasswordByPhoneNo")
    public ResultMessage changePasswordByPhoneNo(String phoneNo, String code, String newPassword, HttpServletRequest request) {
        logger.info("服务员控制器：短信验证码登陆：号码：" + phoneNo + " 验证码：" + code + " 新密码密文：" + newPassword);
        try {
            // 参数及令牌校验。
            if (!StringUtils.hasText(phoneNo) || !StringUtils.hasText(code) || !StringUtils.hasText(newPassword))
                return ResultMessage.newFailure("手机号、验证码和新密码不能为空！");
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            if (!StringUtils.hasText(token))
                return ResultMessage.newFailure("令牌token不能为空！");

            // 验证短信码，短信码可重复验证。
            String key = phoneNo + token;
            String codeSend = shortMessageCache.get(key);
            if (!code.equals(codeSend))
                return ResultMessage.newFailure("短信验证码不正确！");
            // 去掉加密的盐值，解码加密的密码。
            newPassword = RSAEncryptUtil.decryptString(newPassword).substring(6);
            // 验证通过移除验证码。
            shortMessageCache.remove(key);

            // 短信验证通过，获取服务员信息。
            serviceProviderService.changePasswordByPhoneNo(phoneNo, newPassword);
            return ResultMessage.newSuccess("密码修改成功！");
        } catch (Exception e) {
            logger.error("密码修改成功异常：", e);
            return ResultMessage.newFailure("密码修改成功失败。");
        }
    }

    /**
     * 查询在线服务人员人数
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryOnlineNum")
    public ResultMessage queryOnlineNum(String phoneNo) {
        logger.info("服务员控制器：查询在线服务人员人数：");
        try {
            Integer onLineNum = serviceProviderService.queryOnlineNum(phoneNo);
            return ResultMessage.newSuccess().setData(onLineNum);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("查询在线服务人员人数异常：", e);
            return ResultMessage.newFailure("查询在线服务人员人数异常！");
        }
    }

    /**
     * 确认收货验证码。
     */
    @ResponseBody
    @RequestMapping("/sendGetGoodsCode")
    public ResultMessage sendGetGoodsCode(String phoneNo,String orderId) {
        logger.info("服务员控制器：发送收货码给：" + phoneNo);
        try {
            // 校验手机号是否有效。
            if (phoneNo == null || !phoneNo.matches(AppConfig.PHONE_NO_PATTEN))
                return ResultMessage.newFailure("该手机号码无效！");
            if (!serviceProviderService.hasPhoneNo(phoneNo))
                return ResultMessage.newFailure("该手机号未注册！");

            // 发送短信验证码，并生成对应token令牌。
            String token = RandomString.generate64UUID();
            String key = phoneNo + token;
            String code = RandomString.randomNumberString(6);
            if (SendMSG.sendGetGoodsCode(phoneNo, code)) {
                shortMessageCache.put(key, code);
                return ResultMessage.newSuccess("发送收货码成功！")
                        .setData(new LoginMessage(null, token));
            } else {
                return ResultMessage.newFailure("发送收货码失败。");
            }
        } catch (Exception e) {
            logger.error("发送收货码异常：", e);
            return ResultMessage.newFailure("发送收货码失败。");
        }
    }
}
