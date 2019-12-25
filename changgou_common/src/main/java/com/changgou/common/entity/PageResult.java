package com.changgou.common.entity;
import java.util.List;

public class PageResult<T> {

    private Long total;//总记录数
    private List<T> rows;//记录
    // 添加一个字段,总共有多少页码
    private Integer page;

    public PageResult(Long total, List<T> rows,Integer page) {
        this.total = total;
        this.rows = rows;
        this.page = page;
    }

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public PageResult() {
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
