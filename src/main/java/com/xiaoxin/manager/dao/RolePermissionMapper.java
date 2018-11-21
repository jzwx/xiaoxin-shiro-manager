package com.xiaoxin.manager.dao;

import com.xiaoxin.manager.pojo.RolePermission;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RolePermissionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role_permission
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(@Param("permitId") Integer permitId, @Param("roleId") Integer roleId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role_permission
     *
     * @mbggenerated
     */
    int insert(RolePermission record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table role_permission
     *
     * @mbggenerated
     */
    List<RolePermission> selectAll();

    List<RolePermission> findByRole(int roleId);

    int deleteByPrimary(RolePermission key);
}