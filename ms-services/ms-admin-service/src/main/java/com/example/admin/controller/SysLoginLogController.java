package com.example.admin.controller;

import java.util.Map;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.admin.entity.SysLoginLog;
import com.example.admin.service.SysLoginLogService;
import com.example.common.core.base.AbstractController;
import com.example.common.core.entity.R;
import com.example.common.mp.utils.MPPageConvert;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;


/**
 * 登录日志
 * 
 * @author czx
 * @email object_czx@163.com
 * @date 2019-08-15 16:25:10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/log/login")
public class SysLoginLogController  extends AbstractController {
    private final SysLoginLogService sysLoginLogService;
    private final MPPageConvert mpPageConvert;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public R list(@RequestBody SysLoginLog params){
        //查询列表数据
        QueryWrapper<SysLoginLog> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isBlank(params.getKeyword())){
            queryWrapper
                    .lambda()
                    .like(SysLoginLog::getUser_name,params.getUser_name())
                    .or()
                    .like(SysLoginLog::getClient_id,params.getClient_id());
        }
        IPage<SysLoginLog> loginLogIPage = sysLoginLogService.page(mpPageConvert.<SysLoginLog>pageParamConvert(params),queryWrapper);
        return R.ok().setData(loginLogIPage);
    }


    @RequestMapping(value = "/info/{id}",method = RequestMethod.GET)
    public R info(@PathVariable("id") Integer id){
        return R.ok().setData(sysLoginLogService.getById(id));
    }

}
