package com.imooc.mumushengxian.Service;

import com.github.pagehelper.PageInfo;
import com.imooc.mumushengxian.exception.ImoocMallException;
import com.imooc.mumushengxian.model.request.CreateOrderReq;
import com.imooc.mumushengxian.model.vo.OrderVO;
import jakarta.validation.Valid;

public interface OrderService {
    String createOrder(Integer userId, @Valid CreateOrderReq createOrderReq) throws ImoocMallException;

    OrderVO orderDetail(Integer userId, String orderNO) throws ImoocMallException;

    PageInfo orderlistForUser(Integer id, Integer pageNum, Integer pageSize) throws ImoocMallException;

    void cancelOrder(Integer userId, String orderNO) throws ImoocMallException;

    PageInfo orderListForAdmin(Integer pageNum, Integer pageSize) throws ImoocMallException;

    void deliver(String orderNo) throws ImoocMallException;

    void finished(String orderNo) throws ImoocMallException;
}
