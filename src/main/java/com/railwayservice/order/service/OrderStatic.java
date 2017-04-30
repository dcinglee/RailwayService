package com.railwayservice.order.service;

/**
 * 订单常量值，保存订单状态，支付状态，支付类型等
 *
 * @author lid
 * @date 2017.2.10
 */
public class OrderStatic {
    /******************************************
     订单状态   预留了一些状态 7001~7009、7021~7029等
     *****************************************/
	/**
	 * 新建订单待支付
	 */
	public static final int MAINORDER_STATUS_WAIT_PAY = 7001;
	
    /**
     * 等待商家接单
     */
    public static final int MAINORDER_STATUS_WAIT_ACCEPT = 7010;

    /**
     * 商家已接单
     */
    public static final int MAINORDER_STATUS_ACCEPT = 7020;

    /**
     * 商家拒绝接单
     * 初始状态 ，商户可以选择接单，也可以不选择接单。
     */
    public static final int MAINORDER_STATUS_REJECT = 7030;

    /**
     * 配送员已接单
     */
    public static final int MAINORDER_STATUS_SERVICER_ACCEPT = 7040;

    /**
     * 配送员已取货
     */
    public static final int MAINORDER_STATUS_SERVICER_GET_GOOD = 7050;

    /**
     * 等待用户取货
     */
    public static final int MAINORDER_STATUS_WAIT_USER_GET = 7055;

    /**
     * 订单完成
     */
    public static final int MAINORDER_STATUS_COMPLETED = 7060;

    /**
     * 订单已取消
     */
    public static final int MAINORDER_STATUS_CANCELED = 7070;

    /**
     * 订单已失效
     */
    public static final int MAINORDER_STATUS_EXPIRE = 7080;
    
    /**
     * 订单已超时
     */
    public static final int MAINORDER_STATUS_OVERTIME = 7090;

    /**
     * 用户申请取消
     */
    public static final int MAINORDER_CANCEL_STATUS_APPEAL = 9010;

    /**
     * 商家同意取消订单
     */
    public static final int MAINORDER_CANCEL_STATUS_AGREE = 9020;

    /**
     * 商家拒绝取消订单
     */
    public static final int MAINORDER_CANCEL_STATUS_REFUSED = 9030;

    /******************************************
     *            支付状态                     *
     *****************************************/
    /**
     * 支付状态：已支付
     */
    public static final int PAY_STATUS_PAYED = 13001;

    /**
     * 支付状态：未支付
     */
    public static final int PAY_STATUS_UNPAYED = 13002;

    /**
     * 支付状态：申请退款
     */
    public static final int PAY_STATUS_REFUND_APPLY = 13003;

    /**
     * 支付状态：已退款
     */
    public static final int PAY_STATUS_REFUNDED = 13004;

    /******************************************
     *            支付类型                     *
     *****************************************/
    /**
     * 支付类型：微信支付
     */
    public static final int PAY_TYPE_WEIXIN = 14001;

    /**
     * 支付类型：支付宝
     */
    public static final int PAY_TYPE_ZFB = 14002;

    /**
     * 支付类型：信美分期
     */
    public static final int PAY_TYPE_XINMEI = 14003;

    /**
     * 支付类型：银联
     */
    public static final int PAY_TYPE_UNIONPAY = 14004;

    /******************************************
     *            送货类型                     *
     *****************************************/
    /**
     * 送货类型：服务员派送
     */
    public static final int DELIVER_TYPE_SEND = 16002;

    /**
     * 送货类型：顾客自取
     */
    public static final int DELIVER_TYPE_SELF = 16001;
}
