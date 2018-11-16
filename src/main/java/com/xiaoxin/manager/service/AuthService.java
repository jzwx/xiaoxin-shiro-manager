package com.xiaoxin.manager.service;

import com.xiaoxin.manager.pojo.Permission;
import com.xiaoxin.manager.pojo.Role;

import java.util.List;

/**
 * @Author:jzwx
 * @Desicription: AuthService
 * @Date:Created in 2018-11-16 14:20
 * @Modified By:
 */
public interface AuthService {
    /**
     * 根据用户获取角色列表
     * @param userId
     * @return
     */
    List<Role> getRoleByUserId(Integer userId);

    /**
     * 根据角色id获取权限数据
     * @param id
     * @return
     */
    List<Permission> findPermsByRoleId(Integer id);
}
