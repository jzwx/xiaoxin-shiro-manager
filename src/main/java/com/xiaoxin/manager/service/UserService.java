package com.xiaoxin.manager.service;

import com.xiaoxin.manager.entity.UserRolesVO;
import com.xiaoxin.manager.entity.UserSearchDTO;
import com.xiaoxin.manager.pojo.User;
import com.xiaoxin.manager.utils.PageDataResult;

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

    /**
     * 分页查询用户列表
     * @param page
     * @param limit
     * @return
     */
    PageDataResult getUsers(UserSearchDTO userSearch, int page, int limit);

    /**
     * 查询用户数据
     * @param id
     * @return
     */
    UserRolesVO getUserAndRoles(Integer id);

    /**
     *	设置用户【新增或更新】
     * @param user
     * @param roleIds
     * @return
     */
    String setUser(User user, String roleIds);

    /**
     * 设置用户是否离职
     * @param id
     * @param isJob
     * @param insertUid
     * @return
     */
    String setJobUser(Integer id, Integer isJob,Integer insertUid,Integer version);

    /**
     * 删除用户
     * @param id
     * @param isDel
     * @return
     */
    String setDelUser(Integer id, Integer isDel,Integer insertUid,Integer version);


}
