package com.aspire.service;

import com.aspire.mapper.UserMapper;
import com.aspire.pojo.User;
import com.aspire.util.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author xiaos
 * @create 2018-02-02-16:20
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    //查询用户信息
    public ResultJson getUserInfo() throws Exception{
        List<User> users = userMapper.findAllUserInfo();
        return new ResultJson(users);
    }

    /**
     * 用户登录smartclient
     * @param username
     * @param password
     * @return
     */
    public ResultJson checkLogin(String username, String password) throws Exception{
        if(null==username||username.trim().isEmpty()){
            return ResultJson.build(1,"请求用户名为空");
        }
        if(null==password||password.trim().isEmpty()){
            return ResultJson.build(1,"请求密码为空");
        }
        //判断用户名
        User user = userMapper.findByName(username);
        if(null==user){
            return ResultJson.build(1,"用户名不存在");
        }
        //判断密码
        if(!password.equals(user.getPassword())){
            return ResultJson.build(1,"密码错误");
        }
        //将用户输入的明文加密
        //登录成功
        user.setPassword(null);//把密码屏蔽不返回

        return ResultJson.oK(user);
}

}
