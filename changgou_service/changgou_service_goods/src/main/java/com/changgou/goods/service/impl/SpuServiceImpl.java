package com.changgou.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.common.exception.ExceptionCast;
import com.changgou.common.model.response.goods.GoodsCode;
import com.changgou.common.util.IdWorker;
import com.changgou.goods.dao.*;
import com.changgou.goods.pojo.*;
import com.changgou.goods.service.SpuService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;

    /**
     * 查询全部列表
     * @return
     */
    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Spu findById(String id){
        return  spuMapper.selectByPrimaryKey(id);
    }


    /**
     * 增加
     * @param spu
     */
    @Override
    public void add(Spu spu){
        spuMapper.insert(spu);
    }


    /**
     * 修改
     * @param spu
     */
    @Override
    public void update(Spu spu){
        spuMapper.updateByPrimaryKey(spu);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id){
        spuMapper.deleteByPrimaryKey(id);
    }


    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    @Override
    public List<Spu> findList(Map<String, Object> searchMap){
        Example example = createExample(searchMap);
        return spuMapper.selectByExample(example);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Spu> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return (Page<Spu>)spuMapper.selectAll();
    }

    /**
     * 条件+分页查询
     * @param searchMap 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public Page<Spu> findPage(Map<String,Object> searchMap, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        return (Page<Spu>)spuMapper.selectByExample(example);
    }


    /**
     * 构建查询对象
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 主键
            if(searchMap.get("id")!=null && !"".equals(searchMap.get("id"))){
                criteria.andEqualTo("id",searchMap.get("id"));
           	}
            // 货号
            if(searchMap.get("sn")!=null && !"".equals(searchMap.get("sn"))){
                criteria.andEqualTo("sn",searchMap.get("sn"));
           	}
            // SPU名
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
           	}
            // 副标题
            if(searchMap.get("caption")!=null && !"".equals(searchMap.get("caption"))){
                criteria.andLike("caption","%"+searchMap.get("caption")+"%");
           	}
            // 图片
            if(searchMap.get("image")!=null && !"".equals(searchMap.get("image"))){
                criteria.andLike("image","%"+searchMap.get("image")+"%");
           	}
            // 图片列表
            if(searchMap.get("images")!=null && !"".equals(searchMap.get("images"))){
                criteria.andLike("images","%"+searchMap.get("images")+"%");
           	}
            // 售后服务
            if(searchMap.get("saleService")!=null && !"".equals(searchMap.get("saleService"))){
                criteria.andLike("saleService","%"+searchMap.get("saleService")+"%");
           	}
            // 介绍
            if(searchMap.get("introduction")!=null && !"".equals(searchMap.get("introduction"))){
                criteria.andLike("introduction","%"+searchMap.get("introduction")+"%");
           	}
            // 规格列表
            if(searchMap.get("specItems")!=null && !"".equals(searchMap.get("specItems"))){
                criteria.andLike("specItems","%"+searchMap.get("specItems")+"%");
           	}
            // 参数列表
            if(searchMap.get("paraItems")!=null && !"".equals(searchMap.get("paraItems"))){
                criteria.andLike("paraItems","%"+searchMap.get("paraItems")+"%");
           	}
            // 是否上架
            if(searchMap.get("isMarketable")!=null && !"".equals(searchMap.get("isMarketable"))){
                criteria.andEqualTo("isMarketable",searchMap.get("isMarketable"));
           	}
            // 是否启用规格
            if(searchMap.get("isEnableSpec")!=null && !"".equals(searchMap.get("isEnableSpec"))){
                criteria.andEqualTo("isEnableSpec", searchMap.get("isEnableSpec"));
           	}
            // 是否删除
            if(searchMap.get("isDelete")!=null && !"".equals(searchMap.get("isDelete"))){
                criteria.andEqualTo("isDelete",searchMap.get("isDelete"));
           	}
            // 审核状态
            if(searchMap.get("status")!=null && !"".equals(searchMap.get("status"))){
                criteria.andEqualTo("status",searchMap.get("status"));
           	}

            // 品牌ID
            if(searchMap.get("brandId")!=null ){
                criteria.andEqualTo("brandId",searchMap.get("brandId"));
            }
            // 一级分类
            if(searchMap.get("category1Id")!=null ){
                criteria.andEqualTo("category1Id",searchMap.get("category1Id"));
            }
            // 二级分类
            if(searchMap.get("category2Id")!=null ){
                criteria.andEqualTo("category2Id",searchMap.get("category2Id"));
            }
            // 三级分类
            if(searchMap.get("category3Id")!=null ){
                criteria.andEqualTo("category3Id",searchMap.get("category3Id"));
            }
            // 模板ID
            if(searchMap.get("templateId")!=null ){
                criteria.andEqualTo("templateId",searchMap.get("templateId"));
            }
            // 运费模板id
            if(searchMap.get("freightId")!=null ){
                criteria.andEqualTo("freightId",searchMap.get("freightId"));
            }
            // 销量
            if(searchMap.get("saleNum")!=null ){
                criteria.andEqualTo("saleNum",searchMap.get("saleNum"));
            }
            // 评论数
            if(searchMap.get("commentNum")!=null ){
                criteria.andEqualTo("commentNum",searchMap.get("commentNum"));
            }

        }
        return example;
    }

    // 添加新的商品
    @Override
    public void add(Goods goods) {
        // 先获取到spu信息
        Spu spu = goods.getSpu();
        // 根据分布式id给这个商品设置一个id值,得到的是long类型的id
        long spuId = idWorker.nextId();
        // 设置的id是字符串格式的
        spu.setId(String.valueOf(spuId));
        // 设置spu的删除状态为0,上架状态为0,审核状态是0, 启用规格默认的是启用 1
        spu.setIsDelete("0");
        spu.setIsMarketable("0");
        spu.setStatus("0");
        spu.setIsEnableSpec("1");
        // 调用spuMapper里面的新增spu的方法
        spuMapper.insertSelective(spu);

        // 调用这个类里面的新增sku的方法
        this.saveSkuList(goods);
    }

    //添加sku数据
    private void saveSkuList(Goods goods) {
        // 添加sku信息的时候,也直接建立分类和品牌之间的关系
        // 获取spu对象
        Spu spu = goods.getSpu();
        // 通过spu对象里面的分类id获取到对应的分类对象
        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());
        // 通过spu对象里面的品牌id,获取到对应的品牌的信息
        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());
        // 设置品牌与分类之间的关系,先要判断两者是否有关系,有关系,就不用再建立了,没有关系的话,就要重新建立关系
        CategoryBrand categoryBrand = new CategoryBrand();
        categoryBrand.setBrandId(spu.getBrandId());
        categoryBrand.setCategoryId(spu.getCategory3Id());
        // 调用mapper里面的方法,查看两者是否存在关系,
        // 这个方法是说根据实体中的实例作为参数,来查询总数,
        int count = categoryBrandMapper.selectCount(categoryBrand);
        // 判断是否存在关系
        if(count==0){
            // 就是说分类和品牌之间还没有关系,那就建立关系,insert()是说保存一个实体,包括Null,不使用数据库默认值
            categoryBrandMapper.insert(categoryBrand);
        }
        // 获取到sku的集合
        List<Sku> skuList = goods.getSkuList();
        // 判断是否为空,如果为空的话,就抛出异常
        if(skuList==null && skuList.size()<=0){
            ExceptionCast.cast(GoodsCode.GOODS_SPU_ADD_SPEC_ERROR);
        }
        // 如果不为0的话,只有一个spu的话,sku也是spu
        for (Sku sku:skuList){
            // 设置sku的id
            long skuId = idWorker.nextId();
            sku.setId(String.valueOf(skuId));
            // 设置sku的规格数据,如果为空的话,就设置成空的字符串
            if(StringUtils.isEmpty(sku.getSpec())){
                sku.setSpec("{}");
            }
            // 设置sku的名称,要分别遍历之后再进行名称的拼接, 是spu的名称加上每一个sku的名称,再写入数据库
            String name = spu.getName();

            // 将规格json转换为map,把map中的value进行名称的拼接
            Map<String,String> specMap = JSON.parseObject(sku.getSpec(), Map.class);
            if(specMap != null && specMap.size() >0){
                // 如果集合不为空的话,获取到所有的value值,再进行名称的拼接
                for (String value: specMap.values()){
                    name += ""+value;
                }
            }
            // 设置sku的名称
            sku.setName(name);
            // 设置spu的id值
            sku.setSpuId(spu.getId());
            // 设置创建与修改的时间
            sku.setCreateTime(new Date());
            sku.setUpdateTime(new Date());
            // 设置商品的分类id
            sku.setCategoryId(category.getId());
            // 设置商品的分类名称
            sku.setCategoryName(category.getName());
            // 设置商品的品牌名称
            sku.setBrandName(brand.getName());

            // 将数据全部添加到sku表里去
            skuMapper.insertSelective(sku);
        }
    }

    // 根据id查询对应的商品信息
    @Override
    public Goods findGoodsById(String id) {
        // 根据商品的id查询到spu的信息
        Spu spu = spuMapper.selectByPrimaryKey(id);
        // 封装查询的条件信息
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId",id);
        List<Sku> skuList = skuMapper.selectByExample(example);

        Goods goods = new Goods();
        goods.setSpu(spu);
        goods.setSkuList(skuList);
        return goods;
    }

    // 修改商品的信息
    @Override
    public void update(Goods goods) {
        // 先取出spu的部分
        Spu spu = goods.getSpu();
        spuMapper.updateByPrimaryKey(spu);

        // 再删除原本已经有的sku的数据,
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId",spu.getId());
        skuMapper.deleteByExample(example);

        // 再将新的数据写回数据库
        this.saveSkuList(goods);
    }



}
