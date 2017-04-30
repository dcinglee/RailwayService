package com.railwayservice.messages.web;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.messages.entity.Message;
import com.railwayservice.messages.service.MessageService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 消息模块请求控制器。
 *
 * @author Ewing
 */
@Controller
@RequestMapping(value = "/message", produces = {"application/json;charset=UTF-8"})
@Api(value = "消息模块请求控制器", description = "消息模块的相关操作")
public class MessageController {
    private final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private MessageService messageService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 新增消息。
     */
    @ResponseBody
    @RequestMapping("/add")
    public ResultMessage addMessage(Message message) {
        logger.info("消息控制层：新增消息：");
        try {
            messageService.addMessage(message);
            return ResultMessage.newSuccess("新增消息成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("新增消息异常：", e);
            return ResultMessage.newFailure("新增消息异常！");
        }
    }

    /**
     * 更新消息。
     */
    @ResponseBody
    @RequestMapping("/update")
    public ResultMessage updateMessage(Message message) {
        logger.info("消息控制层：更新消息：");
        try {
            messageService.updateMessage(message);
            return ResultMessage.newSuccess("修改消息成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("修改消息异常：", e);
            return ResultMessage.newFailure("修改消息异常！");
        }
    }

    /**
     * 删除消息。
     */
    @ResponseBody
    @RequestMapping("/delete")
    public ResultMessage deleteMessage(String messageId) {
        logger.info("消息控制层：删除消息：");
        try {
            messageService.deleteMessage(messageId);
            return ResultMessage.newSuccess("删除消息成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("删除消息异常：", e);
            return ResultMessage.newFailure("删除消息异常！");
        }
    }

    /**
     * 查询消息。
     */
    @ResponseBody
    @RequestMapping("/query")
    public ResultMessage queryMessages(PageParam param, String name) {
        logger.info("消息控制层：查询消息：");
        try {
            Page dataPage = messageService.queryMessages(name, param.newPageable());
            return ResultMessage.newSuccess().setData(new PageData(dataPage));
        } catch (Exception e) {
            logger.error("查询数据异常：", e);
            return ResultMessage.newFailure("查询数据异常！");
        }
    }

}
