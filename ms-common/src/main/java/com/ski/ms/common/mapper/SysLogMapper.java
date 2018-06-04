package com.ski.ms.common.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ski.ms.lib.entity.SysLog;
import org.apache.ibatis.annotations.Mapper;

/**
  * 日志表 Mapper 接口
  *  Created by czx on 2018/4/24.
  */
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {

}