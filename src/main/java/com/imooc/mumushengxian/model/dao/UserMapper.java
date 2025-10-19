package com.imooc.mumushengxian.model.dao;

import com.imooc.mumushengxian.model.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User row);

    int insertSelective(User row);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User row);

    int updateByPrimaryKey(User row);

    //用户登录
    User selectLogin(@Param("username") String username, @Param("password") String password);

    //根据用户名查询用户
    User selectByUsername(String username);
}