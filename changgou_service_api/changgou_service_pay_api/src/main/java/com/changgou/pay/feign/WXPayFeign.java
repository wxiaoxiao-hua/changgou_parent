package com.changgou.pay.feign;

import com.changgou.common.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("pay")
public interface WXPayFeign {

    // 远程调用下单这个服务
    @GetMapping("/wxpay/nativePay")
    public Result nativePay(@RequestParam("orderId")String orderId,@RequestParam("payMoney")Integer payMoney);
}
