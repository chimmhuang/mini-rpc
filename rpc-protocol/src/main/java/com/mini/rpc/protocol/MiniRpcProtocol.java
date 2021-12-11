package com.mini.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * 自定义协议
 *
 * +---------------------------------------------------------------+
 * | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  |
 * +---------------------------------------------------------------+
 * | 状态 1byte |        消息 ID 8byte     |      数据长度 4byte     |
 * +---------------------------------------------------------------+
 * |                   数据内容 （长度不定）                          |
 * +---------------------------------------------------------------+
 */
@Data
public class MiniRpcProtocol<T> implements Serializable {

    /**
     * 请求消息协议
     */
    private MsgHeader header;

    /**
     * 携带参数
     */
    private T body;
}
