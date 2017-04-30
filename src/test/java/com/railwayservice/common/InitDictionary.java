package com.railwayservice.common;

import com.railwayservice.common.dao.DictionaryDao;
import com.railwayservice.common.entity.Dictionary;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 字典值命名规范采用两级：类别（从1开始）+具体值（从001开始）。
 * type字段用于区分字典值的类别，以模块名+类别名的方式定义，比如ManagerType。
 */
public class InitDictionary {
    private DictionaryDao dictionaryDao;

    public InitDictionary() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:springDataJpa.xml");
        dictionaryDao = ac.getBean(DictionaryDao.class);
    }

    public static void main(String[] args) {
        InitDictionary initDictionary = new InitDictionary();
        // 调用不同的方法执行初始化数据。
        initDictionary.initCommon();
    }

    /**
     * 插入数据到字典表，已存在则跳过。
     */
    private void insertDictionary(int[] values, String[] names, String type, String detail) {
        for (int i = 0; i < values.length; i++) {
            if (dictionaryDao.findOne(values[i]) == null) {
                Dictionary dictionary = new Dictionary(values[i], names[i], type, detail);
                dictionaryDao.save(dictionary);
            }
        }
    }

    public void initCommon() {
        // 公共性别1xxx
        int[] genderValues = {1000, 1001, 1002};
        String[] genderNames = {"保密", "男", "女"};
        insertDictionary(genderValues, genderNames, "Gender", "性别");
    }

    public void initManager() {
        // 管理员类型2xxx
        int[] typeValues = {2001, 2002};
        String[] typeNames = {"超级管理员", "普通管理员"};
        insertDictionary(typeValues, typeNames, "ManagerType", "管理员类别");
        // 管理员状态3xxx
        int[] stateValues = {3001, 3002};
        String[] stateNames = {"正常", "锁定"};
        insertDictionary(stateValues, stateNames, "ManagerState", "管理员状态");
    }

}
