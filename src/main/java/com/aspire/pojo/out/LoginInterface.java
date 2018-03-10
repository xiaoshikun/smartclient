package com.aspire.pojo.out;

import java.io.Serializable;

public class LoginInterface implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4292338710443160586L;
	private String pagepath;
	private String loginurl;
	private String jumpurl;
	private String layout;
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
	public String getLayout() {
		return layout;
	}
	public void setLayout(String layout) {
		this.layout = layout;
	}
	@Override
	public String toString() {
		return "LoginInterface [ pagepath=" + pagepath + ", loginurl=" + loginurl + ", jumpurl="
				+ jumpurl + ", layout=" + layout + "]";
	}
	
}
