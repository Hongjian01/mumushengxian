package com.imooc.mumushengxian.model.request;

public class BatchUpdateProductRequest {
    private Integer[] ids;
    private Integer sellStatus;

    public Integer[] getIds() {
        return ids;
    }

    public void setIds(Integer[] ids) {
        this.ids = ids;
    }

    public Integer getSellStatus() {
        return sellStatus;
    }

    public void setSellStatus(Integer sellStatus) {
        this.sellStatus = sellStatus;
    }
}
