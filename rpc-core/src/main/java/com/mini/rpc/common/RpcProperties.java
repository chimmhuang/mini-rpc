package com.mini.rpc.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 服务提供者 rpc 参数
 */
@Data
@ConfigurationProperties(prefix = "rpc")
public class RpcProperties {

    /**
     * 服务暴露端口
     */
    private int servicePort;

    /**
     * 注册中心地址
     */
    private String registryAddr;

    /**
     * 注册中心类型
     */
    private String registryType;

}
