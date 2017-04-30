package com.railwayserviceWX.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import net.sf.json.JSONObject;

public class CheckTrainUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(CheckTrainUtil.class);
	
	private static final String minute = "分钟";
	private static final String hour = "小时";
	private static final String requestUrl = "http://7li7li.com/querytrain.php";
	private static final Integer minutesAHour = 60; 
	private static final Integer hoursADay = 24; 
	
	@SuppressWarnings("unchecked")
	public static String getCheckInfo(String station, String lineNo) throws IOException, NullPointerException {
		// 连接列车正晚点API
		//对中文参数进行utf-8编码
		String stationUTF8 =  URLEncoder.encode(station, "utf-8");
		URL url = new URL(requestUrl + "?cz=" + stationUTF8	+ "&cc="+lineNo);
		logger.info("url:"+url);
		URLConnection connectionData = url.openConnection();
		connectionData.setConnectTimeout(1000);
        
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(connectionData.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null)
				sb.append(line);
			String datas = sb.toString();
			datas = datas.substring(datas.indexOf("{"), datas.length());
			logger.info("datas:"+datas);
			JSONObject jsonData = JSONObject.fromObject(datas);
			Map<String, Object> map = (Map<String, Object>) JSONObject.fromObject(jsonData); 
			Map<String, Object> map1 = (Map<String, Object>) map.get("data");
			return (String) map1.get("time");
			
		} catch (SocketTimeoutException e) {
			logger.info("连接超时");
		} catch (FileNotFoundException e) {
			logger.info("加载文件出错");
		}
		return null;
	}
	
	/**
	 * 计算晚点时间
	 * @return
	 */
	public static String calDelayTime(String scheduledTime, String actualTime){
		Integer actualTimeHour = Integer.valueOf(actualTime.substring(0, 2));
		Integer actualTimeMinute = Integer.valueOf(actualTime.substring(3, 5));
		Integer scheduledTimeHour = Integer.valueOf(scheduledTime.substring(0, 2));
		Integer scheduledTimeMinute = Integer.valueOf(scheduledTime.substring(3, 5));
		
		String delayHour = null;
		String delayMinute = null;
		
		//如果时钟相等，则获取分钟差数
		if(scheduledTimeHour.equals(actualTimeHour)){
			delayMinute = String.valueOf(actualTimeMinute - scheduledTimeMinute);
			return delayMinute + minute;
		}
		
		//如果时钟不相等，先判断实际到达时间是否大于预计到达时间
		if(actualTimeHour.compareTo(scheduledTimeHour) > 0){
			if(actualTimeMinute.compareTo(scheduledTimeMinute) > 0){
				delayHour = String.valueOf(actualTimeHour - scheduledTimeHour);
				delayMinute = String.valueOf(actualTimeMinute - scheduledTimeMinute);
				return delayHour + hour + delayMinute + minute;
			}
			
			if(actualTimeMinute.compareTo(scheduledTimeMinute) == 0){
				delayHour = String.valueOf(actualTimeHour - scheduledTimeHour);
				return delayHour + hour;
			}
			if(actualTimeHour - scheduledTimeHour == 1){
				delayMinute  =  String.valueOf(actualTimeMinute - scheduledTimeMinute + minutesAHour);
				return delayMinute + minute;
			}
			delayHour = String.valueOf(actualTimeHour - scheduledTimeHour - 1);
			delayMinute  =  String.valueOf(actualTimeMinute - scheduledTimeMinute + minutesAHour);
			return delayHour + hour + delayMinute + minute;
		}
		
		//当预计到达时间为午夜0点之前，而晚点时间为0点之后的处理。
		delayHour = String.valueOf(hoursADay - scheduledTimeHour + actualTimeHour);
		
		if(actualTimeMinute.compareTo(scheduledTimeMinute) > 0){
			delayMinute = String.valueOf(actualTimeMinute - scheduledTimeMinute);
			return delayHour + hour + delayMinute + minute;
		}
		
		if(actualTimeMinute.compareTo(scheduledTimeMinute) == 0){
			delayHour = String.valueOf(actualTimeHour - scheduledTimeHour);
			return delayHour + hour;
		}
		
		if(Integer.valueOf(delayHour) == 1){
			delayMinute  =  String.valueOf(actualTimeMinute - scheduledTimeMinute + minutesAHour);
			return delayMinute + minute;
		}
		delayHour = String.valueOf(Integer.valueOf(delayHour) - 1);
		delayMinute  =  String.valueOf(actualTimeMinute - scheduledTimeMinute + minutesAHour);
		return delayHour + hour + delayMinute + minute;
	}
	
	public static void main(String args[]){
		try {
			String time = getCheckInfo( "长沙南", "G531");
			if(!StringUtils.hasText(time)){
				System.out.println("null == time");
			}else{
				System.out.println("time:"+time);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*String scheduledTime = "21:28";
		String actualTime = "23:29";
		String delayTime = calDelayTime(scheduledTime, actualTime);
		System.out.println(delayTime);*/
	}
}
