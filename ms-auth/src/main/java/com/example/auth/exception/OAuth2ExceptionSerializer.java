package com.example.auth.exception;

import cn.hutool.http.HttpStatus;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.SneakyThrows;

/**
 * @Description //TODO 认证异常格式化$
 * @Date 21:33
 * @Author yzcheng90@qq.com
 **/
public class OAuth2ExceptionSerializer extends StdSerializer<CustomOAuth2Exception> {

    public OAuth2ExceptionSerializer() {
        super(CustomOAuth2Exception.class);
    }

    @Override
    @SneakyThrows
    public void serialize(CustomOAuth2Exception value, JsonGenerator gen, SerializerProvider provider) {
        gen.writeStartObject();
        gen.writeObjectField("code", HttpStatus.HTTP_INTERNAL_ERROR);
        gen.writeStringField("msg", value.getMessage());
        gen.writeStringField("data", value.getErrorType());
        gen.writeEndObject();
    }
}
