package com.aspire.pojo.app;

import com.aspire.util.ResultJson;

import java.io.Serializable;

/**
 * 返回客户端个人用户登录封装返回信息
 *
 * @author xiaos
 * @create 2018-02-02-17:55
 */
public class ResultAppLoginMessage implements Serializable {
    private  int status;
    private String message;


    public  ResultAppLoginMessage(){

    }

    public static ResultAppLoginMessage build(Integer status, String message) {
        return new ResultAppLoginMessage(status, message);
    }

    public ResultAppLoginMessage(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResultAppLoginMessage(int status) {
        this.status = status;
        this.message = "OK";
    }


    public Boolean isOk() {
        return this.status == 200;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
