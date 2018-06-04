package com.ski.ms.common.service.impl;

import com.baomidou.dynamic.datasource.DS;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ski.ms.common.mapper.SysUserMapper;
import com.ski.ms.common.model.SysUser;
import com.ski.ms.common.service.SysUserService;
import com.ski.ms.lib.constant.CommonConstant;
import com.ski.ms.lib.constant.MqQueueConstant;
import com.ski.ms.lib.constant.SecurityConstant;
import com.ski.ms.lib.entity.UserEntity;
import com.ski.ms.lib.utils.MobileMsgTemplate;
import com.ski.ms.lib.utils.R;
import com.xiaoleilu.hutool.collection.CollectionUtil;
import com.xiaoleilu.hutool.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by czx on 2018/4/24.
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    //@Cacheable(value = "user_details", key = "#username")
    public UserEntity selectUserEntityByUsername(String username) {
        log.info("--------------------------这是V1库的请求selectUserEntityByUsername");
        return sysUserMapper.selectUserEntityByUsername(username);
    }

    @DS("one")
    @Override
    //@Cacheable(value = "user_details_v2", key = "#username")
    public UserEntity selectUserEntityByUsername_v2(String username) {
        log.info("--------------------------这是V2库的请求selectUserEntityByUsername_v2");
        return sysUserMapper.selectUserEntityByUsername(username);
    }

    /**
     * 通过手机号查询用户信息
     * @param mobile 手机号
     * @return 用户信息
     */
    @Override
    //@Cacheable(value = "user_details_mobile", key = "#mobile")
    public UserEntity selectUserEntityByMobile(String mobile) {
        log.info("--------------------------这是V1库的请求selectUserEntityByMobile");
        return sysUserMapper.selectUserEntityByMobile(mobile);
    }




    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public UserEntity selectUserEntityById(Integer id) {
        return sysUserMapper.selectUserEntityById(id);
    }

    /**
     * 保存用户验证码，和randomStr绑定
     *
     * @param randomStr 客户端生成
     * @param imageCode 验证码信息
     */
    @Override
    public void saveImageCode(String randomStr, String imageCode) {
        redisTemplate.opsForValue().set(SecurityConstant.DEFAULT_CODE_KEY + randomStr, imageCode, SecurityConstant.DEFAULT_IMAGE_EXPIRE, TimeUnit.SECONDS);
        Object codeObj = redisTemplate.opsForValue().get(SecurityConstant.DEFAULT_CODE_KEY + randomStr);
        log.info("缓存的key:{}验证码是：{}",SecurityConstant.DEFAULT_CODE_KEY + randomStr,codeObj);
    }

    /**
     * 发送验证码
     * <p>
     * 1. 先去redis 查询是否 60S内已经发送
     * 2. 未发送： 判断手机号是否存 ? false :产生4位数字  手机号-验证码
     * 3. 发往消息中心-》发送信息
     * 4. 保存redis
     *
     * @param mobile 手机号
     * @return true、false
     */
    @Override
    public R<Boolean> sendSmsCode(String mobile) {
        Object tempCode = redisTemplate.opsForValue().get(SecurityConstant.DEFAULT_CODE_KEY + mobile);
        if (tempCode != null) {
            log.error("用户:{}验证码未失效{}", mobile, tempCode);
            return new R<>(false, "验证码未失效，请失效后再次申请");
        }

        SysUser params = new SysUser();
        params.setMobile(mobile);
        List<SysUser> userList = this.selectList(new EntityWrapper<>(params));

        if (CollectionUtil.isEmpty(userList)) {
            log.error("根据用户手机号{}查询用户为空", mobile);
            return new R<>(false, "手机号不存在");
        }

        String code = RandomUtil.randomNumbers(6);
        log.info("短信发送请求消息中心 -> 手机号:{} -> 验证码：{}", mobile, code);

        /**
         * 使用MQ去发送信息
         * 参数1  发送类型
         * 参数2  发送消息对象
         */
        rabbitTemplate.convertAndSend(MqQueueConstant.MOBILE_CODE_QUEUE, new MobileMsgTemplate(mobile, code, CommonConstant.ALIYUN_SMS));
        //redis 缓存
        redisTemplate.opsForValue().set(SecurityConstant.DEFAULT_CODE_KEY + mobile, code, SecurityConstant.DEFAULT_IMAGE_EXPIRE, TimeUnit.SECONDS);
        return new R<>(true);
    }
}
