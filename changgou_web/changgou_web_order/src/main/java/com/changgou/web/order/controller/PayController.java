/*
package com.changgou.web.order.controller;

import com.changgou.pay.feign.WXPayFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        // 根据orderid, 来查询到订单的信息, 如果订单不存在的话, 跳转到错误页面
        orderFeign.findById(orderId);
    }
}
*/
