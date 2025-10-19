package com.imooc.mumushengxian.model.dao;

import com.imooc.mumushengxian.model.pojo.Order;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order row);

    int insertSelective(Order row);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order row);

    int updateByPrimaryKey(Order row);

    Order selectByOrderNO(String orderNO);

    List<Order> selectByUserId(Integer userId);

    List<Order> selectAllOrder();
}