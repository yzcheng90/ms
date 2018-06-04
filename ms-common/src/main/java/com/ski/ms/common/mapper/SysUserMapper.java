package com.ski.ms.common.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ski.ms.common.model.SysUser;
import com.ski.ms.lib.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户表 Mapper 接口
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 通过用户名查询用户信息（含有角色信息）
     *
     * @param username 用户名
     * @return UserEntity
     */
    UserEntity selectUserEntityByUsername(String username);


    /**
     * 通过手机号查询用户信息（含有角色信息）
     *
     * @param mobile 用户名
     * @return UserEntity
     */
    UserEntity selectUserEntityByMobile(String mobile);


    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     * @return UserEntity
     */
    UserEntity selectUserEntityById(Integer id);
}