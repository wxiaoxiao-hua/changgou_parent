package com.changgou.order.controller;

import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.order.config.TokenDecode;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private TokenDecode tokenDecode;

    @Autowired
    private CartService cartService;

    // 添加数据到购物车
    @GetMapping("/add")
    public Result add(@RequestParam("skuId")String skuId,@RequestParam("num")Integer num,
                      @RequestParam(value = "type",required = false,defaultValue = "")Integer type){
        // 动态获取用户名 (从令牌里面获取信息)
        String username = tokenDecode.getUserInfo().get("username");

        cartService.add(skuId,num,username,type);

        return new Result(true, StatusCode.OK,"加入购物车成功");
    }

    // 查看购物车列表
    @GetMapping("/list")
    public Map list(){
        // 先静态获取用户名
        // String username ="itcast";
        // 动态获取用户的信息,这些内容都封装到令牌信息里面去了
        String username = tokenDecode.getUserInfo().get("username");

        Map map = cartService.list(username);
        return map;
    }
}
