package com.imooc.mumushengxian.Service;

import com.github.pagehelper.PageInfo;
import com.imooc.mumushengxian.exception.ImoocMallException;
import com.imooc.mumushengxian.model.request.CategoryRequest;
import com.imooc.mumushengxian.model.vo.CategoryVO;

import java.util.List;

public interface CategoryService {
    void addCategory(CategoryRequest categoryRequest) throws ImoocMallException;

    void updateCategory(CategoryRequest categoryRequest) throws ImoocMallException;

    void deleteCategory(Integer categoryId) throws ImoocMallException;

    PageInfo categoryListForAdmin(Integer pageNum, Integer pageSize);

    List<CategoryVO> categoryListForUser(Integer parentId);
}
