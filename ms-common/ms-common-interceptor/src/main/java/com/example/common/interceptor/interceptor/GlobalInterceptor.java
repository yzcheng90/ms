package com.example.common.interceptor.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.common.cache.component.RedisUUID;
import com.example.common.core.constants.SecurityConstants;
import com.example.common.core.entity.R;
import com.example.common.resource.config.AuthIgnoreConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @Description //TODO $全局过滤
 * @Date 21:35
 * @Author yzcheng90@qq.com
 **/
@Slf4j
public class GlobalInterceptor implements HandlerInterceptor {

    @Getter
    @Setter
    private RedisUUID redisUUID;

    @Getter
    @Setter
    private AuthIgnoreConfig authIgnoreConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        long exist =  authIgnoreConfig.getIgnoreUrls().stream().filter(url-> url.trim().equals(request.getRequestURI())).count();
        if(exist != 0){
            return true;
        }
        String secretKey = request.getHeader(SecurityConstants.SECRET_KEY);
        if(StrUtil.isNotBlank(secretKey)){
            String key = (String) redisUUID.get(SecurityConstants.SECRET_KEY);
            if(!StrUtil.isBlank(key) && secretKey.equals(key)){
                return true;
            }
        }
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSONUtil.toJsonStr(R.error("illegal request")));
        return false;
    }
}
