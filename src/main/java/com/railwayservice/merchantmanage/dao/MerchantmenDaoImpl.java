package com.railwayservice.merchantmanage.dao;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.common.dao.BaseDaoImpl;
import com.railwayservice.merchantmanage.vo.MerchantAchievement;
import com.railwayservice.merchantmanage.vo.MerchantMainOrder;
import com.railwayservice.merchantmanage.vo.Merchantmen;
import com.railwayservice.order.service.OrderStatic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商户数据库接口实现类
 *
 * @author lid
 * @date 2017.3.10
 */
@Repository
public class MerchantmenDaoImpl extends BaseDaoImpl implements MerchantmenDao {
    private final Logger logger = LoggerFactory.getLogger(MerchantmenDaoImpl.class);

    public List<MerchantMainOrder> queryWaitDealOrders(String merchantId) {
        StringBuilder sql = new StringBuilder("SELECT mo.orderId AS mainOrderId,mo.merchantId,mo.userId,mo.orderStatus,\n" +
                "  mo.refuseReason,mo.cancelReason,mo.orderCancelStatus,mo.orderNo,mo.customerName,mo.customerPhoneNo,\n" +
                "  mo.payDate,mo.updateDate,mo.deliverType,mo.deliverAddress,\n" +
                "  mo.latestServiceTime,mo.aboardTime,mo.orderTotalPrice,mo.cancelReason,mo.refuseReason,\n" +
                "  so.subOrderId,so.productId,so.productName,so.productPrice,so.productCount\n" +
                "FROM MainOrder mo LEFT JOIN SubOrder so ON mo.orderId = so.mainOrderId\n" +
                " WHERE mo.merchantId = ?\n" +
                " AND (mo.orderStatus = " + OrderStatic.MAINORDER_STATUS_WAIT_ACCEPT +
                " OR  mo.orderCancelStatus = " + OrderStatic.MAINORDER_CANCEL_STATUS_APPEAL +
                ") ORDER BY mo.updateDate DESC");
        logger.info("queryWaitDealOrders：SQL：" + sql.toString() + "\nmerchantId：" + merchantId);
        return this.getOperations().query(sql.toString(), BeanPropertyRowMapper.newInstance(MerchantMainOrder.class), merchantId);
    }

    public List<MerchantMainOrder> queryWaitUserOrders(String merchantId) {
        StringBuilder sql = new StringBuilder("SELECT mo.orderId AS mainOrderId,mo.merchantId,mo.userId,mo.orderStatus,\n" +
                "  mo.refuseReason,mo.cancelReason,mo.orderCancelStatus,mo.orderNo,mo.customerName,mo.customerPhoneNo,\n" +
                "  mo.payDate,mo.updateDate,mo.deliverType,mo.deliverAddress,\n" +
                "  mo.latestServiceTime,mo.aboardTime,mo.orderTotalPrice,mo.cancelReason,mo.refuseReason,\n" +
                "  so.subOrderId,so.productId,so.productName,so.productPrice,so.productCount\n" +
                "FROM MainOrder mo LEFT JOIN SubOrder so ON mo.orderId = so.mainOrderId\n" +
                " WHERE mo.merchantId = ?\n" +
                " AND mo.orderStatus in (" + OrderStatic.MAINORDER_STATUS_ACCEPT
                + "," + OrderStatic.MAINORDER_STATUS_WAIT_USER_GET +
                ") AND mo.deliverType = " + OrderStatic.DELIVER_TYPE_SELF +
                " ORDER BY mo.updateDate DESC");
        logger.info("queryWaitDealOrders：SQL：" + sql.toString() + "\nmerchantId：" + merchantId);
        return this.getOperations().query(sql.toString(), BeanPropertyRowMapper.newInstance(MerchantMainOrder.class), merchantId);
    }

    @Override
    public List<MerchantMainOrder> queryHistoryOrders(String merchantId, Date startDate, Date endDate, Integer deliverType, Integer... orderStatus) {
        StringBuilder sql = new StringBuilder("SELECT mo.orderId AS mainOrderId, mo.merchantId, mo.userId," +
                " mo.orderStatus, mo.orderCancelStatus, mo.orderNo, mo.customerName, mo.customerPhoneNo," +
                " mo.payDate, mo.updateDate, mo.deliverType, mo.deliverAddress, sp.serviceProviderId," +
                " sp.name AS serviceProviderName, sp.phoneNo AS serviceProviderPhoneNo," +
                " mo.latestServiceTime, mo.aboardTime, mo.orderTotalPrice, mo.cancelReason, so.subOrderId," +
                " so.productId, so.productName, so.productPrice, so.productCount,mo.refuseReason" +
                " FROM MainOrder mo LEFT JOIN SubOrder so ON mo.orderId = so.mainOrderId" +
                " LEFT JOIN ServiceProvider sp ON mo.serviceProviderId = sp.serviceProviderId" +
                " WHERE mo.merchantId = ?");
        List<Object> params = new ArrayList<>();
        params.add(merchantId);
        // 数组参数只有一个时用=，多个参数用IN。
        if (orderStatus != null && orderStatus.length > 0) {
            int count = 0;
            for (Integer status : orderStatus) {
                if (status != null) {
                    params.add(status);
                    count++;
                }
            }
            if (count == 1) {
                sql.append(" AND mo.orderStatus = ?");
            } else if (count > 1) {
                sql.append(" AND mo.orderStatus IN (?");
                while (count-- > 1) {
                    sql.append(",?");
                }
                sql.append(")");
            }
        }
        if (deliverType != null) {
            sql.append(" AND mo.deliverType = ?");
            params.add(deliverType);
        }
        if (startDate != null) {
            sql.append(" AND mo.updateDate > ?");
            params.add(startDate);
        }
        if (endDate != null) {
            sql.append(" AND mo.updateDate < ?");
            params.add(endDate);
        }
        sql.append(" ORDER BY mo.updateDate DESC");
        logger.info("queryHistoryOrders：SQL：" + sql.toString() + "\nmerchantId：" + merchantId);
        return this.getOperations().query(sql.toString(), BeanPropertyRowMapper
                .newInstance(MerchantMainOrder.class), params.toArray());
    }

    @Override
    public MerchantAchievement queryTotalAchievement(String merchantId) {
        StringBuilder sql = new StringBuilder("SELECT '总营业额' AS label, sum(mo.orderTotalPrice)" +
                " AS total FROM MainOrder mo WHERE mo.merchantId = ?");
        logger.info("queryTotalAchievement：SQL：" + sql.toString() + "\nmerchantId：" + merchantId);
        return this.findOneObject(sql.toString(), MerchantAchievement.class, merchantId);
    }

    @Override
    public PageData queryDailyAchievement(PageParam pageParam, String merchantId) {
        StringBuilder sql = new StringBuilder("SELECT DATE_FORMAT(mo.updateDate,'%Y-%m-%d') AS label," +
                " sum(mo.orderTotalPrice) AS total FROM MainOrder mo WHERE mo.merchantId = ?" +
                " GROUP BY DATE_FORMAT(mo.updateDate,'%Y-%m-%d') ORDER BY mo.updateDate DESC");
        logger.info("queryDailyAchievement：SQL：" + sql.toString() + "\nmerchantId：" + merchantId);
        return this.findPageObject(pageParam, sql.toString(), MerchantAchievement.class, merchantId);
    }

    /**
     * 商户获取自己的资料信息。
     */
    @Override
    public Merchantmen getMerchantInfo(String merchantId) {
        StringBuilder sql = new StringBuilder("SELECT m.merchantId,m.name,m.phoneNo,m.address,m.status," +
                "m.startTime,m.stopTime,m.announcement,img.url AS imageUrl FROM Merchant m" +
                " LEFT JOIN ImageInfo img ON img.imageId = m.imageId WHERE m.merchantId = ?");
        logger.info("getMerchantInfo：SQL：" + sql.toString() + "\nmerchantId：" + merchantId);
        return this.findOneObject(sql.toString(), Merchantmen.class, merchantId);
    }

}
