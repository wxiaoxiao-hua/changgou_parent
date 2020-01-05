package com.changgou.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import javax.xml.transform.Result;

@FeignClient(name = "user")
public interface AddressFeign {

    @GetMapping("/address/list")
    public Result list();
}
