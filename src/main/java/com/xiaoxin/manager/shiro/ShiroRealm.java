package com.xiaoxin.manager.shiro;

import com.xiaoxin.manager.common.utils.LoggerUtil;
import com.xiaoxin.manager.dao.UserMapper;
import com.xiaoxin.manager.pojo.Role;
import com.xiaoxin.manager.pojo.User;
import com.xiaoxin.manager.service.AuthService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author:jzwx
 * @Desicription: ShiroRealm
 * @Date:Created in 2018-11-14 17:20
 * @Modified By:
 */
@Service
public class ShiroRealm extends AuthorizingRealm {
    /**
     * 日志打印器
     */
    private static final Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

    @Autowired
    private UserMapper          userMapper;

    @Autowired
    private AuthService         authService;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 授权
        LoggerUtil.debug(logger, "授予角色和权限");
        // 添加权限 和 角色信息
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 获取当前登录用户
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if ("admin".equals(user.getUsername())) {
            //超级管理员，添加所有角色，添加所有权限
            authorizationInfo.addRole("*");
            authorizationInfo.addStringPermission("*");
        } else {
            // 普通用户，查询用户的角色，根据角色查询权限
            Integer userId = user.getId();
            this.authService.getRoleByUserId(userId).stream().forEach(role -> {
                authorizationInfo.addRole(role.getCode());
                this.authService.findPermsByRoleId(role.getId()).stream().forEach(permission -> {
                    authorizationInfo.addStringPermission(permission.getCode());
                });
            });
        }
        return authorizationInfo;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //TODO
        // UsernamePasswordToken用于存放提交的登录信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        LoggerUtil.info(logger, "用户登录认证:验证当前Subject时获取到token为:{0}",
            ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));
        String username = token.getUsername();
        // 调用数据层
        User user = this.userMapper.findUserByUsername(username);
        LoggerUtil.debug(logger, "用户登录认证!用户信息user:{0}", user);
        if (user == null) {
            //没有返回登录用户名对应的SimpleAuthenticationInfo对象时，就会在LoginController中抛出UnknownAccountException异常  用户不存在
            return null;
        } else {
            // 密码存在
            // 第一个参数 ，登陆后，需要在session保存数据
            // 第二个参数，查询到密码(加密规则要和自定义的HashedCredentialsMatcher中的HashAlgorithmName散列算法一致)
            // 第三个参数 ，realm名字
            return new SimpleAuthenticationInfo(user, DigestUtils.md5Hex(user.getPassword()),
                getName());
        }
    }

    /**
     * 清除所有缓存【实测无效】
     */
    public void clearCachedAuth() {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}
