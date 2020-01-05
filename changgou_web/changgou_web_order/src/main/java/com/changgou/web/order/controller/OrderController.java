package com.changgou.web.order.controller;

import com.changgou.order.feign.CartFeign;
import com.changgou.order.pojo.OrderItem;
import com.changgou.user.feign.AddressFeign;
import com.changgou.user.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.*;
import java.util.Map;

@Controller
@CrossOrigin
@RequestMapping("/worder")
public class OrderController {

    @Autowired
    private AddressFeign addressFeign;
    @Autowired
    private CartFeign cartFeign;

    @RequestMapping("/ready/order")
    public String readyOrder(Model model){
        // 收件人的地址信息
        List<Address> addressList = addressFeign.list().getData();
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

}
