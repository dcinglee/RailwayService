package com.railwayservice.messages.web;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.messages.entity.Notice;
import com.railwayservice.messages.service.NoticeService;
import com.railwayservice.user.entity.User;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 通知模块请求控制器。
 *
 * @author lid
 */
@Controller
@RequestMapping(value = "/notice", produces = {"application/json;charset=UTF-8"})
@Api(value = "通知模块请求控制器", description = "通知模块的相关操作")
public class NoticeController {
    private final Logger logger = LoggerFactory.getLogger(NoticeController.class);

    private NoticeService noticeService;

    @Autowired
    public void setNoticeService(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    /**
     * 新增通知。
     */
    @ResponseBody
    @RequestMapping("/add")
    public ResultMessage addNotice(Notice notice) {
        logger.info("通知控制层：添加通知：通知内容：" + notice.getContent());
        try {
            noticeService.addNotice(notice);
            return ResultMessage.newSuccess("新增通知成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("新增通知异常：", e);
            return ResultMessage.newFailure("新增通知异常！");
        }
    }

    /**
     * 查询通知。
     */
    @ResponseBody
    @RequestMapping("/query")
    public ResultMessage queryNotice(HttpServletRequest request) {
        logger.info("通知控制层：查询通知：");
        try {
            User user = (User) request.getSession().getAttribute(AppConfig.USER_SESSION_KEY);
            List<Notice> listNotice = noticeService.getNoticeByReceiverId(user.getUserId());
            return ResultMessage.newSuccess().setData(listNotice);
        } catch (Exception e) {
            logger.error("查询通知异常：", e);
            return ResultMessage.newFailure("查询通知异常！");
        }
    }

    /**
     * 清空通知。
     */
    @ResponseBody
    @RequestMapping("/clean")
    public ResultMessage cleanNotice(HttpServletRequest request) {
        logger.info("通知控制层：清空通知：");
        try {
            User user = (User) request.getSession().getAttribute(AppConfig.USER_SESSION_KEY);
            noticeService.deleteNoticeByReceiverId(user.getUserId());
            return ResultMessage.newSuccess("清空通知成功！");
        } catch (Exception e) {
            logger.error("清空通知异常：", e);
            return ResultMessage.newFailure("清空通知异常！");
        }
    }

}
