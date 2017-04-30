package com.railwayservice.application.vo;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * 通用的页面分页参数
 *
 * @author xuyu
 */
public class PageParam {

    private int offset = 0;
    private int limit = 100;
    private boolean count = true;

    public PageParam() {
    }

    public PageParam(int offset, int limit, boolean count) {
        this.offset = offset;
        this.limit = limit;
        this.count = count;
    }

    public Pageable newPageable() {
        int page = 0;
        if (offset == 0) {
            page = 0;
        } else {
            page = offset / limit;
        }
        return new PageRequest(page, limit);
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isCount() {
        return count;
    }

    public void setCount(boolean count) {
        this.count = count;
    }
}
