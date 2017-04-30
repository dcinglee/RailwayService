package com.railwayservice.order;

import com.railwayservice.application.util.OrderUtil;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.merchantmanage.dao.MerchantDao;
import com.railwayservice.merchantmanage.entity.Merchant;
import com.railwayservice.order.dao.MainOrderDao;
import com.railwayservice.order.dao.SubOrderDao;
import com.railwayservice.order.entity.MainOrder;
import com.railwayservice.order.entity.SubOrder;
import com.railwayservice.order.service.MainOrderService;
import com.railwayservice.order.service.OrderStatic;
import com.railwayservice.order.service.SubOrderService;
import com.railwayservice.order.vo.QueryOrderParam;
import com.railwayservice.user.dao.UserDao;
import com.railwayservice.user.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * 订单测试类。
 *
 * @author lid
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml", "classpath*:springDataJpa.xml"})
public class TestOrder {

    private MainOrderService mainOrderService;
    private SubOrderService subOrderService;

    private MainOrderDao mainOrderDao;

    private SubOrderDao subOrderDao;

    private MerchantDao merchantDao;

    private UserDao userDao;

    @Autowired
    public void setMainOrderService(MainOrderDao mainOrderDao) {
        this.mainOrderDao = mainOrderDao;
    }

    @Autowired
    public void setSubOrderService(SubOrderService subOrderService) {
        this.subOrderService = subOrderService;
    }

    @Autowired
    public void setMainOrderDao(MainOrderService mainOrderService) {
        this.mainOrderService = mainOrderService;
    }

    @Autowired
    public void setSubOrderDao(SubOrderDao subOrderDao) {
        this.subOrderDao = subOrderDao;
    }

    @Autowired
    public void setMerchantDao(MerchantDao merchantDao) {
        this.merchantDao = merchantDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 准备测试数据，主订单、商户、用户、子订单。
     */
    private MainOrder createMainOrder(Merchant merchant, User user) {
        MainOrder mainOrder = new MainOrder();
        mainOrder.setCustomerName("李杜");
        mainOrder.setCustomerPhoneNo("13316483222");
        mainOrder.setOrderNo(OrderUtil.GenerateOrderNo());
        mainOrder.setOrderStatus(OrderStatic.MAINORDER_STATUS_WAIT_ACCEPT);
        mainOrder.setPayStatus(OrderStatic.PAY_STATUS_UNPAYED);
        mainOrder.setTransactionId("123");
        mainOrder.setCreateDate(new Date());
        mainOrder.setMerchantId(merchant.getMerchantId());
        mainOrder.setUserId(user.getUserId());
        mainOrder = mainOrderDao.save(mainOrder);
        return mainOrder;
    }

    private Merchant craeteMerchant() {
        Merchant merchant = new Merchant();
        merchant.setName("星巴克3224546");
        merchant = merchantDao.save(merchant);
        return merchant;
    }

    private User createUser() {
        User user = new User();
        user.setName("TestUser");
        user = userDao.save(user);
        return user;
    }

    private SubOrder createSubOrder(MainOrder mainOrder) {
        SubOrder subOrder = new SubOrder();
        subOrder.setMainOrderId(mainOrder.getOrderId());
        return subOrderService.addSubOrder(subOrder);
    }

    /**
     * 清理测试数据，部分参数无则传入null。
     */
    private void deleteData(MainOrder mainOrder, Merchant merchant, User user, SubOrder subOrder) {
        if (mainOrder != null) mainOrderDao.delete(mainOrder);
        if (merchant != null) merchantDao.delete(merchant);
        if (user != null) userDao.delete(user);
        if (subOrder != null) subOrderDao.delete(subOrder);
    }

    /**
     * 按条件查询主订单
     */
    @Test
    public void testFindMainOrders() {
        Merchant merchant = craeteMerchant();
        User user = createUser();
        MainOrder mainOrder = createMainOrder(merchant, user);

        QueryOrderParam param = new QueryOrderParam();
        param.setOrderNo(mainOrder.getOrderNo());
        param.setStartTime(DateTime.getDay(new Date(), -3));
        param.setEndTime(DateTime.getDay(new Date(), 3));
        param.setCustomerPhoneNo(mainOrder.getCustomerPhoneNo());
        param.setMerchantName(merchant.getName());
        PageData page = mainOrderService.findMainOrders(new PageParam(), param);

        deleteData(mainOrder, merchant, user, null);

        Assert.assertTrue(page.getRows() != null && page.getRows().size() > 0);
    }

    /**
     * 添加主订单
     */
    @Test
    public void testAddMainOrder() {
        Merchant merchant = craeteMerchant();
        User user = createUser();
        MainOrder mainOrder = createMainOrder(merchant, user);

        //创建子订单
        SubOrder subOrder = createSubOrder(mainOrder);

        deleteData(mainOrder, merchant, user, subOrder);

        Assert.assertTrue(true);
    }

    /**
     * 添加子订单
     */
    @Test
    public void testAddSubOrder() {
        Merchant merchant = craeteMerchant();
        User user = createUser();
        MainOrder mainOrder = createMainOrder(merchant, user);

        SubOrder subOrder = new SubOrder();
        subOrder.setMainOrderId(mainOrder.getOrderId());
        SubOrder newSubOrder = subOrderService.addSubOrder(subOrder);
        System.out.println(newSubOrder.getSubOrderId());

        deleteData(mainOrder, merchant, user, subOrder);

        Assert.assertTrue(null != newSubOrder);
    }

    /**
     * 取消主订单
     */
    @Test
    public void testCancelMainOrder() {
        Merchant merchant = craeteMerchant();
        User user = createUser();
        MainOrder mainOrder = createMainOrder(merchant, user);

        mainOrder = mainOrderService.findMainOrderByOrderId(mainOrder.getOrderId());
        MainOrder newMainOrder = mainOrderService.cancelMainOrder(mainOrder.getOrderId(), "我不想要了");

        deleteData(mainOrder, merchant, user, null);

        Assert.assertTrue(OrderStatic.MAINORDER_CANCEL_STATUS_APPEAL == newMainOrder.getOrderStatus());
    }

    /**
     * 取消子订单
     */
    @Test
    public void testCancelSubOrder() {
        Merchant merchant = craeteMerchant();
        User user = createUser();
        MainOrder mainOrder = createMainOrder(merchant, user);

        SubOrder subOrder = new SubOrder();
        subOrder.setMainOrderId(mainOrder.getOrderId());
        subOrderService.addSubOrder(subOrder);

        subOrder = subOrderService.findSubOrderBySubOrderId(subOrder.getSubOrderId());
        subOrderService.cancelSubOrder(subOrder.getSubOrderId());

        deleteData(mainOrder, merchant, user, subOrder);

        Assert.assertTrue(true);
    }

    /**
     * 失效主订单
     */
    @Test
    public void testExpirelMainOrder() {
        Merchant merchant = craeteMerchant();
        User user = createUser();
        MainOrder mainOrder = createMainOrder(merchant, user);

        // 删除主订单，只做逻辑删除。
        MainOrder newMainOrder = mainOrderService.expireMainOrder(mainOrder.getOrderId());

        deleteData(mainOrder, merchant, user, null);

        Assert.assertTrue(OrderStatic.MAINORDER_STATUS_EXPIRE == newMainOrder.getOrderStatus());
    }

    /**
     * 根据主订单id查找对应的子订单
     */
    @Test
    public void testFindSubOrdersByMainOrder() {
        Merchant merchant = craeteMerchant();
        User user = createUser();
        MainOrder mainOrder = createMainOrder(merchant, user);
        SubOrder subOrder = createSubOrder(mainOrder);

        List<SubOrder> listSubOrders = subOrderService.findSubOrdersByMainOrderId(mainOrder.getOrderId());

        deleteData(mainOrder, merchant, user, subOrder);

        Assert.assertTrue(listSubOrders.size() > 0);
    }

    /**
     * 根据userId查找订单
     */
    @Test
    public void testFindOrdersByUserId() {
        Merchant merchant = craeteMerchant();
        User user = createUser();
        MainOrder mainOrder = createMainOrder(merchant, user);

        Date startDate = DateTime.getDay(new Date(), -3);
        Date endDate = DateTime.getDay(new Date(), 3);
        List<MainOrder> listMainOrder = mainOrderService.queryOrdersByUser(mainOrder.getUserId(), startDate, endDate);
        System.out.println("listMainOrder.size():" + listMainOrder.size());

        deleteData(mainOrder, merchant, user, null);

        Assert.assertTrue(listMainOrder.size() > 0);
    }

}