package com.aspire.pojo.app;

import java.io.Serializable;

/**
 * 个人用户动态登录请求客户端
 *
 * @author xiaos
 * @create 2018-02-02-17:53
 */
public class JSONIndivUserDynLoginReq implements Serializable{

    /**
     * 手机号码
     */
    private String mobilePhone;
    /**
     * 验证码
     */
    private String code;

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
