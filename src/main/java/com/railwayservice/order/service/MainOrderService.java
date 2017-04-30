package com.railwayservice.order.service;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.order.entity.MainOrder;
import com.railwayservice.order.vo.OrderParamVo;
import com.railwayservice.order.vo.OrderQuartzVo;
import com.railwayservice.order.vo.ProductInputParamVo;
import com.railwayservice.order.vo.QueryOrderParam;
import com.railwayservice.order.vo.UserOrdersVo;
import com.railwayservice.stationmanage.entity.RailwayStation;
import com.railwayservice.user.entity.User;
import com.railwayserviceWX.vo.BrandWCPayParameterVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author lidx
 * @date 2017年2月8日
 * @describe 订单信息服务类接口
 */
public interface MainOrderService {

    /**
     * 主订单查询
     *
     * @param param
     * @return MainOrder
     * @author lidx
     * @date 2017.2.8
     */
    PageData findMainOrders(PageParam pageParam, QueryOrderParam param);

    /**
     * 确认订单
     *
     * @return Map
     * @author lid
     * @date 2017.3.7
     */
    Map<String, Object> affirmOrder(User user, RailwayStation presentStation, String merchantId);

    /**
     * /**
     * 用户确认支付下订单。
     * 处理逻辑如下：
     * 1，获取参数值，生成订单；
     * 2，调用微信支付；
     * 3，通知商户(支付成功回调函数中通知商户)
     *
     * @return MainOrder
     * @author lid
     * @date 2017.2.20
     */
    BrandWCPayParameterVo createOrder(OrderParamVo vo, User user, String addrip);

    /**
     * 取消主订单
     *
     * @return MainOrder
     * @author lid
     * @date 2017.2.14
     */
    MainOrder cancelMainOrder(String mainOrderId, String reason);

    /**
     * 通过orderId查询主订单
     *
     * @param orderId 订单id
     * @return MainOrder
     */
    MainOrder findMainOrderByOrderId(String orderId);

    /**
     * 用户调用获取订单详情
     *
     * @param orderId
     * @return Map
     * @author lid
     * @date 2017.3.6
     */
    Map<String, Object> queryOrdersByOrderId(String orderId);

    /**
     * 使主订单失效，供需要的时候批量清理失效订单
     *
     * @author lid
     * @date 2017.2.10
     */
    MainOrder expireMainOrder(String mainOrderId);

    /**
     * 根据用户的userId以及起始时间查找对应的订单
     */
    List<MainOrder> queryOrdersByUser(String userId, Date startDate, Date endDate);

    /**
     * 根据产品id返回商品并计算总价
     *
     * @param listProductParamVo
     * @return Map
     * @author lid
     * @date 2017.3.1
     */
    Map<String, Object> getProductInOrder(List<ProductInputParamVo> listProductParamVo);

    /**
     * 根据用户信息查询订单，获取vo类列表
     *
     * @param userId
     * @return List<UserOrdersVo>
     */
    List<UserOrdersVo> getOrdersByUser(String userId);

    /**
     * 更新主订单。
     *
     * @param mainOrder 主订单。
     */
    MainOrder updateMainOrder(MainOrder mainOrder);

    /**
     * 订单退款接口
     *
     * @param mainOrder
     * @return
     * @author lid
     * @date 2017.3.17
     */
    void refundOrder(MainOrder mainOrder);
    
    /**
     * 根据订单状态查找订单
     * @param mainOrderStatus
     * @return List<MainOrder>
     * @author lid
     * @date 2017.3.31
     */
    List<OrderQuartzVo> queryMainOrderByStatus(Integer mainOrderStatus);

}

