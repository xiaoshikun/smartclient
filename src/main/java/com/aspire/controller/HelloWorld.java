package com.aspire.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaos
 * @create 2018-02-02-15:03
 */
@RestController
public class HelloWorld {

    @RequestMapping("/hello")
    public String hello(){
        return "hello spring boot!";
    }
}
