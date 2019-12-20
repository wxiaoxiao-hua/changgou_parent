package com.changgou.goods.api;

import com.changgou.common.entity.Result;
import com.changgou.goods.pojo.Album;
import com.changgou.goods.pojo.Category;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


import java.util.Map;

@Api(value="分类管理接口",description = "分类管理接口，提供页面的增、删、改、查")
public interface CategoryApi {
    /**
     * 查询全部数据
     *
     * @return
     */
    @ApiOperation("查询所有分类列表")
    public Result findAll();

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @ApiOperation("根据ID查询分类数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "分类ID",required=true,paramType="path",dataTypeClass = Integer.class),
    })
    public Result findById(Integer id);


    /***
     * 新增数据
     * @param category
     * @return
     */
    @ApiOperation("新增分类")
    public Result add(Category category);


    /***
     * 修改数据
     * @param category
     * @param id
     * @return
     */
    @ApiOperation("修改分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "分类ID",required=true, paramType = "path", dataTypeClass = Integer.class),
    })
    public Result update(Category category, Integer id);


    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @ApiOperation("删除分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "分类ID",required=true,paramType="path",dataType="int"),
    })
    public Result delete(Integer id);

    /***
     * 多条件搜索分类数据
     * @param searchMap
     * @return
     */
    @ApiOperation("查询分类列表")
    public Result findList(Map searchMap);


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("条件分页查询分类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="searchMap",value = "条件查询 JSON数据， 分类名称或者是品牌首字母"),
            @ApiImplicitParam(name="page",value = "页码",required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value = "每页记录数",required=true,paramType="path",dataType="int")
    })
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message="Indicates ..."),
//            @ApiResponse(code = 404, message = "not found error")
//    })
    public Result findPage(Map searchMap, int page, int size);

    // 分类的一级
    @ApiOperation("一级分类的目录")
    public Result findByParentId();

    // 分类的二级
    @ApiOperation("非一级分类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId",value ="父ID",required = true,paramType ="path",dataType = "Integer")
    })
    public Result findByParentId(Integer parentId);

}
