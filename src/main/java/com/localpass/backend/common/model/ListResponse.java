package com.localpass.backend.common.model;

import java.util.List;

public class ListResponse {

    private List data;
    private Integer totalCount;

    public ListResponse() {
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
