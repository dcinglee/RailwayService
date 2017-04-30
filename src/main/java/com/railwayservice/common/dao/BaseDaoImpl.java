package com.railwayservice.common.dao;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * 基本DAO实现，提供基本的单条查询和分页查询。
 * 更多的查询方法请使用jdbcOperations中的方法。
 *
 * @author Ewing
 * @date 2017/2/22
 */
@Repository
public class BaseDaoImpl implements BaseDao {
    private final Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);

    private JdbcOperations operations;
    private NamedParameterJdbcOperations namedParamOperations;

    @Override
    public JdbcOperations getOperations() {
        return operations;
    }

    @Autowired
    public void setOperations(JdbcOperations operations) {
        this.operations = operations;
    }

    @Override
    public NamedParameterJdbcOperations getNamedParamOperations() {
        return namedParamOperations;
    }

    @Autowired
    public void setNamedParamOperations(NamedParameterJdbcOperations namedParamOperations) {
        this.namedParamOperations = namedParamOperations;
    }

    /**
     * 查询单条记录为Map，无记录返回null。
     */
    @Override
    public Map<String, Object> findOneMap(String querySql, Object... params) {
        logger.info("SQL语句：" + querySql + "\n参数：" + Arrays.toString(params));
        try {
            return this.operations.queryForMap(querySql, params);
        } catch (EmptyResultDataAccessException e) {
            logger.info("没有查询到记录！");
            return null;
        }
    }

    /**
     * 查询单个对象，无记录则返回null。
     */
    @Override
    public <E> E findOneObject(String querySql, Class<E> clazz, Object... params) {
        logger.info("SQL语句：" + querySql + "\n参数：" + Arrays.toString(params) + "\n期望类型：" + clazz.getName());
        try {
            return this.operations.queryForObject(querySql, BeanPropertyRowMapper.newInstance(clazz), params);
        } catch (EmptyResultDataAccessException e) {
            logger.info("没有查询到记录！");
            return null;
        }
    }

    /**
     * 获取分页的数据，若总数为0则不查询数据行。
     */
    @Override
    public PageData findPageMap(PageParam pageParam, String querySql, Object... params) {
        logger.info("SQL语句：" + querySql + "\n参数：" + Arrays.toString(params));
        PageData pageData = new PageData();
        if (pageParam.isCount()) {
            pageData.setTotal(this.operations.queryForObject("select count(1) from ( " + querySql + " ) as _total_", Long.class, params));
            // 没有数据行则返回空的List。
            if (pageData.getTotal() < 1)
                return pageData.setRows(new ArrayList<>(0));
        }
        String sql = querySql + " limit " + pageParam.getOffset() + "," + pageParam.getLimit();
        return pageData.setRows(this.operations.queryForList(sql, params));
    }

    /**
     * 获取分页的数据，若总数为0则不查询数据行。
     */
    @Override
    public PageData findPageObject(PageParam pageParam, String querySql, Class<?> clazz, Object... params) {
        logger.info("SQL语句：" + querySql + "\n参数：" + Arrays.toString(params) + "\n期望类型：" + clazz.getName());
        PageData pageData = new PageData();
        if (pageParam.isCount()) {
            pageData.setTotal(this.operations.queryForObject("select count(1) from ( " + querySql + " ) as _total_", Long.class, params));
            // 没有数据行则返回空的List。
            if (pageData.getTotal() < 1)
                return pageData.setRows(new ArrayList<>(0));
        }
        String sql = querySql + " limit " + pageParam.getOffset() + "," + pageParam.getLimit();
        return pageData.setRows(this.operations.query(sql, BeanPropertyRowMapper.newInstance(clazz), params));
    }

}
