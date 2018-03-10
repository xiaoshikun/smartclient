package com.aspire.pojo.app;

import java.io.Serializable;

/**
 * 个人用户注册验证码请求
 *
 * @author xiaos
 * @create 2018-02-02-17:51
 */
public class JSONGetIndivUserRegisterVerifyCodeReq implements Serializable{

    /**
     * 短信下发的目的号码
     */
    private String msisdn;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
}
