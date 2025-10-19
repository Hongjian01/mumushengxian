package com.imooc.mumushengxian.Service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mumushengxian.Service.CategoryService;
import com.imooc.mumushengxian.exception.ImoocMallException;
import com.imooc.mumushengxian.exception.ImoocMallExceptionEnum;
import com.imooc.mumushengxian.model.dao.CategoryMapper;
import com.imooc.mumushengxian.model.pojo.Category;
import com.imooc.mumushengxian.model.request.CategoryRequest;
import com.imooc.mumushengxian.model.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void addCategory(CategoryRequest categoryRequest) throws ImoocMallException {
        //1.判断要添加的目录是否重复
        Category currentCategory = categoryMapper.selectByName(categoryRequest.getName());
        if (currentCategory != null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CATEGORY_ALREADY_EXISTED);
        }
        //2.将addCategoryReq属性赋值给Category实体类
        Category category = new Category();
        BeanUtils.copyProperties(categoryRequest, category);
        //3.调用categoryMapper，执行数据库添加操作
        int count = categoryMapper.insertSelective(category);
        if (count != 1) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CATEGORY_ADD_FAILED);
        }
    }

    @Override
    public void updateCategory(CategoryRequest categoryRequest) throws ImoocMallException {
        //1.判断要修改的目录是否存在
        Category currentCategory = categoryMapper.selectByPrimaryKey(categoryRequest.getId());
        if (currentCategory == null || !currentCategory.getId().equals(categoryRequest.getId())) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CATEGORY_NOT_EXISTED);
        }
        //2.将updateCategoryReq属性赋值给Category实体类
        Category category = new Category();
        BeanUtils.copyProperties(categoryRequest, category);
        //3.调用categoryMapper，执行数据库添加操作
        int count = categoryMapper.updateByPrimaryKeySelective(category);
        if (count != 1) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CATEGORY_UPDATE_FAILED);
        }
    }

    @Override
    public void deleteCategory(Integer categoryId) throws ImoocMallException {
        //1.判断要删除的目录是否存在
        Category currentCategory = categoryMapper.selectByPrimaryKey(categoryId);
        if (currentCategory == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CATEGORY_NOT_EXISTED);
        }
        int count = categoryMapper.deleteByPrimaryKey(categoryId);
        if (count != 1) {
            throw new ImoocMallException(ImoocMallExceptionEnum.CATEGORY_DELETE_FAILED);
        }
    }

    @Override
    public PageInfo categoryListForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Category> categoryList = categoryMapper.selectList();
        PageInfo pageInfo = new PageInfo(categoryList);
        return pageInfo;
    }

    @Override
    @Cacheable(value = "categoryListForUser")
    public List<CategoryVO> categoryListForUser(Integer parentId) {
        //1.创建categoryVOList用来接收结构化后的目录列表
        List<CategoryVO> categoryVOList = new ArrayList<>();
        //2.将当前目录下的所有子目录放到childCategory里面
        getAllChilds(categoryVOList, parentId);
        //3.返回结构化后的categoryVOList
        return categoryVOList;
    }

    //按照目录结构获取所有子目录：递归获取所有子分类（完整树形结构）
    private void getAllChilds(List<CategoryVO> categoryVOList, Integer parentId) {
        List<Category> categoryList = categoryMapper.selectByParentId(parentId);
        for (Category category : categoryList) {
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category, categoryVO);
            categoryVOList.add(categoryVO);
            getAllChilds(categoryVO.getChildCategory(), category.getId());
        }
    }
}
