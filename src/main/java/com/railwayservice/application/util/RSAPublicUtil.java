package com.railwayservice.application.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.Security;
import java.security.spec.RSAPublicKeySpec;

/**
 * RSA加解密。
 *
 * @author Ewing
 */
public class RSAPublicUtil {

    // 加密规则
    private static final String ALGORITHM = "RSA";

    // RSA加密类型
    private static final String PADDING = "RSA/NONE/NoPadding";

    // 加密提供者
    private static final String PROVIDER = "BC";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 用公钥加密字节数组。
     * Cipher是线程不安全的。
     */
    public static byte[] publicEncrypt(BigInteger modulus, BigInteger exponent, byte[] bytes) {
        try {
            // 生成公钥。
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, new BouncyCastleProvider());
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
            Cipher cipher = Cipher.getInstance(PADDING, PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePublic(keySpec));
            return cipher.doFinal(bytes);
        } catch (Exception e) {
            throw new RuntimeException("使用公钥加密失败！");
        }
    }

    /**
     * 用公钥加密字符串。
     */
    public static String publicEncryptString(BigInteger modulus, BigInteger exponent, String string) {
        byte[] cipherText = publicEncrypt(modulus, exponent, string.getBytes());
        return parseByte2HexStr(cipherText);
    }

    /**
     * 用公钥加密字符串。
     */
    public static String publicEncryptString(String modulus, String exponent, String string) {
        BigInteger modulusBI = new BigInteger(modulus);
        BigInteger exponentBI = new BigInteger(exponent);
        return publicEncryptString(modulusBI, exponentBI, string);
    }

    /**
     * 将二进制转换成16进制。
     */
    public static String parseByte2HexStr(byte buf[]) {
        if (buf == null) return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 测试加密工具并提供示例。
     */
    public static void main(String[] args) {
        try {
            String modulus = "66532171149699270464449113533020333084924997977366723883882481504951743862001";
            String exponent = "65537";
            for (int i = 0; i < 10; i++) {
                // 字符串最长为32
                String originalText = "123456789012345678901234567890" + i;
                System.out.println("原始：" + originalText);

                String cipherText = publicEncryptString(modulus, exponent, originalText);
                System.out.println("加密：" + cipherText);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}