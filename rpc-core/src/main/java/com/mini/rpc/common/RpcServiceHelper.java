package com.mini.rpc.common;

public class RpcServiceHelper {

    /**
     * 构建服务在注册中心的名称
     *
     * @param serviceName    服务名称
     * @param serviceVersion 服务版本号
     */
    public static String buildServiceKey(String serviceName, String serviceVersion) {
        return String.join("#", serviceName, serviceVersion);
    }
}
