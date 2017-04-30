package com.railwayserviceWX.util;

import com.alibaba.fastjson.JSON;
import com.railwayserviceWX.config.WeixinConfig;
import com.railwayserviceWX.exception.WeixinException;
import com.railwayserviceWX.msg.ButtonData;
import com.railwayserviceWX.msg.MenuData;

/**
 * 微信菜单创建
 *
 * @author lid
 * @date 2017年2月23日
 */
public class MenuCreate {
    public static void main(String[] args) {
        // 创建按钮
        ButtonData btn = new ButtonData();

        /*MenuData menu1 = new MenuData("view", "站内点餐", "http://szeiv.com/RailwayService/entrance/index");
        MenuData menu2 = new MenuData("view", "购充电宝", "http://szeiv.com/RailwayService/entrance/index");
        MenuData menu3 = new MenuData("view", "我的订单", "http://szeiv.com/Railervice/entrance/myOrdersIndex");*/
        System.out.println(WeixinConfig.HOST);
        MenuData menu1 = new MenuData("view", "便民服务", WeixinConfig.HOST+"/entrance/index");
        MenuData menu2 = new MenuData("view", "抢火车票", WeixinConfig.HOST+"/entrance/ticket");
        MenuData menu3 = new MenuData("view", "我的订单", WeixinConfig.HOST+"/entrance/myOrdersIndex");

        
       /* *//**************测试账号按键设置*********************//*
        MenuData menu1 = new MenuData("click", "站内点餐", "index");
        MenuData menu2 = new MenuData("view", "购充电宝", "http://119.23.134.10/RailwayService/entrance/index");
        MenuData menu3 = new MenuData("view", "我的订单", "http://119.23.134.10/RailwayService/entrance/myOrdersIndex");
        *//**************测试账号按键设置*********************/
        
        
        try {
            // 菜单之间的关系
            btn.addMenu(menu1);
            btn.addMenu(menu2);
            btn.addMenu(menu3);
            String menustr = JSON.toJSONString(btn);
            MenuUtil.create(menustr);
        } catch (WeixinException e) {
            e.printStackTrace();
        }
    }
}
