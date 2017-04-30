package com.railwayservice.order.dao;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.common.dao.BaseDaoImpl;
import com.railwayservice.order.entity.MainOrder;
import com.railwayservice.order.vo.OrderQuartzVo;
import com.railwayservice.order.vo.QueryOrderParam;
import com.railwayservice.order.vo.UserOrdersVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单DAO实现类。
 *
 * @author Ewing
 * @date 2017/2/22
 */
public class MainOrderDaoImpl extends BaseDaoImpl {
    private final Logger logger = LoggerFactory.getLogger(MainOrderDaoImpl.class);

    /**
     * 查询并分页。
     */
    public PageData queryMainOrderPage(PageParam pageParam, QueryOrderParam param) {
        List<Object> params = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT o.orderId,m.stationId,m.name AS merchantName,o.orderNo,o.createDate,o.customerName," +
                "o.customerPhoneNo,o.orderStatus,o.orderTotalPrice,o.payDate,o.payType,o.refundApplyDate,o.refundDate," +
                "o.payStatus FROM MainOrder o LEFT JOIN Merchant m ON o.merchantId = m.merchantId WHERE 1=1");
        // 处理参数，动态生成SQL。
        if (StringUtils.hasText(param.getCustomerName())) {
            sqlBuilder.append(" AND o.customerName LIKE ?");
            params.add("%" + param.getCustomerName() + "%");
        }
        if (StringUtils.hasText(param.getCustomerPhoneNo())) {
            sqlBuilder.append(" AND o.customerPhoneNo LIKE ?");
            params.add("%" + param.getCustomerPhoneNo() + "%");
        }
        if (StringUtils.hasText(param.getOrderNo())) {
            sqlBuilder.append(" AND o.orderNo LIKE ?");
            params.add("%" + param.getOrderNo() + "%");
        }
        if (param.getStartTime() != null) {
            sqlBuilder.append(" AND o.createDate > ?");
            params.add(param.getStartTime());
        }
        if (param.getEndTime() != null) {
            sqlBuilder.append(" AND o.createDate < ?");
            params.add(param.getEndTime());
        }
        if (StringUtils.hasText(param.getMerchantName())) {
            sqlBuilder.append(" AND m.name LIKE ?");
            params.add("%" + param.getMerchantName() + "%");
        }
        if("desc".equals(param.getOrder())){
            sqlBuilder.append(" order by o.createDate desc");
        }else {
            sqlBuilder.append(" order by o.createDate asc");
        }
        return this.findPageMap(pageParam, sqlBuilder.toString(), params.toArray());
    }

    /**
     * 查询用户的订单信息
     * @param userId
     * @return
     * @author lid
     */
    public List<UserOrdersVo> getOrdersByUser(String userId) {
    	logger.info("getOrdersByUser!userId:"+userId);
        StringBuilder sqlBuilder = new StringBuilder("select o.orderId, o.orderStatus, o.orderCancelStatus, o.orderTotalPrice as totalPrice, DATE_FORMAT(o.createDate,'%Y-%m-%d') as createDate," +
                " m.name as merchantName, i.url as imageUrl" +
                " from MainOrder o left join Merchant m on o.merchantId = m.merchantId"
                + " left join ImageInfo i on m.imageId = i.imageId" +
                " where o.userId = ?");
        return this.getOperations().query(sqlBuilder.toString(), BeanPropertyRowMapper.newInstance(UserOrdersVo.class), userId);
    }
    
    /**
     * 查询用户的订单信息
     * @param userId
     * @return
     * @author lid
     */
    public List<OrderQuartzVo> queryMainOrderByStatus(Integer mainOrderStatus) {
    	logger.info("queryMainOrderByStatus!mainOrderStatus:"+mainOrderStatus);
        StringBuilder sqlBuilder = new StringBuilder("select o.orderId, o.createDate from MainOrder o where o.orderStatus = ?");
        return this.getOperations().query(sqlBuilder.toString(), BeanPropertyRowMapper.newInstance(OrderQuartzVo.class), mainOrderStatus);
    }
}
