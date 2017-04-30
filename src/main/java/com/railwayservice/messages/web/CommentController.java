package com.railwayservice.messages.web;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.messages.entity.Comment;
import com.railwayservice.messages.service.CommentService;
import com.railwayservice.messages.vo.CommentVo;
import com.railwayservice.order.entity.MainOrder;
import com.railwayservice.order.service.MainOrderService;
import com.railwayservice.user.entity.User;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 留言模块请求控制器。
 *
 * @author lid
 * @date 2017.2.4
 */
@Controller
@RequestMapping(value = "/comment", produces = {"application/json;charset=UTF-8"})
@Api(value = "留言模块请求控制器", description = "留言模块的相关操作")
public class CommentController {
    private final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private CommentService commentService;
    
    private MainOrderService mainOrderService;
    
    @Autowired
    public void setMainOrderService(MainOrderService mainOrderService) {
		this.mainOrderService = mainOrderService;
	}

	@Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @ResponseBody
    @RequestMapping("/add")
    public ResultMessage addComment(@RequestBody CommentVo commentVo, HttpServletRequest req, HttpServletResponse res) {
        logger.info("留言控制层：新增留言：商户ID：" + commentVo.getMerchantId() + " 留言内容：" + commentVo.getContent());
        try {
        	MainOrder order = mainOrderService.findMainOrderByOrderId(commentVo.getOrderId());
        	if(null == order){
            	return ResultMessage.newFailure("未找到订单信息");
            }
        	if(1 == order.getCommentFlag()){
        		return ResultMessage.newFailure("不能重复评论！");
        	}
        	
            User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);
            Comment comment = commentService.addComment(user.getUserId(), commentVo);
            //标识订单已被评论
            order.setCommentFlag(1);
            mainOrderService.updateMainOrder(order);
            return ResultMessage.newSuccess("留言成功！").setData(comment);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("留言异常：", e);
            return ResultMessage.newFailure("留言异常！");
        }
    }

    /**
     * 审核单条留言
     *
     * @param commentId
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkComment")
    public ResultMessage checkComment(String commentId) {
        logger.info("留言控制层：审核单条留言：留言ID：" + commentId);
        try {
            Comment comment = commentService.checkComment(commentId);
            return ResultMessage.newSuccess("审核留言成功！").setData(comment);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("审核留言异常：", e);
            return ResultMessage.newFailure("审核留言异常！");
        }
    }

    /**
     * 审核多条留言
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/checkComments")
    public ResultMessage checkComments(String[] commentsId) {
        if (0 == commentsId.length) {
            return ResultMessage.newFailure("留言信息为空");
        }
        logger.info("留言控制层：审核多条留言：留言ID个数：" + commentsId.length);
        try {
            for (int index = 0; index < commentsId.length; index++) {
                commentService.checkComment(commentsId[index]);
            }
            return ResultMessage.newSuccess("审核留言成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("审核留言异常：", e);
            return ResultMessage.newFailure("审核留言异常！");
        }
    }

    /**
     * 查询商户所有留言
     */
    @ResponseBody
    @RequestMapping("/queryComment")
    public ResultMessage queryComment(PageParam pageParam, String merchantId) {
        logger.info("留言控制层：查询商户所有留言：商户ID：" + merchantId);
        try {
            PageData pageData = commentService.listComment(pageParam, merchantId);
            return ResultMessage.newSuccess("查询商户留言成功！").setData(pageData);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("查询商户留言异常：", e);
            return ResultMessage.newFailure("查询商户留言异常！");
        }
    }

    /**
     * 查询商户所有已审核留言
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryCheckedComment")
    public ResultMessage queryCheckedComment(PageParam pageParam, String merchantId) {
        logger.info("留言控制层：查询商户所有已审核留言：商户ID：" + merchantId);
        try {
            PageData pageData = commentService.listCheckedComment(pageParam, merchantId);
            return ResultMessage.newSuccess("查询商户已审核留言成功！").setData(pageData);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("查询商户已审核留言异常：", e);
            return ResultMessage.newFailure("查询商户已审核留言异常！");
        }
    }
}
