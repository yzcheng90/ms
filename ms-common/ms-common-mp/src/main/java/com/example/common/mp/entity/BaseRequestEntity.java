package com.example.common.mp.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author czx
 * @Description //TODO 默认参数 不影响数据库表字段，也不做为数据返回给前台
 * @Date 11:19 2019/5/10
 * @Param
 * @return
 **/
public abstract class BaseRequestEntity<T extends Model> extends Model<T> {

    @Getter
    @Setter
    @TableField(exist = false)
    //@JSONField(serialize=false)
    private int page = 1;

    @Getter
    @Setter
    @TableField(exist = false)
    //@JSONField(serialize=false)
    private int limit = 10;

    @Getter
    @Setter
    @TableField(exist = false)
    //@JSONField(serialize=false)
    private String keyword;

    @Getter
    @Setter
    @TableField(exist = false)
    //@JSONField(serialize=false)
    private String userId;

}
