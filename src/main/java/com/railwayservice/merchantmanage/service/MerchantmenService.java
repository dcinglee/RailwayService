package com.railwayservice.merchantmanage.service;

import com.railwayservice.application.vo.PageParam;
import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.merchantmanage.vo.MerchantAchievement;
import com.railwayservice.merchantmanage.vo.MerchantMainOrder;
import com.railwayservice.merchantmanage.vo.Merchantmen;
import com.railwayservice.order.entity.MainOrder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 商户服务接口。
 *
 * @author Ewing
 */
public interface MerchantmenService {

    /**
     * 商户查询待处理的订单。
     *
     * @param merchant 商户对象。
     * @return 待处理的订单。
     */
    List<MerchantMainOrder> queryWaitDealOrders(Merchant merchant);

    /**
     * 商户查询待取货的订单
     *
     * @param merchant
     * @return
     */
    public List<MerchantMainOrder> queryWaitUserOrders(Merchant merchant);

    /**
     * 商户处理自己的订单为新的状态。
     *
     * @param merchant  商户对象。
     * @param orderId   订单ID。
     * @param newStatus 处理为新的状态。
     * @param reason
     */
    void dealOrderByStatus(Merchant merchant, String orderId, Integer newStatus, String reason);

    /**
     * 商家查询历史的订单。
     *
     * @param merchant    商家。
     * @param startDate   开始时间。
     * @param endDate     结束时间。
     * @param orderStatus 订单状态。
     * @return 历史订单。
     */
    List<MerchantMainOrder> queryHistoryOrders(Merchant merchant, Date startDate, Date endDate, Integer orderStatus);

    /**
     * 商户更新自己的状态：营业中、休息中等。
     *
     * @param merchant 商户对象。
     * @param status   新的状态。
     */
    void updateMerchantStatus(Merchant merchant, Integer status);

    /**
     * 商户统计自己的营业额。
     */
    MerchantAchievement achievement(PageParam pageParam, Merchant merchant);

    /**
     * 商户更新自己的信息。
     */
    void updateMerchant(Merchant merchantmen, Merchant merchant);

    /**
     * 商户获取自己的资料信息。
     */
    Merchantmen getMerchantInfo(Merchant merchant);

}
