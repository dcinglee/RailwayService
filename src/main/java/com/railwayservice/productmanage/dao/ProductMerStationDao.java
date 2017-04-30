package com.railwayservice.productmanage.dao;

import com.railwayservice.productmanage.vo.ProductVo;

import java.util.List;

public interface ProductMerStationDao {

    public List<ProductVo> findProduct(String stationId, String typeId, String merchantName, String productName, int recommend);
}
