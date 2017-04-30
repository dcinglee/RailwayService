package com.railwayservice.grabticket.service;

import com.railwayservice.grabticket.entity.LineTicket;

/**
 * @author lid
 */
public interface LineTicketService {
	/**
	 * 根据车次以及出发车站、到达车站查询票价情况
	 * @param lineNo
	 * @param aboardStation
	 * @param arrivedStation
	 * @return LineTicket
	 * @author lid
	 */
	LineTicket findByLineNoAndStation(String lineNo, String aboardStation, String arrivedStation);
}
