package com.xiaoxin.manager.service.impl;

import com.xiaoxin.manager.dao.PermissionMapper;
import com.xiaoxin.manager.dao.RoleMapper;
import com.xiaoxin.manager.pojo.Permission;
import com.xiaoxin.manager.pojo.Role;
import com.xiaoxin.manager.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:jzwx
 * @Desicription: AuthServiceImpl
 * @Date:Created in 2018-11-16 14:22
 * @Modified By:
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private RoleMapper       roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 根据用户获取角色列表
     * @param userId
     * @return
     */
    @Override
    public List<Role> getRoleByUserId(Integer userId) {
        return this.roleMapper.getRoleByUserId(userId);
    }

    /**
     * 根据角色id获取权限数据
     * @param id
     * @return
     */
    @Override
    public List<Permission> findPermsByRoleId(Integer id) {
        return this.permissionMapper.findPermsByRoleId(id);
    }
}
