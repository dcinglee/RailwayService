package com.railwayservice.common.dao;

import com.railwayservice.common.entity.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.List;

/**
 * 字典数据库访问类。
 *
 * @author Ewing
 */
public interface DictionaryDao extends JpaRepository<Dictionary, Integer> {

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    List<Dictionary> findByTypeOrderByValue(String type);

}
