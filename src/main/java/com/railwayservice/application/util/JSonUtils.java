package com.railwayservice.application.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSonUtils {

    /**
     * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
     *
     * @param content   要转换的JavaBean类型
     * @param valueType 原始Json字符串数据
     * @return JavaBean对象
     */
    public static <T> T readValue(String content, Class<T> valueType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(content, valueType);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 把JavaBean转换为json字符串。
     *
     * @param object JavaBean对象
     * @return Json字符串
     */
    public static String toJSon(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            return null;
        }
    }

}