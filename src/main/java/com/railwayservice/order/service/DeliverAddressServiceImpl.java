package com.railwayservice.order.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.order.dao.DeliverAddressDao;
import com.railwayservice.order.entity.DeliverAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class DeliverAddressServiceImpl implements DeliverAddressService {

    private final Logger logger = LoggerFactory.getLogger(DeliverAddressServiceImpl.class);

    @Autowired
    private DeliverAddressDao deliverAddressDao;

    public DeliverAddressDao getDeliverAddressDao() {
        return deliverAddressDao;
    }

    public void setDeliverAddressDao(DeliverAddressDao deliverAddressDao) {
        this.deliverAddressDao = deliverAddressDao;
    }

    @Override
    @Transactional
    public DeliverAddress addDeliverAddress(DeliverAddress deliverAddress) {
        if (deliverAddress == null) {
            throw new AppException("投递的地址为空");
        }

        if (deliverAddress.getStationId() == null) {
            throw new AppException("投递的所属车站为空");
        }

        if (deliverAddress.getAddress() == null) {
            throw new AppException("投递的地址为空");
        }
        logger.info("配送地址服务层：新增送货地址：送货地址："+deliverAddress.getAddress());

        return deliverAddressDao.save(deliverAddress);
    }

    @Override
    @Transactional
    public DeliverAddress updateDeliverAddress(DeliverAddress deliverAddress) {
        if (deliverAddress == null) {
            throw new AppException("投递的地址为空");
        }

        if (deliverAddress.getStationId() == null) {
            throw new AppException("投递的所属车站为空");
        }

        if (deliverAddress.getDeliverAddressId() == null) {
            throw new AppException("投递的地址ID为空");
        }

        if (deliverAddress.getAddress() == null) {
            throw new AppException("投递的地址为空");
        }
        logger.info("配送地址服务层：修改送货地址：送货地址："+deliverAddress.getAddress());

        return deliverAddressDao.save(deliverAddress);
    }

    @Override
    @Transactional
    public void deleteDeliverAddress(DeliverAddress deliverAddress) {

        if (deliverAddress == null) {
            throw new AppException("投递的地址为空");
        }

        if (deliverAddress.getDeliverAddressId() == null) {
            throw new AppException("投递的地址ID为空");
        }
        logger.info("配送地址服务层：删除送货地址：送货地址："+deliverAddress.getAddress());

        deliverAddressDao.delete(deliverAddress);
    }

    @Override
    @Transactional
    public DeliverAddress findDeliverAddressById(String deliverAddressId) {
        logger.info("配送地址服务层：查询送货地址：送货地址ID："+deliverAddressId);

        return deliverAddressDao.findByDeliverAddressId(deliverAddressId);
    }

    @Override
    @Transactional
    public List<DeliverAddress> findDeliverAddressByStationId(String stationId) {
        if (!StringUtils.hasText(stationId)) {
            return deliverAddressDao.findAllByOrderByStationId();
        }
        logger.info("配送地址服务层：查询送货地址：车站ID："+stationId);

        return deliverAddressDao.findByStationIdOrderByOrderNoAsc(stationId);
    }

}
