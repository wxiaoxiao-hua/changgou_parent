package com.changgou.search.controller;

import com.changgou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;
    
    // 根据条件查询信息
    @GetMapping
    public Map search(@RequestParam Map<String,String> searchMap){
        // 对特殊字符进行一次处理
        handlerSearchMap(searchMap);
        Map searchResult = searchService.search(searchMap);
        return searchResult;
    }

    // 处理特殊的字符
    private void handlerSearchMap(Map<String, String> searchMap) {
        // 判断这个集合存在
        if (searchMap!=null){
            for (Map.Entry<String,String> entry: searchMap.entrySet()){
                if (entry.getKey().startsWith("spec_")){
                    searchMap.put(entry.getKey(),entry.getValue().replace("+","%2B"));
                }
            }
        }
    }
}
