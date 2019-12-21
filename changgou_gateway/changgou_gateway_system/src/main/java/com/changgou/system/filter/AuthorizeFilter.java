package com.changgou.system.filter;

import com.changgou.common.model.response.CommonCode;
import com.changgou.common.model.response.ResponseResult;
import com.changgou.system.util.JsonUtils;
import com.changgou.system.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class AuthorizeFilter implements GlobalFilter,Ordered{
    private static final String AUTHORIZE_TOKEN = "token";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求对象
        ServerHttpRequest request = exchange.getRequest();
        // 获取响应的对象
        ServerHttpResponse response = exchange.getResponse();

        // 判断当前的请求是否为登录请求,如果是的话,就直接放行
        if (request.getURI().getPath().contains("/admin/login")){
            // 如果包含的话,就直接放行了
            return chain.filter(exchange);
        }
        // 获取当前的所有的请求头的信息
        HttpHeaders headers = request.getHeaders();
        // 获取jwt令牌的信息,前端在header里面进行了封装
        String jwtToken = headers.getFirst("token");

        // 判断当前的令牌是否存在
        if (StringUtils.isEmpty(jwtToken)){
            ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHORISE);
            String resultJson = JsonUtils.objectToJson(responseResult);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            return response.writeWith(Flux.just(resultJson).map(bx -> response.bufferFactory().wrap(bx.getBytes())));
        }
        //6.1 如果令牌存在,解析jwt令牌,判断该令牌是否合法,如果令牌不合法,则向客户端返回错误提示信息
        try {
            //解析令牌
            Claims claims = JwtUtil.parseJWT(jwtToken);
            log.info("claims: {} ", claims);

            // TODO  判断当前用户是否有权限操作， 得到的是权限集合
            List<String> roles = claims.get(JwtUtil.JWT_CLAIMS, List.class);  // JSON
            if (roles==null || roles.size()==0 || !"admin".equals(roles.get(0))) {
                ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHORISE);
                String resultJson = JsonUtils.objectToJson(responseResult);
                response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                return response.writeWith(Flux.just(resultJson).map(bx -> response.bufferFactory().wrap(bx.getBytes())));
            }
        } catch (Exception e) {
            e.printStackTrace();
            //令牌解析失败
            //向客户端返回错误提示信息
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();

            ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHORISE_JWT);
            String resultJson = JsonUtils.objectToJson(responseResult);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            return response.writeWith(Flux.just(resultJson).map(bx -> response.bufferFactory().wrap(bx.getBytes())));
        }

        //6.2 如果令牌合法,则放行
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
