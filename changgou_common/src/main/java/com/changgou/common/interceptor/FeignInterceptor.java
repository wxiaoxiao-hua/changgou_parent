package com.changgou.common.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Component
public class FeignInterceptor implements RequestInterceptor {
    /**
     *  feign 支持请求拦截器,在发送请求前,可以对发送的模板进行操作
     *
     */

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 传递令牌,先获取到上下文对象
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null){
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            if(request != null){
                Enumeration<String> headerNames = request.getHeaderNames();
                // 获取到所有的头信息
                while(headerNames.hasMoreElements()){
                    String headerName = headerNames.nextElement();
                    if("authorization".equals(headerName)){
                        String headerValue = request.getHeader(headerName);
                        // 传递令牌
                        requestTemplate.header(headerName,headerValue);
                    }
                }
            }
        }
    }
}
