package com.railwayservice.messages.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.messages.dao.CommentDao;
import com.railwayservice.messages.entity.Comment;
import com.railwayservice.messages.vo.CommentVo;
import com.railwayservice.order.entity.MainOrder;
import com.railwayservice.order.service.MainOrderService;
import com.railwayservice.order.service.OrderStatic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.Date;

@Service
public class CommentServiceImpl implements CommentService {

    private final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    private CommentDao commentDao;
    private MainOrderService mainOrderService;

    @Autowired
    public void setCommentDao(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    @Autowired
    public void setMainOrderService(MainOrderService mainOrderService) {
        this.mainOrderService = mainOrderService;
    }

    @Override
    @Transactional
    public Comment addComment(String userId, CommentVo commentVo) {
        if (!StringUtils.hasText(userId) || null == commentVo.getGrade() || !StringUtils.hasText(commentVo.getOrderId())) {
            throw new AppException("评价参数不完整！");
        }
        logger.info("新增留言!用户ID=" + userId + ",评分等级=" + commentVo.getGrade() + ",留言内容=" + commentVo.getContent() + ",订单ID=" + commentVo.getOrderId());

        MainOrder mainOrder = mainOrderService.findMainOrderByOrderId(commentVo.getOrderId());
        if (mainOrder == null || !userId.equals(mainOrder.getUserId()))
            throw new AppException("该订单不存在或不是您的哦。");

        if (mainOrder.getOrderStatus() != OrderStatic.MAINORDER_STATUS_COMPLETED
                && mainOrder.getOrderStatus() != OrderStatic.MAINORDER_STATUS_CANCELED)
            throw new AppException("您只能评价已完成或已取消的订单哦。");

        Comment comment = new Comment();
        comment.setContent(commentVo.getContent());
        comment.setGrade(commentVo.getGrade());
        comment.setOrderNo(mainOrder.getOrderNo());
        comment.setOrderId(mainOrder.getOrderId());
        comment.setOrderDate(mainOrder.getCreateDate());
        comment.setMerchantId(mainOrder.getMerchantId());
        comment.setUserId(userId);
        comment.setCreateDate(new Date());
        return commentDao.save(comment);
    }

    @Override
    @Transactional
    public Comment checkComment(String commentId) {

        if (!StringUtils.hasText(commentId)) {
            throw new AppException("commentId参数为空！");
        }
        logger.info("审核留言!留言ID=" + commentId);

        Comment comment = commentDao.findOne(commentId);
        if (null == comment) {
            throw new AppException("未找到留言信息！");
        }

        comment.setHasChecked(1);
        return commentDao.save(comment);
    }

    @Override
    public PageData listComment(PageParam pageParam, String merchantId) {
        if (!StringUtils.hasText(merchantId)) {
            throw new AppException("merchantId参数为空！");
        }
        logger.info("留言列表!商户ID=" + merchantId);
        Page<Comment> commentPage = commentDao.findCommentsByMerchantId(merchantId, pageParam.newPageable());
        return new PageData(commentPage);
    }

    @Override
    @Transactional
    public PageData listCheckedComment(PageParam pageParam, String merchantId) {
        // JPA标准查询接口，使用Lambda表达式。root以商户类为根对象。
        Specification<Comment> specification = (root, query, builder) -> {
            // 创建组合条件，默认1=1。
            Predicate predicate = builder.conjunction();
            // 如果名称不为空，添加到查询条件。
            predicate = builder.and(predicate, builder.equal(root.get("hasChecked"), 1));
            return predicate;
        };
        // 调用JPA标准查询接口查询数据。
        Page<Comment> commentPage = commentDao.findAll(specification, pageParam.newPageable());
        return new PageData(commentPage);
    }
}
