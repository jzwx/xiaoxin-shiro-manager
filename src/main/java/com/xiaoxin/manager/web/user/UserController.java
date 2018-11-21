package com.xiaoxin.manager.web.user;

import com.xiaoxin.manager.common.utils.IStatusMessage;
import com.xiaoxin.manager.common.utils.LoggerUtil;
import com.xiaoxin.manager.common.utils.ResponseResult;
import com.xiaoxin.manager.entity.UserDTO;
import com.xiaoxin.manager.entity.UserRolesVO;
import com.xiaoxin.manager.entity.UserSearchDTO;
import com.xiaoxin.manager.pojo.Role;
import com.xiaoxin.manager.pojo.User;
import com.xiaoxin.manager.service.AuthService;
import com.xiaoxin.manager.service.UserService;
import com.xiaoxin.manager.utils.PageDataResult;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:jzwx
 * @Desicription: UserController
 * @Date:Created in 2018-11-16 10:31
 * @Modified By:
 */
@Controller
@RequestMapping("/user")
public class UserController {
    /**
     * 日志打印器
     */
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService         userService;

    @Autowired
    private AuthService         authService;

    /**
     * 用户管理列表页
     * @return
     */
    @RequestMapping("/userList")
    public String toUserList() {
        return "/auth/userList";
    }

    /**
     * 用户登录
     * @param user
     * @param rememberMe
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @RequiresPermissions(value = "usermanage")
    public @ResponseBody ResponseResult login(UserDTO user,
                                              @RequestParam(value = "rememberMe", required = false) boolean rememberMe) {
        LoggerUtil.info(logger, "用户登录，请求参数=user:{0},是否记住我:{1}", user, rememberMe);
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
        if (null == user) {
            responseResult.setCode(IStatusMessage.SystemStatus.PARAM_ERROR.getCode());
            responseResult.setMessage("请求参数有误，请您稍后再试");
            LoggerUtil.info(logger, "用户登录，结果=responseResult:{0}" + responseResult);
            return responseResult;
        }
        if (!validatorRequestParam(user, responseResult)) {
            LoggerUtil.info(logger, "用户登录，结果=responseResult:{0}" + responseResult);
            return responseResult;
        }

        //用户是否存在
        User existUser = this.userService.findUserByUsername(user.getUsername());

        if (existUser == null) {
            responseResult.setMessage("该用户不存在，请您联系管理员");
            LoggerUtil.info(logger, "用户登录，结果=responseResult:{0}" + responseResult);
            return responseResult;
        } else {
            // 是否离职
            if (existUser.getIsJob()) {
                responseResult.setMessage("登录用户已离职，请您联系管理员");
                LoggerUtil.info(logger, "用户登录，结果=responseResult:{0}" + responseResult);
                return responseResult;
            }
        }

        // 用户登录
        try {
            // 1.封装用户名、密码、是否记住我到token令牌对象【支持记住我】
            AuthenticationToken token = new UsernamePasswordToken(user.getUsername(),
                DigestUtils.md5Hex(user.getPassword()), rememberMe);

            // 2.Subject调用login
            Subject subject = SecurityUtils.getSubject();
            ;
            // 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            // 每个Realm 都能在必要时对提交的AuthenticationTokens作出反应
            // 所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            LoggerUtil.debug(logger, "用户登录,用户验证开始！user={0}", user.getUsername());
            subject.login(token);
            responseResult.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
            responseResult.setMessage(IStatusMessage.SystemStatus.SUCCESS.getMessage());
            LoggerUtil.info(logger, "用户登录,用户验证通过!user={0}", user.getUsername());
        } catch (UnknownAccountException uae) {
            LoggerUtil.error(logger, "用户登录，用户验证未通过：未知用户！user={0}", user.getUsername());
            responseResult.setMessage("该用户不存在，请您联系管理员");
        } catch (LockedAccountException lae) {
            LoggerUtil.error(logger, "用户登录，用户验证未通过：账户已锁定！user={0}", user.getUsername());
            responseResult.setMessage("账户已锁定");
        } catch (ExcessiveAttemptsException eae) {
            LoggerUtil.error(logger, "用户登录，用户验证未通过：错误次数大于5次,账户已锁定！user={0}", user);
            responseResult.setMessage(
                "用户名或密码错误次数大于5次,账户已锁定!</br><span style='color:red;font-weight:bold; '>2分钟后可再次登录，或联系管理员解锁</span>");
            // 这里结合了，另一种密码输错限制的实现，基于redis或mysql的实现；也可以直接使用RetryLimitHashedCredentialsMatcher限制5次
        } catch (AuthenticationException ae) {
            // 通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            LoggerUtil.error(logger, "用户登录，用户验证未通过：认证异常，异常信息如下！user={0}", user.getUsername());
            responseResult.setMessage("用户名或密码不正确");
        } catch (Exception e) {
            LoggerUtil.error(logger, "用户登录，用户验证未通过：操作异常，异常信息如下！user={0}", user.getUsername());
            responseResult.setMessage("用户登录失败，请您稍后再试");
        }
        LoggerUtil.debug(logger, "用户登录，user={0},登录结果=responseResult:{1}", user.getUsername(),
            responseResult);
        return responseResult;
    }

    /**
     * @描述：校验请求参数
     * @param obj
     * @param response
     * @return
     */
    protected boolean validatorRequestParam(Object obj, ResponseResult response) {
        boolean flag = false;
        Validator validator = new Validator();
        List<ConstraintViolation> ret = validator.validate(obj);
        if (ret.size() > 0) {
            // 校验参数有误
            response.setCode(IStatusMessage.SystemStatus.PARAM_ERROR.getCode());
            response.setMessage(ret.get(0).getMessageTemplate());
        } else {
            flag = true;
        }
        return flag;
    }

    /**
     * 分页查询用户列表信息
     * @param page
     * @param limit
     * @param userSearch
     * @return
     */
    @RequestMapping(value = "/getUsers", method = RequestMethod.POST)
    public @ResponseBody PageDataResult getUsers(@RequestParam("page") Integer page,
                                                 @RequestParam("limit") Integer limit,
                                                 UserSearchDTO userSearch) {
        LoggerUtil.debug(logger, "分页查询用户列表！搜索条件：userSearch：{0},page:{1},每页记录数量limit:", userSearch,
            limit);
        PageDataResult pdr = new PageDataResult();
        try {
            if (null == page) {
                page = 1;
            }
            if (null == limit) {
                limit = 10;
            }
            //获取用户和角色列表
            pdr = userService.getUsers(userSearch, page, limit);
            LoggerUtil.debug(logger, "用户列表查询=pdr:{0}", pdr);
            //            for (int i = 0; i < pdr.getList().size(); i++) {
            //                System.out.println(i + ":" + pdr.getList().get(i).toString());
            //            }
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.error(logger, "用户列表查询异常！", e);
        }
        return pdr;
    }

    /**
     * 查询用户数据根据用户id
     * @param id
     * @return
     */
    @RequestMapping(value = "/getUserAndRoles", method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> getUserAndRoles(@RequestParam("id") Integer id) {
        LoggerUtil.debug(logger, "查询用户数据！id:{0}", id);
        Map<String, Object> map = new HashMap<>();
        try {
            if (null == id) {
                LoggerUtil.debug(logger, "查询用户数据==请求参数有误，请您稍后再试");
                map.put("msg", "请求参数有误，请您稍后再试");
                return map;
            }
            //查询用户
            UserRolesVO urvo = userService.getUserAndRoles(id);
            LoggerUtil.debug(logger, "查询用户数据！urvo={0}", urvo);
            if (null != urvo) {
                map.put("user", urvo);
                //获取全部角色数据
                List<Role> roles = this.authService.getRoles();
                LoggerUtil.debug(logger, "查询角色数据！roles={0}", roles);
                if (null != roles && roles.size() > 0) {
                    map.put("roles", roles);
                }
                map.put("msg", "ok");
            } else {
                map.put("msg", "查询用户信息有误，请您稍后再试");
            }
            logger.debug("查询用户数据成功！map=" + map);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "查询用户错误，请您稍后再试");
            LoggerUtil.error(logger, "查询用户数据异常！{0}", e);
        }
        return map;
    }

    /**
     * 设置用户[新增或更新]
     * @param roleIds
     * @param user
     * @return
     */
    @RequestMapping(value = "/setUser", method = RequestMethod.POST)
    public @ResponseBody String setUser(@RequestParam("roleIds") String roleIds, User user) {
        LoggerUtil.debug(logger, "设置用户[新增或更新]！user:{0},roleIds:{1}", user, roleIds);
        try {
            if (null == user) {
                LoggerUtil.debug(logger, "置用户[新增或更新]，结果=请您填写用户信息");
                return "请您填写用户信息";
            }
            if (StringUtils.isEmpty(roleIds)) {
                LoggerUtil.debug(logger, "置用户[新增或更新]，结果=请您给用户设置角色");
                return "请您给用户设置角色";
            }
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (null == existUser) {
                LoggerUtil.debug(logger, "置用户[新增或更新]，结果=您未登录或登录超时，请您登录后再试");
                return "您未登录或登录超时，请您登录后再试";
            }
            user.setInsertUid(existUser.getId());
            // 设置用户[新增或更新]
            LoggerUtil.info(logger, "设置用户[新增或更新]成功！user={0},roleIds={1}，操作的用户ID={2}", user, roleIds,
                existUser.getId());
            return userService.setUser(user, roleIds);
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.error(logger, "设置用户[新增或更新]异常！{0}", e);
            return "操作异常，请您稍后再试";
        }
    }

    /**
     * 设置用户是否离职
     * @param id
     * @param isJob
     * @param version
     * @return
     */
    @RequestMapping(value = "/setJobUser", method = RequestMethod.POST)
    public @ResponseBody String setJobUser(@RequestParam("id") Integer id,
                                           @RequestParam("job") Integer isJob,
                                           @RequestParam("version") Integer version) {
        LoggerUtil.debug(logger, "设置用户是否离职！id:{0},isJob:{1},version:{2}", id, isJob, version);
        String msg = "";
        try {
            if (null == id || null == isJob || null == version) {
                LoggerUtil.debug(logger, "设置用户是否离职，结果=请求参数有误，请您稍后再试");
                return "请求参数有误，请您稍后再试";
            }
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (null == existUser) {
                LoggerUtil.debug(logger, "设置用户是否离职，结果=您未登录或登录超时，请您登录后再试");
                return "您未登录或登录超时，请您登录后再试";
            }
            // 设置用户是否离职
            msg = userService.setJobUser(id, isJob, existUser.getId(), version);
            LoggerUtil.info(logger, "设置用户是否离职成功！userID={0},isJob:{1}，操作的用户ID={2}", id, isJob,
                existUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.error(logger, "设置用户是否离职异常！{0}", e);
            msg = "操作异常，请您稍后再试！";
        }
        return msg;
    }

    /**
     * 删除用户
     * @param id
     * @param version
     * @return
     */
    @RequestMapping(value = "/delUser", method = RequestMethod.POST)
    public @ResponseBody String delUser(@RequestParam("id") Integer id,
                                        @RequestParam("version") Integer version) {
        LoggerUtil.debug(logger, "删除用户！id:{0}", id);
        String msg = "";
        try {
            if (null == id || null == version) {
                logger.debug("删除用户，结果=请求参数有误，请您稍后再试");
                return "请求参数有误，请您稍后再试";
            }
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (null == existUser) {
                logger.debug("删除用户，结果=您未登录或登录超时，请您登录后再试");
                return "您未登录或登录超时，请您登录后再试";
            }
            //删除用户
            msg = userService.setDelUser(id, 1, existUser.getId(), version);
            LoggerUtil.info(logger, "删除用户:{0}。userId={1}，操作用户id:{2}", msg, id, existUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除用户异常！", e);
            msg = "操作异常，请您稍后再试";
        }
        return msg;
    }

    /**
     * 恢复用户
     * @param id
     * @param version
     * @return
     */
    @RequestMapping(value = "/recoverUser", method = RequestMethod.POST)
    public @ResponseBody String recoverUser(@RequestParam("id") Integer id,
                                            @RequestParam("version") Integer version) {
        LoggerUtil.debug(logger, "恢复用户！id:{0}", id);
        String msg = "";
        try {
            User existUser = (User) SecurityUtils.getSubject().getPrincipal();
            if (null == existUser) {
                return "您未登录或登录超时，请您登录后再试";
            }
            if (null == id || null == version) {
                return "请求参数有误，请您稍后再试";
            }
            // 删除用户
            msg = userService.setDelUser(id, 0, existUser.getId(), version);
            LoggerUtil.info(logger, "恢复用户【{0}.recoverUser】{1}。用户userId={2}，操作的用户ID={3}",
                this.getClass().getName(), msg, id, existUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("恢复用户【" + this.getClass().getName() + ".recoverUser】用户异常！", e);
            msg = "操作异常，请您稍后再试";
        }
        return msg;
    }
}
