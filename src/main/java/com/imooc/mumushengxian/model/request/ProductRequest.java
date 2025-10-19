package com.imooc.mumushengxian.model.request;


import com.imooc.mumushengxian.model.request.group.AddProductGroup;
import com.imooc.mumushengxian.model.request.group.UpdateProductGroup;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductRequest {

    @NotNull(groups = UpdateProductGroup.class, message = "商品ID不能为空")
    private Integer id;

    @NotBlank(groups ={AddProductGroup.class, UpdateProductGroup.class}, message = "商品名称不能为空")
    private String name;

    @NotBlank(groups ={AddProductGroup.class, UpdateProductGroup.class}, message = "商品图片不能为空")
    private String image;

    @NotBlank(groups ={AddProductGroup.class, UpdateProductGroup.class}, message = "商品详情不能为空")
    private String detail;

    @NotNull(groups ={AddProductGroup.class, UpdateProductGroup.class}, message = "商品分类不能为空")
    private Integer categoryId;

    @NotNull(groups ={AddProductGroup.class, UpdateProductGroup.class}, message = "商品价格不能为空")
    @Min(value = 1, message = "价格不能小于1分")
    private Integer price;

    @NotNull(groups ={AddProductGroup.class, UpdateProductGroup.class})
    private Integer status = 1;

    @NotNull(groups ={AddProductGroup.class, UpdateProductGroup.class}, message = "商品库存不能为空")
    @Max(value = 10000, message = "库存不能大于10000")
    private Integer stock;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UpdateProductReq{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", detail='" + detail + '\'' +
                ", categoryId=" + categoryId +
                ", price=" + price +
                ", stock=" + stock +
                ", status=" + status +
                '}';
    }
}