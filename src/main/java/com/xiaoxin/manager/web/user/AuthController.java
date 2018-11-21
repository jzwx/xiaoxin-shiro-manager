package com.xiaoxin.manager.web.user;

import com.xiaoxin.manager.common.utils.LoggerUtil;
import com.xiaoxin.manager.entity.PermissionVO;
import com.xiaoxin.manager.entity.RoleVO;
import com.xiaoxin.manager.pojo.Permission;
import com.xiaoxin.manager.pojo.Role;
import com.xiaoxin.manager.pojo.RolePermission;
import com.xiaoxin.manager.pojo.User;
import com.xiaoxin.manager.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * @Author:jzwx
 * @Desicription: AuthController
 * @Date:Created in 2018-11-19 10:55
 * @Modified By:
 */
@Controller
@RequestMapping("/auth")
public class AuthController {
    /**
     * 日志打印器
     */
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService         authService;

    /**
     * 跳转到角色列表
     * @return
     */
    @RequestMapping("/roleManage")
    public ModelAndView toPage() {
        return new ModelAndView("/auth/roleManage");
    }

    /**
     * 角色列表
     * @return ok/fail
     */
    @RequestMapping(value = "/getRoleList", method = RequestMethod.GET)
    @ResponseBody
    public List<Role> getRoleList() {
        logger.debug("角色列表！");
        List<Role> roleList = null;
        try {
            roleList = authService.roleList();
            logger.debug("角色列表查询=roleList:" + roleList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("角色查询异常！", e);
        }
        return roleList;
    }

    /**
     * 查询权限树数据
     * @return PermTreeDTO
     */
    @RequestMapping(value = "/findPerms", method = RequestMethod.GET)
    @ResponseBody
    public List<PermissionVO> findPerms() {
        logger.debug("权限树列表！");
        List<PermissionVO> pvo = null;
        try {
            pvo = authService.findPerms();
            //生成页面需要的json格式
            logger.debug("权限树列表查询=pvo:" + pvo);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("权限树列表查询异常！", e);
        }
        return pvo;
    }

    /**
     * 根据用户id查询权限树列表
     * @return List<PermissionVO>
     */
    @RequestMapping(value = "/getUserPerms", method = RequestMethod.GET)
    public @ResponseBody List<PermissionVO> getUserPerms() {
        LoggerUtil.debug(logger, "根据用户id查询限树列表！");
        List<PermissionVO> pvo = null;
        User existUser = (User) SecurityUtils.getSubject().getPrincipal();
        if (null == existUser) {
            LoggerUtil.debug(logger, "根据用户id查询限树列表！用户未登录");
            return pvo;
        }
        try {
            pvo = authService.getUserPerms(existUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.error(logger, "根据用户id查询权限树列表查询异常！{0}", e);
        }
        return pvo;
    }

    /**
     * 查找所有角色
     * @return List<Role>
     */
    @RequestMapping(value = "/getRoles", method = RequestMethod.GET)
    public @ResponseBody List<Role> getRoles() {
        LoggerUtil.debug(logger, "查找所有角色!");
        List<Role> roles = null;
        try {
            return this.authService.getRoles();
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.error(logger, "查找所有角色异常！{0}", e);
        }
        return roles;
    }

    /**
     * 根据id查询角色
     * @param id
     * @return
     */
    @RequestMapping(value = "/updateRole/{id}", method = RequestMethod.GET)
    public ModelAndView updateRole(@PathVariable("id") Integer id) {
        LoggerUtil.debug(logger, "根据id查询角色id：{0}", id);
        ModelAndView mv = new ModelAndView("/auth/roleManage");
        try {
            if (null == id) {
                mv.addObject("msg", "请求参数有误,请您稍后再试");
                return mv;
            }
            mv.addObject("flag", "updateRole");
            RoleVO rvo = this.authService.findRoleAndPerms(id);
            //角色下的权限
            List<RolePermission> rpks = rvo.getRolePerms();
            //获取全部权限数据
            List<PermissionVO> pvos = authService.findPerms();
            for (RolePermission rp : rpks) {
                //设置角色下的权限checked状态为：true
                for (PermissionVO pvo : pvos) {
                    if (String.valueOf(rp.getPermitId()).equals(String.valueOf(pvo.getId()))) {
                        pvo.setChecked(true);
                    }
                }
            }
            mv.addObject("perms", pvos.toArray());
            //角色详情
            mv.addObject("roleDetail", rvo);
            LoggerUtil.debug(logger, "根据id查询角色数据：{0}", mv);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加角色并授权！异常！", e);
            mv.addObject("msg", "请求异常，请您稍后再试");
        }
        return mv;
    }

    /**
     * 添加角色并授权
     * @param permIds
     * @param role
     * @return
     */
    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    public @ResponseBody String addRole(@RequestParam("permIds") String permIds, Role role) {
        LoggerUtil.debug(logger, "添加角色并授权！角色数据role：{0}，权限数据permIds：{1}", role, permIds);
        try {
            if (StringUtils.isEmpty(permIds)) {
                return "未授权，请您给该角色授权";
            }
            if (null == role) {
                return "请您填写完整的角色数据";
            }
            role.setInsertTime(new Date());
            return authService.addRole(role, permIds);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("添加角色并授权！异常！", e);
        }
        return "操作错误，请您稍后再试";
    }

    /**
     * 更新角色并授权
     * @param permIds
     * @param role
     * @return
     */
    @RequestMapping(value = "/setRole", method = RequestMethod.POST)
    public @ResponseBody String setRole(@RequestParam("rolePermIds") String permIds, Role role) {
        LoggerUtil.debug(logger, "更新角色并授权！角色数据role：{0},权限数据permIds：{1}", role, permIds);
        try {
            if (StringUtils.isEmpty(permIds)) {
                return "未授权，请您给该角色授权";
            }
            if (null == role) {
                return "请您填写完整的角色数据";
            }
            role.setUpdateTime(new Date());
            return authService.updateRole(role, permIds);
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.error(logger, "更新角色并授权！异常！{0}", e);
        }
        return "操作错误，请您稍后再试";
    }

    /**
     * 删除角色以及它对应的权限
     * @param id
     * @return
     */
    @RequestMapping(value = "/delRole", method = RequestMethod.POST)
    public @ResponseBody String delRole(@RequestParam("id") int id) {
        LoggerUtil.debug(logger, "删除角色以及它对应的权限--id-{0}", id);
        try {
            if (id > 0) {
                return this.authService.delRole(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.error(logger, "删除角色异常！{1}", e);
        }
        return "删除角色出错，请您稍后再试";
    }

    /**
     * 权限列表
     * @return
     */
    @RequestMapping(value = "/permList", method = RequestMethod.GET)
    public @ResponseBody ModelAndView permList() {
        LoggerUtil.debug(logger, "权限列表！");
        ModelAndView mav = new ModelAndView("/auth/permList");
        try {
            List<Permission> permList = authService.permList();
            mav.addObject("permList", permList);
            mav.addObject("msg", "ok");
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.error(logger, "权限查询异常！{0}", e);
        }
        return mav;
    }

    /**
     * 添加权限
     * @param type type [0：编辑；1：新增子节点权限]
     * @param permission
     * @return
     */
    @RequestMapping(value = "/setPerm", method = RequestMethod.POST)
    public @ResponseBody String setPerm(@RequestParam("type") int type, Permission permission) {
        LoggerUtil.debug(logger, "设置权限--区分type-{0}【0：编辑；1：新增子节点权限】，权限--permission-{1}", type,
            permission);
        try {
            if (null != permission) {
                Date date = new Date();
                if (0 == type) {
                    permission.setUpdateTime(date);
                    //编辑权限
                    this.authService.updatePerm(permission);
                } else if (1 == type) {
                    permission.setInsertTime(date);
                    //增加子节点权限
                    this.authService.addPermission(permission);
                }
                LoggerUtil.debug(logger, "设置权限成功！-permission-{0}", permission);
                return "ok";
            }
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.error(logger, "设置权限异常！{0}", e);
        }
        return "设置权限出错，请您稍后再试";
    }

    /**
     * 删除权限
     * @param id
     * @return
     */
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public @ResponseBody String del(@RequestParam("id") int id) {
        LoggerUtil.debug(logger, "删除权限--id-{0}", id);
        try {
            if (id > 0) {
                return this.authService.delPermission(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LoggerUtil.error(logger, "删除权限异常！{0}", e);
        }
        return "删除权限出错，请您稍后再试";
    }

    /**
     * 获取权限
     * @param id
     * @return
     */
    @RequestMapping(value = "/getPerm", method = RequestMethod.GET)
    public @ResponseBody Permission getPerm(@RequestParam("id") int id) {
        LoggerUtil.debug(logger, "获取权限--id-{0}", id);
        try {
            if (id > 0) {
                Permission perm = this.authService.getPermission(id);
                LoggerUtil.debug(logger, "获取权限成功！-permission-{0}" + perm);
                return perm;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取权限异常！", e);
        }
        return null;
    }
}
