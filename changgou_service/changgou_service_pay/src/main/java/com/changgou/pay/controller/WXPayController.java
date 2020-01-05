package com.changgou.pay.controller;

import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.pay.service.WXPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/wxpay")
public class WXPayController {

    @Autowired
    private WXPayService wxPayService;

    // 统一下单
    @GetMapping("/nativePay")
    // 参数是订单的id 以及需要支付的金额
    public Result nativePay(@RequestParam("orderId")String orderId,@RequestParam("payMoney")Integer payMoney){
        Map map = wxPayService.nativePay(orderId, payMoney);
        return new Result(true, StatusCode.OK,"",map);
    }

}
