package com.mini.rpc.common;

import lombok.Data;

import java.io.Serializable;

/**
 * rpc请求对象
 */
@Data
public class MiniRpcRequest implements Serializable {

    /**
     * 服务版本
     */
    private String serviceVersion;

    /**
     * 服务接口名
     */
    private String className;

    /**
     * 服务方法名
     */
    private String methodName;

    /**
     * 方法参数列表
     */
    private Object[] params;

    /**
     * 方法参数类型列表
     */
    private Class<?>[] parameterTypes;
}
