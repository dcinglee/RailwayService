package com.railwayservice.order.service;

import com.railwayservice.order.entity.DeliverAddress;

import java.util.List;

/**
 * 送货地址
 *
 * @author xuyu
 */
public interface DeliverAddressService {

    DeliverAddress addDeliverAddress(DeliverAddress deliverAddress);

    DeliverAddress updateDeliverAddress(DeliverAddress deliverAddress);

    void deleteDeliverAddress(DeliverAddress deliverAddress);

    DeliverAddress findDeliverAddressById(String deliverAddress);

    List<DeliverAddress> findDeliverAddressByStationId(String stationId);

}
