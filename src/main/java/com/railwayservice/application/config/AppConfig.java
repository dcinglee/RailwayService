package com.railwayservice.application.config;

/**
 * 统一全局配置。
 *
 * @author Ewing
 */
public final class AppConfig {
    // UUID生成器
    public static final String UUID_GENERATOR = "com.railwayservice.application.util.GlobalIdWorker";

    // 统一日期时间格式
    public static final String TIMEZONE = "GMT+8";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT_MINUTE = "HH:mm";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT_DETAIL = "yyyyMMddHHmmssSSS";
    public static final String DATE_TIME_FORMAT_PAY = "yyyyMMddHHmmss";

    // 登陆者SessionKey
    public static final String ADMIN_SESSION_KEY = "admin";
    public static final String USER_SESSION_KEY = "user";
    public static final String TOKEN_KEY = "token";

    // 手机号规则
    public static final String PHONE_NO_PATTEN = "1\\d{10}";
    // 密码规则：英文字母和数字，6位到20位
    public static final String PASSWORD_PATTEN = "[A-Za-z0-9]{6,20}";
    // 用户名称规则：英文字母和数字、中文文字，2到50个字符
    public static final String NAME_PATTEN = "[A-Za-z0-9\\u4e00-\\u9fa5]{2,50}";

    // 权限拦截类型
    public static final String AUTHORITY_NONE = "None"; // 不拦截。
    public static final String AUTHORITY_USER = "User"; // 公共用户权限。
    public static final String AUTHORITY_SERVICE_PROVIDER = "ServiceProvider"; // 服务人员拦截。
    public static final String AUTHORITY_MERCHANT = "Merchant"; // 商户权限拦截。
    public static final String AUTHORITY_ADMIN = "Admin"; // 管理员权限。

}
