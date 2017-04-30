package com.railwayservice.common.dao;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.Map;

/**
 * 基本DAO接口。
 *
 * @author Ewing
 * @date 2017/2/22
 */
public interface BaseDao {

    /**
     * 获取数据库操作模板类。
     *
     * @return 数据库操作模板。
     */
    JdbcOperations getOperations();

    /**
     * 获取命名参数的数据库操作模板类。
     *
     * @return 可命名参数的数据库操作模板。
     */
    NamedParameterJdbcOperations getNamedParamOperations();

    /**
     * 查询单条记录为Map，无记录返回null。
     *
     * @param querySql 查询SQL。
     * @param params   参数列表。
     * @return 单条记录的Map。
     */
    Map<String, Object> findOneMap(String querySql, Object... params);

    /**
     * 查询单个对象，无记录则返回null。
     *
     * @param querySql 查询SQL。
     * @param clazz    对象类型。
     * @param params   参数列表。
     * @return 指定类型的对象。
     */
    <E> E findOneObject(String querySql, Class<E> clazz, Object... params);

    /**
     * 获取分页的数据，若总数为0则不查询数据行。
     *
     * @param querySql 查询的SQL语句。
     * @param params   参数列表。
     * @return 当前页的数据，每行为Map类型。
     */
    PageData findPageMap(PageParam pageParam, String querySql, Object... params);

    /**
     * 获取分页的数据，若总数为0则不查询数据行。
     *
     * @param querySql 查询的SQL语句。
     * @param clazz    业务对象类Class。
     * @param params   参数列表。
     * @return 当前页的数据，返回自定义类型。
     */
    PageData findPageObject(PageParam pageParam, String querySql, Class<?> clazz, Object... params);

}
