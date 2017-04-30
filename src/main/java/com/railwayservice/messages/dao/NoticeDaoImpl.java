package com.railwayservice.messages.dao;

import com.railwayservice.common.dao.BaseDaoImpl;
import com.railwayservice.messages.vo.OrderNotice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 说明。
 *
 * @author Ewing
 * @date 2017/4/10
 */
public class NoticeDaoImpl extends BaseDaoImpl {

    private final Logger logger = LoggerFactory.getLogger(NoticeDaoImpl.class);

    /**
     * 查询要通知的内容。
     */
    public OrderNotice getNoticeOrderInfo(String orderId){
        StringBuilder sql = new StringBuilder("SELECT mo.orderId AS mainOrderId, u.openid," +
                " sp.name AS servantName, me.name AS merchantName, img.url AS merchantLogoUrl," +
                " group_concat(so.productName) AS productDetail, d.name AS orderStatusName" +
                " FROM MainOrder mo LEFT JOIN SubOrder so ON mo.orderId = so.mainOrderId" +
                " LEFT JOIN Merchant me ON mo.merchantId = me.merchantId" +
                " LEFT JOIN ImageInfo img ON img.imageId = me.imageId" +
                " LEFT JOIN Dictionary d ON mo.orderStatus = d.value" +
                " LEFT JOIN ServiceProvider sp ON mo.serviceProviderId = sp.serviceProviderId" +
                " LEFT JOIN User u ON u.userId = mo.userId" +
                " WHERE mo.orderId = ? GROUP BY mo.orderId");
        logger.info("getMerchantOrderInfo：SQL：" + sql.toString() + "\norderId：" + orderId);
        return this.findOneObject(sql.toString(), OrderNotice.class, orderId);
    }

}
