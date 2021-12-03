package com.mini.rpc.consumer.annotation;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义服务引用注解
 * @author huangshuai
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Autowired
public @interface RpcReference {

    /**
     * 服务号版本
     */
    String serviceVersion() default "1.0";

    /**
     * 注册中心类型
     */
    String registryType() default "ZOOKEEPER";

    /**
     * 注册中心地址
     */
    String registryAddress() default "127.0.0.1:2181";

    /**
     * 超时时间
     */
    long timeout() default 5000;

}
