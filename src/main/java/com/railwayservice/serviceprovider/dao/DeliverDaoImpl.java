package com.railwayservice.serviceprovider.dao;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.common.dao.BaseDaoImpl;
import com.railwayservice.order.service.OrderStatic;
import com.railwayservice.serviceprovider.vo.CountOrdersVO;
import com.railwayservice.serviceprovider.vo.DeliverOrderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 送货数据访问实现。
 *
 * @author Ewing
 * @date 2017/3/3
 */
@Repository
public class DeliverDaoImpl extends BaseDaoImpl implements DeliverDao {
    private final Logger logger = LoggerFactory.getLogger(DeliverDaoImpl.class);

    @Override
    public PageData queryAcceptOrders(PageParam pageParam, String serviceProviderId, String stationId, Integer orderStatus) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT mo.orderId AS mainOrderId, mo.orderNo,\n" +
                " me.merchantId, mo.updateDate,u.userId, me.name AS merchantName, img.url AS merchantLogoUrl,\n" +
                " me.phoneNo AS merchantPhoneNo, mo.customerName, mo.customerPhoneNo, me.address\n" +
                " AS fromAddress, mo.deliverAddress, mo.aboardTime, mo.latestServiceTime, mo.orderStatus\n" +
                " FROM MainOrder mo LEFT JOIN ServiceByProviderRela spr ON mo.serviceTypeId = spr.serviceTypeId\n" +
                " LEFT JOIN Merchant me ON mo.merchantId = me.merchantId\n" +
                " LEFT JOIN ImageInfo img ON img.imageId = me.imageId\n" +
                " LEFT JOIN User u ON mo.userId = u.userId\n" +
                " WHERE mo.deliverType = " + OrderStatic.DELIVER_TYPE_SEND +
                " AND spr.serviceProvider = ? AND mo.stationId = ?\n" +
                " AND mo.orderStatus = ? ORDER BY mo.latestServiceTime ASC");
        logger.info("服务人员查询订单：SQL：" + sqlBuilder.toString() + "参数：serviceProvider=" + serviceProviderId
                + " stationId=" + stationId + " orderStatus=" + orderStatus);
        return this.findPageObject(pageParam, sqlBuilder.toString(), DeliverOrderVO.class, serviceProviderId, stationId, orderStatus);
    }

    @Override
    public CountOrdersVO countOrdersTimeGreaterEquals(String serviceProviderId, Date startTime) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT count(1) as completeOrdersToday" +
                " FROM MainOrder mo WHERE  mo.orderStatus = ")
                .append(OrderStatic.MAINORDER_STATUS_COMPLETED)
                .append(" AND mo.serviceProviderId=? AND mo.updateDate >?");
        logger.info("服务人员查询订单完成量：SQL：" + sqlBuilder.toString()
                + "参数：serviceProvider=" + serviceProviderId + " stationId=" + startTime);
        return this.findOneObject(sqlBuilder.toString(), CountOrdersVO.class, serviceProviderId, startTime);
    }

    @Override
    public PageData queryMyOrders(PageParam pageParam, String serviceProviderId, String stationId, Integer... orderStatus) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT mo.orderId AS mainOrderId, mo.orderNo,\n" +
                " me.merchantId, mo.updateDate,u.userId, me.name AS merchantName, img.url AS merchantLogoUrl,\n" +
                " me.phoneNo AS merchantPhoneNo, mo.customerName, mo.customerPhoneNo, me.address\n" +
                " AS fromAddress, mo.deliverAddress, mo.aboardTime, mo.latestServiceTime, mo.orderStatus\n" +
                " FROM MainOrder mo\n" +
                " LEFT JOIN Merchant me ON mo.merchantId = me.merchantId\n" +
                " LEFT JOIN ImageInfo img ON img.imageId = me.imageId\n" +
                " LEFT JOIN User u ON mo.userId = u.userId\n" +
                " WHERE mo.deliverType = " + OrderStatic.DELIVER_TYPE_SEND +
                " AND mo.serviceProviderId = ? AND mo.stationId = ?");
        List<Object> params = new ArrayList<>();
        params.add(serviceProviderId);
        params.add(stationId);
        // 至少有1个，第1个直接添加
        params.add(orderStatus[0]);
        if (orderStatus.length > 1) {
            sqlBuilder.append(" AND mo.orderStatus in (?");
            for (int i = 1; i < orderStatus.length; i++) {
                sqlBuilder.append(",?");
                params.add(orderStatus[i]);
            }
            sqlBuilder.append(")");
        } else {
            sqlBuilder.append(" AND mo.orderStatus = ?");
        }
        sqlBuilder.append(" ORDER BY mo.latestServiceTime ASC");
        logger.info("服务人员查询订单：SQL：" + sqlBuilder.toString() + "参数：serviceProvider=" + serviceProviderId
                + " stationId=" + stationId + " orderStatus=" + Arrays.toString(orderStatus));
        return this.findPageObject(pageParam, sqlBuilder.toString(), DeliverOrderVO.class, params.toArray());
    }

    /**
     * 查询今天已完成的订单。
     */
    @Override
    public List<DeliverOrderVO> queryCompleteOrdersToday(String serviceProviderId, Date beginTime) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT mo.orderId AS mainOrderId, mo.orderNo," +
                " me.merchantId, mo.updateDate,u.userId, me.name AS merchantName, img.url AS merchantLogoUrl," +
                " me.phoneNo AS merchantPhoneNo, mo.customerName, mo.customerPhoneNo, me.address" +
                " AS fromAddress, mo.deliverAddress, mo.aboardTime, mo.latestServiceTime, mo.orderStatus" +
                " FROM MainOrder mo LEFT JOIN Merchant me ON mo.merchantId = me.merchantId" +
                " LEFT JOIN ImageInfo img ON img.imageId = me.imageId" +
                " LEFT JOIN User u ON mo.userId = u.userId" +
                " WHERE mo.orderStatus = " + OrderStatic.MAINORDER_STATUS_COMPLETED +
                " AND mo.serviceProviderId = ? AND mo.updateDate > ? ORDER BY mo.updateDate DESC");
        return this.getOperations().query(sqlBuilder.toString(), BeanPropertyRowMapper.newInstance(DeliverOrderVO.class), serviceProviderId, beginTime);
    }

    /**
     * 查询今天被取消的订单。
     */
    @Override
    public List<DeliverOrderVO> queryCancelOrdersToday(String serviceProviderId, Date beginTime) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT mo.orderId AS mainOrderId, mo.orderNo, mo.cancelReason," +
                " me.merchantId, mo.updateDate,u.userId, me.name AS merchantName, img.url AS merchantLogoUrl," +
                " me.phoneNo AS merchantPhoneNo, mo.customerName, mo.customerPhoneNo, me.address" +
                " AS fromAddress, mo.deliverAddress, mo.aboardTime, mo.latestServiceTime, mo.orderStatus" +
                " FROM MainOrder mo LEFT JOIN Merchant me ON mo.merchantId = me.merchantId" +
                " LEFT JOIN ImageInfo img ON img.imageId = me.imageId" +
                " LEFT JOIN User u ON mo.userId = u.userId" +
                " WHERE mo.orderStatus = " + OrderStatic.MAINORDER_STATUS_CANCELED +
                " AND mo.serviceProviderId = ? AND mo.updateDate > ? ORDER BY mo.updateDate DESC");
        return this.getOperations().query(sqlBuilder.toString(), BeanPropertyRowMapper.newInstance(DeliverOrderVO.class), serviceProviderId, beginTime);
    }
}
