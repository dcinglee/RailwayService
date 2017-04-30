package com.railwayservice.merchantmanage.web;

import com.railwayservice.application.cache.LoginSaltCache;
import com.railwayservice.application.cache.MerchantSessionCache;
import com.railwayservice.application.cache.ShortMessageCache;
import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.interceptor.Authorize;
import com.railwayservice.application.util.RSAEncryptUtil;
import com.railwayservice.application.util.RandomString;
import com.railwayservice.application.util.SendMSG;
import com.railwayservice.application.vo.LoginMessage;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.merchantmanage.service.MerchantService;
import com.railwayservice.messages.service.ChannelInfoService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 商户模块请求控制器。
 *
 * @author Ewing
 */
@Controller
@RequestMapping(value = "/merchant", produces = {"application/json;charset=UTF-8"})
@Api(value = "商户模块请求控制器", description = "商户模块的相关操作")
public class MerchantController {
    private final Logger logger = LoggerFactory.getLogger(MerchantController.class);

    private LoginSaltCache loginSaltCache;

    private MerchantSessionCache merchantSessionCache;

    private ShortMessageCache shortMessageCache;

    private MerchantService merchantService;

    private ChannelInfoService channelInfoService;

    @Autowired
    public void setLoginSaltCache(LoginSaltCache loginSaltCache) {
        this.loginSaltCache = loginSaltCache;
    }

    @Autowired
    public void setMerchantSessionCache(MerchantSessionCache merchantSessionCache) {
        this.merchantSessionCache = merchantSessionCache;
    }

    @Autowired
    public void setChannelInfoService(ChannelInfoService channelInfoService) {
        this.channelInfoService = channelInfoService;
    }

    @Autowired
    public void setMerchantService(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @Autowired
    public void setShortMessageCache(ShortMessageCache shortMessageCache) {
        this.shortMessageCache = shortMessageCache;
    }

    /**
     * 新增商户。
     */
    @ResponseBody
    @RequestMapping("/add")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_MERCHANT_MANAGE"})
    public ResultMessage addMerchant(Merchant merchant, HttpServletRequest request) {
        logger.info("商户控制器：新增商户：商户名称：" + merchant.getName());
        try {
            Admin adminLogined = (Admin) request.getSession().getAttribute(AppConfig.ADMIN_SESSION_KEY);
            merchantService.addMerchant(adminLogined, merchant);
            return ResultMessage.newSuccess("新增商户成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("新增商户异常：", e);
            return ResultMessage.newFailure("新增商户异常！");
        }
    }

    /**
     * 更新商户。
     */
    @ResponseBody
    @RequestMapping("/update")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_MERCHANT_MANAGE"})
    public ResultMessage updateMerchant(Merchant merchant, HttpServletRequest request) {
        logger.info("商户控制器：更新商户：商户名称：" + merchant.getName());
        try {
            Admin adminLogined = (Admin) request.getSession().getAttribute(AppConfig.ADMIN_SESSION_KEY);
            merchantService.updateMerchant(adminLogined, merchant);
            return ResultMessage.newSuccess("修改商户成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("修改商户异常：", e);
            return ResultMessage.newFailure("修改商户异常！");
        }
    }

    /**
     * 删除商户。
     */
    @ResponseBody
    @RequestMapping("/delete")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_MERCHANT_MANAGE"})
    public ResultMessage deleteMerchant(String merchantId, HttpServletRequest request) {
        logger.info("商户控制器：删除商户：商户id：" + merchantId);
        try {
            Admin adminLogined = (Admin) request.getSession().getAttribute(AppConfig.ADMIN_SESSION_KEY);
            merchantService.deleteMerchant(adminLogined, merchantId);
            return ResultMessage.newSuccess("删除商户成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("删除商户异常：", e);
            return ResultMessage.newFailure("删除商户异常！");
        }
    }

    /**
     * 查询商户。
     */
    @ResponseBody
    @RequestMapping("/query")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_MERCHANT_MANAGE"})
    public ResultMessage queryMerchants(PageParam param, String name, String phoneNo, HttpServletRequest request) {
        logger.info("商户控制器：查询商户：商户名称：" + name + "商家联系方式：" + phoneNo);
        try {
            Admin adminLogined = (Admin) request.getSession().getAttribute(AppConfig.ADMIN_SESSION_KEY);
            Page<Merchant> dataPage = merchantService.queryMerchants(adminLogined, name, phoneNo, param.newPageable());
            return ResultMessage.newSuccess("获取商户列表成功！").setData(dataPage);
        } catch (Exception e) {
            logger.error("查询数据异常：", e);
            return ResultMessage.newFailure("查询数据异常！");
        }
    }

    /**
     * 更新商户图片信息。
     */
    @ResponseBody
    @RequestMapping("/updateImage")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_USER_MERCHANT_MANAGE"})
    public ResultMessage addImage(String merchantId, @RequestParam("imageFile") MultipartFile imageFile) {
        logger.info("商户控制器：新增图片：商户ID：" + merchantId);
        try {
            Merchant merchant = merchantService.updateImage(merchantId, imageFile.getInputStream());
            return ResultMessage.newSuccess("更新商户图片成功！").setData(merchant);
        } catch (Exception e) {
            logger.error("更新商户图片异常：", e);
            return ResultMessage.newFailure("更新商户图片失败！");
        }
    }

    /**
     * 根据商户id获取所有的商品分类以及商品
     *
     * @param merchantId
     * @return
     * @author lid
     * @date 2017.3.1
     */
    @ResponseBody
    @RequestMapping("/getProductsByMerchant")
    public ResultMessage getProductsByMerchant(String merchantId) {
        logger.info("根据商户id获取所有的商品分类以及商品,商户ID:" + merchantId);
        try {
            Map<String, Object> mapResult = merchantService.getProductsByMerchant(merchantId);
            return ResultMessage.newSuccess().setData(mapResult);
        } catch (Exception e) {
            logger.error("获取所有的商品分类以及商品异常：", e);
            return ResultMessage.newFailure("获取所有的商品分类以及商品异常！");
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
                merchantSessionCache.remove(token);
            return ResultMessage.newSuccess();
        } catch (Exception e) {
            logger.error("商户令牌退出登陆异常：", e);
            return ResultMessage.newFailure("商户令牌退出登陆异常！");
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
            Merchant loginer = merchantSessionCache.get(token);
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
     * 商户登陆。
     *
     * @param phoneNo  商户用户名。
     * @param password 商户密码。
     */
    @ResponseBody
    @RequestMapping("/login")
    public ResultMessage merchantLogin(@RequestParam("phoneNo") String phoneNo,
                                       @RequestParam("password") String password, HttpServletRequest request) {
        logger.info("商户控制器：商户登陆，手机号：" + phoneNo + "，加密密码：" + password);
        try {
            // 获取加密盐。
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            if (!StringUtils.hasText(token))
                return ResultMessage.newFailure("验证令牌无效，请重试。");
            String tokenSalt = loginSaltCache.get(token + "tokenSalt");
            loginSaltCache.remove(token + "tokenSalt");

            //使用私钥解密字符串。
            password = RSAEncryptUtil.decryptString(password);
            // 取出加密盐与之前给客户端的盐比对。
            if (tokenSalt == null || password == null || password.length() < 10
                    || !password.substring(0, 10).equals(tokenSalt))
                return ResultMessage.newFailure("操作过期，请重试！");

            // 去掉附加盐才是真正的密码。
            password = password.substring(10);
            ResultMessage message = merchantService.loginByPhoneNo(phoneNo, password);
            // 登陆成功把token放入缓存，超时后自动清理。
            if (message.isSuccess()) {
                merchantSessionCache.put(token, message.getData());
            }
            return message;
        } catch (Exception e) {
            logger.error("商户登陆异常：", e);
            return ResultMessage.newFailure("商户登陆失败！");
        }
    }

    /**
     * 发送短信验证码。
     */
    @ResponseBody
    @RequestMapping("/sendShortMessage")
    public ResultMessage sendShortMessage(String phoneNo) {
        logger.info("商户控制器：发送短信验证码给：" + phoneNo);
        try {
            // 校验手机号是否有效。
            if (phoneNo == null || !phoneNo.matches(AppConfig.PHONE_NO_PATTEN))
                return ResultMessage.newFailure("该手机号码无效！");
            if (!merchantService.hasPhoneNo(phoneNo))
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
        logger.info("商户控制器：短信验证码登陆：号码：" + phoneNo + " 验证码：" + code);
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

            // 短信验证通过，获取商户信息。
            Merchant merchant = merchantService.findByPhoneNo(phoneNo);
            // 登陆成功把token放入缓存，超时后自动清理。
            merchantSessionCache.put(token, merchant);

            // 返回token和商户信息。
            return ResultMessage.newSuccess().setData(merchant);
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
        logger.info("商户控制器：短信验证码登陆：号码：" + phoneNo + " 验证码：" + code + " 新密码密文：" + newPassword);
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

            // 短信验证通过，获取商户信息。
            merchantService.changePasswordByPhoneNo(phoneNo, newPassword);
            return ResultMessage.newSuccess("密码修改成功！");
        } catch (Exception e) {
            logger.error("密码修改成功异常：", e);
            return ResultMessage.newFailure("密码修改成功失败。");
        }
    }

    /**
     * 查询车站的所有商户信息
     * 公众号用户调用
     *
     * @param stationId
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryMerchantByStation")
    public ResultMessage queryMerchantByStation(String stationId, String serviceType) {
        logger.info("商户控制器：queryMerchantByStation");
        try {
            List<Merchant> listMerchant = merchantService.findByStationIdAndServiceType(stationId, serviceType);
            return ResultMessage.newSuccess("获取当前车站商户列表成功！").setData(listMerchant);
        } catch (Exception e) {
            logger.error("获取当前车站商户列表异常：", e);
            return ResultMessage.newFailure("获取当前车站商户列表异常！");
        }
    }

    /**
     * 判断商家是否在线
     *
     * @param merchantId
     * @return
     */
    @ResponseBody
    @RequestMapping("/isMerchantOnline")
    public ResultMessage isMerchantOnline(String merchantId) {
        logger.info("商户控制器：isMerchantOnline!merchantId:" + merchantId);
        try {
            boolean isOnline = channelInfoService.hasMerchantOnline(merchantId);
            return ResultMessage.newSuccess("查询商户是否在线状态成功！").setData(isOnline);
        } catch (Exception e) {
            logger.error("查询商户是否在线状态异常：", e);
            return ResultMessage.newFailure("查询商户是否在线状态异常！");
        }
    }

    /**
     * 判断服务人员是否在线
     *
     * @param stationId
     * @param serviceType
     * @return
     */
    @ResponseBody
    @RequestMapping("/isServiceProviderOnline")
    public ResultMessage isServiceProviderOnline(String stationId, String serviceType) {
        logger.info("商户控制器：判断服务人员是否在线!车站ID:" + stationId + ",服务类型:" + serviceType);
        try {
            boolean isOnline = channelInfoService.hasServiceProviderOnline(stationId, serviceType);
            return ResultMessage.newSuccess("查询服务员是否在线状态成功！").setData(isOnline);
        } catch (Exception e) {
            logger.error("查询服务员是否在线状态异常：", e);
            return ResultMessage.newFailure("查询服务员是否在线状态异常！");
        }
    }
}
