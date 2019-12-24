package com.itheima.canal.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 申明这是一个配置类
public class RabbitMQConfig {

    // 定义队列的名称
    public static final String AD_UPDATE_QUEUE="ad_update_queue";

    // 申明队列,这是一个更新广告的队列
    @Bean
    public Queue queue(){
        return new Queue("ad_update_queue");
    }
}
