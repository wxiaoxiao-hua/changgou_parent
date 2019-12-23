package com.changgou.goods.pojo;

import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;

@Data
@Table(name = "tb_category_brand")
public class CategoryBrand implements Serializable{
    private Integer categoryId;
    private Integer brandId;
}
