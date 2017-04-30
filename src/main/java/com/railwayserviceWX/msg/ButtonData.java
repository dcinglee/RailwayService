package com.railwayserviceWX.msg;

import com.railwayserviceWX.exception.WeixinMenuOutOfBoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Button
 *
 * @author lid
 * @date 2017.2.22
 */
public class ButtonData {
    // 一级菜单数组，个数应为1~3个
    public List<MenuData> button = new ArrayList<MenuData>(3);

    /**
     * 添加一级菜单
     *
     * @param menu
     * @throws WeixinMenuOutOfBoundException
     */
    public void addMenu(MenuData menu) throws WeixinMenuOutOfBoundException {
        if (button.size() < 3) {
            button.add(menu);
        } else {
            throw new WeixinMenuOutOfBoundException();
        }
    }
}
