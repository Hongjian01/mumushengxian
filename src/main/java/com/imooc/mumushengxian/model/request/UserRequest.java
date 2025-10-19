package com.imooc.mumushengxian.model.request;

import com.imooc.mumushengxian.model.request.group.LoginGroup;
import com.imooc.mumushengxian.model.request.group.RegisterGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {
    // 用户名在登录和注册时都需要验证
    @NotBlank(groups = {LoginGroup.class, RegisterGroup.class}, message = "用户名不能为空")
    private String username;

    // 密码在登录 and 注册时需非空，注册时还需要长度验证
    @NotBlank(groups = {LoginGroup.class, RegisterGroup.class}, message = "密码不能为空") // 登录组 和 注册组均需要非空验证
    @Size(min = 8, groups = {LoginGroup.class, RegisterGroup.class}, message = "密码最少8位数") // 登录组 和 注册组需要长度验证
    private String password;

    //注册时邮箱需非空
    @NotBlank(groups = RegisterGroup.class, message = "邮箱不能为空")
    private String emailAddress;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
