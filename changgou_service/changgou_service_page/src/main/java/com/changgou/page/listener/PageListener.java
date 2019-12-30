package com.changgou.page.listener;


import com.changgou.page.config.RabbitMQConfig;
import com.changgou.page.service.PageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PageListener {

    // 创建这个监听类,用来监听队列,一旦获得消息,就生成静态化页面
    @Autowired
    private PageService pageService;

    // 申明要监听的队列是哪一个
    @RabbitListener(queues = RabbitMQConfig.PAGE_CREATE_QUEUE)
    public void receiveMessage(String spuId){
        // 可以用日志打印一下信息
        log.info("生成商品的详情页面,商品的id为:"+spuId);
        // 生成静态化页面
        pageService.generateItemPage(spuId);
    }
}
