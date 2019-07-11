package com.example.admin.controller;

import com.example.admin.service.SysUserService;
import com.example.common.resource.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {

    @Value("${server.port}")
    private String port;

    @Autowired
    private SysUserService sysUserService;

    private int count = 0;

    @RequestMapping(value = "/getUser/{id}",method = RequestMethod.GET)
    public SysUser getUser(@PathVariable("id") Integer id){
        count ++;
        log.info("===================次数：{},port：{}=================",count,port);
        return sysUserService.getUserById(id);
    }

}
