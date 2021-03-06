package com.changgou.goods.feign;

import com.changgou.common.entity.PageResult;
import com.changgou.common.entity.Result;
import com.changgou.goods.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "goods")
public interface SkuFeign {

    // 根据多条件查询品牌的数据
    @GetMapping("/sku/spu/{spuId}/{page}")
    public PageResult<Sku> findSkuPageBySpuId(@PathVariable("spuId")String spuId, @PathVariable("page")Integer page);

    @GetMapping("/sku/spu/{spuId}")
    public List<Sku> findSkuListBySpuId(@PathVariable("spuId")String spuId);

    @GetMapping("/sku/{id}")
    public Result<Sku> findById(@PathVariable("id")String id);

    // 减少库存的远程api
    @PostMapping("/sku/decr/count")
    public Result decrCount(@RequestParam("username")String username);
}
