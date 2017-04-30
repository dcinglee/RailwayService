package com.railwayservice.common.dao;

import com.railwayservice.common.entity.ImageInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 图片存取类。
 *
 * @author Ewing
 */
public interface ImageInfoDao extends JpaRepository<ImageInfo, String> {

}
