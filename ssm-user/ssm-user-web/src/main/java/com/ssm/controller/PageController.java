package com.ssm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author brusion
 * @date 2018/9/4
 */
@Controller
public class PageController {

    @RequestMapping(value = "/user")
    public String index() {
        return "/user/main";
    }

    @RequestMapping(value = "/home")
    public String home() {
        return "/domain/home";
    }

}
