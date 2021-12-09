package com.mini.rpc.consumer;

import com.mini.rpc.codec.MiniRpcDecoder;
import com.mini.rpc.codec.MiniRpcEncoder;
import com.mini.rpc.common.MiniRpcRequest;
import com.mini.rpc.common.RpcServiceHelper;
import com.mini.rpc.common.ServiceMeta;
import com.mini.rpc.handler.RpcResponseHandler;
import com.mini.rpc.protocol.MiniRpcProtocol;
import com.mini.rpc.provider.registry.RegistryService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcConsumer {
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public RpcConsumer() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(4);
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                // rpc 编码器
                                .addLast(new MiniRpcEncoder())
                                // rpc 解码器
                                .addLast(new MiniRpcDecoder())
                                // rpc 返回值处理器
                                .addLast(new RpcResponseHandler());
                    }
                });
    }

    /**
     * 发送 rpc 请求
     *
     * @param protocol        rpc协议
     * @param registryService 注册中心服务
     */
    public void sendRequest(MiniRpcProtocol<MiniRpcRequest> protocol, RegistryService registryService) throws Exception {
        MiniRpcRequest request = protocol.getBody();
        Object[] params = request.getParams();
        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getServiceVersion());

        int invokerHashCode = params.length > 0 ? params[0].hashCode() : serviceKey.hashCode();

        // 发现服务
        ServiceMeta serviceMetadata = registryService.discovery(serviceKey, invokerHashCode);

        if (serviceMetadata != null) {
            // 连接服务
            ChannelFuture future = bootstrap.connect(serviceMetadata.getServiceAddr(), serviceMetadata.getServicePort()).sync();

            // 添加返回结果监听
            future.addListener((ChannelFutureListener) arg0 -> {
                if (future.isSuccess()) {
                    log.info("connect rpc server {} on port {} success.", serviceMetadata.getServiceAddr(), serviceMetadata.getServicePort());
                } else {
                    log.error("connect rpc server {} on port {} failed.", serviceMetadata.getServiceAddr(), serviceMetadata.getServicePort());
                    future.cause().printStackTrace();
                    eventLoopGroup.shutdownGracefully();
                }
            });

            // 发送请求
            future.channel().writeAndFlush(protocol);
        }
    }
}
