package com.example.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.auth.entity.SysPermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    @Select({
            "<script>",
            "select",
            "*",
            "from sys_permission",
            "where id in",
            "( select permission_id from sys_role_permission where role_id in",
            "<foreach collection='roleIds' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            ")",
            "</script>"
    })
    List<SysPermission> getPermission(@Param("roleIds") List<Long> roleIds);
}
