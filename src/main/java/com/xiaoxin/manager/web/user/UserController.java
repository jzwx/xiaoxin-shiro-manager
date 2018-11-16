package com.xiaoxin.manager.web.user;

import com.xiaoxin.manager.common.utils.IStatusMessage;
import com.xiaoxin.manager.common.utils.LoggerUtil;
import com.xiaoxin.manager.common.utils.ResponseResult;
import com.xiaoxin.manager.entity.UserDTO;
import com.xiaoxin.manager.pojo.User;
import com.xiaoxin.manager.service.UserService;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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

    @RequestMapping(value = "login", method = RequestMethod.POST)
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
            Subject subject = SecurityUtils.getSubject();;
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
}
