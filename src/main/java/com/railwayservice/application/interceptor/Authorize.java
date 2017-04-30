package com.railwayservice.application.interceptor;

import com.railwayservice.application.config.AppConfig;

import java.lang.annotation.*;

@Inherited
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {
    // 鉴权类型（必须指定才有效）
    String type() default AppConfig.AUTHORITY_NONE;

    // 需要的权限数组（可选）
    String[] value() default {};
}