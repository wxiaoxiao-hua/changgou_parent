package com.changgou.pay.service.impl;

import com.changgou.pay.service.WXPayService;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class WXPayServiceImpl implements WXPayService {

    @Autowired
    private WXPay wxPay;

    // 直接从配置文件里面获取 回调的url地址
    @Value("${wxpay.notify_url}")
    private String notify_url;

    // 统一下单 接口调用
    @Override
    public Map nativePay(String orderId, Integer money) {
        try {
            // 首先要封装请求的参数
            Map<String,String> map = new HashMap<>();
            map.put("body","畅购");
            map.put("out_trade_no",orderId);

            // 价格,一般支付的价格要用 bigDecimal 来处理
            BigDecimal payMoney = new BigDecimal("0.01");  // 这里的价格是以 元 为单位的
            // 这里的结果是1,这个方法是指, payMoney * 100
            BigDecimal fen = payMoney.multiply(new BigDecimal("100"));
            //这个方法的第一个参数: 是说保留几位小数. 第二个参数:是始终都+1 的意思
            // 准确的说是小数点后面有n+1位小数，当保留n位小数时，这第n位小数始终+1
            fen = fen.setScale(0,BigDecimal.ROUND_UP);
            map.put("total_fee",String.valueOf(fen));

            map.put("spbill_create_ip","127.0.0.1");
            map.put("notify_url",notify_url);
            map.put("trade_type","NATIVE");

            // 基于wxpay这个对象来完成统一下单的 接口的调用,并且获取返回的结果
            Map<String, String> result = wxPay.unifiedOrder(map);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
