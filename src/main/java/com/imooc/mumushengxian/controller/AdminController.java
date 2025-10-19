package com.imooc.mumushengxian.controller;

import com.imooc.mumushengxian.Service.UserService;
import com.imooc.mumushengxian.common.ApiRestResponse;
import com.imooc.mumushengxian.common.Constant;
import com.imooc.mumushengxian.exception.ImoocMallException;
import com.imooc.mumushengxian.exception.ImoocMallExceptionEnum;
import com.imooc.mumushengxian.model.pojo.User;
import com.imooc.mumushengxian.model.request.group.LoginGroup;
import com.imooc.mumushengxian.model.request.UserRequest;
import com.imooc.mumushengxian.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员接口
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 管理员登陆
     * @param userRequest
     * @param response
     * @return user
     */
    @PostMapping("/login")
    public ApiRestResponse<User> login(@Validated(LoginGroup.class) @RequestBody UserRequest userRequest,
                                       HttpServletResponse response) throws ImoocMallException {
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();
        //1.获取用户对象
        User user = userService.login(username, password);
        user.setPassword(null);
        //2.生成JWT token并添加到响应头
        String token = JwtTokenUtil.generateToken(user);
        response.setHeader("Authorization", "Bearer " + token);
        //3.返回用户对象
        return ApiRestResponse.success(user);
    }

    /**
     * 退出登录，清除session
     * @return
     */
    @PostMapping("/logout")
    public ApiRestResponse<User> logout() {
        //Step1: 获取当前会话
        HttpSession session = request.getSession(false);
        //Step2: 清楚用户信息
        if (session != null) {
            // 1. 清除用户信息
            session.removeAttribute(Constant.IMOOC_MALL_USER);
            // 2. 可选：使整个会话失效（更安全）
            session.invalidate();
        }
        //Step3.返回成功响应
        return ApiRestResponse.success();
    }

    /**
     * 退出登录
     * 注意：JWT是无状态的，服务端无法主动使token失效
     * 客户端需要自行删除存储的token
     * @return
     */
    @PostMapping("/logoutWithJwt")
    public ApiRestResponse<User> logoutWithJwt() {
        // JWT无状态，服务端无法主动使token失效
        // 客户端需要自行删除存储的token
        return ApiRestResponse.success();
    }
}
