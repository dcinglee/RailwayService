package com.railwayservice.order.web;

import com.railwayservice.application.config.AppConfig;
import com.railwayservice.application.exception.AppException;
import com.railwayservice.application.interceptor.Authorize;
import com.railwayservice.application.vo.PageData;
import com.railwayservice.application.vo.PageParam;
import com.railwayservice.application.vo.ResultMessage;
import com.railwayservice.order.entity.OrderStatusRecord;
import com.railwayservice.order.entity.ShoppingCart;
import com.railwayservice.order.entity.SubOrder;
import com.railwayservice.order.service.MainOrderService;
import com.railwayservice.order.service.OrderStatusRecordService;
import com.railwayservice.order.service.ShoppingCartService;
import com.railwayservice.order.service.SubOrderService;
import com.railwayservice.order.vo.*;
import com.railwayservice.productmanage.entity.Product;
import com.railwayservice.productmanage.service.ProductService;
import com.railwayservice.stationmanage.entity.RailwayStation;
import com.railwayservice.stationmanage.service.RailwayStationService;
import com.railwayservice.user.entity.User;
import com.railwayservice.user.service.UserService;
import com.railwayserviceWX.config.WeixinConfig;
import com.railwayserviceWX.controller.WechatControllerTest;
import com.railwayserviceWX.util.IpAddressUtils;
import com.railwayserviceWX.vo.BrandWCPayParameterVo;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

/**
 * 订单模块请求控制器。
 * 主单和子单在一个控制器处理。
 * 后台和前端控制器统一处理。
 *
 * @author lid
 * @date 2017.2.13
 */
@Controller
@RequestMapping(value = "/order", produces = {"application/json;charset=UTF-8"})
@Api(value = "订单模块请求控制器", description = "订单模块的相关操作")
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private UserService userService;

    private MainOrderService mainOrderService;

    private SubOrderService subOrderService;

    private ShoppingCartService shoppingCartService;

    private ProductService productService;

    private RailwayStationService railwayStationService;

    private OrderStatusRecordService orderStatusRecordService;

    @Autowired
    public void setOrderStatusRecordService(OrderStatusRecordService orderStatusRecordService) {
        this.orderStatusRecordService = orderStatusRecordService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRailwayStationService(RailwayStationService railwayStationService) {
        this.railwayStationService = railwayStationService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Autowired
    public void setMainOrderService(MainOrderService mainOrderService) {
        this.mainOrderService = mainOrderService;
    }

    @Autowired
    public void setSubOrderService(SubOrderService subOrderService) {
        this.subOrderService = subOrderService;
    }

    /**
     * 主订单查询
     *
     * @return ResultMessage
     * @author lidx
     * @date 2017.2.8
     */
    @ResponseBody
    @RequestMapping("/queryMainOrder")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_ORDER_ORDER_LIST"})
    public ResultMessage listMainOrder(PageParam pageParam, QueryOrderParam orderParam) {
        logger.info("订单控制层: 主订单列表：" + orderParam);
        try {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(orderParam.getEndTime());
            calendar.add(calendar.DATE, 1);      //把日期往后增加一天.整数往后推,负数往前移动
            orderParam.setEndTime(calendar.getTime());   //这个时间就是日期往后推一天的结果
            PageData dataPage = mainOrderService.findMainOrders(pageParam, orderParam);
            return ResultMessage.newSuccess().setData(dataPage);
        } catch (Exception e) {
            logger.error("查询主订单数据异常：", e);
            return ResultMessage.newFailure("查询主订单数据异常！");
        }
    }

    /**
     * 子订单查询（子订单根据主单查找）
     *
     * @return ResultMessage
     * @author lid
     * @date 2017.2.13  affirm
     */
    @ResponseBody
    @RequestMapping("/querySubOrder")
    @Authorize(type = AppConfig.AUTHORITY_ADMIN, value = {"WEB_ORDER_ORDER_LIST"})
    public ResultMessage listSubOrder(String mainOrderId) {
        logger.info("订单控制层：子订单查询：" + mainOrderId);
        try {
            List<SubOrder> listSubOrder = subOrderService.findSubOrdersByMainOrderId(mainOrderId);
            return ResultMessage.newSuccess().setData(listSubOrder);
        } catch (Exception e) {
            logger.error("查询子订单数据异常：", e);
            return ResultMessage.newFailure("查询子订单数据异常！");
        }
    }

    @ResponseBody
    @RequestMapping("/affirmOrder")

    public ResultMessage affirmOrder(HttpServletRequest req, HttpServletResponse res, String merchantId, String stationId) {

        logger.info("订单控制层：用户获取确认订单信息：商户ID:" + merchantId + ",车站ID:" + stationId);
        //获取当前用户信息
        User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);

        if (null == user) {
            String jumpUrl = WeixinConfig.HOST + "/entrance/index";
            try {
                res.sendRedirect(jumpUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ResultMessage.newFailure("获取当前用户失败，页面已跳转！");
        }

        RailwayStation station = railwayStationService.findByStationId(stationId);

        try {
            Map<String, Object> mapResult = mainOrderService.affirmOrder(user, station, merchantId);
            return ResultMessage.newSuccess("用户获取确认订单信息成功").setData(mapResult);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("用户获取确认订单信息异常：", e);
            return ResultMessage.newFailure("用户获取确认订单信息异常！");
        }
    }

    /**
     * 用户确认支付下订单。
     * 处理逻辑如下：
     * 1，获取参数值，生成订单；
     * 2，调用微信支付；
     * 3，通知商户
     *
     * @return ResultMessage
     * @author lid
     * @date 2017.2.13
     */
    @ResponseBody
    @RequestMapping("/createOrder")
    public ResultMessage createOrder(@RequestBody OrderParamVo orderParamVo, HttpServletRequest req, HttpServletResponse res) {
        logger.info("订单控制层：用户确认支付下订单：");
        //获取当前用户信息
        User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);

        if (null == user) {
            String jumpUrl = WeixinConfig.HOST + "/entrance/index";
            try {
                res.sendRedirect(jumpUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ResultMessage.newFailure("获取当前用户失败，页面已跳转！");
        }

        String addrip = IpAddressUtils.getIpAddr(req);
        
        /**
         * 测试环境流程代码
         */
        if (WechatControllerTest.isTest) {
        	logger.info("当前为测试环境");
        	addrip = "175.13.249.134";
        }
        /*** 测试环境流程代码 */
        
        
        logger.info("addrip:" + addrip);
        try {
            BrandWCPayParameterVo brandWCPayParameterVo = mainOrderService.createOrder(orderParamVo, user, addrip);
            return ResultMessage.newSuccess("用户下订单成功").setData(brandWCPayParameterVo);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("添加订单异常：", e);
            return ResultMessage.newFailure("添加订单异常！");
        }
    }

    /**
     * 用户调用
     * 取消主订单，子订单一并取消。
     * 如果订单已完成则不能取消；存在状态为服务完成、用户确认、完成的子单时不能取消
     *
     * @return ResultMessage
     * @author lid
     * @date 2017.2.13
     */
    @ResponseBody
    @RequestMapping("/cancelMainOrder")
    public ResultMessage cancelMainOrder(@RequestBody CancelOrderVo vo) {
        logger.info("订单控制层：取消主订单：" + vo.getMainOrderId());
        try {
            mainOrderService.cancelMainOrder(vo.getMainOrderId(), vo.getReason());
            return ResultMessage.newSuccess("取消主订单成功");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("取消主订单异常：", e);
            return ResultMessage.newFailure("取消主订单异常！");
        }
    }

    /**
     * 取消子订单
     * 已完成的子订单不能取消
     *
     * @return ResultMessage
     * @author lid
     * @date 2017.2.13
     */
    @ResponseBody
    @RequestMapping("/cancelSubOrder")
    public ResultMessage cancelSubOrder(String subOrderId) {
        logger.info("订单控制层: 取消子订单：" + subOrderId);
        try {
            subOrderService.cancelSubOrder(subOrderId);
            return ResultMessage.newSuccess("取消子订单成功！");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("取消子订单异常：", e);
            return ResultMessage.newFailure("取消子订单异常！");
        }
    }

    /**
     * 失效主订单（子单不处理）
     * 后台管理员操作
     *
     * @return ResultMessage
     * @author lid
     * @date 2017.2.13
     */
    @ResponseBody
    @RequestMapping("/expireMainOrder")
    public ResultMessage expireMainOrder(String mainOrderId) {
        logger.info("订单控制层： 失效主订单:" + mainOrderId);
        try {
            mainOrderService.expireMainOrder(mainOrderId);
            return ResultMessage.newSuccess("失效主订单成功");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("失效主订单异常：", e);
            return ResultMessage.newFailure("失效主订单异常！");
        }
    }

    /**
     * 删除子订单
     * 后台管理员操作
     *
     * @return ResultMessage
     * @author lid
     * @date 2017.2.13
     */
    @ResponseBody
    @RequestMapping("/deleteSubOrder")
    public ResultMessage deleteSubOrder(String subOrderId) {
        logger.info("订单控制层； 删除子订单:" + subOrderId);
        try {
            subOrderService.deleteSubOrder(subOrderId);
            return ResultMessage.newSuccess("删除子订单成功");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("删除子订单异常：", e);
            return ResultMessage.newFailure("删除子订单异常！");
        }
    }

    /**
     * 用户调用获取订单详情
     *
     * @param mainOrderId
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryOrdersByOrderId")
    public ResultMessage queryOrderDetailByOrderId(String userId, String orderId, HttpServletRequest req, HttpServletResponse res) {
        logger.info("订单控制层；用户调用获取订单详情:" + orderId);
        try {
            User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);
            if ((null == user)
                    || (!user.getUserId().equals(userId))) {
                user = userService.getUserByUserId(userId);
                logger.info("queryOrdersByOrderId:user.getNickName()" + user.getNickName());
                req.getSession().setAttribute(AppConfig.USER_SESSION_KEY, user);
            }
            logger.info("user.getNickName()" + user.getNickName());
            Map<String, Object> mapResult = mainOrderService.queryOrdersByOrderId(orderId);
            return ResultMessage.newSuccess("获取订单详情成功").setData(mapResult);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取订单详情异常：", e);
            return ResultMessage.newFailure("获取订单详情异常！");
        }
    }

    /**
     * 用户调用获取订单详情
     *
     * @param mainOrderId
     * @return
     */
    @ResponseBody
    @RequestMapping("/queryOrderRecordsByOrderId")
    public ResultMessage queryOrderRecordsByOrderId(String orderId) {
        logger.info("订单控制层；用户调用获取订单状态记录:" + orderId);
        try {
            List<OrderStatusRecord> listOrderStatusRecord = orderStatusRecordService.findByOrderId(orderId);
            logger.info("listOrderStatusRecord.size():" + listOrderStatusRecord.size());
            return ResultMessage.newSuccess("获取订单状态记录成功").setData(listOrderStatusRecord);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取订单状态记录异常：", e);
            return ResultMessage.newFailure("获取订单状态记录异常！");
        }
    }

    /**
     * 获取订单列表
     * 用户调用
     *
     * @param userId    用户ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return ResultMessage
     * @author lid
     * @date 2017.2.15
     */
    @ResponseBody
    @RequestMapping("/queryOrdersByUser")
    public ResultMessage queryOrdersByUser(HttpServletRequest req, HttpServletResponse res) {
        logger.info("订单控制层；获取订单列表!");
        User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);
        if (null == user) {
            String jumpUrl = WeixinConfig.HOST + "/entrance/index";
            try {
                res.sendRedirect(jumpUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ResultMessage.newFailure("获取当前用户失败，页面已跳转！");
        }
        try {
            List<UserOrdersVo> listUserOrdersVo = mainOrderService.getOrdersByUser(user.getUserId());
            return ResultMessage.newSuccess("获取用户订单成功").setData(listUserOrdersVo);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取用户订单异常：", e);
            return ResultMessage.newFailure("获取用户订单异常！");
        }
    }

    /**
     * 获取订单中的商品信息
     *
     * @return ResultMessage
     * @author lid
     * @date 2017.3.1
     */
    @ResponseBody
    @RequestMapping("/getProductInOrder")
    public ResultMessage getProductInOrder(List<ProductInputParamVo> listProductParamVo) {
        logger.info("订单控制层；获取订单中的商品信息!");
        try {
            Map<String, Object> mapResult = mainOrderService.getProductInOrder(listProductParamVo);
            return ResultMessage.newSuccess("获取订单商品信息成功").setData(mapResult);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取订单商品信息异常：", e);
            return ResultMessage.newFailure("获取订单商品信息异常！");
        }
    }

    /**
     * 获取购物车信息
     *
     * @return ResultMessage
     * @author lid
     * @date 2017.3.2
     */
    @ResponseBody
    @RequestMapping("/getShoppingCart")
    public ResultMessage getShoppingCart(String merchantId, HttpServletRequest req, HttpServletResponse res) {
        logger.info("订单控制层；获取购物车信息!");
        User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);

        if (null == user) {
            String jumpUrl = WeixinConfig.HOST + "/entrance/index";
            try {
                res.sendRedirect(jumpUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ResultMessage.newFailure("获取当前用户失败，页面已跳转！");
        }
        logger.info("user.getUserId()   :" + user.getUserId());
        try {
            List<ShoppingCart> listShoppingCart = shoppingCartService.findByUserIdAndMerchantId(user.getUserId(), merchantId);
            //判断商品信息是否有修改，包括产品名称和产品价格
            for (ShoppingCart shoppingCart : listShoppingCart) {
                Product product = productService.findProductById(shoppingCart.getProductId());
                if ((!product.getName().equals(shoppingCart.getProductName())
                        || (product.getPrice().compareTo(shoppingCart.getPrice())) != 0)) {
                    shoppingCart.setHasModified(true);
                }
            }
            return ResultMessage.newSuccess("获取购物车信息成功").setData(listShoppingCart);
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("获取购物车信息异常：", e);
            return ResultMessage.newFailure("获取购物车信息异常！");
        }
    }

    /**
     * 修改购物车信息
     *
     * @return ResultMessage
     * @author lid
     * @date 2017.3.2
     */
    @ResponseBody
    @RequestMapping("/changeShoppingCart")
    public ResultMessage changeShoppingCart(@RequestBody ShoppingCartParamVo vo, HttpServletRequest req, HttpServletResponse res) {
        logger.info("订单控制层；修改购物车信息!");
        User user = (User) req.getSession().getAttribute(AppConfig.USER_SESSION_KEY);

        if (null == user) {
            String jumpUrl = WeixinConfig.HOST + "/entrance/index";
            try {
                res.sendRedirect(jumpUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return ResultMessage.newFailure("获取当前用户失败，页面已跳转！");
        }
        logger.info("userId:" + user.getUserId());
        try {
            //如果新购物车为空  则删除原购物车中的数据
            List<ShoppingCart> listOldShoppingCart = shoppingCartService.findByUserIdAndMerchantId(user.getUserId(), vo.getMerchantId());
            logger.info("原购物车数据量：" + listOldShoppingCart.size());

            //如果当前购物车数量为0，则删除购物车数据
            if (0 == vo.getProductList().size()) {
                logger.info("新购物车内商品数量为0！vo.getPro"
                        + ""
                        + "ductList().size()：" + vo.getProductList().size());

                if (0 < listOldShoppingCart.size()) {
                    logger.info("删除原购物车数据");

                    for (ShoppingCart shoppingCart : listOldShoppingCart) {
                        shoppingCartService.deleteShoppingCart(shoppingCart.getCartId());
                    }
                    return ResultMessage.newSuccess("修改购物车信息成功");
                }
                return ResultMessage.newSuccess("修改购物车信息成功");
            }

            //如果原购物车中数据量为0，则直接添加当前购物车的信息
            if (0 == listOldShoppingCart.size()) {
                for (CartProductParamVo paramVo : vo.getProductList()) {
                    logger.info("增加购物车数据！");

                    ShoppingCart shoppingCart = new ShoppingCart();
                    Product product = productService.findProductById(paramVo.getProductId());
                    if (null == product) {
                        logger.info("未找到对应的商品信息！");
                        continue;
                    }

                    shoppingCart.setCount(paramVo.getCount());
                    shoppingCart.setHasModified(false);
                    shoppingCart.setMerchantId(vo.getMerchantId());
                    shoppingCart.setPrice(product.getPrice());
                    shoppingCart.setProductId(product.getProductId());
                    shoppingCart.setProductName(product.getName());
                    shoppingCart.setUserId(user.getUserId());
                    shoppingCartService.addShoppingCart(shoppingCart);
                }
                return ResultMessage.newSuccess("修改购物车信息成功");
            }

            //判断购物车中的数据是否有变化，如果没有变化则不做操作
            if (hasChangeShippingCart(listOldShoppingCart, vo.getProductList())) {
                return ResultMessage.newSuccess("修改购物车信息成功");
            }

            //否则先删除所有购物车信息然后再更新。
            for (ShoppingCart shoppingCart : listOldShoppingCart) {
                shoppingCartService.deleteShoppingCart(shoppingCart.getCartId());
            }

            for (CartProductParamVo paramVo : vo.getProductList()) {
                logger.info("增加购物车数据！");

                ShoppingCart shoppingCart = new ShoppingCart();
                Product product = productService.findProductById(paramVo.getProductId());
                if (null == product) {
                    logger.info("未找到对应的商品信息！");
                    continue;
                }
                shoppingCart.setCount(paramVo.getCount());
                shoppingCart.setHasModified(false);
                shoppingCart.setMerchantId(vo.getMerchantId());
                shoppingCart.setPrice(product.getPrice());
                shoppingCart.setProductId(product.getProductId());
                shoppingCart.setProductName(product.getName());
                shoppingCart.setUserId(user.getUserId());
                shoppingCartService.addShoppingCart(shoppingCart);
            }
            return ResultMessage.newSuccess("修改购物车信息成功");
        } catch (AppException ae) {
            return ResultMessage.newFailure(ae.getMessage());
        } catch (Exception e) {
            logger.error("修改购物车信息异常：", e);
            return ResultMessage.newFailure("修改购物车信息异常！");
        }
    }

    /**
     * 判断购物车是否发生了改变
     *
     * @param listShoppingCart
     * @param listCartProductParamVo
     * @return
     */
    public boolean hasChangeShippingCart(List<ShoppingCart> listShoppingCart, List<CartProductParamVo> listCartProductParamVo) {
        logger.info("订单控制层；判断购物车是否发生了改变!");

        //判断购物车中商品种类个数是否发生了变化
        if (listShoppingCart.size() != listCartProductParamVo.size()) {
            logger.info("购物车数量发生了变化!");
            return false;
        }

        //判断商品具体种类和每个种类的数量是否发生改变
        for (ShoppingCart shoppingCart : listShoppingCart) {
            int index = 0;
            //如果商品id和商品数量都一样则表示未发生改变
            while ((index < listCartProductParamVo.size())
                    && (!shoppingCart.getCartId().equals(listCartProductParamVo.get(index).getProductId()))) {
                index++;
            }

            //如果遍历完了还没找到一样的商品id则表示发生了改变
            if (index == listCartProductParamVo.size()) {
                return false;
            }

            //如果商品id一样 但是数量不一样则表示发生了改变
            if (!shoppingCart.getCount().equals(listCartProductParamVo.get(index).getCount())) {
                return false;
            }
        }
        return true;
    }
}
