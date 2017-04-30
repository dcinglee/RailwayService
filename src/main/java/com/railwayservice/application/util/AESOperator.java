package com.railwayservice.application.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化。
 */
public class AESOperator {
    /* 
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。 
     */
    private String secretKey = "qazedctgbujm1593";
    private String ivParameter = "3951okmuhbtfcesz";
    private static AESOperator instance = new AESOperator();

    private AESOperator() {

    }

    public static AESOperator getInstance() {
        return instance;
    }

    // 加密  
    public String encrypt(String sSrc) {
        String result = "";
        try {
            Cipher cipher;
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] raw = secretKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
            result = new BASE64Encoder().encode(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 此处使用BASE64做转码。  
        return result;

    }

    // 解密  
    public String decrypt(String sSrc) {
        try {
            byte[] raw = secretKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        // 需要加密的字串
        String cSrc = "测试需要加密的字串";
        System.out.println(cSrc + " 长度为" + cSrc.length());
        // 加密
        String enString = AESOperator.getInstance().encrypt(cSrc);
        long lStart = System.currentTimeMillis();
        System.out.println("加密后的字串是：" + enString + " 长度为" + enString.length());

        long lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("加密耗时：" + lUseTime + "毫秒");
        // 解密
        lStart = System.currentTimeMillis();
        String DeString = AESOperator.getInstance().decrypt(enString);
        System.out.println("解密后的字串是：" + DeString);
        lUseTime = System.currentTimeMillis() - lStart;
        System.out.println("解密耗时：" + lUseTime + "毫秒");
    }
} 