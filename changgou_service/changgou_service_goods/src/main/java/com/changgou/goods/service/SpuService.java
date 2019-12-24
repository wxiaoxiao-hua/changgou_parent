package com.changgou.goods.service;

import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Spu;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SpuService {

    /***
     * 查询所有
     * @return
     */
    List<Spu> findAll();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Spu findById(String id);

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    List<Spu> findList(Map<String, Object> searchMap);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<Spu> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<Spu> findPage(Map<String, Object> searchMap, int page, int size);


    // 添加新的商品
    void add(Goods goods);

    // 根据id查询对应的商品
    public Goods findGoodsById(String id);

    // 修改商品的信息
    void update(Goods goods);

    // 商品的审核
    void audit(String id);

    // 商品的下架
    void pull(String id);

    // 商品的上架
    void put(String id);

    // 逻辑删除商品的信息
    void delete(String id);

    // 商品从回收站恢复
    void restore(String id);

    // 商品的物理删除,直接从数据库里面进行删除
    void realDelete(String id);
}
