package com.imooc.mumushengxian.Service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mumushengxian.Service.CategoryService;
import com.imooc.mumushengxian.Service.ProductService;
import com.imooc.mumushengxian.exception.ImoocMallException;
import com.imooc.mumushengxian.exception.ImoocMallExceptionEnum;
import com.imooc.mumushengxian.model.dao.ProductMapper;
import com.imooc.mumushengxian.model.pojo.Product;
import com.imooc.mumushengxian.model.query.ProductQuery;
import com.imooc.mumushengxian.model.request.ProductQueryRequest;
import com.imooc.mumushengxian.model.request.ProductRequest;
import com.imooc.mumushengxian.model.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.metadata.IIOMetadataNode;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryService categoryService;

    @Override
    public void addProduct(ProductRequest productRequest) throws ImoocMallException {
        Product product = new Product();
        BeanUtils.copyProperties(productRequest, product);
        int count = productMapper.insertSelective(product);
        if (count != 1) {
            throw new ImoocMallException(ImoocMallExceptionEnum.PRODUCT_ADD_FAILED);
        }
    }

    @Override
    public void updateProduct(ProductRequest productRequest) throws ImoocMallException {
        Product product = new Product();
        BeanUtils.copyProperties(productRequest, product);
        int count = productMapper.updateByPrimaryKeySelective(product);
        if (count != 1) {
            throw new ImoocMallException(ImoocMallExceptionEnum.PRODUCT_UPDATE_FAILED);
        }
    }

    @Override
    public void deleteProduct(Integer productId) throws ImoocMallException {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.PRODUCT_NOT_EXISTED);
        }
        int count = productMapper.deleteByPrimaryKey(productId);
        if (count != 1) {
            throw new ImoocMallException(ImoocMallExceptionEnum.PRODUCT_DELETE_FAILED);
        }
    }

    @Override
    public void batchUpdateProductSellStatus(Integer[] ids, Integer sellStatus) throws ImoocMallException {
        if (ids == null || ids.length == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.PARAMETER_ERROR);
        }
        int count = productMapper.batchUpdateProductSellStatus(ids, sellStatus);
        // 可选：验证是否所有ID都成功更新
        if (count != ids.length) {
            throw new ImoocMallException(ImoocMallExceptionEnum.BATCH_UPDATE_FAILED);
        }
    }

    @Override
    public PageInfo list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectList();
        PageInfo pageInfo = new PageInfo(productList);
        return pageInfo;
    }

    @Override
    public Product detail(Integer id) {
        return productMapper.selectByPrimaryKey(id);
    }

    @Override
    public PageInfo listForUser(ProductQueryRequest productQueryRequest) {
        //构建Query对象
        ProductQuery productQuery = new ProductQuery();
        //1.搜索处理:模糊处理
        if (!StringUtils.isEmpty(productQueryRequest.getKeyword())) {
            String keyword = new StringBuilder().append("%").append(productQueryRequest.getKeyword())
                    .append("%").toString();
            productQuery.setKeyword(keyword);
        }
        //2.目录处理
        if (productQueryRequest.getCategoryId() != null) {
            //获取categoryId下面的所有目录列表:categoryVOList
            List<CategoryVO> categoryVOList =categoryService.categoryListForUser(productQueryRequest.getCategoryId());
            //从categoryVOList提取所有目录的id:包括本目录id
            ArrayList<Integer> categoryIds = new ArrayList<>();
            //先添加进本目录id
            categoryIds.add(productQueryRequest.getCategoryId());
            getAllCategroyIds(categoryVOList, categoryIds);
            productQuery.setCategoryIds(categoryIds);
        }

        //3.排序处理
        String orderBy = productQueryRequest.getOrderBy();
        if (orderBy == "price desc" || orderBy == "price asc") {
            PageHelper.startPage(productQueryRequest.getPageNum(), productQueryRequest.getPageSize(), orderBy);
        }else{
            PageHelper.startPage(productQueryRequest.getPageNum(), productQueryRequest.getPageSize());
        }

        List<Product> productList = productMapper.selectByKeyword(productQuery);
        PageInfo pageInfo = new PageInfo(productList);
        return pageInfo;
    }

    private void getAllCategroyIds(List<CategoryVO> categoryVOList, List<Integer> categoryIds) {
        for (CategoryVO categoryVO : categoryVOList) {
            if (categoryVO!=null) {
                categoryIds.add(categoryVO.getId());
                getAllCategroyIds(categoryVO.getChildCategory(), categoryIds);
            }
        }
    }
}
