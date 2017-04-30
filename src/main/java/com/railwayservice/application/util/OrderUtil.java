package com.railwayservice.application.util;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.order.entity.SubOrder;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 订单工具类， 包括生成订单号等工具
 *
 * @author lid
 * @date 2017.2.13
 */
public class OrderUtil {
	
	public static final int randomStrLength = 4;
	
	public static void main(String args[]){
		System.out.println(GenerateOrderNo());
	}

	/**
     * 根据当前系统时间和随机字符串来生成订单号
     * 随机字符串长度为6
     * @author lid
     * @date 2017.2.13
     */
    public static String GenerateOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat(AppConfig.DATE_TIME_FORMAT_DETAIL);
        String random = String.valueOf(Math.random());
        random = random.substring(random.length() - randomStrLength, random.length());
        return sdf.format(new Date());
    }

    /**
     * 根据子单列表计算主单总价格
     */
    public static BigDecimal calculateTotalPrice(List<SubOrder> listSubOrder) {
        BigDecimal totalPrice = new BigDecimal(0);
        if (0 == listSubOrder.size()) {
            return totalPrice;
        }

        for (int index = 0; index < listSubOrder.size(); index++) {
            if (null != listSubOrder.get(index).getTotalPrice()) {
                totalPrice = totalPrice.add(listSubOrder.get(index).getTotalPrice());
            }
        }
        return totalPrice;
    }
}
