package com.railwayservice.operatemanage.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.common.entity.ImageInfo;
import com.railwayservice.common.service.CommonService;
import com.railwayservice.operatemanage.dao.AdBannerDao;
import com.railwayservice.operatemanage.entity.AdBanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * 广告管理服务类。
 *
 * @author lidx
 * @date 2017.3.10
 */
@Service
public class AdBannerServiceImpl implements AdBannerService {

    private final Logger logger = LoggerFactory.getLogger(AdBannerServiceImpl.class);

    @Autowired
    private AdBannerDao adBannerDao;

    @Autowired
    private CommonService commonService;

    
    

/*    @Override
    public Page<AdBanner> queryAdBanners(Admin currentAdmin, String title, Pageable pageable) {
        logger.info("queryAdBanners:" + title);
        if (currentAdmin == null) {
            throw new AppException("当前用户未登陆");
        }

        // JPA标准查询接口，使用Lambda表达式。root以商户类为根对象。
        Specification<AdBanner> specification = (root, query, builder) -> {
            // 创建组合条件，默认1=1。
            Predicate predicate = builder.conjunction();

            if (currentAdmin.getAdminId() != null && !"".equals(currentAdmin.getAdminId().trim())) {
                predicate = builder.and(predicate, builder.equal(root.get("adminId"), currentAdmin.getAdminId()));
            }
            // 如果广告标题不为空，添加到查询条件。
            if (StringUtils.hasText(title)) {
                predicate = builder.and(predicate, builder.like(root.get("title"), "%" + title + "%"));
            }
            return predicate;
        };
        // 调用JPA标准查询接口查询数据。
        return adBannerDao.findAll(specification, pageable);
    }*/

    public AdBannerDao getAdBannerDao() {
        return adBannerDao;
    }

    public void setAdBannerDao(AdBannerDao adBannerDao) {
        this.adBannerDao = adBannerDao;
    }

    public CommonService getCommonService() {
        return commonService;
    }

    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    @Override
    public PageData queryAdBanners(PageParam param, String title) {

        return adBannerDao.queryAdBannerPage(param, title);
    }

    @Override
    public List<AdBanner> queryAdBanner4WX() {
        return adBannerDao.queryAdBanner4WX();
    }

    @Override
    @Transactional
    public AdBanner addAdBanner(Admin currentAdmin, AdBanner adBanner) {
        // 校验传入参数合法性。
        if (adBanner == null) {
            throw new AppException("广告对象不能为空");
        }
        logger.info("广告服务层：新增广告：广告所属管理员：" + currentAdmin.getName() + " 广告内容：" + adBanner.getContent());

        if (currentAdmin != null && currentAdmin.getAdminId() != null && !"".equals(currentAdmin.getAdminId().trim())) {
            adBanner.setAdminId(currentAdmin.getAdminId());
        }
        // 防止与已有的对象重复。
        if (adBannerDao.countByTitle(adBanner.getTitle()) > 0)
            throw new AppException("广告标题已被使用！");

        // 缺省属性给默认值。
        adBanner.setCreateDate(new Date());

        // 保存实体对象。
        return adBannerDao.save(adBanner);
    }

    @Override
    @Transactional
    public AdBanner updateImage(String adBannerId, InputStream inputStream) {
        // 校验传入参数合法性。
        if (!StringUtils.hasText(adBannerId)) {
            throw new AppException("请选择要更新的广告记录");
        }
        logger.info("广告服务层：更新图片：广告id：" + adBannerId);

        // 广告是否存在。
        AdBanner adBannerOld = adBannerDao.findOne(adBannerId);
        if (adBannerOld == null)
            throw new AppException("该广告不存在或已删除！");

        // 删除旧的图片。
        if (StringUtils.hasText(adBannerOld.getImageId()))
            commonService.deleteImage(adBannerOld.getImageId());

        // 保存图片信息。
        ImageInfo imageInfo = commonService.addImage(inputStream);

        // 更新广告的图片ID。
        adBannerOld.setImageId(imageInfo.getImageId());
        return adBannerDao.save(adBannerOld);
    }

    @Override
    @Transactional
    public AdBanner updateAdBanner(Admin currentAdmin, AdBanner adBanner) {
        // 校验传入参数合法性。
        if (adBanner == null) {
            throw new AppException("广告对象不能为空");
        }
        logger.info("广告服务层：更新广告：广告所属管理员：" + currentAdmin.getName() + " 广告内容：" + adBanner.getContent());

        // 广告是否存在。
        AdBanner adBannerOld = adBannerDao.findOne(adBanner.getAdBannerId());
        if (adBannerOld == null)
            throw new AppException("该广告不存在或已删除！");

        //只有发布此广告的管理员有权修改该广告信息
        if (currentAdmin == null || !StringUtils.hasText(currentAdmin.getAdminId())
                || !currentAdmin.getAdminId().equals(adBanner.getAdminId()))
            throw new AppException("你不能修改此广告信息");

        //更新需要修改的字段
        adBannerOld.setAdType(adBanner.getAdType());
        adBannerOld.setTitle(adBanner.getTitle());
        adBannerOld.setContent(adBanner.getContent());
        adBannerOld.setLinkUrl(adBanner.getLinkUrl());
        adBannerOld.setAdPosition(adBanner.getAdPosition());
        adBannerOld.setAdWeight(adBanner.getAdWeight());
        adBannerOld.setDuration(adBanner.getDuration());
        adBannerOld.setHits(adBanner.getHits());

        //保存已存在的实体对象
        return adBannerDao.save(adBannerOld);
    }

    @Override
    @Transactional
    public void deleteAdBanner(Admin currentAdmin, String adBannerId) {
        // 校验传入参数合法性。
        if (!StringUtils.hasText(adBannerId))
            throw new AppException("请选择要删除的广告记录");
        logger.info("广告服务层：删除广告：广告所属管理员：" + currentAdmin.getName() + " 广告ID：" + adBannerId);

        // 广告是否存在。
        AdBanner adBannerOld = adBannerDao.findOne(adBannerId);
        if (adBannerOld == null)
            throw new AppException("该广告不存在或已删除！");

        //只有发布此广告的管理员有权删除该广告信息
        if (currentAdmin == null || !StringUtils.hasText(currentAdmin.getAdminId())
                || !adBannerOld.getAdminId().equals(currentAdmin.getAdminId()))
            throw new AppException("你不能删除此广告信息");

        // 删除旧的图片。
        if (StringUtils.hasText(adBannerOld.getImageId()))
            commonService.deleteImage(adBannerOld.getImageId());

        adBannerDao.delete(adBannerId);
    }

}
