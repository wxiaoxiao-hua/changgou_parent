package com.changgou.search.service;

import java.util.Map;

public interface SearchService {

    // 条件查询所有的信息,方法的参数是一个map集合,返回值也是一个map集合
    public Map search(Map<String,String> paramMap);
}
