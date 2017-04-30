package com.railwayservice.merchantmanage.vo;

/**
 * 商家查看的评论信息。
 *
 * @author Ewing
 * @date 2017/3/14
 */
public class MerchantComment {
    /**
     * 评论ID
     */
    private String commentId;

    /**
     * 留言的用户
     */
    private String userId;

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
    
}
