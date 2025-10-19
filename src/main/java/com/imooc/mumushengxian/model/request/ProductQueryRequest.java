package com.imooc.mumushengxian.model.request;

public class ProductQueryRequest {
    private Integer pageNum = 1;

    private Integer pageSize = 10;

    private Integer categoryId;

    private String keyword;

    private String orderBy;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public String toString() {
        return "ProductQueryRequest{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", categoryId=" + categoryId +
                ", keyword='" + keyword + '\'' +
                ", orderBy='" + orderBy + '\'' +
                '}';
    }
}
