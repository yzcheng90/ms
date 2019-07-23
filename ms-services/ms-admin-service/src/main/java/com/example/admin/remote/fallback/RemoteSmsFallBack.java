package com.example.admin.remote.fallback;

import com.example.admin.remote.RemoteSmsService;
import com.example.common.core.entity.R;
import org.springframework.stereotype.Component;

/**
 * @author czx
 * @title: RemoteSmsFallBack
 * @projectName ms
 * @description: TODO 熔错返回
 * @date 2019/7/2310:45
 */
@Component
public class RemoteSmsFallBack implements RemoteSmsService {

    @Override
    public R hello(String secretKey) {
        return R.error("熔错返回");
    }
}
