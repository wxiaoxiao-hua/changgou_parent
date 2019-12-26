package com.changgou.search.controller;

import com.changgou.common.entity.Page;
import com.changgou.search.pojo.SkuInfo;
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

        // 封装分页数据并返回: 总记录数,当前页,每页显示的条数
        Page<SkuInfo> page = new Page<>(
                Long.parseLong(String.valueOf(resultMap.get("total"))),
                Integer.parseInt(String.valueOf(resultMap.get("pageNum"))),
                Page.pageSize  // 这里使用的是默认值
        );
        model.addAttribute("page",page);

        //拼装url超链接的路径
        StringBuilder url = new StringBuilder("/search/list");
        // 判断查询的条件是否为空
        if(searchMap !=null && searchMap.size()>0){
            // 判断并拼接查询条件
            url.append("?");
            for (String paramKey : searchMap.keySet()){
                // 拼接key和value
                if(!"sortRule".equals(paramKey) && !"sortField".equals(paramKey) && !"pageNum".equals(paramKey)){
                    url.append(paramKey).append("=").append(searchMap.get(paramKey)).append("&");
                }
            }
            // 将拼接好的内容转换成字符串
            String urlString = url.toString();
            // 去除掉路径上面的最后一个&
            urlString = urlString.substring(0, urlString.length() - 1);
            model.addAttribute("url",urlString);
        }else{
            // 如果没有查询条件的话,就直接原来的路径
            model.addAttribute("url",url);
        }

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
