package com.railwayservice.operatemanage.dao;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.common.dao.BaseDaoImpl;
import com.railwayservice.operatemanage.entity.AdBanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidx
 * @date 2017/3/10
 * @describe
 */
public class AdBannerDaoImpl extends BaseDaoImpl {
    private final Logger logger = LoggerFactory.getLogger(AdBannerDaoImpl.class);

    public PageData queryAdBannerPage(PageParam param, String title) {
        logger.info("queryAdBannerPage");
        List<Object> params = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT " +
                "ad.adBannerId," +
                "ad.adminId," +
                "ad.adPosition," +
                "ad.adType," +
                "ad.adWeight," +
                "ad.content," +
                "ad.createDate," +
                "ad.duration," +
                "ad.endDate," +
                "ad.hits," +
                "ad.imageId," +
                "ad.linkUrl," +
                "ad.startDate," +
                "ad.title," +
                "a.name AS adminName " +
                "FROM AdBanner ad, Admin a WHERE ad.adminId=a.adminId and 1=1");


        // 处理参数，动态生成SQL。
        if (StringUtils.hasText(title)) {
            sqlBuilder.append(" and ad.title like ?");
            params.add("%" + title + "%");
        }
        sqlBuilder.append(" order by ad.createDate desc");
        return this.findPageObject(param, sqlBuilder.toString(), AdBanner.class, params.toArray());
    }
    
    public List<AdBanner> queryAdBanner4WX(){
    	logger.info("queryAdBanner4WX");
        StringBuilder sql = new StringBuilder("select m.*, i.url as imageUrl" +
                " from AdBanner m left join ImageInfo i on m.imageId = i.imageId" +
                " order by m.adWeight");
        logger.info("sql:" + sql);
        List<AdBanner> listMerchant = this.getOperations().query(sql.toString(), BeanPropertyRowMapper.newInstance(AdBanner.class));

//        logger.info("listMerchant.size:" + listMerchant.size());
        return listMerchant;
    }
}
