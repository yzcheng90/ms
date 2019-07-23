package com.example.common.core.entity;

import cn.hutool.http.HttpStatus;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description //TODO 返回参数包装类$
 * @Date 20:26
 * @Author yzcheng90@qq.com
 **/

@Builder
@ToString
@Accessors(chain = true)
@AllArgsConstructor
public class R<T> implements Serializable {

    @Getter
    @Setter
    public int code = HttpStatus.HTTP_OK;

    @Getter
    @Setter
    public String message = "success";

    @Getter
    @Setter
    public T data;

    public R() {
        super();
    }

    public R(T data) {
        super();
        this.data = data;
    }

    public R(T data, String message) {
        super();
        this.data = data;
        this.message = message;
    }

    public R(Throwable e) {
        super();
        this.message = e.getMessage();
        this.code = HttpStatus.HTTP_INTERNAL_ERROR;
    }

    public static R ok(){
        return R
                .builder()
                .code(HttpStatus.HTTP_OK)
                .message("success")
                .build();
    }

    public static R error(){
        return R
                .builder()
                .code(HttpStatus.HTTP_INTERNAL_ERROR)
                .message("error")
                .build();
    }

    public static R error(String message){
        return R
                .builder()
                .code(HttpStatus.HTTP_INTERNAL_ERROR)
                .message(message)
                .build();
    }

    public static R ok(Object data){
        return R
                .builder()
                .code(HttpStatus.HTTP_OK)
                .data(data)
                .message("success")
                .build();
    }
}
