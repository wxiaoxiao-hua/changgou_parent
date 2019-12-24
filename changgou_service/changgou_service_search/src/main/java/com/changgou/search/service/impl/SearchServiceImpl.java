package com.changgou.search.service.impl;


import com.changgou.search.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.Map;


@Service
@Transactional
public class SearchServiceImpl implements SearchService {
    // 注入es的模板
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    // 条件查询以及分页
    @Override
    public Map search(Map<String, String> searchMap) throws Exception {
        // 创建返回值的对象
        Map<String,Object> resultMap = new HashMap<>();
        /*
        *   首先是搜索框里面的关键字: keyword
            对应的过滤条件: 品牌名称, 以及规格名称对应的规格选项
            排序的条件: 价格,销量,评价等
        * */
        // 先判断这个方法的参数是否不为空
        if(searchMap!=null && searchMap.size()>0){
            // 参数不为空的话,就构建封装查询条件的对象

            // 创建的是条件查询生成器
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

            // 获取关键字,并且拼接到参数上去
            if(StringUtils.isNotEmpty(searchMap.get("keywords"))){
                // must是说分词
                // matchQuery: 会将搜索的词进行分词,再与目标查询字段进行匹配, 后面的and表示的是并且的意思
                boolQuery.must(QueryBuilders.matchQuery("name",searchMap.get("keywords")).operator(Operator.AND));
            }

            // 获取对应的品牌的名称信息
            if(StringUtils.isNotEmpty(searchMap.get("brand"))){
                // filter是说不分词
                // termQuery不会将 搜索的词进行分词,而是作为一个整体
                boolQuery.filter(QueryBuilders.termQuery("brandName",searchMap.get("brand")));
            }

            // 获取对应的规格的名称,以及规格的选项的信息,这里要用一个for循环,循环遍历出来,并且拼接条件
            // 查询所有的key值出来,判断spec_开头的key,就是要查找的规格的名称,对应的value值就是规格选项
            for (String key:searchMap.keySet()){
                if(key.startsWith("spec_")){
                    // 获取到这个键的值
                    String value = searchMap.get(key).replace("%2B", "+");
                    // 拼接条件
                    boolQuery.filter(QueryBuilders.termQuery(("specMap."+key.substring(5)+".keyword"),value));
                }
            }

            // 根据价格区间来进行查询
            if (StringUtils.isNotEmpty(searchMap.get("price"))){
                // 价格区间是用?-?分隔的,通过字符串的分隔的方法来获取一个字符串数组
                String[] prices = searchMap.get("price").split("-");
                // 第一个索引位置上的是低一些的价格,第二个索引位置上的是高一些的价格
                // 要判断一下,价格的长度是否是2个,是两个的话,就是个区间,有大小之分
                if (prices.length==2){
                    // 如果长度大于1的话,那就是值小于price[1]
                    boolQuery.filter(QueryBuilders.rangeQuery("price").lte(prices[1]));
                }
                //然后查找的条件是要大于price[0]上面的值
                boolQuery.filter(QueryBuilders.rangeQuery("price").gte(prices[0]));
            }
            // 再根据封装好的条件创建 条件查询生成器
            nativeSearchQueryBuilder.withQuery(boolQuery);


            // 按照品牌再进行分组查询
            String skuBrand = "skuBrand";
            // 这里的意思是添加一个聚合(归类),聚合的类型是terms,聚合的名称是skuBrand的值,聚合的字段是brandName
            // 按照brandName来进行归类分组
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(skuBrand).field("brandName"));

            // 按照规格进行分类
            String skuSpec = "skuSpec";
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms(skuSpec).field("spec.keyword"));

            // 进行分页,分页需要的三个变量,总记录数,当前页码,每页显示的条数
            String pageNum = searchMap.get("pageNum");
            String pageSize = searchMap.get("pageSize");
            if (StringUtils.isEmpty(pageNum)){
                // 如果前台没有传过来数据,就给一个默认的数据
                pageNum = "1";
            }
            if (StringUtils.isEmpty(pageSize)){
                pageSize = "30";
            }
            // 设置分页,第一个参数: 当前页,是从0开始的, 第二个参数: 每页显示多少条
            nativeSearchQueryBuilder.withPageable(PageRequest.of(Integer.parseInt(pageNum),Integer.parseInt(pageSize)));

            //



        }
        return resultMap;
    }
}
