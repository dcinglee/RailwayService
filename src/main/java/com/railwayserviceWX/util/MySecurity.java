package com.railwayserviceWX.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密算法工具类
 *
 * @author lid
 * @date 2017.2.21
 **/
public class MySecurity {
    public static final String SHA_1 = "SHA-1";

    public static final String MD5 = "MD5";

    public String encode(String strSrc, String encodeType) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt ;
		try {
			bt = strSrc.getBytes("UTF-8");
			try {
	            if (encodeType == null || "".equals(encodeType))
	                encodeType = MD5;//默认使用MD5
	            md = MessageDigest.getInstance(encodeType);
	            md.update(bt);
	            strDes = bytes2Hex(md.digest());
	        } catch (NoSuchAlgorithmException e) {
	            return strSrc;
	        }
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        return strDes;
    }

    public String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
