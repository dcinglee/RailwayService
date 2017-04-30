package com.railwayservice.messages.service;

import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.push.PushFactory;
import com.railwayservice.application.push.PushService;
import com.railwayservice.application.push.PushServiceType;
import com.railwayservice.application.util.TimeUtil;
import com.railwayservice.merchantmanage.dao.MerchantDao;
import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.merchantmanage.service.MerchantStatic;
import com.railwayservice.messages.dao.ChannelInfoDao;
import com.railwayservice.messages.entity.ChannelInfo;
import com.railwayservice.serviceprovider.dao.ServiceProviderDao;
import com.railwayservice.serviceprovider.entity.ServiceProvider;
import com.railwayservice.serviceprovider.service.ServiceProviderStatic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.List;

@Service
@PropertySource("classpath:common.properties")
public class ChannelInfoServiceImpl implements ChannelInfoService {

    private final Logger logger = LoggerFactory.getLogger(ChannelInfoServiceImpl.class);

    @Value("${heartBeatInteral}")
    private long heartBeatInterval;

    @Autowired
    private ChannelInfoDao channelInfoDao;

    @Autowired
    private MerchantDao merchantDao;

    @Autowired
    private ServiceProviderDao serviceProviderDao;

    public MerchantDao getMerchantDao() {
        return merchantDao;
    }

    public void setMerchantDao(MerchantDao merchantDao) {
        this.merchantDao = merchantDao;
    }

    public ServiceProviderDao getServiceProviderDao() {
        return serviceProviderDao;
    }

    public void setServiceProviderDao(ServiceProviderDao serviceProviderDao) {
        this.serviceProviderDao = serviceProviderDao;
    }

    public ChannelInfoDao getChannelInfoDao() {
        return channelInfoDao;
    }

    public void setChannelInfoDao(ChannelInfoDao channelInfoDao) {
        this.channelInfoDao = channelInfoDao;
    }

    @Override
    public ChannelInfo updateChannelInfoAndMerchantServiceStatus(String userId, String channelId, String deviceType, Integer status) {
        if (userId == null) {
            throw new AppException("找不到userId");
        }
        if (channelId == null) {
            throw new AppException("找不到channelId");
        }
        ChannelInfo channelInfo = channelInfoDao.findByUserIdAndChannelId(userId, channelId);
        if (channelInfo == null) {
            channelInfo = new ChannelInfo();
            channelInfo.setStatus(ChannelConst.CHANNEL_DEVICE_WORK);
        }
        channelInfo.setUserId(userId);
        channelInfo.setChannelId(channelId);
        channelInfo.setDeviceType(deviceType);
        //如果前台传过来的状态为空，则不更新状态字段，只更新时间
        if (status != null) {
            channelInfo.setStatus(status);

            //更新商户状态
            Merchant merchant = merchantDao.findByMerchantId(userId);
            if (merchant != null) {
                if (ChannelConst.CHANNEL_DEVICE_REST == status) {
                    merchant.setStatus(MerchantStatic.MERCHANT_STATUS_REST);
                } else {
                    merchant.setStatus(MerchantStatic.MERCHANT_STATUS_WORK);
                }
                merchantDao.save(merchant);
            }

            ServiceProvider serviceProvider = serviceProviderDao.findByServiceProviderId(userId);
            if (serviceProvider != null) {
                if (ChannelConst.CHANNEL_DEVICE_REST == status) {
                    serviceProvider.setStatus(ServiceProviderStatic.SERVICEPROVIDER_STOP);
                } else {
                    serviceProvider.setStatus(ServiceProviderStatic.SERVICEPROVIDER_NORMAL);
                }
                serviceProviderDao.save(serviceProvider);
            }
        }
        channelInfo.setLastTime(TimeUtil.getTimestamp());
        return channelInfoDao.save(channelInfo);

    }

    @Override
    public ChannelInfo updateChannelInfo(String userId, String channelId, String deviceType, Integer status) {

        if (userId == null || "".equals(userId)) {
            logger.info("updateChannelInfo 找不到userId，userId=" + userId);
            throw new AppException("找不到userId");
        }

        if (channelId == null || "".equals(channelId)) {
            logger.info("updateChannelInfo 找不到channelId，channelId=" + channelId);
            throw new AppException("找不到channelId");
        }

        ChannelInfo channelInfo = channelInfoDao.findByUserIdAndChannelId(userId, channelId);
        if (channelInfo == null) {
            channelInfo = new ChannelInfo();
            channelInfo.setStatus(ChannelConst.CHANNEL_DEVICE_WORK);
        }

        channelInfo.setUserId(userId);
        channelInfo.setChannelId(channelId);
        channelInfo.setDeviceType(deviceType);
        //如果前台传过来的状态为空，则不更新状态字段，只更新时间
        if (status != null) {
            channelInfo.setStatus(status);
        }
        channelInfo.setLastTime(TimeUtil.getTimestamp());

        return channelInfoDao.save(channelInfo);
    }

    private List<ChannelInfo> getChannelInfoByUserIdAndStatus(String userId, Integer status) {

        if (!StringUtils.hasText(userId)) {
            throw new AppException("查询用户手机信道的时候用户ID不允许为空");
        }

        final Long lastTime = (TimeUtil.getTimestamp() - (heartBeatInterval * 60 * 1000));

        // JPA标准查询接口，使用Lambda表达式。root以商户类为根对象。
        Specification<ChannelInfo> specification = (root, query, builder) -> {
            // 创建组合条件，默认1=1。
            Predicate predicate = builder.conjunction();
            // 如果名称不为空，添加到查询条件。
            if (StringUtils.hasText(userId)) {
                predicate = builder.and(predicate, builder.equal(root.get("userId"), userId));
            }

            if (status != null) {
                predicate = builder.and(predicate, builder.equal(root.get("status"), status));
            }

            predicate = builder.and(predicate, builder.ge(root.get("lastTime"), lastTime));

            return predicate;
        };
        // 调用JPA标准查询接口查询数据。

        return channelInfoDao.findAll(specification);

    }

    @Override
    public List<ChannelInfo> getOnlineWorkChannelInfoByUserId(String userId) {
        return getChannelInfoByUserIdAndStatus(userId, ChannelConst.CHANNEL_DEVICE_WORK);
    }

    @Override
    public List<ChannelInfo> getOnlineWorkServiceProviderInfoByStationIdAndServiceType(String stationId, String serviceType) {

        Long lastTime = (TimeUtil.getTimestamp() - (heartBeatInterval * 60 * 1000));
        return channelInfoDao.getOnlineWorkServiceProviderChannelInfo(stationId, serviceType, ServiceProviderStatic.SERVICEPROVIDER_NORMAL, ChannelConst.CHANNEL_DEVICE_WORK, lastTime);
    }

    @Override
    public List<ChannelInfo> getOnlineWorkMerchantInfoByStationIdAndServiceType(String stationId, String serviceType) {
        Long lastTime = (TimeUtil.getTimestamp() - (heartBeatInterval * 60 * 1000));
        return channelInfoDao.getOnlineWorkMerchantChannelInfo(stationId, serviceType, MerchantStatic.MERCHANT_STATUS_WORK, ChannelConst.CHANNEL_DEVICE_WORK, lastTime);
    }

    @Override
    public List<ChannelInfo> getOnlineWorkMerchantChannelInfoByMerchantId(String merchantId) {
        Long lastTime = (TimeUtil.getTimestamp() - (heartBeatInterval * 60 * 1000));
        return channelInfoDao.getOnlineWorkMerchantChannelInfoByMerchantId(merchantId, MerchantStatic.MERCHANT_STATUS_WORK, ChannelConst.CHANNEL_DEVICE_WORK, lastTime);
    }

    @Override
    public void pushMessageToBatch4ServiceProvider(List<ChannelInfo> channelInfos, String title, String description) {
        if (channelInfos != null) {
            String[] channelIds = new String[channelInfos.size()];
            ChannelInfo info;
            PushService service = PushFactory.getPushService(PushServiceType.JIGUANG);
            for (int i = 0; i < channelInfos.size(); i++) {
                info = channelInfos.get(i);
                channelIds[i] = info.getChannelId();
                service.pushSms4ServiceProvider(channelIds[i], title, description);
            }

        }
    }

    @Override
    public void pushMessageToBatch4Merchant(List<ChannelInfo> channelInfos, String title, String description) {
        if (channelInfos != null) {
            String channelId = null;
            ChannelInfo info;
            PushService service = PushFactory.getPushService(PushServiceType.JIGUANG);
            for (int i = 0; i < channelInfos.size(); i++) {
                info = channelInfos.get(i);
                channelId = info.getChannelId();
                service.pushSms4Merchant(channelId, title, description);
            }

        }
    }

    @Override
    public void pushNoticeToServiceProvider(String serviceProviderId, String title, String content) {
        try {
            List<ChannelInfo> channels = this.getOnlineWorkChannelInfoByUserId(serviceProviderId);
            if (channels != null && channels.size() > 0) {
                this.pushMessageToBatch4ServiceProvider(channels, title, content);
            }
        } catch (Exception e) {
            logger.error("发送新订单给服务人员时出现错误!" + e, e);
        }
    }

    @Override
    public void pushNoticeToStationServiceProvider(String stationId, String serviceTypeId, String title, String content) {
        try {
            //通知配送人员抢单
            List<ChannelInfo> channels = this.getOnlineWorkServiceProviderInfoByStationIdAndServiceType(stationId, serviceTypeId);
            if (channels != null && channels.size() > 0) {
                this.pushMessageToBatch4ServiceProvider(channels, title, content);
            }
        } catch (Exception e) {
            logger.error("发送新订单给服务人员时出现错误!" + e, e);
        }
    }

    @Override
    public boolean hasServiceProviderOnline(String stationId, String serviceType) {
        List<ChannelInfo> results = this.getOnlineWorkServiceProviderInfoByStationIdAndServiceType(stationId, serviceType);
        if (results != null && results.size() > 0) return true;
        return false;
    }

    @Override
    public boolean hasMerchantOnline(String merchantId) {
        List<ChannelInfo> results = getOnlineWorkMerchantChannelInfoByMerchantId(merchantId);
        if (results != null && results.size() > 0) return true;
        return false;
    }
}
