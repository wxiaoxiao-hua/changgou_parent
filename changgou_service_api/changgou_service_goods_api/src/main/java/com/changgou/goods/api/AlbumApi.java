package com.changgou.goods.api;

import com.changgou.common.entity.Result;
import com.changgou.goods.pojo.Album;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


import java.util.Map;

@Api(value="相册管理接口",description = "相册管理接口，提供页面的增、删、改、查")
public interface AlbumApi {
    /**
     * 查询全部数据
     *
     * @return
     */
    @ApiOperation("查询所有相册列表")
    public Result findAll();

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @ApiOperation("根据ID查询相册数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "相册ID",required=true,paramType="path",dataType="long"),
    })
    public Result findById(Long id);


    /***
     * 新增数据
     * @param album
     * @return
     */
    @ApiOperation("新增相册")
    public Result add(Album album);


    /***
     * 修改数据
     * @param
     * @param id
     * @return
     */
    @ApiOperation("修改相册")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "相册ID",required=true, paramType = "path", dataType="long"),
    })
    public Result update(Album album, Long id);


    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @ApiOperation("删除相册")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "相册ID",required=true,paramType="path",dataType="long"),
    })
    public Result delete(Long id);

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @ApiOperation("查询相册列表")
    public Result findList(Map searchMap);


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("条件分页查询相册列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="searchMap",value = "条件查询 JSON数据， 品牌名称或者是品牌首字母"),
            @ApiImplicitParam(name="page",value = "页码",required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value = "每页记录数",required=true,paramType="path",dataType="int")
    })
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message="Indicates ..."),
//            @ApiResponse(code = 404, message = "not found error")
//    })
    public Result findPage(Map searchMap, int page, int size);
}
