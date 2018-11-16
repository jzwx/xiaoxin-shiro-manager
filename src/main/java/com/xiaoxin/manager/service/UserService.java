package com.xiaoxin.manager.service;

import com.xiaoxin.manager.pojo.User;

/**
 * @Author:jzwx
 * @Desicription: UserService
 * @Date:Created in 2018-11-16 11:16
 * @Modified By:
 */
public interface UserService {
    /**
     * 根据用户名查询用户数据
     * @param username
     * @return
     */
    User findUserByUsername(String username);
}
