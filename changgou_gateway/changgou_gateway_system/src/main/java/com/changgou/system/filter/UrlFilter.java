package com.changgou.system.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


public class UrlFilter implements GlobalFilter,Ordered { // GlobalFilter 全局过滤器,会应用到所有的路由上
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("经过的第二个过滤器");
        ServerHttpRequest request = exchange.getRequest();
        String url = request.getURI().getPath();
        System.out.println("url:"+url);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {  // 这个方法是给过滤器设置优先级别的,值越大,优先级越低
        return 2;
    }
}
