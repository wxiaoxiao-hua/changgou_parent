package com.changgou.order.feign;

import com.changgou.order.pojo.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.xml.transform.Result;

@FeignClient(name = "order")
public interface OrderFeign {

    // 提交ding订单数据
    @PostMapping("/order")
    Result add(@RequestBody Order order);

    @GetMapping("/order/{id}")
    public Result findById(@PathVariable("id") String id);
}