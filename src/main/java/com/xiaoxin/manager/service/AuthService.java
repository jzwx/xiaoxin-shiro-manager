package com.xiaoxin.manager.service;

import com.xiaoxin.manager.entity.PermissionVO;
import com.xiaoxin.manager.entity.RoleVO;
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

    /**
     * 根据用户id获取权限数据
     * @param id
     * @return
     */
    List<PermissionVO> getUserPerms(Integer id);

    /**
     * 查找所有角色
     * @return
     */
    List<Role> getRoles();

    /**
     * 查询所有角色
     * @return
     */
    List<Role> roleList();

    /**
     * 关联查询权限树列表
     * @return
     */
    List<PermissionVO> findPerms();

    RoleVO findRoleAndPerms(Integer id);

    /**
     * 添加角色
     * @param role
     * @param permIds
     * @return
     */
    String addRole(Role role, String permIds);

    /**
     * 更新角色并授权
     * @param role
     * @param permIds
     * @return
     */
    String updateRole(Role role, String permIds);

    /**
     * 删除角色以及它对应的权限
     * @param id
     * @return
     */
    String delRole(int id);

    List<Permission> permList();

    int updatePerm(Permission permission);

    /**
     * 增加子节点权限
     * @param permission
     * @return
     */
    int addPermission(Permission permission);

    String delPermission(int id);

    Permission getPermission(int id);
}
