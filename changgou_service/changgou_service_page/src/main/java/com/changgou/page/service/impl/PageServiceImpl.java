package com.changgou.page.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.common.entity.Result;
import com.changgou.goods.feign.CategoryFeign;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.page.service.PageService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class PageServiceImpl implements PageService {
    @Autowired
    private SpuFeign spuFeign;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private CategoryFeign categoryFeign;

    // 直接从配置文件里面获取到的路径信息
    @Value("${pagepath}")
    private String pagepath;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void generateItemPage(String spuId) {
        // 获取上下文对象,用来存放商品的详细信息
        // 注意: 导包导的是 thymeleaf底下的,不能导错了
        Context context = new Context();
        // 获取到页面所需要的商品的信息
        Map<String, Object> itemData = this.getItemData(spuId);
        context.setVariables(itemData);

        // 获取商品详情页的存储位置
        File dir = new File(pagepath);
        // 如果这个文件夹不存在的话,还要手动创建一下
        if(!dir.exists()){
            dir.mkdirs();
        }

        // 定义输出流,完成文件的生成,创建文件的名称
        File file = new File(dir+"/"+spuId+".html");
        // 先定义输出流为空
        Writer out = null;
        try{
            out = new PrintWriter(file);
            // 生成静态化页面
            templateEngine.process("item",context,out);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            // 关闭输出流
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    // 创建方法,获取静态化页面所需要的数据
    public Map<String,Object> getItemData(String spuId){
        // 创建返回值对象
        Map<String,Object> resultMap = new HashMap<>();
        // 获取对应的spu的数据,
        // spu里面有想要的image数据,分类名称的id,
        Spu spu = spuFeign.findSpuById(spuId).getData();
        resultMap.put("spu",spu);
        // 获取里面的图片信息
        if (spu!=null){
            // 如果相片信息不为空的话
            if(StringUtils.isNotEmpty(spu.getImages())){
                String[] imageList = spu.getImages().split(",");
                resultMap.put("imageList",imageList);
            }
        }

        // 获取对应的分类id,根据id值,在分类表里面查询到对应的分类的信息
        // 方法的返回值全部是result,在这个类里面封装了,整条信息,  getData()
        Integer category1Id = spu.getCategory1Id();
        Category category1 = categoryFeign.findById(category1Id).getData();
        resultMap.put("category1",category1);

        Integer category2Id = spu.getCategory2Id();
        Category category2 = categoryFeign.findById(category2Id).getData();
        resultMap.put("category2",category2);

        Integer category3Id = spu.getCategory3Id();
        Category category3 = categoryFeign.findById(category3Id).getData();
        resultMap.put("category3",category3);

        // 获取相关的sku的信息
        List<Sku> skuList = skuFeign.findSkuListBySpuId(spuId);
        resultMap.put("skuList",skuList);

        // 获取商品的规格信息
        String specItems = spu.getSpecItems();
        // 转换成map集合
        Map map = JSON.parseObject(specItems, Map.class);
        resultMap.put("specificationList",map);

        return resultMap;
    }
}
