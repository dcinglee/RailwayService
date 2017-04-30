package com.railwayservice.order.dao;

import com.railwayservice.order.entity.DeliverAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 送货地址DAO
 *
 * @author xuyu
 */
public interface DeliverAddressDao extends JpaRepository<DeliverAddress, String>, JpaSpecificationExecutor<DeliverAddress> {

    /**
     * 通过ID查找送货地址
     *
     * @param id
     * @return
     */
    DeliverAddress findByDeliverAddressId(String id);

    /**
     * 通过车站查找投递地址
     *
     * @param stationId
     * @return
     */
    List<DeliverAddress> findByStationIdOrderByOrderNoAsc(String stationId);
    
    List<DeliverAddress> findAllByOrderByStationId();

}
