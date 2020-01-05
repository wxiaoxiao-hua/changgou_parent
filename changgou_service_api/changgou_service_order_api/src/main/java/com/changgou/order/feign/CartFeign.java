package com.changgou.order.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.xml.transform.Result;
import java.util.Map;


@FeignClient(name = "order") // 申明调用的是erueka里面的 order 服务
public interface CartFeign {

    // 添加购物车信息
    @GetMapping("/cart/add")
    public Result add(@RequestParam("skuId")String skuId,@RequestParam("num")Integer num,Integer type);

    // 查询用户的购物车列表
    @GetMapping("/cart/list")
    public Map list();
}
