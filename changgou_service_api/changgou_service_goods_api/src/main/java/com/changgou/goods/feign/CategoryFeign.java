package com.changgou.goods.feign;

import com.changgou.common.entity.Result;
import com.changgou.goods.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "goods")
// 是注册中心里面的goods 服务
public interface CategoryFeign {

    // 填方法的路径的时候,一定要连端口号后面,所有的路径全部拿过来
    @GetMapping("/category/{id}")
    public Result<Category> findById(@PathVariable("id")Integer id );
}
