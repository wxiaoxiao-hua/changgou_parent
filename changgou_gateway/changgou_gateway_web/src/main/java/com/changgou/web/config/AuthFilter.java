package com.changgou.web.config;

import com.changgou.web.service.AuthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter,Ordered{

    public static final String Authorization = "Authorization";  // 认证

    @Autowired
    private AuthService authService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取当前的请求对象
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // 判断当前的请求路径是否为登录请求,如果是,就直接放行
        String path = request.getURI().getPath();
        if("/api/oauth/login".equals(path)){
            // 放行
            return chain.filter(exchange);
        }

        //从cookie中获取jti的值,如果该值不存在,拒绝本次访问
        String jti = authService.getJtiFromCookie(request);
        if(StringUtils.isNotEmpty(jti)){
            // 返回拒绝访问
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 从cookie中取得jwt的值,如果该值不存在,拒绝本次访问
        String jwt = authService.getJwtFromRedis(jti);
        if(StringUtils.isEmpty(jwt)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        // 对当前的请求对象进行增强,让它会携带令牌的信息
        request.mutate().header("Authorization","Bearer "+jwt);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
