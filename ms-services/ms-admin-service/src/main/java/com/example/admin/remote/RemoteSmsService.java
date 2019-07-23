package com.example.admin.remote;

import com.example.admin.remote.fallback.RemoteSmsFallBack;
import com.example.common.core.constants.SecurityConstants;
import com.example.common.core.constants.ServiceNameConstants;
import com.example.common.core.entity.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author czx
 * @title: RemoteSmsService
 * @projectName ms
 * @description: TODO
 * @date 2019/7/2310:00
 */
@FeignClient(contextId = "remoteSmsService", value = ServiceNameConstants.MS_SMS_SERVICE, fallback = RemoteSmsFallBack.class)
public interface RemoteSmsService {

    @RequestMapping(value = "/sms/hello", method = RequestMethod.GET)
    R hello(@RequestHeader(SecurityConstants.SECRET_KEY) String secretKey);
}
