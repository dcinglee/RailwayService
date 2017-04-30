package com.railwayservice.grabticket.dao;

import com.railwayservice.grabticket.entity.RailwayTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 车票数据库访问接口。
 * 该接口使用了Spring Data JPA提供的方法。
 *
 * @author Ewing
 */
public interface RailwayTicketDao extends JpaRepository<RailwayTicket, String>, JpaSpecificationExecutor<RailwayTicket> {

    List<RailwayTicket> findByTicketOrderId(String ticketOrderId);
}
