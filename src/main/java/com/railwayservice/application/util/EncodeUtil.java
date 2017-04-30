package com.railwayservice.application.util;

import org.springframework.util.DigestUtils;

/**
 * 编码转码工具类。
 *
 * @author Ewing
 * @date 2017/2/9
 */
public class EncodeUtil {

    /**
     * 密码加密，用于加密存储。
     *
     * @param password 密码。
     * @param salt     加密盐。
     * @return 加密后的密码。
     */
    public static String encodePassword(String password, String salt) {
        String md5Pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        String strA = md5Pwd.substring(0, 16);
        String strB = md5Pwd.substring(16, 32);
        return DigestUtils.md5DigestAsHex((strB + salt + strA).getBytes());
    }
}
