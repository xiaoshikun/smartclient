package com.aspire.component.splash;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * 闪屏模板
 *
 * @author xiaos
 * @create 2018-03-09-18:06
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Params implements Serializable {
    private String splashimage;
    private String splashtime;
    private String jumpurl;

    public String getSplashimage() {
        return splashimage;
    }

    public void setSplashimage(String splashimage) {
        this.splashimage = splashimage;
    }

    public String getSplashtime() {
        return splashtime;
    }

    public void setSplashtime(String splashtime) {
        this.splashtime = splashtime;
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
                "splashimage='" + splashimage + '\'' +
                ", splashtime='" + splashtime + '\'' +
                ", jumpurl='" + jumpurl + '\'' +
                '}';
    }
}
