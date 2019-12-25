package com.changgou.common.model.response.goods;


import com.changgou.common.model.response.ResultCode;
import lombok.ToString;

@ToString
public enum GoodsCode implements ResultCode {

    GOODS_BRAND_ADD_ERROR(false,22001,"商品添加失败"),
    GOODS_SPU_ADD_SPEC_ERROR(false,22002 ,"规格添加失败" ),
    GOODS_IS_DELETE(false,22004 ,"当前商品处于删除状态" ),
    GOODS_PUT_AUDIT_ERROR(false,22005 ,"未通过审核的商品不能上架" ),
    GOODS_DELETE_PULL_ERROR(false,22006 ,"当前商品必须处于下架状态才能删除" ),
    GOODS_DELETE_RESTORE_ERROR(false,22007,"当前商品必须处于删除状态才能恢复" ),
    GOODS_REALDELETE_ERROR(false,22008,"当前商品必须处于删除状态才能从回收站彻底删除" ),
    GOODS_IMPORT_ERROR(false,22009,"当前没有数据被查询到,无法导入索引库" ),
    GOODS_IMPORT_DELETE_ERROR(false,22010,"当前没有数据被查询到,无法删除索引库数据" ),
    GOODS_NOT_FOUND(false,22003,"当前商品不存在");

    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private GoodsCode(boolean success,int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }
    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }


}
