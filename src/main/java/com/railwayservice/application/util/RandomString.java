package com.railwayservice.application.util;

import java.util.Random;
import java.util.UUID;

/**
 * 随机字符串生成器。
 *
 * @author Ewing
 * @since 2017/2/15
 */
public class RandomString {

    private static char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
            'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private static char[] allChars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o'
            , 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_', '='};

    /**
     * 获取一定长度的随机字符串。
     *
     * @param length 指定字符串长度。
     * @return 一定长度的字符串。
     */
    public static String randomString(int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(chars.length);
            stringBuilder.append(chars[number]);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取一定长度的随机数字字符串。
     *
     * @param length 指定字符串长度。
     * @return 一定长度的数字字符串。
     */
    public static String randomNumberString(int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }

    /**
     * 生成16进制的UUID。
     *
     * @return 16进制的UUID。
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成64进制的UUID，使用数字、字母、- 和 _ 字符。
     *
     * @return 64进制的UUID。
     */
    public static String generate64UUID() {
        UUID uuid = UUID.randomUUID();
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();
        // 转换UUID为64进制。
        char[] out = new char[24];
        int tmp = 0, idx = 0;
        tmp = (int) ((msb >>> 40) & 0xffffff);
        out[idx + 3] = allChars[tmp & 0x3f];
        tmp >>= 6;
        out[idx + 2] = allChars[tmp & 0x3f];
        tmp >>= 6;
        out[idx + 1] = allChars[tmp & 0x3f];
        tmp >>= 6;
        out[idx] = allChars[tmp & 0x3f];
        idx += 4;

        tmp = (int) ((msb >>> 16) & 0xffffff);
        out[idx + 3] = allChars[tmp & 0x3f];
        tmp >>= 6;
        out[idx + 2] = allChars[tmp & 0x3f];
        tmp >>= 6;
        out[idx + 1] = allChars[tmp & 0x3f];
        tmp >>= 6;
        out[idx] = allChars[tmp & 0x3f];
        idx += 4;

        tmp = (int) (((msb & 0xffff) << 8) | (lsb >>> 56 & 0xff));
        out[idx + 3] = allChars[tmp & 0x3f];
        tmp >>= 6;
        out[idx + 2] = allChars[tmp & 0x3f];
        tmp >>= 6;
        out[idx + 1] = allChars[tmp & 0x3f];
        tmp >>= 6;
        out[idx] = allChars[tmp & 0x3f];
        idx += 4;

        tmp = (int) ((lsb >>> 32) & 0xffffff);
        out[idx + 3] = allChars[tmp & 0x3f];
        tmp >>= 6;
        out[idx + 2] = allChars[tmp & 0x3f];
        tmp >>= 6;
        out[idx + 1] = allChars[tmp & 0x3f];
        tmp >>= 6;
        out[idx] = allChars[tmp & 0x3f];
        idx += 4;

        tmp = (int) ((lsb >>> 8) & 0xffffff);
        out[idx + 3] = allChars[tmp & 0x3f];
        tmp >>= 6;
        out[idx + 2] = allChars[tmp & 0x3f];
        tmp >>= 6;
        out[idx + 1] = allChars[tmp & 0x3f];
        tmp >>= 6;
        out[idx] = allChars[tmp & 0x3f];
        idx += 4;

        tmp = (int) (lsb & 0xff);
        out[idx + 3] = allChars[64];
        out[idx + 2] = allChars[64];
        tmp <<= 4;
        out[idx + 1] = allChars[tmp & 0x3f];
        tmp >>= 6;
        out[idx] = allChars[tmp & 0x3f];

        return new String(out, 0, 22);
    }

    /**
     * 生成指定长度的中文字符串。
     *
     * @param length 长度。
     * @return 中文字符串。
     */
    public static String getChinese(int length) {
        StringBuilder builder = new StringBuilder();
        while (length-- > 0)
            builder.append((char) (0x4e00 + (int) (Math.random() * (0x9fa5 - 0x4e00 + 1))));
        return builder.toString();
    }

}