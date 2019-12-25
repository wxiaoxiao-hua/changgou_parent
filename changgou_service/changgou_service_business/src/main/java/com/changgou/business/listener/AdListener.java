package com.changgou.business.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class AdListener {

    @Autowired
    private RestTemplate restTemplate;

    @RabbitListener(queues = "ad_update_queue") // 用的是更新的这个队列
    public void receiveMessage(String message){
        System.out.println("接收到的消息为:"+message);
        String url = "http://192.168.200.128/ad_update?position="+message;
        Map mapResult = restTemplate.getForObject(url, Map.class);
        System.out.println("执行结果:"+mapResult);
    }

}
