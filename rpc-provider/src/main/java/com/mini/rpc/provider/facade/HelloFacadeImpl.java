package com.mini.rpc.provider.facade;

import com.mini.rpc.provider.annotation.RpcService;

/**
 * 服务实现类
 */
@RpcService(serviceInterface = HelloFacade.class, serviceVersion = "1.0.0")
public class HelloFacadeImpl implements HelloFacade {

    @Override
    public String hello(String name) {
        return "hello" + name;
    }
}
