package com.imooc.mumushengxian.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mumushengxian.Service.OrderService;
import com.imooc.mumushengxian.Service.UserService;
import com.imooc.mumushengxian.common.ApiRestResponse;
import com.imooc.mumushengxian.common.Constant;
import com.imooc.mumushengxian.exception.ImoocMallException;
import com.imooc.mumushengxian.exception.ImoocMallExceptionEnum;
import com.imooc.mumushengxian.model.pojo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
public class OrderAdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    /**
     * 管理员订单列表
     * @param pageNum
     * @param pageSize
     * @return
     * @throws ImoocMallException
     */
    @GetMapping("/listForAdmin")
    public ApiRestResponse listForAdmin(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) throws ImoocMallException {
        PageInfo pageInfo = orderService.orderListForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    /**
     * 管理员订单发货
     * @param orderNo
     * @return
     * @throws ImoocMallException
     */
    @PostMapping("/deliver")
    public ApiRestResponse deliver(@RequestParam("orderNO") String orderNo) throws ImoocMallException {
        orderService.deliver(orderNo);
        return ApiRestResponse.success();
    }

    /**
     * 完结订单。订单状态流程：0用户已取消，10未付款，20已付款，30已发货，40交易完成。管理员和用户都可以调用
     */
    @PostMapping("/finished")
    public ApiRestResponse finished(@RequestParam("orderNO") String orderNo) throws ImoocMallException {
        orderService.finished(orderNo);
        return ApiRestResponse.success();
    }
}
