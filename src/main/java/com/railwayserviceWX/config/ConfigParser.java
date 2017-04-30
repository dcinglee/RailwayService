package com.railwayserviceWX.config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * 读取配置文件工具类
 *
 * @author lid
 * @date 2017.3.31
 */
public class ConfigParser {
    private static Properties property = new Properties();

    static {
        InputStream is = null;
        try {
            is = ConfigParser.class.getClassLoader().getResourceAsStream("config.properties");
            if (is != null) {
                property.load(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        is = null;
        try {
            is = ConfigParser.class.getClassLoader().getResourceAsStream("wx.properties");
            if (is != null) {
                property.load(is);
            }

            WeixinConfig wxConfig = WeixinConfig.class.newInstance();
            Field[] fields = wxConfig.getClass().getFields();        //获取实体类的所有属性，返回Field数组
            Method method = null;
            Field field = null;
            for (int j = 0; j < fields.length; j++) {     //遍历所有属性
                field = fields[j];
//	        	 System.out.println("=======name1="+field.getName()+",modifier="+field.getModifiers());
                if (9 == field.getModifiers()) {//获取字段的修饰符，public 1,static 8
                    String name = field.getName();    //获取属性的名字
                    //有这个属性
                    if (property.containsKey(name)) {
                        field.set(wxConfig, String.valueOf(property.get(name)));
                    }
                }
            }

        } catch (IOException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //取Config中的订单超时参数
    public static String getOrderOverTime() {
        return property.getProperty("orderOverTime");
    }

    //取Config中的是否为测试环境参数
    public static String getIsTest() {
        return property.getProperty("isTest");
    }

    public static void main(String args[]) {
        final String orderOverTime = ConfigParser.getOrderOverTime();

        System.out.println("orderOverTime:" + orderOverTime);

        WeixinConfig wxConfig;
        try {
            wxConfig = WeixinConfig.class.newInstance();

            Field[] fields = wxConfig.getClass().getFields();        //获取实体类的所有属性，返回Field数组
            Method method = null;
            Field field = null;
//	        System.out.println("==================111=========");
            for (int j = 0; j < fields.length; j++) {     //遍历所有属性
                field = fields[j];
                if (9 == field.getModifiers()) {//获取字段的修饰符，public 1,static 8
                    String name = field.getName();    //获取属性的名字
                    System.out.println("[" + name + "]=" + field.get(wxConfig));
                }
            }

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
