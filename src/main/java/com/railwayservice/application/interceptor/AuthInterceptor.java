package com.railwayservice.application.interceptor;

import com.railwayservice.application.cache.MerchantSessionCache;
import com.railwayservice.application.cache.ServiceProviderSessionCache;
import com.railwayservice.application.config.AppConfig;
import com.railwayservice.authority.entity.Admin;
import com.railwayservice.authority.entity.Authority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AuthInterceptor extends HandlerInterceptorAdapter {
    private static final String noneFail = "{\"code\":2,\"success\":false,\"data\":\"\",\"message\":\"无效的服务地址。\"}";
    private static final String ajaxFail = "{\"code\":2,\"success\":false,\"data\":\"\",\"message\":\"您没有足够的权限！\"}";
    private final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
    private MerchantSessionCache merchantSessionCache;
    private ServiceProviderSessionCache serviceProviderSessionCache;

    @Autowired
    public void setMerchantSessionCache(MerchantSessionCache merchantSessionCache) {
        this.merchantSessionCache = merchantSessionCache;
    }

    @Autowired
    public void setServiceProviderSessionCache(ServiceProviderSessionCache serviceProviderSessionCache) {
        this.serviceProviderSessionCache = serviceProviderSessionCache;
    }

    /**
     * 在业务处理器处理请求之前被调用。
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 未定义的服务请求禁止通过。
        if (handler instanceof DefaultServletHttpRequestHandler) {
            logger.debug("拦截到未定义的url：" + request.getRequestURL().toString());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/javascript;charset=utf-8");
            response.getWriter().write(noneFail);
            return false;
        }
        Authorize authorize = ((HandlerMethod) handler).getMethodAnnotation(Authorize.class);
        String type = AppConfig.AUTHORITY_NONE;
        if (authorize != null) { // 方法上的注解优先。
            type = authorize.type();
        } else { // 方法上没有注解才考虑类上的注解。
            authorize = ((HandlerMethod) handler).getBeanType().getAnnotation(Authorize.class);
            if (authorize != null) {
                type = authorize.type();
            }
        }

        // 用户权限拦截，基于Session。
        if (AppConfig.AUTHORITY_USER.equals(type)) {
            // TODO 放行
            if (true) return true;

//            @SuppressWarnings("unused")
//        	logger.info("AuthInterceptor");
//			Object user = request.getSession().getAttribute(AppConfig.USER_SESSION_KEY);
//            if (user == null) {
//
////            	logger.info("");
//
//            	String jumpUrl = WeixinConfig.HOST + "/entrance/index";
//            	response.sendRedirect(jumpUrl);
//                /*dealFailResponse(response);*/
//                return false;
//            }else{
//            	return true;
//            }
            // 服务人员权限拦截，基于Token。
        } else if (AppConfig.AUTHORITY_SERVICE_PROVIDER.equals(type)) {
            // TODO 放行
            if (true) return true;
            // 验证Header中的token令牌。
            @SuppressWarnings("unused")
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            if (token == null || serviceProviderSessionCache.get(token) == null) {
                dealFailResponse(request, response);
                return false;
            }
            // 商户权限拦截，基于Token。
        } else if (AppConfig.AUTHORITY_MERCHANT.equals(type)) {
            // TODO 放行
            if (true) return true;
            // 验证Header中的token令牌。
            @SuppressWarnings("unused")
            String token = request.getHeader(AppConfig.TOKEN_KEY);
            if (token == null || merchantSessionCache.get(token) == null) {
                dealFailResponse(request, response);
                return false;
            }
            // 管理员权限拦截，基于Session。
        } else if (AppConfig.AUTHORITY_ADMIN.equals(type)) {
            // TODO 放行
            if (true) return true;
            @SuppressWarnings("unused")
            Admin admin = (Admin) request.getSession().getAttribute(AppConfig.ADMIN_SESSION_KEY);
            if (admin == null || admin.getAuthoritys() == null
                    || !checkAuthorities(authorize.value(), admin.getAuthoritys())) {
                dealFailResponse(request, response);
                return false;
            }
        }

        // 没有拦截注解或拦截类型不支持的默认不拦截。
        return true;
    }

    /**
     * 检查权限编码数组是否完全具有。
     *
     * @param authorities   需要的权限编码。
     * @param authorityList 提供的权限对象。
     */
    private boolean checkAuthorities(String[] authorities, List<Authority> authorityList) throws IOException {
        int countAuthority = 0;
        for (String authorityCode : authorities) {
            for (Authority authority : authorityList) {
                if (authorityCode.equals(authority.getCode())) {
                    countAuthority++;
                    break; // 找到权限，本次循环结束。
                }
            }
        }
        return countAuthority == authorities.length;
    }

    /**
     * 处理鉴权失败的请求，异步调用返回Json。
     */
    private void dealFailResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("拦截到未授权的用户：" + request.getRemoteAddr() + " 请求：" + request.getRequestURI());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/javascript;charset=utf-8");
        response.getWriter().write(ajaxFail);
    }

}

