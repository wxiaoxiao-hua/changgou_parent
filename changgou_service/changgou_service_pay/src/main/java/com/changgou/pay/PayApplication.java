package com.changgou.pay;

import com.github.wxpay.sdk.MyConfig;
import com.github.wxpay.sdk.WXPay;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class PayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class,args);
    }

    // 创建wxPay的bean对象, 给微信发送支付请求的时候,需要用这个对象来调用方法
    @Bean
    public WXPay wxPay(){
        try {
            return new WXPay(new MyConfig());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
