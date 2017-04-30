package com.railwayserviceWX.util;

/**
 * 签名工具类
 * @author lid
 * @date 2017.3.7
 *
 */
public class SignUtil {
	 //用于字典排序
    public static void sort(String array[]) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[j].compareTo(array[i]) < 0) {
                    String temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }
    }
}
