package com.example.common.core.entity;

import lombok.Data;

/**
 * Created by czx on 2019/8/28.
 */
@Data
public class IPEntity {

    private int code;
    private DataBean data;

    @Data
    public static class DataBean {
        /**
         * ip : 175.6.6.227
         * country : 中国
         * area :
         * region : 湖南
         * city : 长沙
         * county : XX
         * isp : 电信
         * country_id : CN
         * area_id :
         * region_id : 430000
         * city_id : 430100
         * county_id : xx
         * isp_id : 100017
         */
        private String ip;
        private String country;
        private String area;
        private String region;
        private String city;
        private String county;
        private String isp;
        private String country_id;
        private String area_id;
        private String region_id;
        private String city_id;
        private String county_id;
        private String isp_id;
    }
}
