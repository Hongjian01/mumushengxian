package com.imooc.mumushengxian.model.dao;

import com.imooc.mumushengxian.model.pojo.Product;
import com.imooc.mumushengxian.model.query.ProductQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product row);

    int insertSelective(Product row);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product row);

    int updateByPrimaryKey(Product row);

    int batchUpdateProductSellStatus(@Param("ids") Integer [] ids, @Param("sellStatus") Integer sellStatus);

    List<Product> selectList();

    List<Product> selectByKeyword(@Param("query") ProductQuery productQuery);
}