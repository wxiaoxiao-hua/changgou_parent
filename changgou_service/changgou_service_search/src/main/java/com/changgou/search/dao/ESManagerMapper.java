package com.changgou.search.dao;

import com.changgou.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

// 搜索微服务批量导入数据的逻辑
public interface ESManagerMapper extends ElasticsearchRepository<SkuInfo,Long>{
}
