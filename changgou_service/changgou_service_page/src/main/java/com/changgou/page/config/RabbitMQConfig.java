package com.changgou.page.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 申明这是一个配置类
public class RabbitMQConfig {
    // 定义交换机的名称,上传商品
    public static final String GOODS_UP_EXCHANGE = "goods_up_exchange";
    // 定义交换机的名称,下架商品
    public static final String GOODS_DOWN_EXCHANGE = "goods_down_exchange";

    // 定义队列的名称,更新广告
    public static final String AD_UPDATE_QUEUE="ad_update_queue";
    public static final String SEARCH_ADD_QUEUE ="search_add_queue";
    public static final String SEARCH_DEL_QUEUE ="search_delete_queue";
    // 定义队列的名称,用于创建静态页面
    public static final String PAGE_CREATE_QUEUE="page_create_queue";

    // 申明交换机
    @Bean(GOODS_UP_EXCHANGE)
    public Exchange GOODS_UP_EXCHANGE(){
        return ExchangeBuilder.fanoutExchange(GOODS_UP_EXCHANGE).durable(true).build();
    }

    @Bean(GOODS_DOWN_EXCHANGE)
    public Exchange GOODS_DOWN_EXCHANGE(){
        return ExchangeBuilder.fanoutExchange(GOODS_DOWN_EXCHANGE).durable(true).build();
    }


    // 申明队列,这是一个更新广告的队列
    @Bean(AD_UPDATE_QUEUE)
    public Queue AD_UPDATE_QUEUE(){
        return new Queue("ad_update_queue");
    }

    @Bean(SEARCH_ADD_QUEUE)
    public Queue SEARCH_ADD_QUEUE(){
        return new Queue(SEARCH_ADD_QUEUE);
    }

    @Bean(SEARCH_DEL_QUEUE)
    public Queue SEARCH_DEL_QUEUE(){
        return new Queue(SEARCH_DEL_QUEUE);
    }

    @Bean(PAGE_CREATE_QUEUE)
    public Queue PAGE_CREATE_QUEUE(){
        return new Queue(PAGE_CREATE_QUEUE);
    }


    // 申明交换机和队列的绑定
    @Bean
    public Binding GOODS_UP_EXCHANGE_BINDING(@Qualifier(SEARCH_ADD_QUEUE)Queue queue,@Qualifier(GOODS_UP_EXCHANGE)Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
    }

    @Bean
    public Binding GOODS_DOWN_EXCHANGE_BINDING(@Qualifier(SEARCH_DEL_QUEUE)Queue queue,@Qualifier(GOODS_DOWN_EXCHANGE)Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
    }

    // 申明创建静态页面的队列和交换机的绑定
    @Bean
    public Binding PAGE_CREATE_QUEUE_BINDING(@Qualifier(PAGE_CREATE_QUEUE)Queue queue,@Qualifier(GOODS_UP_EXCHANGE)Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
    }
}
