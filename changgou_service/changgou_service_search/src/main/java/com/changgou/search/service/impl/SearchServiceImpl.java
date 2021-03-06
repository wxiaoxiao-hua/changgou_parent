package com.changgou.search.service.impl;


import com.alibaba.fastjson.JSON;
import com.changgou.goods.pojo.Spec;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class SearchServiceImpl implements SearchService {
    // 注入es的模板
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    // 条件查询以及分页
    @Override
    public Map search(Map<String, String> searchMap) {
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

            // 创建的是条件查询生成器,会将所有的查询条件封装到一起,成为一个json格式的字符串
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            // 创建的是布尔查询的对象,要用布尔查询的对象将条件串联起来
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

            // 获取关键字,并且拼接到参数上去
            if(StringUtils.isNotEmpty(searchMap.get("keywords"))){
                // must是说分词
                // matchQuery: 会将搜索的词进行分词,再与目标查询字段进行匹配, 后面的and表示的是并且的意思
                // operator : 是指切分的意思
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


            // 按照品牌再进行分组查询,  对前面的结果进行聚合,按照品牌进行分组
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

            // 按照相关的字段,进行排序,两种情况,可能升序,也可能降序
            if(StringUtils.isNotEmpty(searchMap.get("sortField"))&& StringUtils.isNotEmpty(searchMap.get("sortRule"))){
                // 如果不为空的话, 再进行升降序的排列
                if("ASC".equals(searchMap.get("sortRule"))){
                    // 这种是升序排列
                    nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(searchMap.get("sortField")).order(SortOrder.ASC));
                }else{
                    // 这种是降序排列
                    nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(searchMap.get("sortField")).order(SortOrder.DESC));
                }
            }

            // 拼接完了排序之后,设置高亮域以及高亮的样式
            HighlightBuilder.Field field = new HighlightBuilder.Field("name") //高亮的区域
                    // 设置高亮样式的前缀
                    .preTags("<span style='color:red'>")
                    // 设置高亮样式的后缀
                    .postTags("</span>");
            // 再拼接到条件查询器
            nativeSearchQueryBuilder.withHighlightFields(field);

            /* 开始使用模板进行查询
            *   1.第一个参数是: 条件查询器对象
            *   2.查询操作的实体类对象
            *   3. 查询结果的操作对象
            * */
            AggregatedPage<SkuInfo> resultInfo = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class, new SearchResultMapper() {
                @Override
                public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                    // 查询结果的操作
                    List<T> list = new ArrayList<>();
                    // 获取查询命中的结果数据
                    SearchHits hits = searchResponse.getHits();
                    if (hits != null) {
                        // 有查询结果的时候
                        for (SearchHit hit : hits) {
                            SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(), SkuInfo.class);

                            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                            if (highlightFields != null && highlightFields.size() > 0) {
                                // 替换掉数据,拼接的时候,建议使用循环,这里直接选择0索引上的,是因为只有一个.
                                // 循环遍历之前,先进行一次长度的判断
                                skuInfo.setName(highlightFields.get("name").getFragments()[0].toString());
                            }
                            list.add((T) skuInfo);
                        }
                    }
                    return new AggregatedPageImpl<T>(list, pageable, hits.getTotalHits(), searchResponse.getAggregations());
                }
            });

            /**
             *  封装返回的对象
             *  1, 总记录数,总页数,查询到的数据的集合
             *  2, 对应的品牌的分组结果
             *  3, 对应的规格的分组结果
             *  4, 当前页码
             */
            // 总记录数
            resultMap.put("total",resultInfo.getTotalElements());
            // 总页数
            resultMap.put("totalPages",resultInfo.getTotalPages());
            // 查询到的数据集合
            resultMap.put("rows",resultInfo.getContent());

            // 封装品牌的分组结果
            StringTerms brandTerms = (StringTerms) resultInfo.getAggregation(skuBrand);
            List<String> brandList = brandTerms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
            resultMap.put("brandList",brandList);

            // 封装规格的分组结果
            StringTerms specTerms = (StringTerms) resultInfo.getAggregation(skuSpec);
            List<String> specList = specTerms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
            resultMap.put("specList",this.formartSpec(specList));

            // 封装当前页
            resultMap.put("pageNum",pageNum);

            return resultMap;

        }
        return null;
    }

    /**
     * 原有数据
     *  [
     *         "{'颜色': '黑色', '尺码': '平光防蓝光-无度数电脑手机护目镜'}",
     *         "{'颜色': '红色', '尺码': '150度'}",
     *         "{'颜色': '黑色', '尺码': '150度'}",
     *         "{'颜色': '黑色'}",
     *         "{'颜色': '红色', '尺码': '100度'}",
     *         "{'颜色': '红色', '尺码': '250度'}",
     *         "{'颜色': '红色', '尺码': '350度'}",
     *         "{'颜色': '黑色', '尺码': '200度'}",
     *         "{'颜色': '黑色', '尺码': '250度'}"
     *     ]
     *
     *    需要的数据格式
     *    {
     *        颜色:[黑色,红色],
     *        尺码:[100度,150度]
     *    }
     */
    // 将这些数据改成页面显示需要的格式:
    // 颜色: 黑色,红色
    // 尺码: 100度,200度

    public Map<String,Set<String>> formartSpec(List<String> specList){
        // 创建封装结果的集合
        Map<String,Set<String>> resultMap = new HashMap<>();
        // 判断参数是否存在,不为0
        if(specList!=null && specList.size()>0){
            // 遍历这个结合,获取到集合里面的每一个元素,是一个长的字符串
            for (String specJsonString:specList){
                // 拿到集合里面的每一个长的字符串元素,将其转换成map集合的格式
                Map<String,String> specMap = JSON.parseObject(specJsonString, Map.class);
                // 遍历集合,拿到里面的每一个键的信息
                for (String specKey:specMap.keySet()){
                    // 根据键的信息,获取resultMap集合里面相同键对应的值的信息(这里面肯定是没有信息的)
                    Set<String> specSet = resultMap.get(specKey);
                    // 再进行判断
                    if(specSet == null){
                        // 创建新的集合
                        specSet = new HashSet<>();
                    }
                    // 将对应的规格的信息封装到集合里
                    specSet.add(specMap.get(specKey));
                    // 再将set放入到resultMap集合里
                    resultMap.put(specKey,specSet);
                }
            }
        }
        return resultMap;
    }
}
