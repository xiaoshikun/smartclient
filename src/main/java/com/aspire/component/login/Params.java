package com.aspire.component.login;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 这是一个登陆控件模板
 *
 * @author xiaos
 * @create 2018-03-09-17:51
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Params implements Serializable{
    private String pagepath;
    private String loginurl;
    private String jumpurl;

    public String getPagepath() {
        return pagepath;
    }

    public void setPagepath(String pagepath) {
        this.pagepath = pagepath;
    }

    public String getLoginurl() {
        return loginurl;
    }

    public void setLoginurl(String loginurl) {
        this.loginurl = loginurl;
    }

    public String getJumpurl() {
        return jumpurl;
    }

    public void setJumpurl(String jumpurl) {
        this.jumpurl = jumpurl;
    }

    @Override
    public String toString() {
        return "Params{" +
                "pagepath='" + pagepath + '\'' +
                ", loginurl='" + loginurl + '\'' +
                ", jumpurl='" + jumpurl + '\'' +
                '}';
    }
}
