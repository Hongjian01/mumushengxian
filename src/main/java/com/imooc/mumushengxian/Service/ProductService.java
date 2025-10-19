package com.imooc.mumushengxian.Service;

import com.github.pagehelper.PageInfo;
import com.imooc.mumushengxian.exception.ImoocMallException;
import com.imooc.mumushengxian.model.pojo.Product;
import com.imooc.mumushengxian.model.request.ProductQueryRequest;
import com.imooc.mumushengxian.model.request.ProductRequest;

public interface ProductService {
    void addProduct(ProductRequest productRequest) throws ImoocMallException;

    void updateProduct(ProductRequest productRequest) throws ImoocMallException;

    void deleteProduct(Integer productId) throws ImoocMallException;

    void batchUpdateProductSellStatus(Integer[] ids, Integer sellStatus) throws ImoocMallException;

    PageInfo list(Integer pageNum, Integer pageSize);

    Product detail(Integer id);

    PageInfo listForUser(ProductQueryRequest productQueryRequest);
}
