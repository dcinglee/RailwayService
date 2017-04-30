package com.railwayservice.common.web;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.util.RSAEncryptUtil;
import com.railwayservice.application.util.RandomString;
import com.railwayservice.application.vo.LoginMessage;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.application.vo.RsaKeyData;
import com.railwayservice.common.entity.Dictionary;
import com.railwayservice.common.entity.ImageInfo;
import com.railwayservice.common.entity.Location;
import com.railwayservice.common.service.CommonService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务控制器。
 *
 * @author Ewing
 */
@Controller
@RequestMapping(value = "/common", produces = {"application/json;charset=UTF-8"})
@Api(value = "业务控制器", description = "业务控制器")
public class CommonController {
    private final Logger logger = LoggerFactory.getLogger(CommonController.class);

    private CommonService commonService;

    @Autowired
    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    /**
     * 登陆时获取公钥。
     */
    @ResponseBody
    @RequestMapping("/rsaKey")
    public ResultMessage getRsaKey() {
        logger.info("登陆前获取RsaKey");
        try {
            RsaKeyData rsaKeyData = RSAEncryptUtil.getPublicKeyData();
            return ResultMessage.newSuccess().setData(rsaKeyData);
        } catch (Exception e) {
            logger.error("获取公钥异常111：", e);
            return ResultMessage.newFailure("获取公钥失败！");
        }
    }

    /**
     * 登陆时获取随机盐。
     */
    @ResponseBody
    @RequestMapping("/sessionSalt")
    public ResultMessage getSessionSalt(HttpServletRequest request) {
        logger.info("登陆时获取随机盐");
        try {
            String salt = RandomString.randomString(10);
            request.getSession().setAttribute("sessionSalt", salt);
            return ResultMessage.newSuccess().setData(new LoginMessage(salt));
        } catch (Exception e) {
            logger.error("获取随机盐异常：", e);
            return ResultMessage.newFailure("获取随机盐异常！");
        }
    }

    /**
     * Session退出登陆。
     *
     * @return ResultMessage
     * @author xuy
     * @date 2017.2.14
     */
    @ResponseBody
    @RequestMapping("/sessionLogout")
    public ResultMessage sessionLogout(HttpServletRequest request) {
        logger.info("Session退出登陆：" + request.getRemoteAddr());
        try {
            request.getSession().removeAttribute(AppConfig.ADMIN_SESSION_KEY);
            return ResultMessage.newSuccess();
        } catch (Exception e) {
            logger.error("Session退出登陆异常：", e);
            return ResultMessage.newFailure("退出登陆异常！");
        }
    }

    /**
     * 根据字典类型获取字典值。
     */
    @ResponseBody
    @RequestMapping("/dictionary/{type}")
    public List<Dictionary> dictionary(@PathVariable("type") String type) {
        logger.info("根据字典类型获取字典值：");
        try {
            return commonService.findDictionaryByType(type);
        } catch (Exception e) {
            logger.error("获取数据字典异常：", e);
            return new ArrayList<>(0);
        }
    }

    /**
     * 根据上级位置获取下级位置列表。
     */
    @ResponseBody
    @RequestMapping("/location/{parent}")
    public List<Location> location(@PathVariable("parent") String parent) {
        logger.info("根据上级位置获取下级位置列表：");
        try {
            return commonService.findLocationByParentId(parent);
        } catch (Exception e) {
            logger.error("获取地理位置数据异常：", e);
            return new ArrayList<>(0);
        }
    }

    /**
     * 添加图片的方法。
     */
    @ResponseBody
    @RequestMapping("/addImage")
    public ResultMessage addImage(MultipartFile imageFile) {
        logger.info("添加图片的方法：");
        try {
            ImageInfo img = commonService.addImage(imageFile.getInputStream());
            return ResultMessage.newSuccess("添加图片成功！").setData(img);
        } catch (Exception e) {
            logger.error("添加图片异常：", e);
            return ResultMessage.newFailure("添加图片失败！");
        }
    }

    /**
     * 根据图片ID查询图片。
     */
    @ResponseBody
    @RequestMapping("/getImageUrl")
    public ResultMessage getImageUrl(String imageId) {
        logger.info("根据图片ID查询图片：");
        try {
            String url = commonService.getImageUrl(imageId);
            return ResultMessage.newSuccess().setData(url);
        } catch (Exception e) {
            logger.error("查询图片异常：", e);
            return ResultMessage.newFailure("查询图片失败！");
        }
    }

}
