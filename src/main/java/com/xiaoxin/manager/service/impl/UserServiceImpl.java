package com.xiaoxin.manager.service.impl;

import com.xiaoxin.manager.dao.UserMapper;
import com.xiaoxin.manager.pojo.User;
import com.xiaoxin.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author:jzwx
 * @Desicription: UserServiceImpl
 * @Date:Created in 2018-11-16 11:16
 * @Modified By:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据用户名查询用户数据
     * @param username
     * @return
     */
    @Override
    public User findUserByUsername(String username) {
        return this.userMapper.findUserByUsername(username);
    }
}
