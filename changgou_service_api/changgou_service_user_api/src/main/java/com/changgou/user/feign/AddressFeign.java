package com.changgou.user.feign;

import com.changgou.common.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;



@FeignClient(name = "user")
public interface AddressFeign {

    @GetMapping("/address/list")
    public Result list();
}
