package com.imooc.mumushengxian.controller;

import com.imooc.mumushengxian.Service.CartService;
import com.imooc.mumushengxian.common.ApiRestResponse;
import com.imooc.mumushengxian.common.Constant;
import com.imooc.mumushengxian.exception.ImoocMallException;
import com.imooc.mumushengxian.exception.ImoocMallExceptionEnum;
import com.imooc.mumushengxian.model.pojo.User;
import com.imooc.mumushengxian.model.vo.CartVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    /**
     * 前台购物车列表
     */
    @GetMapping("/list")
    public ApiRestResponse cartlist(HttpServletRequest request) throws ImoocMallException {
        //内部获取用户ID，防止横向越权
        User user =(User) request.getAttribute(Constant.IMOOC_MALL_USER);
        List<CartVO> cartVOList = cartService.cartList(user.getId());
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 购物车添加商品
     * @param productId
     * @param count
     * @param request
     * @return
     */
    @PostMapping("/add")
    public ApiRestResponse addCart(Integer productId, Integer count, HttpServletRequest request) throws ImoocMallException {
        User user =(User) request.getAttribute(Constant.IMOOC_MALL_USER);
        List<CartVO> cartVOList = cartService.addCart(productId, count, user.getId());
        return ApiRestResponse.success(cartVOList);
    }

    /**
     *  购物车更新商品的数量
     * @param productId
     * @param count
     * @param request
     * @return
     */
    @PostMapping("/update")
    public ApiRestResponse updateCart(Integer productId, Integer count, HttpServletRequest request) throws ImoocMallException {
        User user =(User) request.getAttribute(Constant.IMOOC_MALL_USER);
        List<CartVO> cartVOList = cartService.updateCart(productId, count, user.getId());
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 删除商品
     * @param productId
     * @param request
     * @return
     * @throws ImoocMallException
     */
    @PostMapping("/delete")
    public ApiRestResponse deleteCart(Integer productId, HttpServletRequest request) throws ImoocMallException {
        User user =(User) request.getAttribute(Constant.IMOOC_MALL_USER);
        List<CartVO> cartVOList = cartService.deleteCart(productId, user.getId());
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 选中/不选中购物车的某个商品
     * @param productId
     * @param request
     * @return
     * @throws ImoocMallException
     */
    @PostMapping("/selectSingle")
    public ApiRestResponse selectedSingleCart(Integer productId, Integer selected, HttpServletRequest request) throws ImoocMallException {
        User user =(User) request.getAttribute(Constant.IMOOC_MALL_USER);
        List<CartVO> cartVOList = cartService.selectCart(productId, selected, user.getId());
        return ApiRestResponse.success(cartVOList);
    }

    /**
     * 全部选中/全不选中购物车商品
     * @param selected
     * @param request
     * @return
     * @throws ImoocMallException
     */
    @PostMapping("/selectAll")
    public ApiRestResponse selectedAllCart(Integer selected, HttpServletRequest request) throws ImoocMallException {
        User user =(User) request.getAttribute(Constant.IMOOC_MALL_USER);
        List<CartVO> cartVOList = cartService.selectAllCart(selected, user.getId());
        return ApiRestResponse.success(cartVOList);
    }
}
