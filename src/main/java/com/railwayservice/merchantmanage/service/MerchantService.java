package com.railwayservice.merchantmanage.service;

import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.merchantmanage.entity.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 商户服务接口。
 *
 * @author Ewing
 */
public interface MerchantService {

    /**
     * 校验并添加单个商户。
     *
     * @param merchant 商户。
     * @return 添加成功的商户。
     */
    Merchant addMerchant(Admin currentAdmin, Merchant merchant);

    /**
     * 校验并更新单个商户。
     *
     * @param merchant 商户。
     * @return 更新成功的商户。
     */
    Merchant updateMerchant(Admin currentAdmin, Merchant merchant);

    /**
     * 查询所有商户。
     *
     * @param name     商户名称。
     * @param phoneNo  商户电话。
     * @param pageable 分页参数。
     * @return 分页数据。
     */
    Page<Merchant> queryMerchants(Admin currentAdmin, String name, String phoneNo, Pageable pageable);

    /**
     * 删除单个商户。
     *
     * @param merchantId 商户ID。
     */
    void deleteMerchant(Admin currentAdmin, String merchantId);

    /**
     * 通过ID获取商户信息。
     *
     * @param merchantId 商户ID。
     * @return 商户。
     */
    Merchant findMerchantById(String merchantId);

    /**
     * 给商户添加图片。
     */
    Merchant updateImage(String merchantId, InputStream inputStream);

    /**
     * 根据商户id获取所有的商品分类以及商品
     *
     * @param merchantId
     * @return
     * @author lid
     * @date 2017.3.1
     */
    Map<String, Object> getProductsByMerchant(String merchantId);

    /**
     * 根据手机号查询登陆对象。
     *
     * @param phoneNo 手机号。
     * @return 登陆对象。
     */
    Merchant findByPhoneNo(String phoneNo);

    /**
     * 手机号码是否已存在。
     *
     * @param phoneNo 手机号码。
     * @return 是否存在。
     */
    boolean hasPhoneNo(String phoneNo);

    Merchant changePasswordByPhoneNo(String phoneNo, String password);

    ResultMessage loginByPhoneNo(String phoneNo, String password);

    /**
     *
     * @param StationId
     * @param serviceType
     * @return List
     * @author lid
     * @date 2017.3.7
     */
    List<Merchant> findByStationIdAndServiceType(String stationId,String serviceType);
}
