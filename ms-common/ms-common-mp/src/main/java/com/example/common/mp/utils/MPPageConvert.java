package com.example.common.mp.utils;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.mp.entity.BaseRequestEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zdrjy
 * @Description //TODO Page 数据转换
 * @Date 17:13 2019/4/18
 **/
@Component
public class MPPageConvert {

    /**
     * @Author zdrjy
     * @Description //TODO 前台传过来的参数转换为MyBatis Plus的Page
     * @Date 17:14 2019/4/18
     * @Param [param]
     * @return com.baomidou.mybatisplus.core.metadata.IPage<T>
     **/
    public <T> IPage<T> pageParamConvert(BaseRequestEntity param){
        IPage<T> page = new Page<>(param.getPage(),param.getLimit());
        return page;
    }
    /**
     * @Author zdrjy
     * @Description //TODO 前台传过来的参数转换为MyBatis Plus的Page
     * @Date 17:14 2019/4/18
     * @Param [param]
     * @return com.baomidou.mybatisplus.core.metadata.IPage<T>
     **/
    public <T> IPage<T> pageParamConvert(Map<String, Object> param){
        int currPage = 1;
        int limit = 10;
        if(MapUtil.getInt(param,"page") != null){
            currPage = MapUtil.getInt(param,"page");
        }
        if(MapUtil.getInt(param,"limit") != null){
            limit = MapUtil.getInt(param,"limit");
        }
        IPage<T> page = new Page<>(currPage,limit);
        return page;
    }

    /**
     * @Author zdrjy
     * @Description //TODO 将MyBatis Plus 的Page 转换为前台能用的Page
     * @Date 17:14 2019/4/18
     * @Param [page]
     * @return java.util.HashMap
     **/
    public HashMap pageValueConvert(IPage<?> page){
        HashMap<Object,Object> pageData = new HashMap<>();
        pageData.put("list",page.getRecords());
        pageData.put("totalCount",page.getTotal());
        pageData.put("pageSize",page.getSize());
        pageData.put("currPage",page.getCurrent());
        pageData.put("totalPage",page.getPages());
        return pageData;
    }

    public HashMap getDefaultPage(Map<String, Object> param){
        HashMap<Object,Object> pageData = new HashMap<>();
        pageData.put("list",new ArrayList());
        pageData.put("totalCount",1);
        pageData.put("pageSize",MapUtil.getInt(param,"limit"));
        pageData.put("currPage",MapUtil.getInt(param,"page"));
        pageData.put("totalPage",1);
        return pageData;
    }

    public HashMap getDefaultPage(BaseRequestEntity param){
        HashMap<Object,Object> pageData = new HashMap<>();
        pageData.put("list",new ArrayList());
        pageData.put("totalCount",1);
        pageData.put("pageSize",param.getPage());
        pageData.put("currPage",param.getLimit());
        pageData.put("totalPage",1);
        return pageData;
    }
}
