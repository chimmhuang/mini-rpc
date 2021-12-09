package com.mini.rpc.common;

import lombok.Data;

import java.io.Serializable;

/**
 * rpc 请求服务对象
 */
@Data
public class MiniRpcRequest implements Serializable {
    private String serviceVersion;
    private String className;
    private String methodName;
    private Object[] params;
    private Class<?>[] parameterTypes;
}
