package com.railwayservice.messages.vo;

/**
 * 留言vo类
 *
 * @author lid
 * @date 2017.3.6
 */
public class CommentVo {

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 评论等级
     */
    private Integer grade;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 商户id
     */
    private String merchantId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

}
