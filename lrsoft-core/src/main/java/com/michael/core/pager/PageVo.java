package com.michael.core.pager;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页对象
 * Created by miles on 13-11-14.
 */
public class PageVo {
    private Long total;
    private List data;
    private Integer pageNo;

    @SuppressWarnings("unchecked")
    public PageVo addDatum(Object datum) {
        if (data == null) {
            data = new ArrayList();
        }
        data.add(datum);
        return this;
    }

    public PageVo() {

    }

    public PageVo(Long total, List data) {
        this.total = total;
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    @Override
    public String toString() {
        return "PageVo{" +
                "total=" + total +
                ", data=" + data +
                ", pageNo=" + pageNo +
                '}';
    }
}
