package com.railwayservice.grabticket.service;

/**
 * 车票常量值。
 */
public class RailwayTicketStatic {
	/** 旅客类型常量值 */
	/**
	 * 成人
	 */
	public static final int PASSENGER_TYPE_ADULT = 18001;

	/**
	 * 儿童
	 */
	public static final int PASSENGER_TYPE_CHILD = 18002;

	/**
	 * 学生
	 */
	public static final int PASSENGER_TYPE_STUDENT = 18003;
	
	/**
	 * 伤残军人和警察
	 */
	public static final int PASSENGER_TYPE_SOLDIER = 18004;
	
	/***列车类型常量值*/
	
	/**
	 * 高铁
	 */
	public static final int TRAIN_TYPE_G = 18101;
	
	/**
	 * 动车
	 */
	public static final int TRAIN_TYPE_D = 18102;
	
	/**
	 * 直达特快
	 */
	public static final int TRAIN_TYPE_Z = 18103;
	
	/**
	 * 特快
	 */
	public static final int TRAIN_TYPE_K = 18104;
	
	/**
	 * 管内快速
	 */
	public static final int TRAIN_TYPE_N = 18105;
	
	/**
	 * 临时列车
	 */
	public static final int TRAIN_TYPE_L = 18106;
	
	
	/*********证件类型 *************/
	//TODO  证件类型待完善
	
	/**
	 * 身份证
	 */
	public static final int CARD_TYPE_IDENTIFY = 18201;
	
	/**
	 * 临时身份证
	 */
	public static final int CARD_TYPE_IDENTIFY_L = 18202;
	
	/**
	 * 中国人民解放军军人保障卡
	 */
	public static final int CARD_TYPE_SOILDIER = 18203;
	
	/**
	 * 港澳通行证
	 */
	public static final int CARD_TYPE_HK_MC_PERMIT= 18204;
	
	/**
	 * 台湾居民来往大陆通行证
	 */
	public static final int CARD_TYPE_TAIWAN_PERMIT= 18205;
	
	
	/********坐席类型********/
	
	/****高铁座位类型：一等座、二等座、商务座、特等座、无座*/
	
	/**
	 * 高铁一等座
	 */
	public static final int SEAT_TYPE_G_FIRST_CLASS_SEATS = 18401;
	
	/**
	 * 高铁二等座
	 */
	public static final int SEAT_TYPE_G_SECOND_CLASS_SEATS = 18402;
	
	/**
	 * 高铁商务座
	 */
	public static final int SEAT_TYPE_G_BUSINESS_SEATS = 18403;
	
	/**
	 * 高铁特等座
	 */
	public static final int SEAT_TYPE_G_STATE_SEATS = 18404;
	
	/**
	 * 高铁无座
	 */
	public static final int SEAT_TYPE_G_NO_SEATS = 18405;
	
	/****普通火车票:硬座 硬卧 软卧 商级软卧 无座*/
	
	/**
	 * 普通火车硬座
	 */
	public static final int SEAT_TYPE_HARD_SEATS = 18406;
	
	/**
	 * 普通火车硬卧
	 */
	public static final int SEAT_TYPE_HARD_BERTH = 18407;
	
	/**
	 * 普通火车 软卧
	 */
	public static final int SEAT_TYPE_SOFT_BERTH = 18408;
	
	/**
	 * 普通火车商级软卧
	 */
	public static final int SEAT_TYPE_ADVANCED_SOFT_BERTH = 18409;
	
	/**
	 * 普通火车无座
	 */
	public static final int SEAT_TYPE_NO_SEATS = 18410;
	
	
	/*****订单状态*****/
	/***订票状态：1：订票成功；2：订票异常***/
	
	/**
	 * 订票成功
	 */
	public static final int ORDER_STATUS_BOOK_SUCCESS = 18501;
	
	/**
	 * 订票异常
	 */
	public static final int ORDER_STATUS_BOOK_ABNORMAL = 18502;
	
	
	/***抢票状态：1、待抢票；2、抢票中；3、用户取消；4、订票成功；5、未抢到票；6、抢票异常***/
	
	/**
	 * 待抢票
	 */
	public static final int ORDER_STATUS_WAIT_FOR_GRAB = 18503;
	
	/**
	 * 抢票中
	 */
	public static final int ORDER_STATUS_GRABING = 18504;
	
	/**
	 * 用户取消
	 */
	public static final int ORDER_STATUS_GRAB_CANCELED = 18505;
	
	/**
	 * 停止抢票
	 */
	public static final int ORDER_STATUS_STOP_GRAB = 18506;
	
	/**
	 * 抢票成功
	 */
	public static final int ORDER_STATUS_GRAB_SUCCESS = 18507;
	
	/**
	 * 未抢到票
	 */
	public static final int ORDER_STATUS_GRAB_FAILED = 18508;
	
	/**
	 * 抢票异常
	 */
	public static final int ORDER_STATUS_GRAB_ABNORMAL = 18509;
	
	
	
	/**************订单类型，抢票还是订票*******************/
	/**
	 * 订票
	 */
	public static final int ORDER_TYPE_BOOK = 18601;
	
	/**
	 * 抢票
	 */
	public static final int ORDER_TYPE_GRAB = 18602;
	
}
