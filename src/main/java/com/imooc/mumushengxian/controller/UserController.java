package com.imooc.mumushengxian.controller;

import com.imooc.mumushengxian.Service.UserService;
import com.imooc.mumushengxian.common.ApiRestResponse;
import com.imooc.mumushengxian.common.Constant;
import com.imooc.mumushengxian.exception.ImoocMallException;
import com.imooc.mumushengxian.exception.ImoocMallExceptionEnum;
import com.imooc.mumushengxian.model.dao.UserMapper;
import com.imooc.mumushengxian.model.pojo.User;
import com.imooc.mumushengxian.model.request.group.LoginGroup;
import com.imooc.mumushengxian.model.request.group.RegisterGroup;
import com.imooc.mumushengxian.model.request.UserRequest;
import com.imooc.mumushengxian.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private HttpServletRequest request;

    /**
     * 用户登录
     * @param userRequest
     * @param response
     * @return user
     * @throws Exception
     */
    @PostMapping("/login")
    public ApiRestResponse<User> login(
            @Validated(LoginGroup.class) @RequestBody UserRequest userRequest,
            HttpServletResponse response) throws Exception {
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();
        //获取用户
        User user = userService.login(username, password);
        //防止密码泄露
        user.setPassword(null);
        // 生成JWT token并添加到响应头
        String token = JwtTokenUtil.generateToken(user);
        response.setHeader("Authorization", "Bearer " + token);

        //在session中保存用户对象,有需要时可以通过getAttribute()获取
//        request.setAttribute(Constant.IMOOC_MALL_USER, user);

        return ApiRestResponse.success(user);
    }

    /**
     * 用户注册
     * @param userRequest
     * @param response
     * @return user
     * @throws Exception
     */
    @PostMapping("/register")
    public ApiRestResponse<User> register(@Validated(RegisterGroup.class) @RequestBody UserRequest userRequest,
                                          HttpServletResponse response) throws Exception {
        String username = userRequest.getUsername();
        String password = userRequest.getPassword();
        String emailAddress = userRequest.getEmailAddress();
        //用户注册
        User user = userService.register(username, password, emailAddress);
        if (user == null) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.USER_REGISTER_FAILED);
        }
        user.setPassword(null);
        //生成JWT token并添加到响应头
        String token = JwtTokenUtil.generateToken(user);
        response.setHeader("Authorization", "Bearer " + token);

        return ApiRestResponse.success(user);
    }

    /**
     * 更新个人签名
     * @param signature
     * @return user
     */
    @PostMapping("/update")
    public ApiRestResponse<User> update(@RequestParam("signature") String signature, HttpServletRequest request) throws ImoocMallException {
        //Step1:从请求属性获取用户
        User currentUser = (User) request.getAttribute(Constant.IMOOC_MALL_USER);
        //Step2:更新用户签名
        User user = userService.update(signature, currentUser);
        if (user==null) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
        user.setPassword(null);
        //Step3:更新Session中的用户信息
        request.getSession().setAttribute(Constant.IMOOC_MALL_USER, user);
        return ApiRestResponse.success(user);
    }

    /**
     * 退出登录，清楚session
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
}
