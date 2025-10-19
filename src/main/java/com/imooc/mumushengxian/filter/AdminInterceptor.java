package com.imooc.mumushengxian.filter;

import com.imooc.mumushengxian.Service.UserService;
import com.imooc.mumushengxian.common.Constant;
import com.imooc.mumushengxian.exception.ImoocMallException;
import com.imooc.mumushengxian.exception.ImoocMallExceptionEnum;
import com.imooc.mumushengxian.model.pojo.User;
import com.imooc.mumushengxian.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(UserInterceptor.class);

    private UserService userService;

    @Autowired
    public AdminInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("拦截请求: {}?{}", request.getRequestURI(), request.getQueryString());
        //Step1.获取Authorization头
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ImoocMallException(ImoocMallExceptionEnum.USER_NEED_LOGIN);
        }
        //Step2.提取token
        String token = authHeader.substring(7);
        if (!JwtTokenUtil.validateToken(token)) {
            throw new ImoocMallException(ImoocMallExceptionEnum.USER_NEED_LOGIN);
        }
        //Step3.解析token获取用户信息
        Claims claims = JwtTokenUtil.parseToken(token);
        User user = new User();
        user.setId(claims.get("userId", Integer.class));
        user.setUsername(claims.get("username", String.class));
        user.setRole(claims.get("role", Integer.class));
        //Step4.检查管理员身份
        if (!userService.checkAdminRole(user)) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NEED_ADMIN);
        }
        //Step5.将用户信息存储在请求属性中
        request.setAttribute(Constant.IMOOC_MALL_USER, user);
        return true;
    }
}
