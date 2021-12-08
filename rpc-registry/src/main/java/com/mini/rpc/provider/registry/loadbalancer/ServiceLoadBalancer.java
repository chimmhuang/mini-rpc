package com.mini.rpc.provider.registry.loadbalancer;

import java.util.List;

/**
 * 负载均衡接口
 */
public interface ServiceLoadBalancer<T> {
    T select(List<T> servers, int hashCode);
}