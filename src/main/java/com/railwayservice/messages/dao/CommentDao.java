package com.railwayservice.messages.dao;

import com.railwayservice.messages.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 留言信息数据库访问接口
 *
 * @author lid
 * @date 2017.3.6
 */

public interface CommentDao extends JpaRepository<Comment, String>, JpaSpecificationExecutor<Comment> {

    /**
     * 查找商户的所有留言
     *
     * @param merchantId
     * @param pageable
     * @return
     */
    Page<Comment> findCommentsByMerchantId(String merchantId, Pageable pageable);
}
