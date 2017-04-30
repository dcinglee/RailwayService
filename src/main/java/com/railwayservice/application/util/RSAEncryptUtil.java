package com.railwayservice.application.util;

import com.railwayservice.application.vo.RsaKeyData;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * RSA加解密。
 *
 * @author Ewing
 */
public class RSAEncryptUtil {

    // 加密规则
    private static final String ALGORITHM = "RSA";

    // RSA加密类型
    private static final String PADDING = "RSA/NONE/NoPadding";

    // 加密提供者
    private static final String PROVIDER = "BC";

    // 加密公钥
    private static RSAPublicKey publicKey;

    // 加密私钥
    private static RSAPrivateKey privateKey;

    private static RsaKeyData publicKeyData;

    static {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
            keyPairGen.initialize(256, new SecureRandom());
            KeyPair keyPair = keyPairGen.generateKeyPair();
            publicKey = (RSAPublicKey) keyPair.getPublic();
            privateKey = (RSAPrivateKey) keyPair.getPrivate();
            publicKeyData = new RsaKeyData(publicKey.getModulus(),
                    publicKey.getPublicExponent());
        } catch (Exception e) {
            throw new RuntimeException("初始化加密工具失败！");
        }
    }

    /**
     * 获取公钥信息。
     */
    public static RsaKeyData getPublicKeyData() {
        return publicKeyData;
    }

    /**
     * 用公钥加密字符串。
     * Cipher是线程不安全的。
     */
    public static byte[] encrypt(String text) {
        try {
            Cipher cipher = Cipher.getInstance(PADDING, PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 使用私钥解密字符串。
     * Cipher是线程不安全的。
     */
    public static String decrypt(byte[] text) {
        try {
            Cipher cipher = Cipher.getInstance(PADDING, PROVIDER);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] dectyptedText = cipher.doFinal(text);
            return new String(dectyptedText);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 使用私钥解密字符串。
     * Cipher是线程不安全的。
     */
    public static String decryptString(String str) {
        return decrypt(parseHexStr2Byte(str));
    }

    /**
     * 将16进制字符串转换为二进制。
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr == null) return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
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
     * 获取整数公私钥共用模。
     */
    public static BigInteger getModulus() {
        return publicKey.getModulus();
    }

    /**
     * 获取整数公钥指数。
     */
    public static BigInteger getPublicExponent() {
        return publicKey.getPublicExponent();
    }

    /**
     * 获取整数私钥指数。
     */
    public static BigInteger getPrivateExponent() {
        return privateKey.getPrivateExponent();
    }

    /**
     * 使用模和指数生成RSA公钥。
     */
    public static RSAPublicKey createPublicKey(BigInteger modulus, BigInteger exponent) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, new BouncyCastleProvider());
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 使用模和指数生成RSA私钥。
     */
    public static RSAPrivateKey createPrivateKey(BigInteger modulus, BigInteger exponent) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, new BouncyCastleProvider());
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, exponent);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 测试加密工具并提供示例。
     */
    public static void main(String[] args) {
        try {
            System.out.println(privateKey.getModulus().equals(publicKey.getModulus()) + "\n");
            for (int i = 0; i < 10; i++) {
                String originalText = "123456789012345678901234567890" + i;
                System.out.println("原始：" + originalText);

                // 加密
                byte[] cipherText = encrypt(originalText);
                System.out.println("加密：" + parseByte2HexStr(cipherText));

                // 解密
                String plainText = decrypt(cipherText);
                System.out.println("解密：" + plainText + "\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}