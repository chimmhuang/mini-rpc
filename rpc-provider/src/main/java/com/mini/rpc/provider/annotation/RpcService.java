package com.mini.rpc.provider.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义服务提供注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface RpcService {

    /**
     * 服务器提供接口
     */
    Class<?> serviceInterface() default Object.class;

    /**
     * 服务版本号
     */
    String serviceVersion() default "1.0";
}
