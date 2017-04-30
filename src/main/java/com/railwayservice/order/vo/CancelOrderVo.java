package com.railwayservice.order.vo;

/**
 * 取消订单vo类
 *
 * @author lid
 * @date 2017.3.6
 */
public class CancelOrderVo {
    /**
     * 订单号
     */
    private String mainOrderId;

    /**
     * 取消原因
     */
    private String reason;

    public String getMainOrderId() {
        return mainOrderId;
    }

    public void setMainOrderId(String mainOrderId) {
        this.mainOrderId = mainOrderId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}
