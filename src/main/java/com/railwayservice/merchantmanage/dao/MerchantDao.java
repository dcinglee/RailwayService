package com.railwayservice.merchantmanage.dao;

import com.railwayservice.merchantmanage.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 商户数据库访问接口。
 *
 * @author Ewing
 */
public interface MerchantDao extends JpaRepository<Merchant, String>, JpaSpecificationExecutor<Merchant> {

    long countByAccount(String account);

    long countByNameAndAddress(String name, String address);

    /**
     * 统计已存在的电话号码的个数。
     *
     * @param phoneNo 电话号码。
     * @return 已存在的电话号码的个数
     */
    long countByPhoneNo(String phoneNo);

    /**
     * 根据手机号查询商户。
     *
     * @param phoneNo 手机号。
     * @return 商户。
     */
    Merchant findByPhoneNo(String phoneNo);

    
    /**
     * 根据商户id查找商户
     * @param merchantId
     * @return
     */
    Merchant findByMerchantId(String merchantId);
    
    /**
     * 查询当前站点的所有商户以及商户的logo地址
     *
     * @param stationId
     * @return
     */
    List<Merchant> getByStationId(String stationId);

    /**
     * 查询当前站点的所有商户以及商户的logo地址
     *
     * @param stationId
     * @param serviceTypeId
     * @return
     */
    List<Merchant> getByStationIdAndServiceTypeId(String stationId,String serviceTypeId);
    /**
     * 商户更新状态：营业中、休息中等。
     *
     * @param merchantId 商户ID。
     * @param status     新的状态。
     */
    @Modifying
    @Query("update Merchant m set m.status = :status where m.merchantId = :merchantId")
    void updateMerchantStatus(@Param("merchantId") String merchantId, @Param("status") Integer status);
}
