package com.changgou.web.order.controller;

import com.changgou.common.entity.Result;
import com.changgou.order.feign.CartFeign;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.user.feign.AddressFeign;
import com.changgou.user.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/worder")
public class OrderController {

    @Autowired
    private AddressFeign addressFeign;
    @Autowired
    private CartFeign cartFeign;
    @Autowired
    private OrderFeign orderFeign;

    @RequestMapping("/ready/order")
    public String readyOrder(Model model){
        // 收件人的地址信息
        List<Address> addressList = (List<Address>) addressFeign.list().getData();
        model.addAttribute("adress",addressList);

        // 购物车的信息
        Map map = cartFeign.list();
        List<OrderItem> orderItemList = (List<OrderItem>)map.get("orderItemList");
        Integer totalMoney = (Integer) map.get("totalMoney");
        Integer totalNum = (Integer) map.get("totalNum");

        model.addAttribute("carts",orderItemList);
        model.addAttribute("totalMoney",totalMoney);
        model.addAttribute("totalNum",totalNum);

        // 默认收件人的信息
        for (Address address:addressList){
            if("1".equals(address.getIsDefault())){
                // 默认收件人
                model.addAttribute("deAddr",address);
                break;
            }
        }
        return "order";
    }

    @PostMapping("/add")
    @ResponseBody
    public Result add(@RequestBody Order order){
       Result result  = (Result) orderFeign.add(order);
       return result;
    }

}
