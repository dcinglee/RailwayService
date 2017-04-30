package com.railwayservice.common;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 该类临时测试用，用完还原，请勿提交！
 */
public class MainTest {
    private static ApplicationContext context;

    /**
     * 该方法临时测试用，用完还原，请勿提交！
     */
    public static void main(String[] args) {

        System.out.println("Done");
    }

    /**
     * 初始化Spring容器。
     */
    public synchronized static void initSpringContext() {
        if (context == null)
            context = new ClassPathXmlApplicationContext("applicationContext.xml", "springDataJpa.xml");
    }

    /**
     * 获取项目中Spring管理的Bean，Controller除外。
     */
    public static <T> T getBean(Class<T> clazz) {
        initSpringContext();
        return context.getBean(clazz);
    }

}
