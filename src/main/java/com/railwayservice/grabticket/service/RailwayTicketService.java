package com.railwayservice.grabticket.service;

import com.railwayservice.grabticket.entity.RailwayTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 车票服务类。
 *
 * @author Ewing
 */
public interface RailwayTicketService {

    RailwayTicket addRailwayTicket(RailwayTicket railwayTicket);

    RailwayTicket updateRailwayTicket(RailwayTicket railwayTicket);

    Page<RailwayTicket> queryRailwayTickets(String name, Pageable pageable);

    void deleteRailwayTicket(String railwayTicketId);

}
