package com.example.common.core.component;

/**
 * @author czx
 * @title: URLConvertUtil
 * @projectName ms
 * @description: TODO rest url 转驼峰
 * @date 2019/8/29:54
 */
public class URLConvertUtil {

    private static final char UNDERLINE = '/';
    private static final String INDEX = "/{";

    public static String convert(String url) {
        if(url.contains(INDEX)){
            url = url.substring(0,url.indexOf(INDEX));
        }
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<url.length();i++){
            char c = url.charAt(i);
            if(i == 0){
                if(c == UNDERLINE){
                    sb.append(Character.toUpperCase(url.charAt(i+1)));
                    i++;
                }else {
                    sb.append(Character.toUpperCase(url.charAt(i)));
                }
            }else {
                if(c == UNDERLINE){
                    sb.append(Character.toUpperCase(url.charAt(i+1)));
                    i++;
                }else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static String capture(String url) {
        if(url.contains(INDEX)){
            url = url.substring(0,url.indexOf(INDEX));
        }
        return url + "/**";
    }

    public static void main(String[] args) {
        String str = convert("/user/sms");
        System.out.println("----------"+str);
        String str1 = convert("/user/sms/{id}");
        System.out.println("----------"+str1);
    }

}
