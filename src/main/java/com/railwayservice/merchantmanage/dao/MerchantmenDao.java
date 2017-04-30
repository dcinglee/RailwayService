package com.railwayservice.merchantmanage.dao;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.merchantmanage.vo.MerchantAchievement;
import com.railwayservice.merchantmanage.vo.MerchantMainOrder;
import com.railwayservice.merchantmanage.vo.Merchantmen;
import com.railwayservice.messages.vo.OrderNotice;

import java.util.Date;
import java.util.List;

/**
 * 商户数据库访问接口。
 *
 * @author Ewing
 */
public interface MerchantmenDao {
    /**
     * 商户查询待处理的订单。
     *
     * @param merchantId 商户对象ID。
     * @return 待处理的订单。
     */
    List<MerchantMainOrder> queryWaitDealOrders(String merchantId);

    /**
     * 商家查询待取货的订单
     *
     * @param merchantId
     * @return
     */
    public List<MerchantMainOrder> queryWaitUserOrders(String merchantId);

    /**
     * 商家查询历史的订单。
     *
     * @param merchantId  商家ID。
     * @param startDate   开始时间。
     * @param endDate     结束时间。
     * @param orderStatus 订单状态。
     * @return 历史订单。
     */
    List<MerchantMainOrder> queryHistoryOrders(String merchantId, Date startDate, Date endDate, Integer deliverType, Integer... orderStatus);

    /**
     * 商户统计自己的总的营业额。
     */
    MerchantAchievement queryTotalAchievement(String merchantId);

    /**
     * 商户按天统计自己的营业额。
     */
    PageData queryDailyAchievement(PageParam pageParam, String merchantId);

    /**
     * 商户获取自己的资料信息。
     */
    Merchantmen getMerchantInfo(String merchantId);

}
