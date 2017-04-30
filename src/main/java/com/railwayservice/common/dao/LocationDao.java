package com.railwayservice.common.dao;

import com.railwayservice.common.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * 地理位置数据库访问类。
 *
 * @author Ewing
 */
public interface LocationDao extends JpaRepository<Location, String> {
    /**
     * 获取地理位置。
     */
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<Location> findByParentIdOrderBySort(String parentId);
}
