package com.example.common.resource.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

/**
 * @Description //TODO 认证后返回的用户信息$
 * @Date 20:49
 * @Author yzcheng90@qq.com
 **/
public class CustomUserDetailsUser extends User {

    @Getter
    @Setter
    public Integer userId;
    /**
     * 限流等级
     */
    @Getter
    @Setter
    private Integer limitLevel;

    /**
     *
     * @param userId
     * @param limitLevel
     * @param username
     * @param password
     * @param enabled
     * @param accountNonExpired
     * @param credentialsNonExpired
     * @param accountNonLocked
     * @param authorities
     */
    public CustomUserDetailsUser(Integer userId,Integer limitLevel, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
        this.limitLevel = limitLevel;
    }
}
