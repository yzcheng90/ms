package com.example.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.admin.remote.RemoteSmsService;
import com.example.admin.service.SysUserService;
import com.example.common.core.base.AbstractController;
import com.example.common.core.constants.CommonConstants;
import com.example.common.core.entity.R;
import com.example.common.resource.annotation.ResourcePermission;
import com.example.common.user.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends AbstractController {

    @Value("${server.port}")
    private String port;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RemoteSmsService remoteSmsService;

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private int count = 0;

    @RequestMapping(value = "/getUser/{id}", method = RequestMethod.GET)
    public R getUser(@PathVariable("id") Integer id) {
        count++;
        log.info("===================次数：{},port：{}=================", count, port);
        return R.ok(sysUserService.getUserById(id));
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.GET)
    public R createUser() {
        SysUser user = new SysUser();
        user.setUsername("test" + count);
        user.setPassword(ENCODER.encode("123456"));
        user.setDelFlag(CommonConstants.STATUS_NORMAL);
        user.setLimitLevel(Integer.valueOf(CommonConstants.DEFAULT_LEVEL));
        sysUserService.save(user);
        count++;
        return R.builder().data(user).build();
    }

    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    public R getList(Page page) {
        return new R(sysUserService.page(page));
    }

    @ResourcePermission("调用sms服务")
    @RequestMapping(value = "/sms",method = RequestMethod.GET)
    public R getSms(){
        return remoteSmsService.hello();
    }
}
