package com.railwayservice.order.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.order.dao.SubOrderDao;
import com.railwayservice.order.entity.SubOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.List;

/**
 * 子单管理服务实现类
 *
 * @author lid
 * @date 2017.2.10
 */
@Service
public class SubOrderServiceImpl implements SubOrderService {
    private final Logger logger = LoggerFactory.getLogger(SubOrderServiceImpl.class);

    private SubOrderDao subOrderDao;

    @Autowired
    public void setSubOrderDao(SubOrderDao subOrderDao) {
        this.subOrderDao = subOrderDao;
    }

    @Override
    public SubOrder findSubOrderBySubOrderId(String id) {
        logger.info("子单服务层：查询子单：子订单ID" + id);
        Specification<SubOrder> specification = (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (StringUtils.hasText(id)) {
                predicate = builder.and(predicate, builder.like(root.get("subOrderId"), "%" + id + "%"));
            }
            return predicate;
        };
        return subOrderDao.findOne(specification);
    }

    @Override
    public List<SubOrder> findSubOrdersByMainOrderId(String mainOrderId) {
        if (!StringUtils.hasText(mainOrderId)) {
            throw new AppException("订单号为空！");
        }
        logger.info("子单服务层：查询子单：主订单ID" + mainOrderId);

        Specification<SubOrder> specification = (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (StringUtils.hasText(mainOrderId)) {
                predicate = builder.and(predicate, builder.like(root.get("mainOrderId"), "%" + mainOrderId + "%"));
            }
            return predicate;
        };
        return subOrderDao.findAll(specification);
    }

    @Override
    public SubOrder addSubOrder(SubOrder subOrder) {
        if (null == subOrder) {
            throw new AppException("请输入要添加的子订单信息！");
        }
        logger.info("子单服务层：新增子单：子订单id" + subOrder.getSubOrderId());

        subOrder.setCreateDate(new Date());
        return subOrderDao.save(subOrder);
    }

    @Override
    public SubOrder updateSubOrder(SubOrder subOrder) {
        if (null == subOrder) {
            throw new AppException("请选择要修改的子订单！");
        }
        logger.info("子单服务层：更新子单：子订单ID：" + subOrder.getSubOrderId());

        SubOrder oldSubOrder = subOrderDao.findSubOrderBySubOrderId(subOrder.getSubOrderId());

        if (null == oldSubOrder) {
            throw new AppException("没找到对应的子单记录！");
        }

        return subOrderDao.save(oldSubOrder);
    }

    @Override
    public void deleteSubOrder(String subOrderId) {
        if (null == subOrderId) {
            throw new AppException("请选择要删除的子订单！");
        }
        logger.info("子单服务层：删除子单：子订单ID:" + subOrderId);

        SubOrder subOrder = subOrderDao.findSubOrderBySubOrderId(subOrderId);

        if (null == subOrder) {
            throw new AppException("没找到对应的子单记录！");
        }

        subOrderDao.delete(subOrder);
    }

    @Override
    public SubOrder cancelSubOrder(String subOrderId) {
        SubOrder subOrder = subOrderDao.findSubOrderBySubOrderId(subOrderId);
        if (null == subOrder) {
            throw new AppException("没找到对应的子订单信息！");
        }
        logger.info("子单服务层：取消子单：子订单ID:" + subOrderId);

        return subOrderDao.save(subOrder);
    }

}
