package com.railwayservice.grabticket.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.grabticket.dao.KyfwInUserRelaDao;
import com.railwayservice.grabticket.entity.KyfwInUserRela;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 乘车人信息服务类。
 *
 * @author Ewing
 */
@Service
public class KyfwInUserRelaServiceImpl implements KyfwInUserRelaService {
    private final Logger logger = LoggerFactory.getLogger(KyfwInUserRelaServiceImpl.class);

    private KyfwInUserRelaDao kyfwInUserRelaDao;

    @Autowired
    public void setKyfwInUserRelaDao(KyfwInUserRelaDao kyfwInUserRelaDao) {
        this.kyfwInUserRelaDao = kyfwInUserRelaDao;
    }

    @Override
    public KyfwInUserRela addKyfwInUserRela(KyfwInUserRela kyfwInUserRela) {
        if (null == kyfwInUserRela) {
            throw new AppException("未绑定账号！");
        }
        // 设置默认值
        kyfwInUserRela.setCreateDate(new Date());
        //保存实体对象
        return kyfwInUserRelaDao.save(kyfwInUserRela);
    }

    @Override
    public KyfwInUserRela findByUserId(String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new AppException("未绑定账号");
        }
        return kyfwInUserRelaDao.findByUserId(userId);
    }
}
