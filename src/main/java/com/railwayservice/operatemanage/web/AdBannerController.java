package com.railwayservice.operatemanage.web;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.operatemanage.entity.AdBanner;
import com.railwayservice.operatemanage.service.AdBannerService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 广告模块请求控制器。
 *
 * @author lidx
 * @date 2017.3.10
 */
@Api(value = "广告模块请求控制器", description = "广告模块请求控制器")
@Controller
@RequestMapping("/AdBanner")
public class AdBannerController {
    private final Logger logger = LoggerFactory.getLogger(AdBannerController.class);

    private AdBannerService adBannerService;

    @Autowired
    public void setAdBannerService(AdBannerService adBannerService) {
        this.adBannerService = adBannerService;
    }

    /**
     * 删除广告
     */
    @ResponseBody
    @RequestMapping("/delete")
    public ResultMessage deleteAdBanner(String adBannerId, HttpServletRequest request) {
        logger.info("广告控制层：删除广告：广告ID：" + adBannerId);
        try {
            Admin adminLogined = (Admin) request.getSession().getAttribute(AppConfig.ADMIN_SESSION_KEY);
            adBannerService.deleteAdBanner(adminLogined, adBannerId);
            return ResultMessage.newSuccess("删除广告成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("删除广告异常：", e);
            return ResultMessage.newFailure("删除广告异常！");
        }
    }

    /**
     * 修改广告信息
     */
    @ResponseBody
    @RequestMapping("/update")
    public ResultMessage updateAdBanner(AdBanner adBanner, HttpServletRequest request) {
        logger.info("广告控制层：更新广告：广告内容：" + adBanner.getContent());
        try {
            Admin adminLogined = (Admin) request.getSession().getAttribute(AppConfig.ADMIN_SESSION_KEY);
            adBannerService.updateAdBanner(adminLogined, adBanner);
            return ResultMessage.newSuccess("修改广告成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("修改广告异常：", e);
            return ResultMessage.newFailure("修改广告异常！");
        }
    }

    /**
     * 新增广告。
     */
    @ResponseBody
    @RequestMapping("/add")
    public ResultMessage addAdBanner(AdBanner adBanner, HttpServletRequest request) {
        logger.info("广告控制层：添加广告：广告内容：" + adBanner.getContent());
        try {
            Admin adminLogined = (Admin) request.getSession().getAttribute(AppConfig.ADMIN_SESSION_KEY);
            adBannerService.addAdBanner(adminLogined, adBanner);
            return ResultMessage.newSuccess("新增广告成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("新增广告异常：", e);
            return ResultMessage.newFailure("新增广告异常！");
        }
    }

    /**
     * 查询广告。
     */
    @ResponseBody
    @RequestMapping("/query")
    public ResultMessage queryAdBanners(PageParam param, String title) {
        logger.info("广告控制层：查询广告：广告标题：" + title);
        try {
//            Admin adminLogined = (Admin) request.getSession().getAttribute(AppConfig.ADMIN_SESSION_KEY);
            PageData dataPage = adBannerService.queryAdBanners(param, title);
            return ResultMessage.newSuccess("获取广告列表成功！").setData(dataPage);
        } catch (Exception e) {
            logger.error("查询数据异常：", e);
            return ResultMessage.newFailure("查询数据异常！");
        }
    }

    @ResponseBody
    @RequestMapping("/queryAdBanner")
    public ResultMessage queryAdBanner() {
        logger.info("广告控制层：查询广告：查询广告列表：");
        try {
//            Admin adminLogined = (Admin) request.getSession().getAttribute(AppConfig.ADMIN_SESSION_KEY);
            List<AdBanner> dataPage = adBannerService.queryAdBanner4WX();
            return ResultMessage.newSuccess("获取广告列表成功！").setData(dataPage);
        } catch (Exception e) {
            logger.error("查询数据异常：", e);
            return ResultMessage.newFailure("查询数据异常！");
        }
    }

    /**
     * 更新添加广告图片信息。
     */
    @ResponseBody
    @RequestMapping("/updateImage")
    public ResultMessage addImage(String adBannerId, @RequestParam("imageFile") MultipartFile imageFile) {
        logger.info("广告控制层：新增图片：广告ID：" + adBannerId);
        try {
            AdBanner adBanner = adBannerService.updateImage(adBannerId, imageFile.getInputStream());
            return ResultMessage.newSuccess("更新广告图片成功！").setData(adBanner);
        } catch (Exception e) {
            logger.error("更新广告图片异常：", e);
            e.printStackTrace();
            return ResultMessage.newFailure("更新广告图片失败！");
        }
    }

}
