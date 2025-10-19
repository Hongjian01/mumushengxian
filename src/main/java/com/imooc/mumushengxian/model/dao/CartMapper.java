package com.imooc.mumushengxian.model.dao;

import com.imooc.mumushengxian.model.pojo.Cart;
import com.imooc.mumushengxian.model.vo.CartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart row);

    int insertSelective(Cart row);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart row);

    int updateByPrimaryKey(Cart row);

    List<CartVO> selectList(Integer userId);

    Cart selectByPidAndUid(@Param("productId") Integer productId, @Param("userId") Integer userId);

    void selectOrNot(@Param("userId") Integer userId, @Param("productId") Integer productId, @Param("selected") Integer selected);
}