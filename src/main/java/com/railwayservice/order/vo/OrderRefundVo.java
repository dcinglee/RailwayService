package com.railwayservice.order.vo;

/**
 * 订单退款接口参数vo类
 * @author lid
 * @date 2017.3.17
 *
 */
public class OrderRefundVo {
	
	/**
	 *微信订单号
	 *微信生成的订单号，在支付通知中有返回
	 */
	private String transaction_id;
	
	/**
	 * 商户侧传给微信的订单号
	 * mainOrder    orderNo
	 * 参考OrderUtil.GenerateOrderNo()
	 */
	private String out_trade_no;
	
	/**
	 * 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
	 * 
	 * 自己生成
	 * 
	 * 参考OrderUtil.GenerateOrderNo()
	 */
	private String out_refund_no;
	
	/**
	 * 订单总价，单位为分
	 */
	private int total_fee;
	
	/**
	 * 退款金额，单位为分
	 */
	private int refund_fee;

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getOut_refund_no() {
		return out_refund_no;
	}

	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public int getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(int refund_fee) {
		this.refund_fee = refund_fee;
	}
	
}
