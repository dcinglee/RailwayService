package com.railwayservice.order.vo;

import com.railwayservice.order.entity.MainOrder;
import com.railwayservice.order.entity.SubOrder;

import java.util.List;

/**
 * 用户查询订单数据Vo类
 *
 * @author lid
 */
public class OrderResultParamVo {
    /**
     * 主订单信息
     */
    private MainOrder mainOrder;

    /**
     * 子订单列表
     */
    private List<SubOrder> listSubOrder;

    public MainOrder getMainOrder() {
        return mainOrder;
    }

    public void setMainOrder(MainOrder mainOrder) {
        this.mainOrder = mainOrder;
    }

    public List<SubOrder> getListSubOrder() {
        return listSubOrder;
    }

    public void setListSubOrder(List<SubOrder> listSubOrder) {
        this.listSubOrder = listSubOrder;
    }

}
