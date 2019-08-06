package com.example.common.resource.remote.fallback;

import com.example.common.core.entity.R;
import com.example.common.resource.entity.PermissionEntityVO;
import com.example.common.resource.remote.RemotePermissionService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author czx
 * @title: RemoteSmsFallBack
 * @projectName ms
 * @description: TODO 熔错返回
 * @date 2019/7/2310:45
 */
@Slf4j
public class RemotePermissionFallBack implements RemotePermissionService {

    @Override
    public R update(List<PermissionEntityVO> permissionEntityVO) {
        log.error("==========RemotePermissionFallBack:服务不可用=======");
        return R.error("服务不可用");
    }
}
