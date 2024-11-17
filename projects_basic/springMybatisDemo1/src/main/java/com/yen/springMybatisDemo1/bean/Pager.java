package com.yen.springMybatisDemo1.bean;

// https://blog.csdn.net/feinifi/article/details/88769101

import java.util.List;

public class Pager<T> {

    // attr
    private int page;
    private int size;
    private List<T> rows;
    private long total;

    // getter, setter
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}
