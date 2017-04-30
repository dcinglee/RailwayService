package com.railwayservice.order.service;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.util.OrderUtil;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.common.entity.ImageInfo;
import com.railwayservice.common.service.CommonService;
import com.railwayservice.common.service.TimedTaskSchedule;
import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.merchantmanage.service.MerchantService;
import com.railwayservice.messages.entity.ChannelInfo;
import com.railwayservice.messages.service.ChannelInfoService;
import com.railwayservice.order.dao.MainOrderDao;
import com.railwayservice.order.dao.SubOrderDao;
import com.railwayservice.order.entity.DeliverAddress;
import com.railwayservice.order.entity.MainOrder;
import com.railwayservice.order.entity.ShoppingCart;
import com.railwayservice.order.entity.SubOrder;
import com.railwayservice.order.vo.*;
import com.railwayservice.productmanage.entity.Product;
import com.railwayservice.productmanage.service.ProductService;
import com.railwayservice.serviceprovider.dao.ServiceProviderDao;
import com.railwayservice.serviceprovider.entity.ServiceProvider;
import com.railwayservice.serviceprovider.entity.ServiceType;
import com.railwayservice.serviceprovider.service.ServiceTypeService;
import com.railwayservice.stationmanage.entity.RailwayStation;
import com.railwayservice.stationmanage.entity.StationForImageRela;
import com.railwayservice.stationmanage.service.RailwayStationService;
import com.railwayservice.stationmanage.service.StationForImageRelaService;
import com.railwayservice.user.entity.User;
import com.railwayservice.user.service.UserService;
import com.railwayserviceWX.util.WechatPayUtil;
import com.railwayserviceWX.vo.BrandWCPayParameterVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lidx
 * @date 2017年2月8日
 * @describe 订单信息服务类接口实现
 */

@Service
public class MainOrderServiceImpl implements MainOrderService {

    //通知商户的标题
    private static final String title = "订单已取消";

    //通知商户的内容
    private static final String description = "您的商户有一条订单被客户取消，请及时处理！";

    private final Logger logger = LoggerFactory.getLogger(MainOrderServiceImpl.class);

    private ServiceProviderDao serviceProviderDao;

    private MainOrderDao mainOrderDao;

    private SubOrderDao subOrderDao;

    private SubOrderService subOrderService;

    private ProductService productService;

    private MerchantService merchantService;

    private DeliverAddressService deliverAddressService;

    private ShoppingCartService shoppingCartService;

    private ServiceTypeService serviceTypeService;

    private UserService userService;

    private ChannelInfoService channelInfoService;

    private CommonService commonService;

    private OrderStatusRecordService orderStatusRecordService;

    private RailwayStationService railwayStationService;

    private StationForImageRelaService stationForImageRelaService;

    @Autowired
    public void setStationForImageRelaService(StationForImageRelaService stationForImageRelaService) {
        this.stationForImageRelaService = stationForImageRelaService;
    }

    @Autowired
    public void setRailwayStationService(RailwayStationService railwayStationService) {
        this.railwayStationService = railwayStationService;
    }

    @Autowired
    public void setOrderStatusRecordService(OrderStatusRecordService orderStatusRecordService) {
        this.orderStatusRecordService = orderStatusRecordService;
    }

    @Autowired
    public void setServiceProviderDao(ServiceProviderDao serviceProviderDao) {
        this.serviceProviderDao = serviceProviderDao;
    }

    @Autowired
    public void setChannelInfoService(ChannelInfoService channelInfoService) {
        this.channelInfoService = channelInfoService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setServiceTypeService(ServiceTypeService serviceTypeService) {
        this.serviceTypeService = serviceTypeService;
    }

    @Autowired
    public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Autowired
    public void setDeliverAddressService(DeliverAddressService deliverAddressService) {
        this.deliverAddressService = deliverAddressService;
    }

    @Autowired
    public void setMerchantService(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setSubOrderService(SubOrderService subOrderService) {
        this.subOrderService = subOrderService;
    }

    @Autowired
    public void setSubOrderDao(SubOrderDao subOrderDao) {
        this.subOrderDao = subOrderDao;
    }

    @Autowired
    public void setMainOrderDao(MainOrderDao mainOrderDao) {
        this.mainOrderDao = mainOrderDao;
    }

    @Autowired
    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    @Override
    public PageData findMainOrders(PageParam pageParam, QueryOrderParam param) {
        if (param == null)
            throw new AppException("订单参数对象不能为空！");
        return mainOrderDao.queryMainOrderPage(pageParam, param);
    }

    /**
     * 用户确认订单
     */
    @Override
    @Transactional
    public Map<String, Object> affirmOrder(User user, RailwayStation presentStation, String merchantId) {
        if (null == user) {
            throw new AppException("缺少用户信息！");
        }

        if (null == presentStation) {
            throw new AppException("缺少当前车站信息！");
        }
        logger.info("用户确认订单 ：用户名称：" + user.getName());

        //保存用户信息
        Map<String, Object> mapResult = new HashMap<String, Object>();
        mapResult.put("user", user);

        //保存收货位置信息
        List<DeliverAddress> listDeliverAddress = deliverAddressService.findDeliverAddressByStationId(presentStation.getStationId());
        mapResult.put("listDeliverAddress", listDeliverAddress);

        //保存购物车信息
        List<ShoppingCart> listShoppingCart = shoppingCartService.findByUserIdAndMerchantId(user.getUserId(), merchantId);
        mapResult.put("listShoppingCart", listShoppingCart);

        //获取示意图URL
        List<String> stationForImageRelaList = stationForImageRelaService.findByStationId(presentStation.getStationId());
        mapResult.put("stationForImageRelaList", stationForImageRelaList);

        //获取配送费用
        Merchant merchant = merchantService.findMerchantById(merchantId);
        if (null == merchant) {
            throw new AppException("获取商户信息失败！");
        }

        ServiceType serviceType = serviceTypeService.findServiceType(merchant.getServiceTypeId());
        if (null == serviceType) {
            throw new AppException("商户的服务类型为空！");
        }

        //保存配送费用
        mapResult.put("distributionCosts", serviceType.getDistributionCosts());
        //保存商户信息
        mapResult.put("merchant", merchant);

        return mapResult;
    }

    /**
     * 用户确认支付下订单。
     * 处理逻辑如下：
     * 1，获取参数值，生成订单；
     * 2，调用微信支付；
     * 3，通知商户(支付成功回调函数中通知商户)
     */
    @Override
    @Transactional
    public BrandWCPayParameterVo createOrder(OrderParamVo vo, User user, String addrip) {

        //根据用户输入的数据保存用户信息
        if (StringUtils.hasText(vo.getCustomerName())) {
            user.setName(vo.getCustomerName());
        }

        if (StringUtils.hasText(vo.getCustomerPhoneNo())) {
            user.setPhoneNo(vo.getCustomerPhoneNo());
        }

        if ((StringUtils.hasText(vo.getCustomerPhoneNo()))
                || (StringUtils.hasText(vo.getCustomerName()))) {
            userService.updateUser(user);
        }
        logger.info("用户确认支付下订单: 用户名称：" + user.getName());

        //获取子单信息
        List<ProductInputParamVo> listProduct = vo.getListProduct();
        if (0 == listProduct.size()) {
            throw new AppException("缺少商品信息！");
        }

        //保存主订单
        //设置主订单属性
        MainOrder mainOrder = new MainOrder();

        Merchant merchant = merchantService.findMerchantById(vo.getMerchantId());

        ServiceType serviceType = serviceTypeService.findServiceType(merchant.getServiceTypeId());

        //设置服务类型
        mainOrder.setServiceTypeId(merchant.getServiceTypeId());

        //设置配送费用
        if (vo.getDeliverType() == OrderStatic.DELIVER_TYPE_SEND) {
            mainOrder.setDistributionCosts(serviceType.getDistributionCosts());
        }

        mainOrder = SetMainOrderInitialData(mainOrder, vo, user.getUserId());

        //保存主订单
        logger.info("保存主订单");
        MainOrder resultMainOrder = mainOrderDao.save(mainOrder);

        List<SubOrder> listSubOrder = SetSubOrderInitialData(resultMainOrder, vo);
        if (0 == listSubOrder.size()) {
            logger.info("设置子单异常");
            throw new AppException("设置子单异常！");
        }

        //保存子订单
        logger.info("保存子订单");
        for (SubOrder subOrder : listSubOrder) {
            subOrderDao.save(subOrder);
        }

        //调用微信支付接口
        logger.info("调用微信支付接口");

        BrandWCPayParameterVo brandWCPayParameterVo = WechatPayUtil.getPayParameters(merchant, user.getOpenid(), resultMainOrder, addrip, listSubOrder);
        if(null == brandWCPayParameterVo){
        	throw new AppException("微信支付异常！");
        }
        brandWCPayParameterVo.setOrderId(resultMainOrder.getOrderId());
        return brandWCPayParameterVo;
    }

    @Override
    @Transactional
    public MainOrder cancelMainOrder(String mainOrderId, String reason) {
        if (null == mainOrderId) {
            throw new AppException("mainOrderId参数为空！");
        }
        logger.info("主订单取消,主订单ID:" + mainOrderId);

        MainOrder mainOrder = mainOrderDao.findOne(mainOrderId);
        if (null == mainOrder) {
            throw new AppException("未获取主订单信息！");
        }

        //判断订单状态，
        if (OrderStatic.MAINORDER_STATUS_COMPLETED == mainOrder.getOrderStatus()) {
            throw new AppException("订单已完成，不能取消！");
        }

        //如果目前商家还未接单，则订单直接取消
        if (OrderStatic.MAINORDER_STATUS_WAIT_ACCEPT == mainOrder.getOrderStatus()) {
            logger.info("如果目前商家还未接单，则订单直接取消");
            mainOrder.setOrderStatus(OrderStatic.MAINORDER_STATUS_CANCELED);
            mainOrder.setOrderCancelStatus(OrderStatic.MAINORDER_CANCEL_STATUS_AGREE);
            mainOrder.setCancelReason(reason);
            mainOrder.setUpdateDate(new Date());
            MainOrder newMainOrder = mainOrderDao.save(mainOrder);
            if (null == newMainOrder.getRefundId()) {
                this.refundOrder(newMainOrder);
            }

            //从定时任务的map中删除订单
            if (TimedTaskSchedule.waitAcceptOrders.containsKey(mainOrder.getOrderId())) {
                TimedTaskSchedule.waitAcceptOrders.remove(mainOrder.getOrderId());
            }

            //保存订单状态记录
            orderStatusRecordService.addRecordNoException(mainOrder.getOrderId(), OrderStatic.MAINORDER_STATUS_CANCELED, null);

            logger.info("orderstatus:" + mainOrder.getOrderStatus());
            logger.info("cancelstatus:" + mainOrder.getOrderCancelStatus());
            return newMainOrder;

        }
        //设置订单取消状态，主状态不变
        mainOrder.setOrderCancelStatus(OrderStatic.MAINORDER_CANCEL_STATUS_APPEAL);
        mainOrder.setCancelReason(reason);
        mainOrder.setUpdateDate(new Date());
        MainOrder newMainOrder = mainOrderDao.save(mainOrder);
        //通知商户订单取消

        //4, 通知商户 改为 通知服务人员

        //已有配送人员抢单
        if (mainOrder.getServiceProviderId() != null && !"".equals(mainOrder.getServiceProviderId())) {
            channelInfoService.pushNoticeToServiceProvider(mainOrder.getServiceProviderId(),
                    "订单取消", "有订单[" + mainOrder.getOrderNo() + "]申请取消，点击查看详情");
        }

        /*List<ChannelInfo> listChannelInfo = channelInfoService.getOnlineWorkChannelInfoByUserId(mainOrder.getMerchantId());
        channelInfoService.pushMessageToBatch4Merchant(listChannelInfo, title, description);*/

        //保存订单状态记录
        orderStatusRecordService.addRecordNoException(mainOrder.getOrderId(), OrderStatic.MAINORDER_CANCEL_STATUS_APPEAL, reason);

        return newMainOrder;
    }

    @Override
    @Transactional
    public MainOrder expireMainOrder(String mainOrderId) {
        MainOrder mainOrder = mainOrderDao.findOne(mainOrderId);
        if (null == mainOrder) {
            throw new AppException("未找到订单记录！");
        }
        logger.info("失效主订单,主订单ID=" + mainOrderId);

        //如果订单状态为完成则不能失效
        if (OrderStatic.MAINORDER_STATUS_COMPLETED == mainOrder.getOrderStatus()) {
            throw new AppException("主订单已完成，不能失效！");
        }

        //设置主订单状态为已失效
        mainOrder.setOrderStatus(OrderStatic.MAINORDER_STATUS_EXPIRE);

        return mainOrderDao.save(mainOrder);
    }

    @Override
    public List<MainOrder> queryOrdersByUser(String userId, Date startDate, Date endDate) {
        if (null == userId) {
            throw new AppException("未获取当前用户信息！");
        }
        logger.info("用户获取主订单列表，用户ID=" + userId + ",开始日期=" + startDate + ",结束日期=" + endDate);
        Specification<MainOrder> specification = (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (StringUtils.hasText(userId)) {
                predicate = builder.and(predicate, builder.like(root.get("userId"), "%" + userId + "%"));
            }

            if (startDate != null) {
                predicate = builder.and(predicate, builder.greaterThanOrEqualTo(root.get("createDate"), startDate));
            }

            if (endDate != null) {
                predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get("createDate"), endDate));
            }
            query.orderBy(builder.desc(root.get("createDate")));
            return predicate;
        };
        List<MainOrder> listOrder = mainOrderDao.findAll(specification);
        return listOrder;
    }

    /**
     * 添加订单时设置主订单的初始状态。
     * 其余未设置的信息目前计划由前端构造对象之后传入
     *
     * @return MainOrder  主订单对象
     * @author lid
     * @date 2017.2.13
     */
    @Transactional
    public MainOrder SetMainOrderInitialData(MainOrder mainOrder, OrderParamVo vo, String userId) {
        logger.info("SetMainOrderInitialData!");

        //设置用户信息
        mainOrder.setUserId(userId);

        //设置订单号
        mainOrder.setOrderNo(OrderUtil.GenerateOrderNo());

        //设置订单状态   初始状态为未支付
        mainOrder.setOrderStatus(OrderStatic.MAINORDER_STATUS_WAIT_PAY);
        mainOrder.setUpdateDate(new Date());

        //设置支付状态  初始状态为未支付
        mainOrder.setPayStatus(OrderStatic.PAY_STATUS_UNPAYED);

        //设置支付类型为微信支付
        mainOrder.setPayType(OrderStatic.PAY_TYPE_WEIXIN);

        //设置客户姓名和联系电话
        if (StringUtils.hasText(vo.getCustomerName())) {
            mainOrder.setCustomerName(vo.getCustomerName());
        }

        if (StringUtils.hasText(vo.getCustomerPhoneNo())) {
            mainOrder.setCustomerPhoneNo(vo.getCustomerPhoneNo());
        }

        //计算商品总价
        BigDecimal productTotalPrice = new BigDecimal(0);
        logger.info("计算商品总价");

        List<ProductInputParamVo> listProductInputParamVo = vo.getListProduct();
        if (0 < listProductInputParamVo.size()) {
            for (ProductInputParamVo productInputParamVo : listProductInputParamVo) {
                logger.info("productId:" + productInputParamVo.getProductId());

                Product product = productService.findProductById(productInputParamVo.getProductId());
                productTotalPrice = productTotalPrice.add(product.getPrice().multiply(new BigDecimal(productInputParamVo.getQuantity())));
            }
        }

        mainOrder.setProductTotalPrice(productTotalPrice);
        logger.info("设置订单总价");

        //设置订单总价： 商品总价+配送费用
        //获取配送费用
        if (vo.getDeliverType() == OrderStatic.DELIVER_TYPE_SEND) {
            mainOrder.setOrderTotalPrice(productTotalPrice.add(mainOrder.getDistributionCosts()));
        } else {
            mainOrder.setOrderTotalPrice(productTotalPrice);
        }

        //设置车站id
        logger.info("设置车站id");
        String stationId = merchantService.findMerchantById(vo.getMerchantId()).getStationId();
        mainOrder.setStationId(stationId);

        //设置商户
        logger.info("设置商户");
        mainOrder.setMerchantId(vo.getMerchantId());

        //设置收货位置
        logger.info("设置收货位置");
        if (StringUtils.hasText(vo.getDeliverAddress())) {
            DeliverAddress deliverAddress = deliverAddressService.findDeliverAddressById(vo.getDeliverAddress());
            if (null != deliverAddress) {
                mainOrder.setDeliverAddress(deliverAddress.getAddress());
            }
        }

        //设置预计取货时间
        logger.info("设置预计取货时间:" + vo.getLatestServiceTime());

        SimpleDateFormat sdf = new SimpleDateFormat(AppConfig.DATE_TIME_FORMAT);

        try {
            Date date = new Date();
            String formatDate = sdf.format(date);
            String nowHourMinute = formatDate.substring(11, 16);
            Date latestServiceTime = sdf.parse(formatDate.replace(nowHourMinute, vo.getLatestServiceTime()));
            mainOrder.setLatestServiceTime(latestServiceTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //设置送餐方式
        logger.info("设置送餐方式:" + vo.getDeliverType());
        mainOrder.setDeliverType(vo.getDeliverType());

        //设置创建时间
        logger.info("设置创建时间");
        mainOrder.setCreateDate(new Date());

        return mainOrder;
    }

    /**
     * 添加订单时设置子订单的初始状态
     *
     * @param mainOrder 主订单对象
     * @return SubOrder  子订单
     * @author lid
     * @data 2017.2.14
     */
    @Transactional
    public List<SubOrder> SetSubOrderInitialData(MainOrder mainOrder, OrderParamVo vo) {
        logger.info("添加订单时设置子订单的初始状态!");

        List<ProductInputParamVo> listProduct = vo.getListProduct();
        logger.info("SetSubOrderInitialData, listProduct.size():" + listProduct.size());
        List<SubOrder> listSubOrder = new ArrayList<SubOrder>(listProduct.size());

        for (ProductInputParamVo paramVo : listProduct) {

            logger.info("设置子单值");

            Product product = productService.findProductById(paramVo.getProductId());

            SubOrder subOrder = new SubOrder();

            //设置主订单id
            subOrder.setMainOrderId(mainOrder.getOrderId());

            //设置产品id
            subOrder.setProductId(product.getProductId());

            //设置产品名称
            subOrder.setProductName(product.getName());

            //设置产品价格
            subOrder.setProductPrice(product.getPrice());

            //设置产品数量
            subOrder.setProductCount(paramVo.getQuantity());

            //设置子单总价
            subOrder.setTotalPrice(product.getPrice().multiply(new BigDecimal(paramVo.getQuantity())));

            //设置创建时间
            subOrder.setCreateDate(new Date());

            listSubOrder.add(subOrder);
        }
        logger.info("设置子单值成功，子单个数为：" + listSubOrder.size());
        return listSubOrder;
    }

    @Override
    public Map<String, Object> getProductInOrder(List<ProductInputParamVo> listProductParamVo) {
        logger.info("getProductInOrder");
        if (0 == listProductParamVo.size()) {
            return null;
        }

        //对参数链表遍历，设置返回参数
        BigDecimal totalPrice = new BigDecimal(0);
        List<ProductReturnParamVo> listProductReturnParamVo = new ArrayList<ProductReturnParamVo>();
        for (ProductInputParamVo inputParamVo : listProductParamVo) {
            ProductReturnParamVo productReturnParamVo = new ProductReturnParamVo();

            Product product = productService.findProductById(inputParamVo.getProductId());
            if (null == product) {
                continue;
            }
            productReturnParamVo.setPrice(product.getPrice());
            productReturnParamVo.setProductName(product.getName());
            productReturnParamVo.setQuantity(inputParamVo.getQuantity());

            listProductReturnParamVo.add(productReturnParamVo);

            //累加计算总价
            totalPrice = totalPrice.add(product.getPrice().multiply(new BigDecimal(inputParamVo.getQuantity())));
        }

        //将结果保存在map中
        Map<String, Object> mapResult = new HashMap<String, Object>();

        mapResult.put("totalPrice", totalPrice);
        mapResult.put("prudoct", listProductReturnParamVo);
        return mapResult;
    }

    @Override
    public Map<String, Object> queryOrdersByOrderId(String orderId) {
        logger.info("queryOrdersByOrderId");

        //返回结果保存在Map中
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (!StringUtils.hasText(orderId)) {
            throw new AppException("订单id为空！");
        }
        MainOrder order = mainOrderDao.findOne(orderId);

        //设置订单的配送员电话号码
        if (StringUtils.hasText(order.getServiceProviderId())) {
            ServiceProvider serviceProvider = serviceProviderDao.findOne(order.getServiceProviderId());
            if (null != serviceProvider) {
                if (StringUtils.hasText(serviceProvider.getPhoneNo())) {
                    order.setServiceProviderPhoneNo(serviceProvider.getPhoneNo());
                }
            }
        }

        //如果订单中缺少商户id则抛出异常
        if (!StringUtils.hasText(order.getMerchantId())) {
            throw new AppException("订单中商户信息为空！");
        }
        Merchant merchant = merchantService.findMerchantById(order.getMerchantId());
        if (StringUtils.hasText(merchant.getImageId())) {
            ImageInfo image = commonService.getImageInfoById(merchant.getImageId());
            if (null != image) {
                merchant.setImageUrl(image.getUrl());
            }
        }

        //如果订单中缺少车站id则抛出异常
        if (!StringUtils.hasText(order.getStationId())) {
            throw new AppException("订单中车站信息为空！");
        }
        //获取示意图URL
        List<String> stationForImageRelaList = stationForImageRelaService.findByStationId(order.getStationId());
        resultMap.put("stationForImageRelaList", stationForImageRelaList);
//        RailwayStation railwayStation = railwayStationService.findByStationId(order.getStationId());
//        if (StringUtils.hasText(railwayStation.getImageId())) {
//            ImageInfo image = commonService.getImageInfoById(railwayStation.getImageId());
//            if (null != image) {
//                railwayStation.setImgUrl(image.getUrl());
//            }
//        }
//        resultMap.put("railwayStation", railwayStation);

        resultMap.put("merchant", merchant);

        List<SubOrder> listSubOrder = subOrderService.findSubOrdersByMainOrderId(orderId);
        resultMap.put("listSubOrder", listSubOrder);
        resultMap.put("order", order);
        return resultMap;
    }

    @Override
    public List<UserOrdersVo> getOrdersByUser(String userId) {
        logger.info("MainOrderServiceImpl");
        if (!StringUtils.hasText(userId)) {
            throw new AppException("未获取当前用户信息！");
        }
        List<UserOrdersVo> list = mainOrderDao.getOrdersByUser(userId);
        for (UserOrdersVo vo : list) {
            String mainOrderId = vo.getOrderId();
            List<SubOrder> listSubOrder = subOrderService.findSubOrdersByMainOrderId(mainOrderId);
            vo.setListSubOrder(listSubOrder);
        }
        return list;
    }

    @Override
    @Transactional
    public MainOrder updateMainOrder(MainOrder mainOrder) {
        if (mainOrder == null || !StringUtils.hasText(mainOrder.getOrderId()))
            throw new AppException("订单对象或订单ID为空！");
        mainOrderDao.save(mainOrder);
        mainOrderDao.flush();
        return mainOrder;
    }

    @Override
    public MainOrder findMainOrderByOrderId(String orderId) {
        logger.info("findMainOrderByOrderId");
        if (!StringUtils.hasText(orderId)) {
            throw new AppException("orderId参数为空！");
        }
        MainOrder order = mainOrderDao.findOne(orderId);
        if (StringUtils.hasText(order.getServiceProviderId())) {
            ServiceProvider serviceProvider = serviceProviderDao.findOne(order.getServiceProviderId());
            if ((null != serviceProvider)
                    && (StringUtils.hasText(serviceProvider.getPhoneNo()))) {
                order.setServiceProviderPhoneNo(serviceProvider.getPhoneNo());
            }
        }

        return order;
    }

    @Override
    public void refundOrder(MainOrder mainOrder) {
        //退款
        logger.info("refundOrder");
        if (!StringUtils.hasText(mainOrder.getOrderNo())
                || !StringUtils.hasText(mainOrder.getTransactionId())
                || mainOrder.getOrderTotalPrice() == null) {
            throw new AppException("订单信息不完整！");
        }

        // 转换成以分为单位的金额
        int totalMoney = mainOrder.getOrderTotalPrice().multiply(new BigDecimal("100")).intValue();

        Map<String, Object> mapResult = new HashMap<String, Object>();
        //调用退款工具类
        try {
            mapResult = WechatPayUtil.refund(mainOrder.getTransactionId(),
                    mainOrder.getOrderNo(), mainOrder.getOrderNo(),
                    totalMoney, totalMoney);
        } catch (Exception e) {
            logger.error("订单：" + mainOrder.getOrderNo() + " 退款调用微信支付接口异常：", e);
        }

        //退款成功，设置订单状态
        if ("SUCCESS".equals(mapResult.get("returnCode"))) {
            //设置订单状态为已退款，并设置退款单号和退款时间
            mainOrder.setPayStatus(OrderStatic.PAY_STATUS_REFUNDED);
            mainOrder.setRefundId(String.valueOf(mapResult.get("refund_id")));
            mainOrder.setOutRefundNo(mainOrder.getOrderNo());
            mainOrder.setRefundDate(new Date());
            mainOrderDao.save(mainOrder);
        } else {
            throw new AppException("订单：" + mainOrder.getOrderNo()
                    + " 退款失败：" + mapResult.get("return_msg"));
        }
    }

    @Override
    public List<OrderQuartzVo> queryMainOrderByStatus(Integer mainOrderStatus) {
        logger.info("queryMainOrderByStatus!mainOrderStatus:" + mainOrderStatus);
        if (null == mainOrderStatus) {
            throw new AppException("mainOrderStatus参数为空！");
        }
        return mainOrderDao.queryMainOrderByStatus(mainOrderStatus);
    }

}