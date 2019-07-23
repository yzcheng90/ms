package com.example.service.sms.controller;

import com.example.common.core.base.AbstractController;
import com.example.common.core.entity.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author czx
 * @title: SmsController
 * @projectName ms
 * @description: TODO
 * @date 2019/7/239:51
 */
@RestController
@RequestMapping("/sms")
public class SmsController extends AbstractController {

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public R hello(){
        return R.builder().message("sms的hello").build();
    }
}
