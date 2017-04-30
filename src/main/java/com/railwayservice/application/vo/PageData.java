package com.railwayservice.application.vo;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页数据模型。
 *
 * @author Ewing
 */
public class PageData {
    private long total = 0;
    private List rows;

    public PageData() {
    }

    public PageData(List rows) {
        this.total = rows.size();
        this.rows = rows;
    }

    public PageData(long total, List rows) {
        this.total = total;
        this.rows = rows;
    }

    public PageData(Page page) {
        this.setTotal(page.getTotalElements());
        this.setRows(page.getContent());
    }

    public long getTotal() {
        return total;
    }

    public PageData setTotal(long total) {
        this.total = total;
        return this;
    }

    public List getRows() {
        return rows;
    }

    public PageData setRows(List rows) {
        this.rows = rows;
        return this;
    }

}
