package com.railwayservice.merchantmanage.dao;

import com.railwayservice.common.dao.BaseDaoImpl;
import com.railwayservice.merchantmanage.entity.Merchant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;

/**
 * 商户数据库接口实现类
 *
 * @author lid
 * @date 2017.3.10
 */
public class MerchantDaoImpl extends BaseDaoImpl {
    private final Logger logger = LoggerFactory.getLogger(MerchantDaoImpl.class);

    /**
     * 查询当前站点的所有商户以及商户的logo地址
     *
     * @param stationId
     * @return
     */
    public List<Merchant> getByStationId(String stationId) {
        logger.info("findMerchantByStationId,stationId:" + stationId);
        StringBuilder sql = new StringBuilder("select m.*, i.url as imageUrl" +
                " from Merchant m left join ImageInfo i on m.imageId = i.imageId" +
                " where m.stationId = ?");
        logger.info("sql:" + sql);
        List<Merchant> listMerchant = this.getOperations().query(sql.toString(), BeanPropertyRowMapper.newInstance(Merchant.class), stationId);

        logger.info("listMerchant.size:" + listMerchant.size());

        return listMerchant;
    }
    
    public List<Merchant> getByStationIdAndServiceTypeId(String stationId,String serviceTypeId){
    	logger.info("findMerchantByStationId,stationId:" + stationId);
        StringBuilder sql = new StringBuilder("select m.*, i.url as imageUrl" +
                " from Merchant m left join ImageInfo i on m.imageId = i.imageId" +
                " where m.stationId = ? and m.serviceTypeId=? order by m.sailsInMonth desc");
        logger.info("sql:" + sql);
        List<Merchant> listMerchant = this.getOperations().query(sql.toString(), BeanPropertyRowMapper.newInstance(Merchant.class), stationId,serviceTypeId);
        logger.info("listMerchant.size:" + listMerchant.size());
    	return listMerchant;
    }
}
