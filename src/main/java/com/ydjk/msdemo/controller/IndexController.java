package com.ydjk.msdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/")
public class IndexController {
    @RequestMapping
    public String demo() {
        return "index"; // 地址指向index.hml
    }
}
