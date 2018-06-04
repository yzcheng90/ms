package com.ski.ms.common.controller;

import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import com.ski.ms.common.service.SysUserService;
import com.ski.ms.lib.config.FdfsPropertiesConfig;
import com.ski.ms.lib.entity.UserEntity;
import com.xiaoleilu.hutool.io.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 公共用户类
 * Created by czx on 2018/4/24.
 */
@Slf4j
@Api(value = "API - UserController", description = "公共用户接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private FdfsPropertiesConfig fdfsPropertiesConfig;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    /**
     * 通过ID查询当前用户信息
     *
     * @param id ID
     * @return 用户信息
     */
    @ApiOperation(value = "用户信息", notes = "通过ID查询当前用户信息", response = UserEntity.class)
    @GetMapping("/{id}")
    public UserEntity user(@PathVariable Integer id) {
        return sysUserService.selectUserEntityById(id);
    }


    /**
     * 通过用户名查询用户及其角色信息
     *
     * @param username 用户名
     * @return UserEntity 对象
     */
    @ApiOperation(value = "用户名查询", notes = "通过用户名查询用户及其角色信息", response = UserEntity.class)
    @GetMapping("/findUserByUsername/{username}")
    public UserEntity findUserByUsername(@PathVariable String username) {
        return sysUserService.selectUserEntityByUsername(username);
    }

    /**
     * 通过用户名查询用户及其角色信息
     *
     * @param username 用户名
     * @return UserEntity 对象
     */
    @ApiOperation(value = "用户名查询", notes = "通过用户名查询用户及其角色信息", response = UserEntity.class)
    @GetMapping("/findUserByUsername_v2/{username}")
    public UserEntity findUserByUsername_v2(@PathVariable String username) {
        return sysUserService.selectUserEntityByUsername_v2(username);
    }

    /**
     * 通过手机号查询用户及其角色信息
     *
     * @param mobile 手机号
     * @return UserEntity 对象
     */
    @ApiOperation(value = "用户手机号码查询", notes = "通过手机号查询用户及其角色信息", response = UserEntity.class)
    @GetMapping("/findUserByMobile/{mobile}")
    public UserEntity findUserByMobile(@PathVariable String mobile) {
        UserEntity entity = sysUserService.selectUserEntityByMobile(mobile);
        return entity;
    }

    /**
     * 上传用户头像
     * (多机部署有问题，建议使用独立的文件服务器)
     *  搭建单机版的FastDFS服务器 https://blog.csdn.net/u012453843/article/details/69951920
     * @param file 资源
     * @return filename map
     */
    @PostMapping("/upload")
    public Map<String, String> upload(@RequestParam("file") MultipartFile file) {
        String fileExt = FileUtil.extName(file.getOriginalFilename());
        Map<String, String> resultMap = new HashMap<>(1);
        try {
            StorePath storePath = fastFileStorageClient.uploadFile(file.getBytes(), fileExt);
            resultMap.put("filename", fdfsPropertiesConfig.getFileHost() + storePath.getFullPath());
        } catch (IOException e) {
            log.error("文件上传异常", e);
            throw new RuntimeException(e);
        }
        return resultMap;
    }

}
