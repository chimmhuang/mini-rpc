package com.mini.rpc.handler;

import com.mini.rpc.common.MiniRpcRequest;
import com.mini.rpc.common.MiniRpcResponse;
import com.mini.rpc.common.RpcServiceHelper;
import com.mini.rpc.protocol.MiniRpcProtocol;
import com.mini.rpc.protocol.MsgHeader;
import com.mini.rpc.protocol.MsgStatus;
import com.mini.rpc.protocol.MsgType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.reflect.FastClass;

import java.util.Map;

/**
 * 在 rpc 请求调用的场景下，RpcRequestHandler 并不需要承担编解码操作，所以只需要继承最简单的Handler即可
 */
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<MiniRpcProtocol<MiniRpcRequest>> {

    private final Map<String, Object> rpcServiceMap;

    public RpcRequestHandler(Map<String, Object> rpcServiceMap) {
        this.rpcServiceMap = rpcServiceMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MiniRpcProtocol<MiniRpcRequest> protocol) {
        // 使用自己的线程池来执行请求任务
        RpcRequestProcessor.submitRequest(() -> {
            // 定义返回值
            MiniRpcProtocol<MiniRpcResponse> resProtocol = new MiniRpcProtocol<>();
            MiniRpcResponse response = new MiniRpcResponse();
            MsgHeader header = protocol.getHeader();
            header.setMsgType((byte) MsgType.RESPONSE.getType());
            try {
                // 执行方法
                Object result = handle(protocol.getBody());
                response.setData(result);

                header.setStatus((byte) MsgStatus.SUCCESS.getCode());
                resProtocol.setHeader(header);
                resProtocol.setBody(response);
            } catch (Throwable throwable) {
                header.setStatus((byte) MsgStatus.FAIL.getCode());
                response.setMessage(throwable.toString());
                log.error("process request {} error", header.getRequestId(), throwable);
            }
            ctx.writeAndFlush(resProtocol);
        });
    }

    /**
     * 执行 rpc 调用方法
     */
    private Object handle(MiniRpcRequest request) throws Throwable {
        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getServiceVersion());
        // 获取服务提供方
        Object serviceBean = rpcServiceMap.get(serviceKey);

        if (serviceBean == null) {
            throw new RuntimeException(String.format("service not exist: %s:%s", request.getClassName(), request.getMethodName()));
        }

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParams();

        /*
            使用 cglib 提供的 FastClass 机制直接调用方法
            FastClass 机制并没有采用反射的方式调用被代理的方法，而是运行时动态生成一个新的 FastClass 子
            类，向子类中写入直接调用目标方法的逻辑。同时该子类会为代理类分配一个 int 类型的 index 索引，
            FastClass 即可通过 index 索引定位到需要调用的方法。
         */
        FastClass fastClass = FastClass.create(serviceClass);
        int methodIndex = fastClass.getIndex(methodName, parameterTypes);

        // 执行方法，并返回结果
        return fastClass.invoke(methodIndex, serviceBean, parameters);
    }
}
