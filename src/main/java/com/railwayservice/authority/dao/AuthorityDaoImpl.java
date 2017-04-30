package com.railwayservice.authority.dao;

import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.authority.vo.AuthorityInfo;
import com.railwayservice.common.dao.BaseDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限DAO实现类。
 *
 * @author Ewing
 * @date 2017/2/22
 */
public class AuthorityDaoImpl extends BaseDaoImpl {
    private final Logger logger = LoggerFactory.getLogger(AuthorityDaoImpl.class);

    /**
     * 根据名称和URL查询权限并分页。
     *
     * @param param
     * @param name        权限名称
     * @param description 权限类型
     * @return
     */
    public PageData queryAuthorityPage(PageParam param, String name, String description) {
        logger.info("queryAuthorityPage");
        List<Object> params = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("select a.authorityId,a.name,a.description,a.code,a.type," +
                " a.parentId,p.name as parentName,a.orderNo,a.menuUrl,a.icon,a.status,a.createDate " +
                "from Authority a left join Authority p on a.parentId=p.authorityId where 1=1");
        // 处理参数，动态生成SQL。
        if (StringUtils.hasText(name)) {
            sqlBuilder.append(" and a.name like ?");
            params.add("%" + name + "%");
        }
        if (StringUtils.hasText(description)) {
            sqlBuilder.append(" and a.description like ?");
            params.add("%" + description + "%");
        }
        return this.findPageObject(param, sqlBuilder.toString(), AuthorityInfo.class, params.toArray());
    }

}
