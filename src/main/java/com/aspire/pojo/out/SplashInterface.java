package com.aspire.pojo.out;

import java.io.Serializable;

public class SplashInterface implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7667946097328002342L;
	private String splashimage;
	private long splashtime;
	private String jumpurl;
	public String getSplashimage() {
		return splashimage;
	}
	public void setSplashimage(String splashimage) {
		this.splashimage = splashimage;
	}
	public long getSplashtime() {
		return splashtime;
	}
	public void setSplashtime(long splashtime) {
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
		return "SplashInterface [ splashimage=" + splashimage + ", splashtime=" + splashtime
				+ ", jumpurl=" + jumpurl + "]";
	}
	
	
}
