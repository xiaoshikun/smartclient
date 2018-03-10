package com.aspire.pojo.app;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信验证码对象
 * @author liyonglun
 *
 */
public class SMSVerifyCodeVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 接收者手机号
	 */
	private String mobilePhone;
	/**
	 * 验证码
	 */
	private String verifyCode;
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
