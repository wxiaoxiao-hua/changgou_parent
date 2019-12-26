package com.changgou.search.controller;

import com.changgou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/list")
    public String search(@RequestParam Map<String,String> searchMap,Model model){
        // 调用方法,对特殊字符进行处理
        handlerSearchMap(searchMap);
        Map resultMap = searchService.search(searchMap);
        model.addAttribute("result",resultMap);
        model.addAttribute("searchMap",searchMap);


        return "search"; // 跳转到的是这个search.html的页面
    }

    // 根据条件查询信息
    @GetMapping
    @ResponseBody  //添加这个注解,return的是具体的数据
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
