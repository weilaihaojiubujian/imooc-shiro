package com.imooc.dao;

import com.imooc.vo.User;

import java.util.List;

public interface UserDao {
    User getUserByUserName(String username);

    List<String> queryRolesByUserName(String username);
}
