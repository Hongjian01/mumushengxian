package com.imooc.mumushengxian.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.imooc.mumushengxian.Service.ProductService;
import com.imooc.mumushengxian.common.ApiRestResponse;
import com.imooc.mumushengxian.exception.ImoocMallException;
import com.imooc.mumushengxian.model.pojo.Product;
import com.imooc.mumushengxian.model.request.BatchUpdateProductRequest;
import com.imooc.mumushengxian.model.request.ProductRequest;
import com.imooc.mumushengxian.model.request.group.AddProductGroup;
import com.imooc.mumushengxian.model.request.group.UpdateProductGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员后台商品管理模块
 */
@RestController
@RequestMapping("/admin/product")
public class ProductAdminController {
    @Autowired
    private ProductService productService;

    @PostMapping("/addProduct")
    public ApiRestResponse<Product> addProduct(@Validated(AddProductGroup.class) @RequestBody ProductRequest productRequest) throws ImoocMallException {
        productService.addProduct(productRequest);
        return ApiRestResponse.success();
    }

    @PostMapping("/updateProduct")
    public ApiRestResponse<Product> updateProduct(@Validated(UpdateProductGroup.class) @RequestBody ProductRequest productRequest) throws ImoocMallException {
        productService.updateProduct(productRequest);
        return ApiRestResponse.success();
    }

    @PostMapping("/deleteProduct")
    public ApiRestResponse<Product> deleteProduct(@RequestParam Integer productId) throws ImoocMallException {
        productService.deleteProduct(productId);
        return ApiRestResponse.success();
    }

    /**
     *
     * @param request
     * @return
     * @throws ImoocMallException
     */
    @PostMapping("/batchUpdateProductSellStatus")
    public ApiRestResponse<Product> batchUpdateProductSellStatus(@RequestBody BatchUpdateProductRequest request) throws ImoocMallException {
        productService.batchUpdateProductSellStatus(request.getIds(), request.getSellStatus());
        return ApiRestResponse.success();
    }

    @GetMapping("/list")
    public ApiRestResponse list(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) throws ImoocMallException {
        //商品列表操作
        PageInfo pageInfo = productService.list(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }
}
