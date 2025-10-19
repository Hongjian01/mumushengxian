package com.imooc.mumushengxian.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mumushengxian.Service.OrderService;
import com.imooc.mumushengxian.common.ApiRestResponse;
import com.imooc.mumushengxian.common.Constant;
import com.imooc.mumushengxian.exception.ImoocMallException;
import com.imooc.mumushengxian.model.pojo.User;
import com.imooc.mumushengxian.model.request.CreateOrderReq;
import com.imooc.mumushengxian.model.vo.OrderVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/order")
public class OrderUserController {
    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     * @param
     * @param request
     * @return
     * @throws ImoocMallException
     */
    @PostMapping("/createOrder")
    public ApiRestResponse createOrder(@Valid @RequestBody CreateOrderReq createOrderReq, HttpServletRequest request) throws ImoocMallException {
        //从session中获取用户对象,验证用户身份
        User user =(User) request.getAttribute(Constant.IMOOC_MALL_USER);
        String orderNO = orderService.createOrder(user.getId(), createOrderReq);
        //返回订单号给前端
        return ApiRestResponse.success(orderNO);
    }

    /**
     * 订单详情
     * @param orderNO
     * @param request
     * @return
     * @throws ImoocMallException
     */
    @GetMapping("/orderDetail")
    public ApiRestResponse orderDetail(@RequestParam("orderNO") String orderNO, HttpServletRequest request) throws ImoocMallException {
        //从session中获取用户对象,验证用户身份
        User user =(User) request.getAttribute(Constant.IMOOC_MALL_USER);
        OrderVO orderVO = orderService.orderDetail(user.getId(), orderNO);
        return ApiRestResponse.success(orderVO);
    }

    /**
     * 前台用户订单列表
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     * @throws ImoocMallException
     */
    @GetMapping("/orderList")
    public ApiRestResponse orderList(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, HttpServletRequest request) throws ImoocMallException {
        User user =(User) request.getAttribute(Constant.IMOOC_MALL_USER);
        PageInfo pageInfo = orderService.orderlistForUser(user.getId(), pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    /**
     * 用户取消订单
     * @param orderNO
     * @param request
     * @return
     */
    @PostMapping("/cancelOrder")
    public ApiRestResponse cancelOrder(@RequestParam("orderNO") String orderNO, HttpServletRequest request) throws ImoocMallException {
        User user =(User) request.getAttribute(Constant.IMOOC_MALL_USER);
        orderService.cancelOrder(user.getId(), orderNO);
        return ApiRestResponse.success();
    }
}
