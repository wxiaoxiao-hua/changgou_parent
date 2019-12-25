package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.common.entity.PageResult;
import com.changgou.common.exception.ExceptionCast;
import com.changgou.common.model.response.CommonCode;
import com.changgou.common.model.response.goods.GoodsCode;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.ESManagerMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.ESManagerService;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ESManagerServiceImpl implements ESManagerService {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private SkuFeign skuFeign;
    @Autowired
    private ESManagerMapper esManagerMapper;

    // 创建索引库的结构
    @Override
    public void createMappingAndIndex() {
        // 创建索引
        elasticsearchTemplate.createIndex(SkuInfo.class);
        // 创建映射
        elasticsearchTemplate.putMapping(SkuInfo.class);
    }

    // 将所有的信息全部导入索引库
    @Override
    public void importAll() {
        // 查询sku的集合
        PageResult<Sku> pageResult = skuFeign.findSkuPageBySpuId("all", 1);
        if(pageResult == null || pageResult.getTotal()==0 || pageResult.getPage()==0 ){
            ExceptionCast.cast(GoodsCode.GOODS_IMPORT_ERROR);
        }
        // 获取总页数
        Integer page = pageResult.getPage();
        for (int i=1; i<=page; i++){
            List<Sku> skuList = skuFeign.findSkuPageBySpuId("all", i).getRows();

            // 将获取到的skuList转换成为一个json格式的
            String jsonSkuList = JSON.toJSONString(skuList);
            // 再将json格式的数据转换成SkuInfo实体类对象
            List<SkuInfo> skuInfoList = JSON.parseArray(jsonSkuList, SkuInfo.class);
            // 获取到具体的信息,将规格转换成为map集合
            for (SkuInfo skuInfo:skuInfoList){
                Map specMap = JSON.parseObject(skuInfo.getSpec(), Map.class);
                skuInfo.setSpecMap(specMap);
            }

            // 导入索引库
            esManagerMapper.saveAll(skuInfoList);
            System.out.println("导入第"+i+"页sku数据列表成功");
        }

    }

    // 根据spu的id查询到对应的skuIdList,分别将信息导入索引库
    @Override
    public void importDataToESBySpuId(String spuId) {
        // 首先查询到spuid的集合
        PageResult<Sku> pageResult = skuFeign.findSkuPageBySpuId(spuId, 1);
        if(pageResult == null || pageResult.getTotal()==0 || pageResult.getPage()==0 ){
            ExceptionCast.cast(GoodsCode.GOODS_IMPORT_ERROR);
        }
        // 获取总页数
        Integer page = pageResult.getPage();
        for (int i=1; i<=page; i++){
            List<Sku> skuList = skuFeign.findSkuPageBySpuId("all", i).getRows();

            // 将获取到的skuList转换成为一个json格式的
            String jsonSkuList = JSON.toJSONString(skuList);
            // 再将json格式的数据转换成SkuInfo实体类对象
            List<SkuInfo> skuInfoList = JSON.parseArray(jsonSkuList, SkuInfo.class);
            // 获取到具体的信息,将规格转换成为map集合
            for (SkuInfo skuInfo:skuInfoList){
                Map specMap = JSON.parseObject(skuInfo.getSpec(), Map.class);
                skuInfo.setSpecMap(specMap);
            }

            // 导入索引库
            esManagerMapper.saveAll(skuInfoList);
            System.out.println("导入第"+i+"页sku数据列表成功");
        }
    }

    // 根据spu的id对索引库里面的信息进行使用,商品下架之后,不需要调用远程服务,可以直接进行删除
    @Override
    public void delDataBySpuId(String spuId) {
        // 根据id查询数据
        Iterable<SkuInfo> iterable = esManagerMapper.search(QueryBuilders.termQuery("spuId", spuId));
        // 进行判断
        if(iterable == null){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        esManagerMapper.deleteAll(iterable);
    }
}
