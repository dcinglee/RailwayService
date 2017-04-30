package com.railwayserviceWX.util;

import javax.servlet.http.HttpServletRequest;
/**
 * 获取HttpServletRequest的ip地址
 * @author lid
 * @date 2017.3.7
 */
public class IpAddressUtils {
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if(ip.length()>15) {
			String[] ipArr = ip.split(",");
			ip = ipArr[ipArr.length-1].trim();
		}
		return ip;
	}
}
