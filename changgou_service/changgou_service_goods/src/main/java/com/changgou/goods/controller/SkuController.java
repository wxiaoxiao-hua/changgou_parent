package com.changgou.goods.controller;

import com.changgou.common.entity.PageResult;
import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.goods.service.SkuService;
import com.changgou.goods.pojo.Sku;
import com.github.pagehelper.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin
@RequestMapping("/sku")
public class SkuController {


    @Autowired
    private SkuService skuService;

    /**
     * 查询全部数据
     * @return
     */
    @GetMapping
    public Result findAll(){
        List<Sku> skuList = skuService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",skuList) ;
    }

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable String id){
        Sku sku = skuService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功",sku);
    }


    /***
     * 新增数据
     * @param sku
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Sku sku){
        skuService.add(sku);
        return new Result(true,StatusCode.OK,"添加成功");
    }


    /***
     * 修改数据
     * @param sku
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody Sku sku,@PathVariable String id){
        sku.setId(id);
        skuService.update(sku);
        return new Result(true,StatusCode.OK,"修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable String id){
        skuService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @PostMapping(value = "/search" )
    public Result findList(@RequestBody Map searchMap){
        List<Sku> list = skuService.findList(searchMap);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @PostMapping(value = "/search/{page}/{size}" )
    public Result findPage(@RequestBody Map searchMap, @PathVariable  int page, @PathVariable  int size){
        Page<Sku> pageList = skuService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }


    // 根据sku的id查询对应的sku的列表
    @GetMapping("/spu/{spuId}/{page}")
    public PageResult<Sku> findSkuPageBySpuId(@PathVariable("spuId")String spuId, @PathVariable("page")Integer page){
        // 把搜索的条件封装成为一个map集合
        Map<String,Object> searchMap = new HashMap<>();

        // 对条件进行判断
        if(!"all".equals(spuId)){
            // 如果不是所有的spuId的话,,向集合里依次添加数据信息
            searchMap.put("spuId",spuId);
        }
        searchMap.put("status","1");
        Page<Sku> skuPage = skuService.findPage(searchMap, page, 1000);

        // 在pageResult中添加一个 page总页数的字段
        PageResult<Sku> pageResult = new PageResult(skuPage.getTotal(),skuPage.getResult(),skuPage.getPages());
        return pageResult;
    }

    // 修改商品的库存, tb_sku 的 num
    @PostMapping("/decr/count")
    public Result decrCount(@RequestParam("username")String username){
        skuService.decrCount(username);
        return new Result(true,StatusCode.OK,"库存扣减成功");
    }

}
