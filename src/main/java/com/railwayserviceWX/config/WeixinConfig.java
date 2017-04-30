package com.railwayserviceWX.config;

public class WeixinConfig {

	
    // 赋权类型
    public static final String GRANTTYPE = "client_credential";
    
    
    /**************正式账号*******************/
    // TOKEN 微信平台开发模式中设置
    public static String TOKEN = "yiweikeji";
    
    //公众号id
    public static String APPNAME = "gh_2f4cc9c27365";

    //安全密钥
    public static String KEY = "18d53930aacfd12f21870097bedb3e33";
    
    //商户号
    public static String MCHID = "1448894202";

    // appid
//    public static String APPID = "wx76e685e048a40319";
    public static String APPID = "wxf6f0674e2d33c153";
    
    //secret密钥
//    public static String SECRET = "e42708608886ce7c77abeb742eeb3821";
    public static String SECRET = "3016e947901e5796b5dc09b447663ad6";
    
    //正式环境下的域名访问
    public static String HOST = "http://szeiv.com/RailwayService";   
    
    //支付回调通知地址
    public static String NOTIFY_URL = "/payNotifyFromWechat/payNotifyFromOrder";
    /**************正式账号*******************/
    
    
    /**************测试账号*******************/
    /*// 测试账号appid
    //public static final String APPID = "wx2e902b2888350652";
    
    // 测试账号SECRET
    public static final String SECRET = "7d7546a03811932dd2d5b601d77995fb";
    
    //测试环境下的域名访问   
    public static final String HOST = "http://119.23.134.10/RailwayService";
    
    //测试环境下支付回调通知地址
    public static final String NOTIFY_URL = "http://119.23.134.10/RailwayService/payNotifyFromWechat/payNotifyFromOrder";*/
    
    /**************测试账号*******************/
       
    
    /**************湖南几好玩*******************/
    /*public static final String TOKEN = "hnzhenhaowantest";
    
    public static final String APPID = "wxe5976fada5d432e3";
    
    public static final String APPNAME = "gh_2f4cc9c27365";
    
    public static final String SECRET = "5eeb956fb265c79005a362d57e73de91";

    public static final String KEY = "05347148538346029poiuytrewqLKJHG";
    
    public static final String MCHID = "1365949902";
    
    public static final String HOST = "http://119.23.134.10/RailwayService";
    
    public static final String NOTIFY_URL = "http://119.23.134.10/RailwayService/payNotifyFromWechat/payNotifyFromOrder";*/
    
    /**************湖南几好玩**********************/
             

    //消息加密密钥
    public static String EncodingAESKey = "eDdBFhaZkfVoAW919Ld3ZuItzfhrujnXkyKGLgsVMzH";
    
    //获取用户信息微信接口
    public static String GET_USERINFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    // 获取tokenURL
    public static String URL_ACCESSTOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=CREDENTIAL&appid=APPID&secret=APPSECRET";

    // 创建菜单URL
    public static String URL_MENU_CREATE = "https://api.weixin.qq.com/cgi-bin/menu/create";

    // 获取菜单URL
    public static String URL_MENU_GET = "https://api.weixin.qq.com/cgi-bin/menu/get";

    // 删除菜单URL
    public static String URL_MENU_DELETE = "https://api.weixin.qq.com/cgi-bin/menu/delete";

    //授权获取用户信息
    public static String CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

    //通过code换取网页授权access_token
    public static String WEBPAGE_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    //获取用户授权后的信息
    public static String GET_AUTHED_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    //获取jsapi_ticket的地址
    public static String GET_JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    //发送模板消息给微信用户
    public static String WX_MESSAGE_SEND = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    /** 微信支付相关常量**/
    /**********************************************************/

    //设备号
    public static String DEVICE_INFO_WEB = "WEB";

    //人民币货币类型 
    public static String CURRENCY_RMB_TYPE = "CNY";

    //微信支付类型
    public static String NO_CREDIT = "no_credit";

    //微信交易类型
    public static String TRADE_TYPE_JSAPI = "JSAPI";

    //统一下单微信接口
    public static String UNIFY_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    //申请退款接口
    public static String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    //签名加密方式
    public static String SIGNTYPEMD5 = "MD5";

    /****************************************************微信消息模板*****************************************************/

    // 订单受理消息模板
    public static String MESSAGE_TEMPLATE_MERCHANT_ACCEPT = "0L9eSIJHWFos86yaldRKHW7pO_k2c1VUG-u601jY49c";

    // 配送人员接单消息模板
    public static String MESSAGE_TEMPLATE_SERVANT_ACCEPT = "pG30s0bZ8mZSyv60DSACXl0y2UmVSP7zGwEloBvR7Dk";

    // 配送人员取货消息模板
    public static String MESSAGE_TEMPLATE_SERVANT_GET_GOODS = "089Nie05ibturHPYESqfSVfG6lGtvf1gApOmX03FVmc";

    // 订单完成消息模板
    public static String MESSAGE_TEMPLATE_ORDER_COMPLETED = "JUVUqiRqBeJzKQChghVPTbn55KsYm4-xf_BPPxoD6Iw";

   
    /*********************************************微信菜单入口**********************************************************/
    //站内点餐
    public static final String INDEX = "index";
    
    //购充电宝
    public static final String POWERBANK = "powerBank";
    
    //我的订单
    public static final String MYORDERS = "myOrders";
}
