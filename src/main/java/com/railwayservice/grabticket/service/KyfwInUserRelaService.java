package com.railwayservice.grabticket.service;

import com.railwayservice.grabticket.entity.KyfwInUserRela;

/**
 * 车票服务类。
 *
 * @author Ewing
 */
public interface KyfwInUserRelaService {

    KyfwInUserRela addKyfwInUserRela(KyfwInUserRela kyfwInUserRela);

    KyfwInUserRela findByUserId(String userId);

}
