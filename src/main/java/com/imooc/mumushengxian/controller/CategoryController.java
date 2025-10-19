package com.imooc.mumushengxian.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mumushengxian.Service.CategoryService;
import com.imooc.mumushengxian.Service.UserService;
import com.imooc.mumushengxian.common.ApiRestResponse;
import com.imooc.mumushengxian.common.Constant;
import com.imooc.mumushengxian.exception.ImoocMallException;
import com.imooc.mumushengxian.exception.ImoocMallExceptionEnum;
import com.imooc.mumushengxian.model.pojo.User;
import com.imooc.mumushengxian.model.request.group.AddCategoryGroup;
import com.imooc.mumushengxian.model.request.CategoryRequest;
import com.imooc.mumushengxian.model.request.group.UpdateCategoryGroup;
import com.imooc.mumushengxian.model.vo.CategoryVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品分类接口
 */
@RestController
public class CategoryController {

    @Autowired
    private  UserService userService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/admin/addCategory")
    public ApiRestResponse addCategory(@Validated(AddCategoryGroup.class) @RequestBody CategoryRequest categoryRequest) throws ImoocMallException {
        //不需要进行用户验证，AdminInterceptor会进行拦截验证
        //Step1.执行添加目录操作;
        categoryService.addCategory(categoryRequest);
        //Step2.返回结果
        return ApiRestResponse.success();
    }

    @PostMapping("/admin/updateCategory")
    public ApiRestResponse updateCategory(@Validated(UpdateCategoryGroup.class) @RequestBody CategoryRequest categoryRequest) throws ImoocMallException {
            //Step1.执行添加目录操作
            categoryService.updateCategory(categoryRequest);
            //Step2.返回结果
            return ApiRestResponse.success();
    }

    @PostMapping("/admin/deleteCategory")
    public ApiRestResponse deleteCategory(@RequestParam Integer categoryId) throws ImoocMallException {
            //Step1.执行添加目录操作
            categoryService.deleteCategory(categoryId);
            //Step2.返回结果
            return ApiRestResponse.success();
    }

    /**
     * 后台商品目录列表展示只需要把所有的商品目录依次列出即可，不需要有目录层级结构
     */
    @GetMapping("/admin/categoryList")
    public ApiRestResponse categoryListForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize) throws ImoocMallException {
        //不需要进行用户验证，AdminInterceptor会进行拦截验证
        //Step1.目录列表操作
        PageInfo pageInfo = categoryService.categoryListForAdmin(pageNum, pageSize);
        //Step2.返回结果
        return ApiRestResponse.success(pageInfo);
    }

    /**
     * 前端商品目录列表展示 不仅需要把所有的商品目录列出，还需要有目录层级结构;
     * 前端商品目录不需要进行用户验证，任何人都可以浏览;不需要通过Interceptor进行拦截
     * CategoryVO是专门用来封装前端展示信息的
     */
    @GetMapping("/categoryList")
    public ApiRestResponse categoryListForUser(@RequestParam(defaultValue = "0") Integer parentId) throws ImoocMallException {
            List<CategoryVO> categoryVOList = categoryService.categoryListForUser(parentId);
            //返回结果
            return ApiRestResponse.success(categoryVOList);
    }
}
