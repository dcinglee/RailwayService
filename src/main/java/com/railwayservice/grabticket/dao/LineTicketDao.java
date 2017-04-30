package com.railwayservice.grabticket.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.railwayservice.grabticket.entity.LineTicket;

/**
 * 爬虫爬取的车次票价数据库接口
 * @author lid
 * @date 2017.4.19
 */
public interface LineTicketDao extends JpaRepository<LineTicket, String>, JpaSpecificationExecutor<LineTicket> {
	/**
	 * 根据车次以及出发车站、到达车站查询票价情况
	 * @param lineNo
	 * @param aboardStation
	 * @param arrivedStation
	 * @return LineTicket
	 * @author lid
	 */
	@Query("select l from LineTicket l where l.lineNo like %:lineNo% and l.aboardStation like :aboardStation and l.arrivedStation like :arrivedStation")
	LineTicket findByLineNoAndStation(@Param("lineNo")String lineNo, @Param("aboardStation")String aboardStation, @Param("arrivedStation")String arrivedStation);
}
