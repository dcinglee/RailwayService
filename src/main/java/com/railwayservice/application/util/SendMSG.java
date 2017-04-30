package com.railwayservice.application.util;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * @author lidx
 * @date 2017/3/1
 * @describe 短信验证码和短信通知发送接口
 */
public class SendMSG {
    /**
     * 公共参数
     */
    private static final String url = "http://gw.api.taobao.com/router/rest";
    private static final String appkey = "23761036";
    private static final String secret = "760f56c7de0b9a50fd0fa6a5419dc3b0";
    /*private static final String session = "";
    private static final String timestamp = "";*/

    /**
     * 短信验证码、短信通知 发送
     *
     * @param phoneNo 电话号码
     * @param code    验证码
     * @param reason  原因
     * @param orderNo 订单号
     * @param modelID 模板ID
     *                验证码：SMS_50980123;
     *                订单取消成功：SMS_50945197;
     *                订单取消失败：SMS_50980149;
     *                订单拒绝通知：SMS_51060139;
     *                通知用户取货: SMS_60845448
     *                通知用户抢票成功：SMS_62485312;
     *                通知用户抢票失败：SMS_62435422;
     *                收货验证码通知：SMS_63275331
     * @return 是否发送成功
     */
    public static boolean sendSMS(String phoneNo, String code, String reason, String orderNo, String address, String lineNo, String departTime, String departStation, String stopStation, String modelID) {

        String station = departStation + "-" + stopStation;

        //短信通知模板的内容
        String jsonMSG = "{\"code\":\"" + code + "\",\"reason\":\"" + reason + "\",\"orderid\":\"" + orderNo + "\",\"address\":\"" + address + "\",\"line\":\"" + lineNo + "\",\"depart\":\"" + departTime + "\",\"station\":\"" + station + "\"}";

        try {
            TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);

            AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
            //公共回传参数reason
            req.setExtend("123456");

            //短信类型
            req.setSmsType("normal");

            //短信签名
            req.setSmsFreeSignName("猿动力");

            //短信验证码模板变量
            req.setSmsParamString(jsonMSG);

            //短信接收号码
            req.setRecNum(phoneNo);

            //短信模板ID（短信通知）
            req.setSmsTemplateCode(modelID);

            AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
            System.out.println(rsp.getBody());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 发送短信验证码
     *
     * @param phoneNo 电话号码
     * @param code    验证码
     * @return 是否发送成功
     */
    public static boolean sendCheckCode(String phoneNo, String code) {
        return sendSMS(phoneNo, code, "", "", "", "", "", "", "", "SMS_50980123");
    }

    /**
     * 发送短信通知（订单取消成功）
     *
     * @param phoneNo 电话号码
     * @param orderNo 订单号
     * @return 是否发送成功
     */
    public static boolean sendOrdeCancelT(String phoneNo, String orderNo) {
        return sendSMS(phoneNo, "", "", orderNo, "", "", "", "", "", "SMS_50945197");
    }

    /**
     * 发送短信通知（订单取消失败）
     *
     * @param phoneNo 电话号码
     * @param reason  原因
     * @return 是否发送成功
     */
    public static boolean sendOrdeCancelF(String phoneNo, String reason) {
        return sendSMS(phoneNo, "", reason, "", "", "", "", "", "", "SMS_50980149");
    }

    /**
     * 发送短信通知（订单被拒绝）
     *
     * @param phoneNo 电话号码
     * @param reason  原因
     * @param orderNo 订单号
     * @return 是否发送成功
     */
    public static boolean sendOrderRefused(String phoneNo, String reason, String orderNo) {
        return sendSMS(phoneNo, "", reason, orderNo, "", "", "", "", "", "SMS_51060139");
    }

    /**
     * 通知用户取货（订单已送达）
     *
     * @param phoneNo 电话号码
     * @param address 送货地点（指定用户取货地点）
     * @return 是否发送成功
     */
    public static boolean sendGoodsDelivery(String phoneNo, String address) {
        return sendSMS(phoneNo, "", "", "", address, "", "", "", "", "SMS_60845448");
    }

    /**
     * 通知用户抢票成功
     *
     * @param phoneNo       手机号
     * @param lineNo        车次
     * @param departTime    发车时间
     * @param departStation 始发站
     * @param stopStation   终点站
     * @return 是否发送成功
     */
    public static boolean sendGrabTicketSuccess(String phoneNo, String lineNo, String departTime, String departStation, String stopStation) {
        return sendSMS(phoneNo, "", "", "", "", lineNo, departTime, departStation, stopStation, "SMS_62485312");
    }

    /**
     * 通知用户抢票失败
     *
     * @param phoneNo       手机号
     * @param lineNo        车次
     * @param departTime    发车时间
     * @param departStation 始发站
     * @param stopStation   终点站
     * @return 是否发送成功
     */
    public static boolean sendGoodsDeliveryFail(String phoneNo, String lineNo, String departTime, String departStation, String stopStation) {
        return sendSMS(phoneNo, "", "", "", "", lineNo, departTime, departStation, stopStation, "SMS_62435422");
    }

    /**
     * 确认收货验证码
     *
     * @param phoneNo 手机号
     * @param code    验证码
     * @return
     */
    public static boolean sendGetGoodsCode(String phoneNo, String code) {
        return sendSMS(phoneNo, code, "", "", "", "", "", "", "", "SMS_63275331");
    }

    public static void main(String[] args) {
        boolean bool = sendCheckCode("15674128008", "G12306");
        System.out.print(bool);
    }

}
