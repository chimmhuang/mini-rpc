package com.mini.rpc.handler;


import com.mini.rpc.common.MiniRpcFuture;
import com.mini.rpc.common.MiniRpcRequestHolder;
import com.mini.rpc.common.MiniRpcResponse;
import com.mini.rpc.protocol.MiniRpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 在 rpc 请求调用的场景下，RpcResponseHandler 并不需要承担编解码操作，所以只需要继承最简单的Handler即可
 */
public class RpcResponseHandler extends SimpleChannelInboundHandler<MiniRpcProtocol<MiniRpcResponse>> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MiniRpcProtocol<MiniRpcResponse> msg) {
        long requestId = msg.getHeader().getRequestId();
        MiniRpcFuture<MiniRpcResponse> future = MiniRpcRequestHolder.REQUEST_MAP.remove(requestId);
        future.getPromise().setSuccess(msg.getBody());
    }
}

