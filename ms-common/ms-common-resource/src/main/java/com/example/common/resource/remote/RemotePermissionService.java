package com.example.common.resource.remote;
import com.example.common.core.constants.ServiceNameConstants;
import com.example.common.core.entity.R;
import com.example.common.resource.entity.PermissionEntityVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author czx
 * @title: RemoteSmsService
 * @projectName ms
 * @description: TODO
 * @date 2019/7/2310:00
 */
@FeignClient(value = ServiceNameConstants.MS_ADMIN_SERVICE, fallback = RemotePermissionService.class)
public interface RemotePermissionService {

    @RequestMapping(value = "/sys/permission/update", method = RequestMethod.GET)
    R update(List<PermissionEntityVO> permissionEntityVO);
}
