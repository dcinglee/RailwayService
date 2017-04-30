package com.railwayserviceWX.util;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.order.entity.MainOrder;
import com.railwayservice.order.entity.SubOrder;
import com.railwayserviceWX.config.WeixinConfig;
import com.railwayserviceWX.vo.BrandWCPayParameterVo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 微信支付工具类
 *
 * @author lid
 * @date 2017.3.4
 */
@SuppressWarnings("deprecation")
public class WechatPayUtil {

	private static final String p12 = "/1448894202.p12";
    private static Logger logger = LoggerFactory.getLogger(WechatPayUtil.class);

    /**
     * 获取微信支付网页端接口参数
     *
     * @param userId
     * @param order
     * @param addrip
     * @return BrandWCPayParameterVo
     * @author lid
     */
    public static BrandWCPayParameterVo getPayParameters(Merchant merchant,String openid, MainOrder order, String addrip, List<SubOrder> listSubOrder) {
        logger.info("getPayParameters!openid:" + openid + ",order:" + order + ",addrip=" + addrip);

        BrandWCPayParameterVo brandWCPayParameterVo = new BrandWCPayParameterVo();

        //时间戳
        String timeStamp = String.valueOf(System.currentTimeMillis()/1000);
		
        //随机字符串
		String nonceStr=String.valueOf(Math.random());
		
		//获取prepay_id
		String prepay_id = getPrePayId(merchant, openid, addrip, order, listSubOrder);
		if(null == prepay_id){
			return null;
		}
		
		logger.info("prepay_id:"+prepay_id);
		
		String packAge = "prepay_id=" + prepay_id;
		
		String []array = {"appId=".concat(WeixinConfig.APPID), "timeStamp=".concat(timeStamp), "nonceStr=".concat(nonceStr),
				"package=".concat(packAge), "signType=".concat(WeixinConfig.SIGNTYPEMD5)};
		
		SignUtil.sort(array);
		
		String stringA = new String() ;
		
		String stringSignTemp = new String();
		
		String paySign = new String();
		
		for(int i = 0 ; i < array.length ; i++)
		{
			stringA = stringA.concat(array[i].concat("&"));
		}
		
		stringSignTemp = stringA.concat("key=").concat(WeixinConfig.KEY);
		
		MySecurity mySecurity = new MySecurity();
		
		paySign = mySecurity.encode(stringSignTemp, "MD5");
		
		paySign = paySign.toUpperCase();
		
		brandWCPayParameterVo.setAppId(WeixinConfig.APPID);
		brandWCPayParameterVo.setNonceStr(nonceStr);
		brandWCPayParameterVo.setPackAge(packAge);
		brandWCPayParameterVo.setPaySign(paySign);
		brandWCPayParameterVo.setSignType(WeixinConfig.SIGNTYPEMD5);
		brandWCPayParameterVo.setTimeStamp(timeStamp);
		
        return brandWCPayParameterVo;
    }

    /**
     * 统一下单接口,获取prepay_id参数
     *
     * @param openid
     * @param addrip
     * @param order
     * @return String
     * @date 2017.3.4
     */
    public static String getPrePayId(Merchant merchant, String openid, String addrip, MainOrder order, List<SubOrder> listSubOrder) {
        logger.info("getPrePayId!");

        //公众账号ID
        String appid = WeixinConfig.APPID;
       
        //商户号
        String mch_id = WeixinConfig.MCHID;
        
        logger.info("appid:"+appid);
        logger.info("mch_id:"+mch_id);
        //设备号
        String device_info = WeixinConfig.DEVICE_INFO_WEB;
        
        //随机字符串
        String nonce_str = String.valueOf(Math.random());
        
        //商品描述
        String body = new String();
        if(1 == listSubOrder.size()){
        	body = merchant.getName()+"-"+listSubOrder.get(0).getProductName();
        }else{
        	body = merchant.getName()+"-"+listSubOrder.get(0).getProductName()+"等";
        }

        //商品详情
      	String detail = getProductDetail(listSubOrder);
        
      	//附加数据 (暂时用订单id作为附加数据)
        String attach = order.getOrderId();
        
        //商户订单号
      	String out_trade_no = order.getOrderNo();
      	
      	//设置货币类型
      	String fee_type = WeixinConfig.CURRENCY_RMB_TYPE;
      	
      	//总金额  整形  单位为分   先将订单总价乘以100， 小数点0位四舍五入取整并转为整形
      	int total_fee =  order.getOrderTotalPrice().multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        
      	//终端IP
      	String spbill_create_ip = addrip;
      	SimpleDateFormat sdf = new SimpleDateFormat(AppConfig.DATE_TIME_FORMAT_PAY);
      	
      	//设置交易开始时间
      	String time_start = sdf.format(new Date());
		
      	//通知地址
		String notify_url = WeixinConfig.HOST + WeixinConfig.NOTIFY_URL;
		
		//交易类型
		String trade_type =WeixinConfig.TRADE_TYPE_JSAPI;
		
		//指定支付方式
		String limit_pay = WeixinConfig.NO_CREDIT;
		
		//签名(最后获取)
		String []array = {"appid=".concat(appid), "mch_id=".concat(mch_id), "device_info=".concat(device_info),
				"nonce_str=".concat(nonce_str), "body=".concat(body), "detail=".concat(detail),  
				"attach=".concat(attach), "out_trade_no=".concat(out_trade_no), "fee_type=".concat(fee_type), 
				"total_fee=".concat(String.valueOf(total_fee)), 
				"spbill_create_ip=".concat(spbill_create_ip), "time_start=".concat(time_start), 
				"notify_url=".concat(notify_url), "trade_type=".concat(trade_type),
				"limit_pay=".concat(limit_pay), "openid=".concat(openid)};
		
		//对数组进行字典排序
		SignUtil.sort(array);
		
		//组合成stringA
		String stringA = new String() ;
				
		String stringSignTemp = new String();
		
		String sign = new String();
				
		String xmlStr = new String();
				
		String prepay_id = new String();
		
		for(int i = 0 ; i < array.length ; i++)
		{
			stringA = stringA.concat(array[i].concat("&"));
		}
		logger.info("stringA:"+stringA);
		
		stringSignTemp = stringA.concat("key=").concat(WeixinConfig.KEY);
		
		logger.info("stringSignTemp:"+stringSignTemp);
		
		//对stringSignTemp进行MD5加密
		MySecurity mySecurity = new MySecurity();
		
		//获取签名
		sign = mySecurity.encode(stringSignTemp, "MD5").toUpperCase();
		logger.info("sign:"+sign);
		Map<String, String> paraMap = new HashMap<String, String>();
		
		paraMap.put("appid", appid);
		paraMap.put("attach", attach);
		paraMap.put("body", body);
		paraMap.put("detail", detail);
		paraMap.put("device_info", device_info);
		paraMap.put("fee_type", fee_type);
		paraMap.put("limit_pay", limit_pay);
		paraMap.put("mch_id", mch_id);
		paraMap.put("nonce_str", nonce_str);
		paraMap.put("notify_url", notify_url);
		paraMap.put("openid", openid);
		paraMap.put("out_trade_no", out_trade_no);
		paraMap.put("spbill_create_ip", spbill_create_ip);
		paraMap.put("time_start", time_start);
		paraMap.put("total_fee", String.valueOf(total_fee));
		paraMap.put("trade_type", trade_type);
		paraMap.put("sign", sign);
		
		String xml = ArrayToXml(paraMap);
		
		logger.info("xml:"+xml);
		
		xmlStr = HttpUtil.sendHttpsPOST(WeixinConfig.UNIFY_ORDER_URL, xml);
		
		logger.info("xmlStr:"+xmlStr);
		
		if(xmlStr.contains("FAIL")){
			return null;
		}
		String start = "<prepay_id><![CDATA[";
		String end = "]]></prepay_id>";
		prepay_id = xmlStr.substring(xmlStr.indexOf(start)+start.length(), xmlStr.indexOf(end));
		logger.info("prepay_id"+prepay_id);
		return prepay_id;
    }
    
    /**
     * 根据子订单表获取订单详情
     * @param listSubOrder
     * @return
     */
    public static String getProductDetail(List<SubOrder> listSubOrder){
    	String detail = new String();
    	if(0 == listSubOrder.size()){
    		return null;
    	}
    	
    	if(1 == listSubOrder.size()){
    		detail = listSubOrder.get(0).getProductName()+"*"+listSubOrder.get(0).getProductCount().toString();
    		return detail;
    	}
    	
    	for(int index = 0; index < listSubOrder.size() - 1; index++){
    		detail = detail.concat(listSubOrder.get(index).getProductName()+"*"+listSubOrder.get(index).getProductCount().toString()+",");
    	}
    	detail.concat(listSubOrder.get(listSubOrder.size()-1).getProductName() + "*"+listSubOrder.get(listSubOrder.size()-1).getProductCount().toString()+"!");
    	return detail;
    }
    
    /**
	 * map转成xml
	 * 
	 * @param arr
	 * @return
	 */
	public static String ArrayToXml(Map<String, String> arr) {
		String xml = "<xml>";
		Iterator<Entry<String, String>> iter = arr.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			String key = entry.getKey();
			String val = entry.getValue();
			xml += "<" + key + ">" + val + "</" + key + ">";
		}
		xml += "</xml>";
		return xml;
	}
	
	/**
	 *  微信退款接口
	 * @author lid
	 * @date 2017.3.17
	 * @param transaction_id   微信返回的订单号
	 * @param out_trade_no     商户生成的订单号
	 * @param out_refund_no    商户退款单号
	 * @param total_fee        总金额
	 * @param refund_fee       退款金额
	 */
	public static Map<String, Object> refund(String transaction_id, String out_trade_no, 
			String out_refund_no, int total_fee,int refund_fee) throws Exception{
		logger.info("refund");
		
		//公众账号ID
        String appid = WeixinConfig.APPID;
       
        //商户号
        String mch_id = WeixinConfig.MCHID;
        
        //设备号
        String device_info = WeixinConfig.DEVICE_INFO_WEB;
        
        //随机字符串
        String nonce_str = String.valueOf(Math.random());
        
        //签名类型
        String sign_type = WeixinConfig.SIGNTYPEMD5;
        
        //设置货币类型
      	String fee_type = WeixinConfig.CURRENCY_RMB_TYPE;
      	
      	//操作员，默认为商户号
      	String op_user_id = WeixinConfig.MCHID;
      	
        //签名(最后获取)
      	String []array = {"appid=".concat(appid), "mch_id=".concat(mch_id), "device_info=".concat(device_info),
      		"nonce_str=".concat(nonce_str), "sign_type=".concat(sign_type), "fee_type=".concat(fee_type),  
      		"op_user_id=".concat(op_user_id), "transaction_id=".concat(transaction_id), "out_trade_no=".concat(out_trade_no), 
      		"out_refund_no=".concat(out_refund_no), 
      		"total_fee=".concat(String.valueOf(total_fee)), "refund_fee=".concat(String.valueOf(refund_fee))};
     
      	//对数组进行字典排序
      	SignUtil.sort(array);
      		
      	//组合成stringA
      	String stringA = new String() ;
      			
      	String stringSignTemp = new String();
      	
      	String sign = new String();
      				
      	for(int i = 0 ; i < array.length ; i++)
      	{
      		stringA = stringA.concat(array[i].concat("&"));
      	}
      	logger.info("stringA:"+stringA);
      		
      	stringSignTemp = stringA.concat("key=").concat(WeixinConfig.KEY);
      		
      	logger.info("stringSignTemp:"+stringSignTemp);
      		
      	//对stringSignTemp进行MD5加密
      	MySecurity mySecurity = new MySecurity();
      		
      	//获取签名
      	sign = mySecurity.encode(stringSignTemp, "MD5").toUpperCase();
      	
      	Map<String, String> paraMap = new HashMap<String, String>();
		
		paraMap.put("appid", appid);
		paraMap.put("device_info", device_info);
		paraMap.put("fee_type", fee_type);
		paraMap.put("mch_id", mch_id);
		paraMap.put("nonce_str", nonce_str);
		paraMap.put("op_user_id", op_user_id);
		paraMap.put("out_refund_no", out_refund_no);
		paraMap.put("out_trade_no", out_trade_no);
		paraMap.put("refund_fee", String.valueOf(refund_fee));
		paraMap.put("sign_type", sign_type);
		paraMap.put("total_fee", String.valueOf(total_fee));
		paraMap.put("transaction_id", transaction_id);
		paraMap.put("sign", sign);
        
		String xml = ArrayToXml(paraMap);
		
		logger.info("xml:"+xml);
		
		KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(p12));
        try {
            keyStore.load(instream, "1448894202".toCharArray());
        } finally {
            instream.close();
        }

        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, "1448894202".toCharArray())
                .build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" },
                null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
    		HttpPost httpPost = new HttpPost(WeixinConfig.REFUND_URL);
    		StringEntity se = new StringEntity(xml);
    		httpPost.setEntity(se);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    SAXReader saxReader = new SAXReader(); 
                    Document document = saxReader.read(entity.getContent());
                    
                    logger.info("document:"+document.toString());
                    
                    Element rootElt = document.getRootElement();

                    String returnCode = rootElt.elementText("return_code");  
                    
                    Map<String, Object> mapResult = new HashMap<String, Object>();
                    if("SUCCESS".equals(returnCode)){
                    	String refund_id = rootElt.elementText("refund_id");  
                    	String transactionId = rootElt.elementText("transaction_id");  
                    	String outTradeNo = rootElt.elementText("out_trade_no");  
                    	if(StringUtils.hasText(rootElt.elementText("total_fee"))){
                    		logger.info("total_fee"+rootElt.elementText("total_fee"));
                    	}
                    	/*int totalFee = Integer.valueOf(rootElt.elementText("total_fee"));*/
                    	String outRefundNo = rootElt.elementText("out_refund_no");  
                    	mapResult.put("returnCode", returnCode);
                    	mapResult.put("refund_id", refund_id);
                    	mapResult.put("transaction_id", transactionId);
                    	mapResult.put("out_trade_no", outTradeNo);
                    	/*mapResult.put("total_fee", totalFee);*/
                    	mapResult.put("out_refund_no", outRefundNo);
                    	logger.info(mapResult.toString());
                    }else{
                    	mapResult.put("returnCode", returnCode);
                    	mapResult.put("err_code_des",rootElt.elementText("err_code_des"));  
                    	mapResult.put("err_code",rootElt.elementText("err_code")); 
                    	logger.info(mapResult.toString());
                    }
                    EntityUtils.consume(entity);
                    return mapResult;
                }
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return null;
	}
	
	public final static void main(String[] args) throws Exception {
		String stringSignTemp = "appid=wx76e685e048a40319&attach=40283cc15b8063e3015b806df3000007&body=长沙充电宝2-啦啦啦啦啦啦啦啦&detail=啦啦啦啦啦啦啦啦*1&device_info=WEB&fee_type=CNY&limit_pay=no_credit&mch_id=1448894202&nonce_str=0.39620056014374505&notify_url=http://szeiv.com/RailwayService/payNotifyFromWechat/payNotifyFromOrder&openid=oo-KAs_n8AL0PSWW4UzaMvagYGjk&out_trade_no=20170418173900942&spbill_create_ip=0:0:0:0:0:0:0:1&time_start=20170418173900&total_fee=100&trade_type=JSAPI&key=18d53930aacfd12f21870097bedb3e33";
		MySecurity mySecurity = new MySecurity();
		String sign = mySecurity.encode(stringSignTemp, "MD5").toUpperCase();
		System.out.println(sign);
    }
}
