package com.mini.rpc.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * MiniRpcRequestHolder 负责响应不同线程的请求结果
 */
public class MiniRpcRequestHolder {

    public static final AtomicLong REQUEST_ID_GEN = new AtomicLong(0);

    public static final Map<Long, MiniRpcFuture<MiniRpcResponse>> REQUEST_MAP = new ConcurrentHashMap<>();
}
