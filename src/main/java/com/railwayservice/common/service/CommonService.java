package com.railwayservice.common.service;

import com.railwayservice.common.entity.Dictionary;
import com.railwayservice.common.entity.ImageInfo;
import com.railwayservice.common.entity.Location;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 业务服务接口。
 *
 * @author Ewing
 */
public interface CommonService {
    /**
     * 获取字典值。
     */
    List<Dictionary> findDictionaryByType(String type);

    /**
     * 获取地理位置。
     */
    List<Location> findLocationByParentId(String parentId);

    /**
     * 根据ID获取图片。
     */
    ImageInfo getImageInfoById(String imageId);

    /**
     * 添加图片的方法。
     */
    ImageInfo addImage(InputStream inputStream);

    /**
     * 删除图片的方法。
     **/
    void deleteImage(String imageId);

    /**
     * 根据图片ID获取图片地址。
     *
     * @param imageId 图片ID。
     * @return 图片地址。
     */
    String getImageUrl(String imageId);

    /**
     * 根据图片ID获取图片地址。
     *
     * @param imageIds 图片ID集合。
     * @return 图片地址集合。
     */
    Map<String, String> getImageUrls(List<String> imageIds);
}
