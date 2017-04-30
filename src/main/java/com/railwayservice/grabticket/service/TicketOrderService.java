package com.railwayservice.grabticket.service;

import java.util.List;
import java.util.Map;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.grabticket.entity.TicketOrder;
import com.railwayservice.grabticket.vo.GrabOrderVo;
import com.railwayservice.grabticket.vo.TicketPassengerVo;

/**
 * 车票订单服务接口
 *
 * @author lid
 * @date 2017.4.10
 */
public interface TicketOrderService {

    /**
     * 根据用户id查找历史订单
     *
     * @param userId
     * @return
     */
    List<TicketOrder> getHistoryOrders(String userId);

    /**
     * 根据用户id查找历史行程
     *
     * @param userId
     * @return
     */
    List<TicketOrder> getHistoryTravels(String userId);

    /**
     * 删除历史记录
     *
     * @param orderId
     */
    void deleteHistoryOrders(String userId);

    /**
     * 删除历史记录
     *
     * @param userId
     */
    void deleteOrder(String userId);

    /**
     * 查询所有的订单记录
     *
     * @return
     */
    List<TicketOrder> findAllTicketOrder();

    /**
     * 查询单个订单
     *
     * @param ticketOrderId
     * @return
     */
    Map<String, Object> findOneTicketOrder(String ticketOrderId);

    /**
     * 将订单改为取消状态
     *
     * @param ticketOrderId
     * @return
     */
    TicketOrder changeTicketOrderToCancle(String ticketOrderId);

    /**
     * 将订单改为停止抢票状态
     *
     * @param ticketOrderId
     * @return
     */
    TicketOrder changeTicketOrderToStop(String ticketOrderId);

    TicketOrder updateTicketOrderStatus(String ticketOrderId, Integer orderStatus);

    /**
     * 生成订票订单
     *
     * @param map
     * @param user
     * @return
     */
    TicketOrder createBookOrder(Map<String, Object> map);

    /**
     * @param vo
     * @return TicketOrder
     * @author lid
     */
    TicketOrder createGrabOrder(GrabOrderVo vo, String userId);

    /**
     * @return list
     * @author lid
     */
    List<TicketOrder> getGrabOrders(String all);

    /**
     * 获取当前车票信息
     *
     * @param ticketOrderId
     * @return
     */
    Map<String, Object> getBookOrder(String ticketOrderId);
	
	/**
	 * 查询车票订单详情
	 * @param ticketPassengerVo
	 * @return
	 */
	PageData queryTicketOrderDetail(PageParam pageParam,TicketPassengerVo ticketPassengerVo);
	
	List<TicketPassengerVo> findTicketOrder(TicketPassengerVo ticketPassengerVo);
	
	/**
	 * 抢票结果通知
	 * @param map
	 * @author lid
	 * @date 2017.4.24
	 */
	void grabOrderResultNotice(Map<String, Object> map);
}
