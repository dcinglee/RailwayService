package com.railwayservice.messages.service;

/**
 * 消息常量值。
 */
public class MessagesStatic {

    // 消息类型：主订单变更
    public static final int NOTICE_TYPE_MAIN_ORDER = 11001;
    // 消息类型：优惠活动
    public static final int NOTICE_TYPE_DISCOUNT = 11002;
    // 消息类型：广告消息
    public static final int NOTICE_TYPE_ADVERT = 11003;

    // 消息状态：未处理
    public static final int NOTICE_STATUS_NOT_DEAL = 12001;
    // 消息状态：已处理
    public static final int NOTICE_STATUS_DEALED = 12002;

}
