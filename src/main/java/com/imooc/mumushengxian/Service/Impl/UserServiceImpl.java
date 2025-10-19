package com.imooc.mumushengxian.Service.Impl;

import com.imooc.mumushengxian.Service.UserService;
import com.imooc.mumushengxian.exception.ImoocMallException;
import com.imooc.mumushengxian.exception.ImoocMallExceptionEnum;
import com.imooc.mumushengxian.model.dao.UserMapper;
import com.imooc.mumushengxian.model.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) throws ImoocMallException {
        // 1. 检查用户是否存在
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.USER_NOT_EXIST);
        }
        // 2. 检查密码是否正确
        if (!password.equals(user.getPassword())) {
            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_PASSWORD);
        }
        return user;
    }

    @Override
    public User register(String username, String password, String emailAddress) throws ImoocMallException {
        //1.检查用户名是否重复
        User result = userMapper.selectByUsername(username);
        if (result != null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.USER_ALREADY_EXISTED);
        }
        //2.将用户写入数据库
        //TODO:用户密码暂时明文存储，后面进行加密
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmailAddress(emailAddress);
        int count = userMapper.insertSelective(user);
        if (count != 1) {
            throw new ImoocMallException(ImoocMallExceptionEnum.USER_REGISTER_FAILED);
        }
        return user;
    }

    @Override
    public User update(String signature, User currentUser) throws ImoocMallException {
        User user = userMapper.selectByUsername(currentUser.getUsername());
        if (user == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.USER_NOT_EXIST);
        }
        user.setPersonalizedSignature(signature);
        int count = userMapper.updateByPrimaryKeySelective(user);
        if (count != 1) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
        return user;
    }

    @Override
    public boolean checkAdminRole(User user) throws ImoocMallException {
//        //1.从数据库中获取用户对象
//        User currentUser = userMapper.selectByUsername(user.getUsername());
//        if (currentUser == null) {
//            throw new ImoocMallException(ImoocMallExceptionEnum.USER_NOT_EXIST);
//        }
//        //2.判断用户是否为管理员
//        if (currentUser.getRole() == 2) {
//            return true;
//        }else{
//            return false;
//        }
        return user.getRole().equals(2);
    }
}
