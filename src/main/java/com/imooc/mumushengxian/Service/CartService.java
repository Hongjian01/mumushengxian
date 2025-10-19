package com.imooc.mumushengxian.Service;

import com.imooc.mumushengxian.exception.ImoocMallException;
import com.imooc.mumushengxian.model.vo.CartVO;

import java.util.List;

public interface CartService {
    List<CartVO> cartList(Integer userId) throws ImoocMallException;

    List<CartVO> addCart(Integer productId, Integer count,Integer userId) throws ImoocMallException;

    List<CartVO> updateCart(Integer productId, Integer count, Integer userId) throws ImoocMallException;

    List<CartVO> deleteCart(Integer productId, Integer id) throws ImoocMallException;

    List<CartVO> selectCart(Integer productId, Integer selected, Integer id) throws ImoocMallException;

    List<CartVO> selectAllCart(Integer selected, Integer id) throws ImoocMallException;
}
