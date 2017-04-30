package com.railwayservice.operatemanage.dao;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.operatemanage.entity.AdBanner;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 广告数据库访问接口。
 *
 * @author lidx
 * @date 2017.3.10
 */
public interface AdBannerDao extends JpaRepository<AdBanner, String>, JpaSpecificationExecutor<AdBanner> {

    Integer countByTitle(String title);

    AdBanner findByTitle(String title);

    /**
     * 根据广告标题查询广告并分页。
     */
    PageData queryAdBannerPage(PageParam param, String title);
    
       
    List<AdBanner> queryAdBanner4WX();
    
}
