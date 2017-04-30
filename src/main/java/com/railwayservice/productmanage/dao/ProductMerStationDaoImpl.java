package com.railwayservice.productmanage.dao;

import com.railwayservice.common.dao.BaseDaoImpl;
import com.railwayservice.productmanage.vo.ProductVo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductMerStationDaoImpl extends BaseDaoImpl implements ProductMerStationDao {

    public List<ProductVo> findProduct(String stationId, String typeId, String merchantName, String productName, int recommend) {

    	/**
    	 * 添加商品在售状态的判断
    	 * lid   2017.3.22
    	 */
        List<Object> params = new ArrayList<>();

        StringBuilder sqlBuilder = new StringBuilder("select m.stationId, r.stationName, m.merchantId,m.name as merchantName, p.price as productPrice, p.name as productName, p.recommend, p.productId, p.sales, p.hits, p.score, m.serviceTypeId as typeId,p.state "
                + " " +
                " from Product p left join Merchant m on p.merchantId = m.merchantId left join RailwayStation r on m.stationId = r.stationId" +
                " where 1 = 1 "
        );

        //处理参数，动态生成SQL。
        if (StringUtils.hasText(stationId)) {
            sqlBuilder.append(" and m.stationId = ?");
            params.add(stationId);
        }

        if (StringUtils.hasText(typeId)) {
            sqlBuilder.append(" and m.serviceTypeId = ?");
            params.add(typeId);
        }

        if (StringUtils.hasText(merchantName)) {
            sqlBuilder.append(" and m.name like ?");
            params.add("%" + merchantName + "%");
        }

        if (StringUtils.hasText(productName)) {
            sqlBuilder.append(" and p.name like ?");
            params.add("%" + productName + "%");
        }

        if (recommend != 0) {
            sqlBuilder.append(" and p.recommend = ?");
            params.add(recommend);
        }
        
       

        sqlBuilder.append(" order by r.stationName,m.name,p.name");

        return this.getOperations().query(sqlBuilder.toString(), BeanPropertyRowMapper.newInstance(ProductVo.class), params.toArray());

    }

}
