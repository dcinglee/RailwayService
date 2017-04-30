package com.railwayservice.grabticket.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.util.OrderUtil;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.grabticket.dao.PassengerDao;
import com.railwayservice.grabticket.dao.RailwayTicketDao;
import com.railwayservice.grabticket.dao.TicketOrderDao;
import com.railwayservice.grabticket.entity.Passenger;
import com.railwayservice.grabticket.entity.RailwayTicket;
import com.railwayservice.grabticket.entity.TicketOrder;
import com.railwayservice.grabticket.vo.GrabOrderVo;
import com.railwayservice.grabticket.vo.TicketPassengerVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

@Service
public class TicketOrderServiceImpl implements TicketOrderService {

    private final Logger logger = LoggerFactory.getLogger(TicketOrderServiceImpl.class);

    private TicketOrderDao ticketOrderDao;

    private RailwayTicketDao railwayTicketDao;
    
    private PassengerDao passengerDao;
    
    @Autowired
    public void setPassengerDao(PassengerDao passengerDao) {
		this.passengerDao = passengerDao;
	}

	@Autowired
    public void setRailwayTicketDao(RailwayTicketDao railwayTicketDao) {
        this.railwayTicketDao = railwayTicketDao;
    }

    @Autowired
    public void setTicketOrderDao(TicketOrderDao ticketOrderDao) {
        this.ticketOrderDao = ticketOrderDao;
    }

    @Override
    @Transactional
    public List<TicketOrder> getHistoryTravels(String userId) {
        if (!StringUtils.hasText(userId)) {
            logger.error("userId参数为空！");
            throw new AppException("userId参数为空！");
        }

        logger.info("getHistoryOrders!userId:" + userId);

        List<TicketOrder> ticketOrder = ticketOrderDao.findByUserIdOrderByCreateDateDesc(userId);
        if (ticketOrder == null) {
            logger.error("历史车次为空！");
            throw new AppException("历史车次为空！");
        }
        List<TicketOrder> listTicketOrder = new ArrayList<TicketOrder>();
        for (int i = 0; i < ticketOrder.size(); i++) {
            if (ticketOrder.get(i).getIsHistoryDelete() == 0) {
                listTicketOrder.add(ticketOrder.get(i));
            }
        }
        return listTicketOrder;
    }

    @Override
    @Transactional
    public List<TicketOrder> getHistoryOrders(String userId) {
        if (!StringUtils.hasText(userId)) {
            logger.error("userId参数为空！");
            throw new AppException("userId参数为空！");
        }

        logger.info("getHistoryOrders!userId:" + userId);

        List<TicketOrder> ticketOrder = ticketOrderDao.findByUserIdOrderByCreateDateDesc(userId);
        if (ticketOrder == null) {
            logger.error("历史订单为空！");
            throw new AppException("历史订单为空！");
        }

        return ticketOrder;
    }

    @Override
    @Transactional
    public void deleteHistoryOrders(String userId) {
        if (!StringUtils.hasText(userId)) {
            logger.error("userId参数为空！");
            throw new AppException("userId参数为空！");
        }
        List<TicketOrder> ticketOrder = ticketOrderDao.findByUserIdOrderByCreateDateDesc(userId);
        if (ticketOrder == null) {
            logger.error("车票订单为空！");
            throw new AppException("车票订单为空！");
        }
        for (int i = 0; i < ticketOrder.size(); i++) {
            TicketOrder data = ticketOrder.get(i);
            data.setIsHistoryDelete(1);
            ticketOrder.set(i, data);
        }
        ticketOrderDao.save(ticketOrder);

    }

    @Override
    @Transactional
    public void deleteOrder(String ticketOrderId) {
        if (!StringUtils.hasText(ticketOrderId)) {
            logger.error("orderId参数为空！");
            throw new AppException("orderId参数为空！");
        }
        TicketOrder ticketOrder = ticketOrderDao.findOne(ticketOrderId);
        if (ticketOrder == null) {
            logger.error("车票订单为空！");
            throw new AppException("车票订单为空！");
        }
        ticketOrder.setIsDelete(1);
        ticketOrderDao.save(ticketOrder);
    }

    @Override
    public List<TicketOrder> findAllTicketOrder() {
        List<TicketOrder> listTicketOrder = ticketOrderDao.findAll();
        return listTicketOrder;
    }

    @Override
    @Transactional
    public TicketOrder changeTicketOrderToCancle(String ticketOrderId) {
        if (!StringUtils.hasText(ticketOrderId)) {
            logger.error("ticketOrderId参数为空！");
            throw new AppException("ticketOrderId参数为空！");
        }
        TicketOrder ticketOrder = ticketOrderDao.findOne(ticketOrderId);
        if (ticketOrder == null) {
            logger.error("车票订单为空！");
            throw new AppException("车票订单为空！");
        }
        ticketOrder.setOrderStatus(RailwayTicketStatic.ORDER_STATUS_GRAB_CANCELED);
        return ticketOrderDao.save(ticketOrder);
    }

    @Override
    @Transactional
    public TicketOrder changeTicketOrderToStop(String ticketOrderId) {
        if (!StringUtils.hasText(ticketOrderId)) {
            logger.error("ticketOrderId参数为空！");
            throw new AppException("ticketOrderId参数为空！");
        }
        TicketOrder ticketOrder = ticketOrderDao.findOne(ticketOrderId);
        if (ticketOrder == null) {
            logger.error("车票订单为空！");
            throw new AppException("车票订单为空！");
        }
        ticketOrder.setOrderStatus(RailwayTicketStatic.ORDER_STATUS_STOP_GRAB);
        return ticketOrderDao.save(ticketOrder);
    }

    @Override
    @Transactional
    public Map<String, Object> findOneTicketOrder(String ticketOrderId) {
        if (!StringUtils.hasText(ticketOrderId)) {
            logger.error("ticketOrderId参数为空！");
            throw new AppException("ticketOrderId参数为空！");
        }
        TicketOrder ticketOrder = ticketOrderDao.findOne(ticketOrderId);
        if (ticketOrder == null) {
            logger.error("车票订单为空！");
            throw new AppException("车票订单为空！");
        }
        String passengerIds = ticketOrder.getPassengerIds();
        if(!StringUtils.hasText(passengerIds)){
        	logger.error("乘车人信息为空！");
        	throw new AppException("乘车人信息为空！");
        }
        logger.info("passengerIds:"+passengerIds);
        List<Passenger> listPassenger = new ArrayList<Passenger>();
        //按逗号分割字符串
        String[] passengerIdsArr = passengerIds.split(",");
        for(int index = 0; index < passengerIdsArr.length; index++){
        	logger.info(passengerIdsArr[index]);
        	Passenger passenger = passengerDao.findByIdentityCardNo(passengerIdsArr[index]);
        	if(null != passenger){
        		logger.info("乘客姓名："+passenger.getPassengerName());
            	listPassenger.add(passenger);
        	}
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("ticketOrder", ticketOrder);
        map.put("listPassenger", listPassenger);
        return map;
    }

    @Override
    @Transactional
    public TicketOrder updateTicketOrderStatus(String ticketOrderId, Integer orderStatus) {
        if (!StringUtils.hasText(ticketOrderId)) {
            logger.error("ticketOrderId参数为空！");
            throw new AppException("ticketOrderId参数为空！");
        }
        TicketOrder ticketOrder = ticketOrderDao.findOne(ticketOrderId);
        if (ticketOrder == null) {
            logger.error("车票订单为空！");
            throw new AppException("车票订单为空！");
        }
        ticketOrder.setOrderStatus(orderStatus);
        return ticketOrderDao.save(ticketOrder);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    @Transactional
	public void grabOrderResultNotice(Map<String, Object> map) {
    	/**
         * 解析map
         */
        String railwayTicketId = (String) map.get("railwayTicketId");
        if(!StringUtils.hasText(railwayTicketId)){
        	throw new AppException("车票订单id参数为空！");
        }
        logger.info("railwayTicketId:" + railwayTicketId);
        
        TicketOrder ticketOrder = ticketOrderDao.findByOrderId(railwayTicketId);
        if(null == ticketOrder){
        	throw new AppException("未找到对应的车票订单！");
        }

        String userId = (String) map.get("userId");
        logger.info("userId:" + userId);
        if(!userId.equals(ticketOrder.getUserId())){
        	throw new AppException("用户信息与订单不匹配！");
        }

        List<Object> orderDBList = (List<Object>) map.get("orderDBList");
        logger.info("orderDBList:" + orderDBList);

        Map<String, Object> orderDBMap = (Map<String, Object>) orderDBList.get(0);
        //取票号
        String sequence_no = (String) orderDBMap.get("sequence_no");
        logger.info("sequence_no:" + sequence_no);

        //时间
        String order_date = (String) orderDBMap.get("order_date");
        logger.info("order_date:" + order_date);

        //起始车站
        List<String> from_station_name_page = (List<String>) orderDBMap.get("from_station_name_page");
        String fromStation = from_station_name_page.get(0);

        List<String> to_station_name_page = (List<String>) orderDBMap.get("to_station_name_page");
        String toStation = to_station_name_page.get(0);
        logger.info("fromStation:" + fromStation);
        logger.info("toStation:" + toStation);

        //出发时间
        String start_time_page = (String) orderDBMap.get("start_time_page");
        logger.info("start_time_page:" + start_time_page);

        //到达时间
        String arrive_time_page = (String) orderDBMap.get("arrive_time_page");
        logger.info("arrive_time_page:" + arrive_time_page);

        //车次
        String train_code_page = (String) orderDBMap.get("train_code_page");
        logger.info("train_code_page:" + train_code_page);

        //票数
        Integer ticket_totalnum = (Integer) orderDBMap.get("ticket_totalnum");
        logger.info("ticket_totalnum:" + ticket_totalnum);

        //总票价
        Integer ticket_price_all = (Integer) orderDBMap.get("ticket_price_all");
        logger.info("ticket_price_all:" + ticket_price_all);
        
        ticketOrder.setAboardStation(fromStation);
        ticketOrder.setArrivedStation(toStation);
        ticketOrder.setAboardTime(start_time_page);
        ticketOrder.setArrivedTime(arrive_time_page);
        ticketOrder.setLineNo(train_code_page);
        ticketOrder.setOrderStatus(RailwayTicketStatic.ORDER_STATUS_GRAB_SUCCESS);
        ticketOrder.setSequenceNo(sequence_no);
        ticketOrder.setTicketNumber(ticket_totalnum);
        ticketOrder.setTotalPrice(new BigDecimal(ticket_price_all));
        //生成订单号
        ticketOrder = ticketOrderDao.save(ticketOrder);

        logger.info("保存车票订单成功:" + ticketOrder.getTicketOrderId());
        
        //获取车票信息
        List<Map<String, Object>> tickets = (List<Map<String, Object>>) orderDBMap.get("tickets");
        logger.info("tickets.size:" + tickets.size());
        
        for (Map<String, Object> mapTicket : tickets) {
            //获取车次信息
            Map<String, Object> stationTrainDTO = (Map<String, Object>) mapTicket.get("stationTrainDTO");
            String lineNo = (String) stationTrainDTO.get("station_train_code");

            //获取乘客名字
            Map<String, Object> passengerDTO = (Map<String, Object>) mapTicket.get("passengerDTO");
            String passenger_name = (String) passengerDTO.get("passenger_name");
            logger.info("passenger_name:" + passenger_name);

            //身份证
            String passenger_id_type_code = (String) passengerDTO.get("passenger_id_type_code");
            String passenger_id_type_name = (String) passengerDTO.get("passenger_id_type_name");
            String passenger_id_no = (String) passengerDTO.get("passenger_id_no");

            logger.info("passenger_id_type_code:" + passenger_id_type_code);
            logger.info("passenger_id_type_name:" + passenger_id_type_name);
            logger.info("passenger_id_no:" + passenger_id_no);

            //乘客类型
            String passenger_type = (String) passengerDTO.get("passenger_type");
            String passenger_type_name = (String) passengerDTO.get("passenger_type_name");

            logger.info("passenger_type:" + passenger_type);
            logger.info("ticket_type_name:" + passenger_type_name);

            //车厢
            String coach_name = (String) mapTicket.get("coach_name");
            logger.info("coach_name:" + coach_name);

            //座位
            String seat_name = (String) mapTicket.get("seat_name");
            logger.info("seat_name:" + seat_name);

            //坐席
            String seat_type_name = (String) mapTicket.get("seat_type_name");
            logger.info("seat_type_name:" + seat_type_name);

            //票类型
            String ticket_type_name = (String) mapTicket.get("ticket_type_name");
            logger.info("ticket_type_name:" + ticket_type_name);

            //票价
            Integer ticket_price = (Integer) mapTicket.get("ticket_price");
            logger.info("ticket_price:" + ticket_price);

            //保存车票信息
            RailwayTicket ticket = new RailwayTicket();
            ticket.setTicketOrderId(ticketOrder.getTicketOrderId());
            ticket.setPassengerName(passenger_name);
            ticket.setIdentityCardNo(passenger_id_no);
            ticket.setIdentityCarType(passenger_id_type_name);
            ticket.setCarriageNumber(coach_name);
            ticket.setSeatNumber(seat_name);
            ticket.setTicketPickNumber(sequence_no);
            ticket.setTicketPrice(new BigDecimal(ticket_price));
            ticket.setStartTime(start_time_page);
            ticket.setArrivedTime(arrive_time_page);
            ticket.setTicketType(ticket_type_name);
            ticket.setCreateDate(new Date());
            ticket.setLineNo(lineNo);
            ticket = railwayTicketDao.save(ticket);
            logger.info("保存车票信息成功！" + ticket.getRailwayTicketId());
        }
	}
    

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public TicketOrder createBookOrder(Map<String, Object> map) {
        /**
         * 解析map
         */
        String userId = (String) map.get("userId");
        logger.info("userId:" + userId);

        List<Object> orderDBList = (List<Object>) map.get("orderDBList");
        logger.info("orderDBList:" + orderDBList);

        Map<String, Object> orderDBMap = (Map<String, Object>) orderDBList.get(0);
        //取票号
        String sequence_no = (String) orderDBMap.get("sequence_no");
        logger.info("sequence_no:" + sequence_no);

        //时间
        String order_date = (String) orderDBMap.get("order_date");
        logger.info("order_date:" + order_date);

        //起始车站
        List<String> from_station_name_page = (List<String>) orderDBMap.get("from_station_name_page");
        String fromStation = from_station_name_page.get(0);

        List<String> to_station_name_page = (List<String>) orderDBMap.get("to_station_name_page");
        String toStation = to_station_name_page.get(0);
        logger.info("fromStation:" + fromStation);
        logger.info("toStation:" + toStation);

        //出发时间
        String start_time_page = (String) orderDBMap.get("start_time_page");
        logger.info("start_time_page:" + start_time_page);

        //到达时间
        String arrive_time_page = (String) orderDBMap.get("arrive_time_page");
        logger.info("arrive_time_page:" + arrive_time_page);

        //车次
        String train_code_page = (String) orderDBMap.get("train_code_page");
        logger.info("train_code_page:" + train_code_page);

        //票数
        Integer ticket_totalnum = (Integer) orderDBMap.get("ticket_totalnum");
        logger.info("ticket_totalnum:" + ticket_totalnum);

        //总票价
        Integer ticket_price_all = (Integer) orderDBMap.get("ticket_price_all");
        logger.info("ticket_price_all:" + ticket_price_all);

        //保存订单
        TicketOrder ticketOrder = new TicketOrder();
        ticketOrder.setAboardStation(fromStation);
        ticketOrder.setArrivedStation(toStation);
        ticketOrder.setAboardTime(start_time_page);
        ticketOrder.setArrivedTime(arrive_time_page);
        ticketOrder.setLineNo(train_code_page);
        ticketOrder.setOrderStatus(RailwayTicketStatic.ORDER_STATUS_BOOK_SUCCESS);
        ticketOrder.setOrderType(RailwayTicketStatic.ORDER_TYPE_GRAB);
        ticketOrder.setSequenceNo(sequence_no);
        ticketOrder.setTicketNumber(ticket_totalnum);
        ticketOrder.setCreateDate(new Date());
        ticketOrder.setTotalPrice(new BigDecimal(ticket_price_all));
        ticketOrder.setUserId(userId);
        //生成订单号
        ticketOrder.setOrderId(OrderUtil.GenerateOrderNo());
        ticketOrder = ticketOrderDao.save(ticketOrder);

        logger.info("保存车票订单成功:" + ticketOrder.getTicketOrderId());
        //获取车票信息
        List<Map<String, Object>> tickets = (List<Map<String, Object>>) orderDBMap.get("tickets");
        logger.info("tickets.size:" + tickets.size());

        for (Map<String, Object> mapTicket : tickets) {
            //获取车次信息
            Map<String, Object> stationTrainDTO = (Map<String, Object>) mapTicket.get("stationTrainDTO");
            String lineNo = (String) stationTrainDTO.get("station_train_code");

            //获取乘客名字
            Map<String, Object> passengerDTO = (Map<String, Object>) mapTicket.get("passengerDTO");
            String passenger_name = (String) passengerDTO.get("passenger_name");
            logger.info("passenger_name:" + passenger_name);

            //身份证
            String passenger_id_type_code = (String) passengerDTO.get("passenger_id_type_code");
            String passenger_id_type_name = (String) passengerDTO.get("passenger_id_type_name");
            String passenger_id_no = (String) passengerDTO.get("passenger_id_no");

            logger.info("passenger_id_type_code:" + passenger_id_type_code);
            logger.info("passenger_id_type_name:" + passenger_id_type_name);
            logger.info("passenger_id_no:" + passenger_id_no);

            //乘客类型
            String passenger_type = (String) passengerDTO.get("passenger_type");
            String passenger_type_name = (String) passengerDTO.get("passenger_type_name");

            logger.info("passenger_type:" + passenger_type);
            logger.info("passenger_type_name:" + passenger_type_name);

            //车厢
            String coach_name = (String) mapTicket.get("coach_name");
            logger.info("coach_name:" + coach_name);

            //座位
            String seat_name = (String) mapTicket.get("seat_name");
            logger.info("seat_name:" + seat_name);

            //坐席
            String seat_type_name = (String) mapTicket.get("seat_type_name");
            logger.info("seat_type_name:" + seat_type_name);

            //票类型
            String ticket_type_name = (String) mapTicket.get("ticket_type_name");
            logger.info("ticket_type_name:" + ticket_type_name);

            //票价
            Integer ticket_price = (Integer) mapTicket.get("ticket_price");
            logger.info("ticket_price:" + ticket_price);

            //保存车票信息
            RailwayTicket ticket = new RailwayTicket();
            ticket.setTicketOrderId(ticketOrder.getTicketOrderId());
            ticket.setPassengerName(passenger_name);
            ticket.setIdentityCardNo(passenger_id_no);
            ticket.setIdentityCarType(passenger_id_type_name);
            ticket.setCarriageNumber(coach_name);
            ticket.setSeatNumber(seat_name);
            ticket.setTicketPickNumber(sequence_no);
            ticket.setTicketPrice(new BigDecimal(ticket_price));
            ticket.setStartTime(start_time_page);
            ticket.setArrivedTime(arrive_time_page);
            ticket.setCreateDate(new Date());
            ticket.setLineNo(lineNo);
            ticket.setTicketType(ticket_type_name);
            ticket = railwayTicketDao.save(ticket);
            logger.info("保存车票信息成功！" + ticket.getRailwayTicketId());
        }

        return ticketOrder;
    }

    @Override
    @Transactional
    public TicketOrder createGrabOrder(GrabOrderVo vo, String userId) {
        logger.info("createGrabOrder");
        if (!StringUtils.hasText(vo.getAboardPlace())) {
            logger.error("出发地参数为空！");
            throw new AppException("出发地参数为空！");
        }

        if (!StringUtils.hasText(vo.getArrivedPlace())) {
            logger.error("到达地参数为空！");
            throw new AppException("到达地参数为空！");
        }

        if (!StringUtils.hasText(vo.getAboardStation())) {
            logger.error("出发站点参数为空！");
            throw new AppException("出发站点参数为空！");
        }

        if (!StringUtils.hasText(vo.getArrivedStation())) {
            logger.error("到达站点参数为空！");
            throw new AppException("到达站点参数为空！");
        }

        if (!StringUtils.hasText(vo.getAboardTimeFromStation())) {
            logger.error("站点出发时间参数为空！");
            throw new AppException("站点出发时间参数为空！");
        }

        if (!StringUtils.hasText(vo.getArrivedTimeToStation())) {
            logger.error("站点到达时间参数为空！");
            throw new AppException("站点到达时间参数为空！");
        }

        if (!StringUtils.hasText(vo.getLineNo())) {
            logger.error("车次参数为空！");
            throw new AppException("车次参数为空！");
        }

        if (!StringUtils.hasText(vo.getUserName())) {
            logger.error("12306账号参数为空！");
            throw new AppException("12306账号参数为空！");
        }

        if (!StringUtils.hasText(vo.getPassWord())) {
            logger.error("12306密码参数为空！");
            throw new AppException("12306密码参数为空！");
        }

        if (!StringUtils.hasText(vo.getNoticePhoneNo())) {
            logger.error("通知电话参数为空！");
            throw new AppException("通知电话参数为空！");
        }

        if (0 == vo.getPassengerIdentityCardNo().length) {
            logger.error("乘客信息参数为空！");
            throw new AppException("乘客信息参数为空！");
        }

        TicketOrder ticketOrder = new TicketOrder();

        ticketOrder.setOrderType(RailwayTicketStatic.ORDER_TYPE_GRAB);
        logger.error("设置订单类型！");
        //设置订单号
        ticketOrder.setOrderId(OrderUtil.GenerateOrderNo());
        ticketOrder.setUserId(userId);
        ticketOrder.setOrderStatus(RailwayTicketStatic.ORDER_STATUS_WAIT_FOR_GRAB);
        ticketOrder.setAboardPlace(vo.getAboardPlace());
        ticketOrder.setArrivedPlace(vo.getArrivedPlace());
        ticketOrder.setEstimatedAboardTime(vo.getEstimatedAboardTime());
        if (StringUtils.hasText(vo.getAlternativeAboardTime())) {
            ticketOrder.setAlternativeAboardTime(vo.getAlternativeAboardTime());
        }
        ticketOrder.setAboardStation(vo.getAboardStation());
        ticketOrder.setArrivedStation(vo.getArrivedStation());
        ticketOrder.setAboardTimeFromStation(vo.getAboardTimeFromStation());
        ticketOrder.setArrivedTimeToStation(vo.getArrivedTimeToStation());
        ticketOrder.setLineNo(vo.getLineNo());
        ticketOrder.setEstimatedLineNo(vo.getEstimatedLineNo());
        ticketOrder.setSeatType(vo.getSeatType());
        ticketOrder.setEstimatedSeatType(vo.getEstimatedSeatType());
        ticketOrder.setNoticePhoneNo(vo.getNoticePhoneNo());
        ticketOrder.setUserName(vo.getUserName());
        ticketOrder.setPassWord(vo.getPassWord());
        //设置passengerIds
        String passengerIds = new String();
        for (int index = 0; index < vo.getPassengerIdentityCardNo().length; index++) {
            passengerIds = passengerIds.concat(vo.getPassengerIdentityCardNo()[index].concat(","));
        }
        ticketOrder.setPassengerIds(passengerIds);
        ticketOrder.setCreateDate(new Date());
        //保存订单
        return ticketOrderDao.save(ticketOrder);
    }

    @Override
    public List<TicketOrder> getGrabOrders(String all) {
        logger.info("getGrabOrders");
        if("0".equals(all)){
        	return ticketOrderDao.findByOrderStatus(RailwayTicketStatic.ORDER_STATUS_WAIT_FOR_GRAB);
        }
        else if("1".equals(all)){
        	List<TicketOrder> listTicketOrder = new ArrayList<TicketOrder>();
        	listTicketOrder = ticketOrderDao.findByOrderStatus(RailwayTicketStatic.ORDER_STATUS_WAIT_FOR_GRAB);
        	List<TicketOrder> listTicketOrderGrabing = ticketOrderDao.findByOrderStatus(RailwayTicketStatic.ORDER_STATUS_GRABING);
        	listTicketOrder.addAll(listTicketOrderGrabing);
        	return listTicketOrder;
        }
        return null;
    }

    @Override
    public Map<String, Object> getBookOrder(String ticketOrderId) {

        //返回结果保存在Map中
        Map<String, Object> resultMap = new HashMap<String, Object>();

        if (!StringUtils.hasText(ticketOrderId)) {
            throw new AppException("订单ID不能为空");
        }
        logger.info("获取当前车票信息：订单ID：" + ticketOrderId);
        TicketOrder ticketOrder = ticketOrderDao.findOne(ticketOrderId);
        if (null != ticketOrder)
            resultMap.put("ticketOrder", ticketOrder);

        List<RailwayTicket> railwayTickets = railwayTicketDao.findByTicketOrderId(ticketOrderId);
        if (null != railwayTickets)
            resultMap.put("railwayTickets", railwayTickets);

        return resultMap;
    }

    @Override
    public PageData queryTicketOrderDetail(PageParam pageParam, TicketPassengerVo ticketPassengerVo) {
        logger.info("queryTicketOrderDetail");
        if (ticketPassengerVo == null) {
            logger.error("查询对象不能为空");
        }
        return ticketOrderDao.findTicketOrderDetail(pageParam, ticketPassengerVo);
    }


	@Override
	public List<TicketPassengerVo> findTicketOrder(TicketPassengerVo ticketPassengerVo) {
		return ticketOrderDao.findTicketOrder(ticketPassengerVo);
	}

}
