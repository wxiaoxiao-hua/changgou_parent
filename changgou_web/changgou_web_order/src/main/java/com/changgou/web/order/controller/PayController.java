package com.changgou.web.order.controller;

import com.changgou.order.feign.OrderFeign;
import com.changgou.common.entity.Result;

import com.changgou.order.pojo.Order;
import com.changgou.pay.feign.WXPayFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;


@Controller
@RequestMapping("/wxpay")
public class PayController {
    // 注入远程调用的api接口
    @Autowired
    private WXPayFeign payFeign;
    @Autowired
    private OrderFeign orderFeign;

    // 跳转到微信支付的二维码页面
    @GetMapping
    public String wxPay(String orderId, Model model){
        // 1.根据orderid, 来查询到订单的信息, 如果订单不存在的话, 跳转到错误页面
        Result orderResult= (Result) orderFeign.findById(orderId);
        // 判断数据是否存在
        if(orderResult.getData()==null){
            return "fail";
        }

        //2.根据订单的支付状态进行判断,如果不是未支付的订单,跳转到错误页面
        Order order = (Order) orderResult.getData();
        if (!"0".equals(order.getPayStatus())){
            return "fail";
        }

        // 3.基于payFeign调用统计下单的接口,并且返回获得的结果
        Result payResult = payFeign.nativePay(orderId, order.getPayMoney());
        if (payResult.getData() == null){
            return "fail";
        }

        // 4.封装结果数据
        Map pagMap = (Map) payResult.getData();
        pagMap.put("orderId",orderId);
        pagMap.put("payMoney",order.getPayMoney());

        model.addAttribute(pagMap);
        return "wxpay";

    }
}
