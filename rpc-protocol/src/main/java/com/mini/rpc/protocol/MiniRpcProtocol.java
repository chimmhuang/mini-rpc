package com.mini.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * rpc 协议对象
 */
@Data
public class MiniRpcProtocol<T> implements Serializable {
    private MsgHeader header;
    private T body;
}
