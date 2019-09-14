package com.example.auth.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auth.service.SysPermissionService;
import com.example.common.core.base.AbstractController;
import com.example.common.core.entity.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description //TODO $
 * @Date 14:21
 * @Author yzcheng90@qq.com
 **/
@RequestMapping("/permission")
@RestController
@AllArgsConstructor
public class PermissionController extends AbstractController {

    private final SysPermissionService sysPermissionService;

    @RequestMapping(value = "list",method = RequestMethod.GET)
    public R list(Page page){
        return R.ok().setData(sysPermissionService.page(page));
    }
}
