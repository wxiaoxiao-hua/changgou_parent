package com.changgou.order.service.impl;

import com.changgou.common.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    private static final String CART="Cart_";

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private SpuFeign spuFeign;

    // 添加数据到购物车
    @Override
    public void add(String skuId, Integer num, String username,Integer type) {
        // 先从redis里面查询是否 数据已经存在过
        OrderItem orderItem = (OrderItem) redisTemplate.boundHashOps(CART + username).get(skuId);
        if (orderItem != null){
            // 如果redis里面存在数据的话,就刷新购物车,主要是更新商品的数量,价格,需要支付的价格
            // 这里判断一下类型是修改还是直接新增
            if(type == 1){
                orderItem.setNum(num);
            }else{
                orderItem.setNum(orderItem.getNum() + num);
            }
            // 处理数量小于等于0 的情况
            if (orderItem.getNum() <= 0){
                // 删除这个商品
                redisTemplate.boundHashOps(CART +username).delete(skuId);
                // 然后方法就直接返回了
                return;
            }
            orderItem.setMoney(orderItem.getNum()*orderItem.getPrice());
            orderItem.setPayMoney(orderItem.getNum()*orderItem.getPrice());
        }else{
            // 不存在,新增购物车信息
            Result<Sku> skuResult = skuFeign.findById(skuId);
            // 获取到对应的sku的信息
            Sku sku = skuResult.getData();
            // 根据sku的id来获取到对应spu的信息
            Result<Spu> spuResult = spuFeign.findSpuById(sku.getSpuId());
            Spu spu = spuResult.getData();

            // 调用方法将 sku转换成 OrderItem
            orderItem = this.sku2OrderItem(sku,spu,num);
        }
        // 将数据存入到redis里去
        redisTemplate.boundHashOps(CART+username).put(skuId,orderItem);
    }

    // 将sku信息转换成orderItem
    private OrderItem sku2OrderItem(Sku sku,Spu spu, Integer num){
        OrderItem orderItem = new OrderItem();
        orderItem.setSpuId(sku.getSpuId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        orderItem.setMoney(orderItem.getPrice()*num);
        orderItem.setPayMoney(orderItem.getPrice()*num);
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(sku.getWeight()*num);
        //分类信息
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        return orderItem;
    }


    // 查看购物车列表
    @Override
    public Map list(String username) {
        // 先创建一个map集合,用来封装数据
        Map map = new HashMap();

        // 购物车里的数据都是存放在redis里面的,用redis来获取
        // 存放进redis里面的时候,键全部是 Cart_ + username 来存入的
        List<OrderItem> orderItemList = redisTemplate.boundHashOps(CART + username).values();
        map.put("orderItemList",orderItemList);

        // 定义商品的数量和价格的变量,并且给初始化的值
        Integer totalNum = 0;
        Integer totalPrice = 0;

        // 遍历从redis里面获取到的每一条数据信息
        for (OrderItem orderItem : orderItemList){
            totalNum += orderItem.getNum();
            totalPrice += orderItem.getMoney();
        }
        // 再将信息全部存入map集合,进行返回
        map.put("totalNum",totalNum);
        map.put("totalPrice",totalPrice);
        return map;
    }
}
