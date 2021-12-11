package com.mini.rpc.serialization;

import java.io.IOException;

/**
 * 通用的序列化接口
 */
public interface RpcSerialization {
    <T> byte[] serialize(T obj) throws IOException;

    <T> T deserialize(byte[] data, Class<T> clz) throws IOException;
}