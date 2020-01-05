package com.changgou.web.order.controller;

import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.order.feign.CartFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/wcart")
public class CartController {

    // 远程调用cartFeign, 首先注入这个接口
    @Autowired
    private CartFeign cartFeign;

    // 添加购物车
    @GetMapping("/add")
    @ResponseBody
    public Result<Map> add(String id,Integer num,Integer type){
        // 调用接口的方法
        cartFeign.add(id,num,type);
        // 添加新数据之后,再重新查询一次列表
        Map map = cartFeign.list();
        return new Result(true, StatusCode.OK,"添加购物车成功",map);
    }

    // 添加购物车,跳转到 cart.html 购物车的页面
    @PostMapping("/addCart")
    public String addCart(String id, Integer num, Integer type, Model model){
        // 调用接口的方法
        cartFeign.add(id,num,type);
        // 添加新数据之后,再重新查询一次列表
        Map map = cartFeign.list();
        // 将数据返回到页面上去
        model.addAttribute("items",map);
        return "cart";
    }

    // 查询购物车的列表
    @GetMapping("/list")
    public String list(Model model){
        Map map = cartFeign.list();
        model.addAttribute("items",map);
        return "cart";
    }

}
