package com.railwayserviceWX.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * java调用中央气象局天气预报接口
 * @author lid
 * @date 2017.3.11
 */
public class WeatherUtil {
	/**
	 * 
	 * 获取实时天气1<br>
	 * 方 法 名： getTodayWeather <br>
	 * 
	 * @param Cityid
	 *            城市编码
	 */
	public static Map<String, Object> getWeather(String cityid)	throws IOException, NullPointerException {
		// 连接中央气象台的API
		URL url = new URL("http://www.weather.com.cn/data/cityinfo/" + cityid
			+ ".html");
		
		URLConnection connectionData = url.openConnection();
		connectionData.setConnectTimeout(1000);
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(connectionData.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null)
				sb.append(line);
			String datas = sb.toString();
			System.out.println(datas);
			JSONObject jsonData = JSONObject.fromObject(datas);
			JSONObject info = jsonData.getJSONObject("weatherinfo");
			map.put("city", info.getString("city").toString());// 城市
			map.put("temp1", info.getString("temp1").toString());// 最高温度
			map.put("temp2", info.getString("temp2").toString());// 最低温度
			map.put("weather", info.getString("weather").toString());//天气
			map.put("ptime", info.getString("ptime").toString());// 发布时间
		} catch (SocketTimeoutException e) {
			System.out.println("连接超时");
		} catch (FileNotFoundException e) {
			System.out.println("加载文件出错");
		}
		return map;
	}
	
	public static void main(String[] args) {
		try {
			//测试获取实时天气1(包含风况，湿度)
			Map<String, Object> map2 = getWeather("101010100");
			System.out.println(map2.get("city") + "\t" + map2.get("temp1")
					+ "\t" + map2.get("temp2") + "\t" + map2.get("weather")
					+ "\t" + map2.get("ptime"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
