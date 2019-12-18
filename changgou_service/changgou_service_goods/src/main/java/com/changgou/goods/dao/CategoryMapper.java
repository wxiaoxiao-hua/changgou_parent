package com.changgou.goods.dao;

import com.changgou.goods.pojo.Category;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;


public interface CategoryMapper extends Mapper<Category> {

    // 分类的一级,二级,三级
    @Select("select id,name from tb_category where parent_id = #{parentId}")
    public List<Map> findByParentId(@Param("parentId") Integer parentId);

}
