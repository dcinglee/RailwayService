package com.railwayservice.order.dao;

import com.railwayservice.common.dao.BaseDaoImpl;
import com.railwayservice.order.service.OrderStatic;
import com.railwayservice.order.vo.FinanceMerchantVo;
import com.railwayservice.order.vo.FinanceServiceProviderVo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 财务报表
 *
 * @author xuyu
 */
@Repository
public class FinanceDaoImpl extends BaseDaoImpl implements FinanceDao {

    /**
     * 查询商户的订单报表
     *
     * @param stationId
     * @param merchantId
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<FinanceMerchantVo> findMerchantMainOrder(String stationId, String merchantName, Date beginDate, Date endDate) {

        /**
          拒绝接单 OrderStatic.MAINORDER_STATUS_REJECT
          取消订单 OrderStatic.MAINORDER_STATUS_CANCELED
          完成订单 OrderStatic.MAINORDER_STATUS_COMPLETED
         */

        List<Object> params = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("select o.stationId,s.stationName, o.merchantId,m.name as merchantName,DATE_FORMAT(o.createDate,'%Y-%m-%d') as createDate," +
                " sum( 1 ) as totalCount," +
                " sum( case when o.orderStatus =" + OrderStatic.MAINORDER_STATUS_REJECT + " then 1 else 0 end ) as rejectCount," +
                " sum( case when o.orderStatus in (" + OrderStatic.MAINORDER_STATUS_CANCELED +"," + OrderStatic.MAINORDER_STATUS_OVERTIME + ") then 1 else 0 end ) as cancelCount," +
                " sum( case when o.orderStatus not in (" +OrderStatic.MAINORDER_STATUS_REJECT+"," + OrderStatic.MAINORDER_STATUS_COMPLETED + "," + OrderStatic.MAINORDER_STATUS_CANCELED +","+OrderStatic.MAINORDER_STATUS_OVERTIME+ ") then 1 else 0 end ) as uncompletedCount," +
                " sum( case when o.orderStatus =" + OrderStatic.MAINORDER_STATUS_COMPLETED + "  then 1 else 0 end ) as completedCount," +
                " sum(  o.productTotalPrice ) as totalPrice," +
                " sum( case when o.orderStatus =" + OrderStatic.MAINORDER_STATUS_REJECT + " then o.productTotalPrice else 0 end ) as rejectPrice," +
                " sum( case when o.orderStatus in (" + OrderStatic.MAINORDER_STATUS_CANCELED +"," + OrderStatic.MAINORDER_STATUS_OVERTIME + ") then o.productTotalPrice else 0 end ) as cancelPrice," +
                " sum( case when o.orderStatus not in (" +OrderStatic.MAINORDER_STATUS_REJECT+"," + OrderStatic.MAINORDER_STATUS_COMPLETED + "," + OrderStatic.MAINORDER_STATUS_CANCELED +","+OrderStatic.MAINORDER_STATUS_OVERTIME+ ") then o.productTotalPrice else 0 end ) as uncompletedPrice," +
                " sum( case when o.orderStatus =" + OrderStatic.MAINORDER_STATUS_COMPLETED + " then o.productTotalPrice else 0 end ) as completedPrice" +
                " from MainOrder o left join RailwayStation s on o.stationId = s.stationId left join Merchant m on o.merchantId = m.merchantId where o.createDate >= ? and o.createDate <= ?"
        );

        params.add(beginDate);
        params.add(endDate);

        //处理参数，动态生成SQL。
        if (StringUtils.hasText(stationId)) {
            sqlBuilder.append(" and o.stationId = ?");
            params.add(stationId);
        }
        if (StringUtils.hasText(merchantName)) {
            sqlBuilder.append(" and m.name like  ?");
            params.add("%" + merchantName + "%");
        }

        sqlBuilder.append(" group by o.stationId,o.merchantId,DATE_FORMAT(o.createDate,'%Y-%m-%d')" +
                " order by DATE_FORMAT(o.createDate,'%Y-%m-%d')");

        return this.getOperations().query(sqlBuilder.toString(), BeanPropertyRowMapper.newInstance(FinanceMerchantVo.class), params.toArray());

    }

    public List<FinanceServiceProviderVo> findServiceProvider(String stationId, String serviceProviderName, Date beginDate, Date endDate) {

        List<Object> params = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("select o.stationId,s.stationName, o.serviceProviderId,m.name as serviceProviderName,DATE_FORMAT(o.createDate,'%Y-%m-%d') as createDate," +
                " sum( 1) as totalCount," +
                " sum( o.distributionCosts ) as totalCost" +
                " from MainOrder o left join RailwayStation s on o.stationId = s.stationId left join ServiceProvider m on o.serviceProviderId = m.serviceProviderId" +
                " where o.deliverType = " + OrderStatic.DELIVER_TYPE_SEND + " and o.orderStatus = " + OrderStatic.MAINORDER_STATUS_COMPLETED + " and o.createDate >= ? and o.createDate <= ?"
        );

        params.add(beginDate);
        params.add(endDate);

        //处理参数，动态生成SQL。
        if (StringUtils.hasText(stationId)) {
            sqlBuilder.append(" and o.stationId = ?");
            params.add(stationId);
        }
        if (StringUtils.hasText(serviceProviderName)) {
            sqlBuilder.append(" and m.name like  ?");
            params.add("%" + serviceProviderName + "%");
        }

        sqlBuilder.append(" group by o.stationId,s.stationName, o.serviceProviderId,DATE_FORMAT(o.createDate,'%Y-%m-%d')" +
                " order by DATE_FORMAT(o.createDate,'%Y-%m-%d')");

        return this.getOperations().query(sqlBuilder.toString(), BeanPropertyRowMapper.newInstance(FinanceServiceProviderVo.class), params.toArray());

    }

}
