package com.example.auth.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/token")
public class TokenController {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    /**
     * 认证页面
     *
     * @return ModelAndView
     */
    @GetMapping("/login")
    public ModelAndView require() {
        return new ModelAndView("ftl/login");
    }

    @RequestMapping(value = "{password}",method = RequestMethod.GET)
    public String encodePassword(@PathVariable("password") String password){
        return ENCODER.encode(password);
    }

}
