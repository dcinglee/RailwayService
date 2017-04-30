package com.railwayservice.grabticket.service;

import com.railwayservice.grabticket.dao.RailwayTicketDao;
import com.railwayservice.grabticket.entity.RailwayTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 车票服务类。
 *
 * @author Ewing
 */
@Service
public class RailwayTicketServiceImpl implements RailwayTicketService {
    private final Logger logger = LoggerFactory.getLogger(RailwayTicketServiceImpl.class);

    private RailwayTicketDao railwayTicketDao;

    @Autowired
    public void setRailwayTicketDao(RailwayTicketDao railwayTicketDao) {
        this.railwayTicketDao = railwayTicketDao;
    }

	@Override
	public RailwayTicket addRailwayTicket(RailwayTicket railwayTicket) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RailwayTicket updateRailwayTicket(RailwayTicket railwayTicket) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<RailwayTicket> queryRailwayTickets(String name, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRailwayTicket(String railwayTicketId) {
		// TODO Auto-generated method stub
		
	}

}
