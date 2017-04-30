package com.railwayservice.grabticket.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.grabticket.dao.LineTicketDao;
import com.railwayservice.grabticket.entity.LineTicket;

@Service
public class LineTicketServiceImpl implements LineTicketService{
	
	private final Logger logger = LoggerFactory.getLogger(LineTicketServiceImpl.class);
	
	private LineTicketDao lineTicketDao;

	@Autowired
	public void setLineTicketDao(LineTicketDao lineTicketDao) {
		this.lineTicketDao = lineTicketDao;
	}

	@Override
	public LineTicket findByLineNoAndStation(String lineNo, String aboardStation, String arrivedStation) {
		logger.info("findByLineNoAndStation!");
		if(!StringUtils.hasText(lineNo)){
			logger.error("车次参数为空！");
			throw new AppException("车次参数为空！");
		}
		
		if(!StringUtils.hasText(aboardStation)){
			logger.error("出发车站参数为空！");
			throw new AppException("出发车站参数为空！");
		}
		
		if(!StringUtils.hasText(arrivedStation)){
			logger.error("到达车站参数为空！");
			throw new AppException("到达车站参数为空！");
		}
		
		return lineTicketDao.findByLineNoAndStation(lineNo, aboardStation, arrivedStation);
	}

}
