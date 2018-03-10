package com.aspire.controller;

import com.aspire.service.UserService;
import com.aspire.util.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author xiaos
 * @create 2018-02-02-16:44
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping("/get_userinfo.do")
    public ResultJson getUserInfo(){
        try {
            return userService.getUserInfo();
        }catch (Exception e){
            e.printStackTrace();
            return ResultJson.build(1,"获取用户信息出错啦");
        }
    }

    //smartclient用户登录
    @RequestMapping("/login.do")
    public ResultJson login(String username, String password, HttpSession session){
        try {
            ResultJson resultJson =  userService.checkLogin(username,password);
            session.setAttribute("user",resultJson.getData());
            return resultJson;
        }catch (Exception e){
            e.printStackTrace();
            return ResultJson.build(1,"登录出错了");
        }

    }


}
