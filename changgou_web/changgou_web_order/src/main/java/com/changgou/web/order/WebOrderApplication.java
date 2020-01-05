package com.changgou.web.order;

import com.changgou.common.interceptor.FeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.changgou.order.feign","com.changgou.user.feign","com.changgou.pay.feign"})
public class WebOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebOrderApplication.class,args);
    }

    // 添加拦截器的申明
    @Bean
    public FeignInterceptor feignInterceptor(){
        return new FeignInterceptor();
    }


}
