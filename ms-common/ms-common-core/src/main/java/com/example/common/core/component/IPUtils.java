package com.example.common.core.component;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.example.common.core.entity.IPEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author czx
 * @title: IPUtils
 * @projectName ms
 * @description: TODO
 * @date 2019/8/2811:50
 */
@Slf4j
public class IPUtils {

    public static String url = "http://ip.taobao.com/service/getIpInfo.php?ip=";
    private static ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 获取IP地址
     *
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StrUtil.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StrUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("IPUtils ERROR ", e);
        }

//        //使用代理，则获取第一个IP地址
//        if(StringUtils.isEmpty(ip) && ip.length() > 15) {
//			if(ip.indexOf(",") > 0) {
//				ip = ip.substring(0, ip.indexOf(","));
//			}
//		}

        return ip;
    }

    public static String getAddressDetail(String ip){
        String address = null;
        if(StrUtil.isNotBlank(ip)){
            String result = HttpUtil.get(url+ip);
            if(StrUtil.isNotBlank(result)){
                try {
                    IPEntity ipEntity = objectMapper.readValue(result, IPEntity.class);
                    if(ipEntity != null && ipEntity.getCode() == 0 && ipEntity.getData() != null){
                        address = ipEntity.getData().getCountry()
                                + " | "
                                + ipEntity.getData().getCity()
                                + " | "
                                + ipEntity.getData().getIsp();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return address;
    }
}
