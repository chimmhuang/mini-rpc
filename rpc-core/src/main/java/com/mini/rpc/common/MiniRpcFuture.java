package com.mini.rpc.common;

import io.netty.util.concurrent.Promise;
import lombok.Data;

/**
 * rpc 异步调用
 */
@Data
public class MiniRpcFuture<T> {

    /**
     * 采用 Netty 提供的 Promise 工具来实现 RPC 请求的同步等待
     * Promise 基于 JDK 的 Future 扩展了更多新的特性
     */
    private Promise<T> promise;
    private long timeout;

    public MiniRpcFuture(Promise<T> promise, long timeout) {
        this.promise = promise;
        this.timeout = timeout;
    }
}
