package com.changgou.search.service;

public interface ESManagerService {
    // 创建索引库的结构,
    void createMappingAndIndex();

    // 导入所有的数据到索引库
    public void importAll();

    // 根据得到的spuId将数据导入到es索引库
    public void importDataToESBySpuId(String spuId);

    // 根据id删除索引库里面的内容
    public void delDataBySpuId(String spuId);
}
