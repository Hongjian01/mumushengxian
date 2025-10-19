package com.imooc.mumushengxian.Service;

import com.imooc.mumushengxian.exception.ImoocMallException;
import com.imooc.mumushengxian.model.pojo.Product;
import com.imooc.mumushengxian.model.pojo.User;
import com.imooc.mumushengxian.model.request.CategoryRequest;

public interface UserService {
    User login(String username, String password) throws ImoocMallException;

    User register(String username, String password, String emailAddress) throws ImoocMallException;

    User update(String signature, User currentUser) throws ImoocMallException;

    boolean checkAdminRole(User user) throws ImoocMallException;
}
