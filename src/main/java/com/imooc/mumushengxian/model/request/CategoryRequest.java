package com.imooc.mumushengxian.model.request;

import com.imooc.mumushengxian.model.request.group.AddCategoryGroup;
import com.imooc.mumushengxian.model.request.group.UpdateCategoryGroup;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoryRequest {
    @NotNull(groups = UpdateCategoryGroup.class, message = "目录的ID不能为空")
    private Integer id;

    @NotBlank(groups ={AddCategoryGroup.class, UpdateCategoryGroup.class}, message = "目录名不能为空")
    private String name;

    @NotNull(message = "目录类型不能为空")
    @Min(value = 1, message = "类型最小为1")
    @Max(value = 3, message = "类型最大为3")
    private Integer type;

    @NotNull(groups = {AddCategoryGroup.class, UpdateCategoryGroup.class},
            message = "父目录ID不能为空")
    @Min(value = 0, message = "父目录ID最小为0")
    private Integer parentId;

    @NotNull(groups = {AddCategoryGroup.class, UpdateCategoryGroup.class},
            message = "排序号不能为空")
    @Min(value = 0, message = "排序号最小为0")
    private Integer orderNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
