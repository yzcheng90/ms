package com.example.admin.controller;

import com.example.admin.service.SysPermissionService;
import com.example.common.core.base.AbstractController;
import com.example.common.core.entity.R;
import com.example.common.resource.entity.PermissionEntityVO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description //TODO $
 * @Date 2019/8/6 23:07
 * @Author yzcheng90@qq.com
 **/
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/sys/permission")
public class SysPermissionController extends AbstractController {

    private final SysPermissionService sysPermissionService;

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public R update(List<PermissionEntityVO> permissionEntityVO){
        sysPermissionService.updateSysPermission(permissionEntityVO);
        return R.ok();
    }

}
