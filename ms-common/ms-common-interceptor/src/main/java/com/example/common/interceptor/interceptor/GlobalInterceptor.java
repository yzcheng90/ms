package com.example.common.interceptor.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.example.common.core.constants.SecurityConstants;
import com.example.common.core.entity.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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

    private RedisTemplate redisTemplate;

    public GlobalInterceptor(RedisTemplate<String,Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        String secretKey = request.getHeader(SecurityConstants.SECRET_KEY);
        if(StrUtil.isNotBlank(secretKey)){
            String key = (String) redisTemplate.opsForValue().get(SecurityConstants.SECRET_KEY);
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
