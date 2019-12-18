package com.changgou.common.model.response.goods;


import com.changgou.common.model.response.ResultCode;
import lombok.ToString;

@ToString
public enum GoodsCode implements ResultCode {

    GOODS_BRAND_ADD_ERROR(false,22001,"商品添加失败");

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
