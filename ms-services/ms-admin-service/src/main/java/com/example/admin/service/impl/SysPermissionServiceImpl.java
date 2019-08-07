package com.example.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.admin.entity.SysPermission;
import com.example.admin.mapper.SysPermissionMapper;
import com.example.admin.service.SysPermissionService;
import com.example.common.core.entity.PermissionEntityVO;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    @Transactional
    @Override
    public void updateSysPermission(List<PermissionEntityVO> permission) {
        Optional<String> serviceId = permission
                .stream()
                .filter(vo -> StrUtil.isNotBlank(vo.getServiceId()))
                .map(PermissionEntityVO::getServiceId).findFirst();
        serviceId.ifPresent(id -> {
            List<String> permissions = baseMapper.selectList(Wrappers.<SysPermission>query().lambda().eq(SysPermission::getServiceId,id))
                    .stream()
                    .map(SysPermission::getPermission)
                    .collect(Collectors.toList());
            List<SysPermission> sysPermissions = Lists.newArrayList();
            if(CollUtil.isNotEmpty(permissions)){
                permission
                        .stream()
                        .filter(p -> !permissions.contains(p.getPermission()))
                        .collect(Collectors.toList())
                        .forEach(permissionEntityVO -> {
                            SysPermission sysPermission = new SysPermission();
                            BeanUtils.copyProperties(permissionEntityVO,sysPermission);
                            sysPermissions.add(sysPermission);
                        });
                sysPermissions.forEach(sys -> baseMapper.insert(sys));
            }else {
                permission.forEach(sys -> {
                    SysPermission sysPermission = new SysPermission();
                    BeanUtils.copyProperties(sys,sysPermission);
                    sysPermission.setCreateTime(LocalDateTime.now());
                    sysPermission.setUpdateTime(LocalDateTime.now());
                    baseMapper.insert(sysPermission);
                });
            }
        });
    }
}
