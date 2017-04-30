package com.railwayservice.grabticket.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.common.dao.BaseDaoImpl;
import com.railwayservice.grabticket.vo.TicketPassengerVo;


/**
 * 车票订单详情Dao实现类
 * @author lixs
 *
 */
@Repository
public class TicketOrderDaoImpl extends BaseDaoImpl{
	
	public	PageData findTicketOrderDetail(PageParam pageParam,TicketPassengerVo ticketPassengerVo ){
		List<Object> params = new ArrayList<>();
	    StringBuilder sqlBuilder = new StringBuilder("select t.ticketOrderId, t.createDate, t.orderType,p.passengerName, t.noticePhoneNo, t.aboardStation, t.arrivedStation, t.ticketNumber, t.orderStatus"
	        + " " +
	        " FROM TicketOrder t , Passenger p " +
	        " where FIND_IN_SET(p.identityCardNo,passengerIds)"
	    );

	    //处理参数，动态生成SQL。
	    if (StringUtils.hasText(ticketPassengerVo.getPassengerName())) {
	        sqlBuilder.append(" and p.passengerName like ?");
	        params.add("%" +ticketPassengerVo.getPassengerName()+ "%");
	    }

	    if (StringUtils.hasText(ticketPassengerVo.getNoticePhoneNo())) {
	        sqlBuilder.append(" and t.noticePhoneNo like ?");
	        params.add("%" +ticketPassengerVo.getNoticePhoneNo()+ "%");
	    }

	    if (ticketPassengerVo.getOrderType() !=0) {
	        sqlBuilder.append(" and t.orderType = ?");
	        params.add(ticketPassengerVo.getOrderType());
	    }
        if (ticketPassengerVo.getOrderStatus()!=0) {
            sqlBuilder.append(" and t.orderStatus = ?");
	        params.add(ticketPassengerVo.getOrderStatus());
	    }

	    if (ticketPassengerVo.getStartTime() != null) {
	        sqlBuilder.append(" AND t.createDate > ?");
	        params.add(ticketPassengerVo.getStartTime());
	    }
	    if (ticketPassengerVo.getEndTime() != null) {
	        sqlBuilder.append(" AND t.createDate < ?");
	        params.add(ticketPassengerVo.getEndTime());
	    }
	    
	    sqlBuilder.append(" order by t.createDate");
	    return this.findPageMap(pageParam, sqlBuilder.toString(), params.toArray());
	}

	public List<TicketPassengerVo> findTicketOrder(TicketPassengerVo ticketPassengerVo) {
		List<Object> params = new ArrayList<>();
	
	    StringBuilder sqlBuilder = new StringBuilder("select t.ticketOrderId, t.createDate, t.orderType,p.passengerName, t.noticePhoneNo, t.aboardStation, t.arrivedStation, t.ticketNumber, t.orderStatus"
	            + " " +
	            " FROM TicketOrder t , Passenger p " +
	            " where FIND_IN_SET(p.identityCardNo,passengerIds)"
	    );
	
	    //处理参数，动态生成SQL。
	    if (StringUtils.hasText(ticketPassengerVo.getPassengerName())) {
	        sqlBuilder.append(" and p.passengerName like ?");
	        params.add("%" +ticketPassengerVo.getPassengerName()+ "%");
	    }
	
	    if (StringUtils.hasText(ticketPassengerVo.getNoticePhoneNo())) {
	        sqlBuilder.append(" and t.noticePhoneNo like ?");
	        params.add("%" +ticketPassengerVo.getNoticePhoneNo()+ "%");
	    }
	
	    if (ticketPassengerVo.getOrderType() !=0) {
	        sqlBuilder.append(" and t.orderType = ?");
	        params.add(ticketPassengerVo.getOrderType());
	    }
	
	    if (ticketPassengerVo.getOrderStatus()!=0) {
	        sqlBuilder.append(" and t.orderStatus = ?");
	        params.add(ticketPassengerVo.getOrderStatus());
	    }
	
	    if (ticketPassengerVo.getStartTime() != null) {
	        sqlBuilder.append(" AND t.createDate > ?");
	        params.add(ticketPassengerVo.getStartTime());
	    }
	    if (ticketPassengerVo.getEndTime() != null) {
	        sqlBuilder.append(" AND t.createDate < ?");
	        params.add(ticketPassengerVo.getEndTime());
	    }
	  
	    sqlBuilder.append(" order by t.createDate");
	    return this.getOperations().query(sqlBuilder.toString(), BeanPropertyRowMapper.newInstance(TicketPassengerVo.class), params.toArray());
	}
}


