package com.changgou.order.service;

import java.util.Map;

public interface CartService {

    // 新增购物车信息
    public void add(String skuId,Integer num,String username,Integer type);

    // 查看购物车列表
    Map list(String username);
}
