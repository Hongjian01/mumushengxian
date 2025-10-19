package com.imooc.mumushengxian.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mumushengxian.Service.ProductService;
import com.imooc.mumushengxian.Service.UserService;
import com.imooc.mumushengxian.common.ApiRestResponse;
import com.imooc.mumushengxian.model.pojo.Product;
import com.imooc.mumushengxian.model.query.ProductQuery;
import com.imooc.mumushengxian.model.request.ProductQueryRequest;
import com.imooc.mumushengxian.model.request.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 用户前端商品模块;任何人都可以查看，不管登没登陆
 */
@RestController
@RequestMapping("/user/product")
public class ProductUserController {

    @Autowired
    private ProductService productService;

    /**
     * 商品详情
     * @param id
     * @return
     */
    @GetMapping("/detail")
    public ApiRestResponse<Product> detail(@RequestParam Integer id) {
        Product product = productService.detail(id);
        return ApiRestResponse.success(product);
    }

    /**
     * 前端搜索商品列表
     * @return
     */
    @GetMapping("/list")
    public ApiRestResponse<PageInfo> list(@RequestBody ProductQueryRequest productQueryRequest) {
        PageInfo pageInfo = productService.listForUser(productQueryRequest);
        return ApiRestResponse.success(pageInfo);
    }
}
