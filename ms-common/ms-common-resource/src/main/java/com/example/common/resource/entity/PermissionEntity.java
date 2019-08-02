package com.example.common.resource.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author czx
 * @title: PermissionEntity
 * @projectName ms
 * @description: TODO 权限对象
 * @date 2019/8/29:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PermissionEntity {
    private String name;
    private String permission;
    private String url;
}
