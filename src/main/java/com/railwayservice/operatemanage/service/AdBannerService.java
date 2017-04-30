package com.railwayservice.operatemanage.service;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.operatemanage.entity.AdBanner;

import java.io.InputStream;
import java.util.List;

/**
 * 广告服务类接口。
 *
 * @author lidx
 * @date 2017.3.10
 */
public interface AdBannerService {

    /**
     * 通过广告标题分页查询广告信息
     *
     * @param currentAdmin 当前管理员
     * @param title        广告标题
     * @param pageable     分页信息
     * @return 成功查询的分页广告信息
     */
//    Page<AdBanner> queryAdBanners(Admin currentAdmin, String title, Pageable pageable);

	List<AdBanner> queryAdBanner4WX();
	
    /**
     * 通过广告标题分页查询广告信息
     *
     * @param currentAdmin 当前管理员
     * @param param        分页信息
     * @param title        广告标题
     * @return 成功查询的分页广告信息
     */
    PageData queryAdBanners( PageParam param, String title);

    /**
     * 添加广告记录
     *
     * @param currentAdmin 当前管理员
     * @param adBanner     广告对象
     * @return 成功添加的广告对象
     */
    AdBanner addAdBanner(Admin currentAdmin, AdBanner adBanner);

    /**
     * 给广告添加图片
     *
     * @param adBannerId  广告id
     * @param inputStream
     * @return 成功修改、添加的广告图片信息
     */
    AdBanner updateImage(String adBannerId, InputStream inputStream);

    /**
     * 修改广告信息
     *
     * @param currentAdmin 当前管理员
     * @param adBanner     广告对象
     * @return 成稿修改的广告信息
     */
    AdBanner updateAdBanner(Admin currentAdmin, AdBanner adBanner);

    /**
     * 删除广告信息
     *
     * @param currentAdmin 当前管理员
     * @param adBannerId   广告ID
     */
    void deleteAdBanner(Admin currentAdmin, String adBannerId);
}
