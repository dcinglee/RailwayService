package com.railwayservice.grabticket.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.grabticket.entity.TicketOrder;
import com.railwayservice.grabticket.vo.TicketPassengerVo;


public interface TicketOrderDao extends JpaRepository<TicketOrder, String>, JpaSpecificationExecutor<TicketOrder> {

	/**
	 * 查询历史车次
	 * @param userId
	 * @return
	 */
	List<TicketOrder> findByUserIdOrderByCreateDateDesc(String userId);
	
	/**
	 * 查询历史订单
	 * 
	 * @param userId
	 * @return
	 */
//	@Query(nativeQuery = true,value="select * from TicketOrder " +
//            " where isDelete = 0")
	List<TicketOrder> findByUserIdOrderByCreateDateAsc(String userId);
	
	TicketOrder findByIsHistoryDelete(int isHistoryDelete);
	
	/**
	 * 根据订单状态查找订单列表
	 * @param orderStatus
	 * @return List<TicketOrder>
	 * @author lid
	 */
	List<TicketOrder> findByOrderStatus(Integer orderStatus);
	
	/**
	 * 根据orderId查找订单
	 * @param orderId
	 * @return TicketOrder
	 * @author lid
	 * @date 2017.4.24
	 */
	TicketOrder findByOrderId(String orderId);
	
	/**
	 * 分页查询订单详情
	 * @param ticketPassengerVo
	 * @return
	 */
	PageData findTicketOrderDetail(PageParam pageParam,TicketPassengerVo ticketPassengerVo );
	
	/**
	 * 查询订单详情，供导出excel使用
	 * @param ticketPassengerVo
	 * @return
	 */
	List<TicketPassengerVo> findTicketOrder(TicketPassengerVo ticketPassengerVo);
}
