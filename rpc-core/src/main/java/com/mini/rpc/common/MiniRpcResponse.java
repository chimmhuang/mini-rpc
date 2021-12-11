package com.mini.rpc.common;

import lombok.Data;

import java.io.Serializable;

/**
 * rpc响应对象
 */
@Data
public class MiniRpcResponse implements Serializable {

    /**
     * 请求结果
     */
    private Object data;

    /**
     * 错误信息
     * 0表示成功
     * 非0表示失败
     */
    private String message;
}
