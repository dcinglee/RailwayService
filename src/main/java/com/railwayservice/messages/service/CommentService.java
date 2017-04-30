package com.railwayservice.messages.service;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.messages.entity.Comment;
import com.railwayservice.messages.vo.CommentVo;

/**
 * 用户留言服务接口
 *
 * @author lid
 * @date 2017.3.6
 */
public interface CommentService {
    /**
     * 用户对商户进行留言评论
     *
     * @param userId
     * @param commentVo
     * @return Comment
     */
    Comment addComment(String userId, CommentVo commentVo);

    /**
     * 审核留言
     *
     * @param commentId
     * @return
     */
    Comment checkComment(String commentId);

    /**
     * 查找商户的所有留言
     *
     * @param pageParam
     * @param merchantId
     * @return
     */
    PageData listComment(PageParam pageParam, String merchantId);

    /**
     * 查找商户的所有已审核通过的留言
     *
     * @param merchantId
     * @return
     */
    PageData listCheckedComment(PageParam pageParam, String merchantId);
}
