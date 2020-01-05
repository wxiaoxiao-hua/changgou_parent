package com.changgou.pay.service;

import java.util.Map;

public interface WXPayService {
    // 统一下单接口
    Map nativePay(String orderId, Integer money);
}
