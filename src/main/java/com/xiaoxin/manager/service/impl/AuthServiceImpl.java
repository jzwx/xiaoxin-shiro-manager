package com.xiaoxin.manager.service.impl;

import com.xiaoxin.manager.common.utils.LoggerUtil;
import com.xiaoxin.manager.dao.PermissionMapper;
import com.xiaoxin.manager.dao.RoleMapper;
import com.xiaoxin.manager.dao.RolePermissionMapper;
import com.xiaoxin.manager.entity.PermissionVO;
import com.xiaoxin.manager.entity.RoleVO;
import com.xiaoxin.manager.pojo.Permission;
import com.xiaoxin.manager.pojo.Role;
import com.xiaoxin.manager.pojo.RolePermission;
import com.xiaoxin.manager.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * @Author:jzwx
 * @Desicription: AuthServiceImpl
 * @Date:Created in 2018-11-16 14:22
 * @Modified By:
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger  logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private RoleMapper           roleMapper;

    @Autowired
    private PermissionMapper     permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

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

    /**
     * 根据用户id获取权限数据
     * @param id
     * @return
     */
    @Override
    public List<PermissionVO> getUserPerms(Integer id) {
        return this.permissionMapper.getUserPerms(id);
    }

    @Override
    public List<Role> getRoles() {
        //TODO 根据部门和权限等级限制角色显示
        return this.roleMapper.getRoles();
    }

    @Override
    public List<Role> roleList() {
        return this.roleMapper.findList();
    }

    @Override
    public List<PermissionVO> findPerms() {
        return this.permissionMapper.findPerms();
    }

    @Override
    public RoleVO findRoleAndPerms(Integer id) {
        return this.roleMapper.findRoleAndPerms(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 30000, rollbackFor = { RuntimeException.class,
                                                                                                                       Exception.class })
    public String addRole(Role role, String permIds) {
        this.roleMapper.insert(role);
        int roleId = role.getId();
        String[] arrays = permIds.split(",");
        LoggerUtil.debug(logger, "权限id =arrays={0}", arrays.toString());
        setRolePerms(roleId, arrays);
        return "ok";
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 30000, rollbackFor = { RuntimeException.class,
                                                                                                                       Exception.class })
    public String updateRole(Role role, String permIds) {
        int roleId = role.getId();
        String[] arrays = permIds.split(",");
        LoggerUtil.debug(logger, "权限id =arrays={0}", arrays.toString());
        //1,更新角色表数据;
        int num = this.roleMapper.updateByPrimaryKeySelective(role);
        if (num < 1) {
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "操作失败";
        }
        //2.删除原角色权限
        batchDelRolePerms(roleId);
        //3.添加新的角色权限数据
        setRolePerms(roleId, arrays);
        return "ok";
    }

    /**
     * 删除角色以及它对应的权限
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 30000, rollbackFor = { RuntimeException.class,
                                                                                                                       Exception.class })
    public String delRole(int id) {
        //1.删除角色对应的权限
        batchDelRolePerms(id);
        //2.删除角色
        int num = this.roleMapper.deleteByPrimaryKey(id);
        if (num < 1) {
            //事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "操作失败";
        }
        return "ok";
    }

    @Override
    public List<Permission> permList() {
        return this.permissionMapper.findAll();
    }

    @Override
    public int updatePerm(Permission permission) {
        return this.permissionMapper.updateByPrimaryKeySelective(permission);
    }

    @Override
    public int addPermission(Permission permission) {
        return this.permissionMapper.insert(permission);
    }

    @Override
    public String delPermission(int id) {
        //查看该权限是否有子节点，如果有，先删除子节点
        List<Permission> childPerm = this.permissionMapper.findChildPerm(id);
        if (null != childPerm && childPerm.size() > 0) {
            return "删除失败，请您先删除该权限的子节点";
        }
        if (this.permissionMapper.deleteByPrimaryKey(id) > 0) {
            return "ok";
        }else {
            return "删除失败，请您稍后再试";
        }
    }

    @Override
    public Permission getPermission(int id) {
        return this.permissionMapper.selectByPrimaryKey(id);
    }

    private void batchDelRolePerms(int roleId) {
        List<RolePermission> rps = this.rolePermissionMapper.findByRole(roleId);
        if (null != rps && rps.size() > 0) {
            for (RolePermission rp : rps) {
                this.rolePermissionMapper.deleteByPrimary(rp);
            }
        }
    }

    /**
     * 给当前角色设置权限
     * @param roleId
     * @param arrays
     */
    private void setRolePerms(int roleId, String[] arrays) {
        for (String permid : arrays) {
            RolePermission rp = new RolePermission();
            rp.setRoleId(roleId);
            rp.setPermitId(Integer.valueOf(permid));
            this.rolePermissionMapper.insert(rp);
        }
    }
}
