package com.example.auth.utils;

import cn.hutool.core.util.StrUtil;
import com.example.common.core.constants.CommonConstants;
import com.example.common.core.constants.SecurityConstants;
import com.example.common.resource.entity.SysUser;
import com.example.common.resource.entity.UserInfo;
import com.example.common.resource.entity.CustomUserDetailsUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description //TODO 转换userDetails工具类$
 * @Date 21:22
 * @Author yzcheng90@qq.com
 **/
public class UserDetailsUtils {

    /**
     * 系统用户转换userDetails
     * @param result 用户信息
     * @return
     */
    public static UserDetails getUserDetails(SysUser result) {
        if (result == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        UserInfo info = UserInfo.builder().user(result).build();
        Set<String> roles = new HashSet<>();
        Collection<? extends GrantedAuthority> authorities  = AuthorityUtils.createAuthorityList(roles.toArray(new String[]{"USER_ROLE"}));
        SysUser user = info.getUser();
        boolean enabled = StrUtil.equals(user.getLockFlag(), CommonConstants.STATUS_NORMAL);

        // 构造security用户
        return new CustomUserDetailsUser(
                user.getUserId(),
                user.getLimitLevel(),
                user.getUsername(),
                SecurityConstants.BCRYPT + user.getPassword(),
                enabled,
                true,
                true,
                !CommonConstants.STATUS_LOCK.equals(user.getLockFlag()),
                authorities);
    }
}
