package com.railwayservice.serviceprovider.vo;

/**
 * 统计订单数量。
 *
 * @author Ewing
 * @date 2017/3/7
 */
public class CountOrdersVO {

    private String serviceProviderId;

    private Integer status;
    private long completeOrdersToday;

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public long getCompleteOrdersToday() {
        return completeOrdersToday;
    }

    public void setCompleteOrdersToday(long completeOrdersToday) {
        this.completeOrdersToday = completeOrdersToday;
    }
}
