package com.aspire.component.horizScrollbanner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
@JsonInclude(Include.NON_NULL)
public class Banner implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8143865312662231121L;
	private String picurl;
	private String contenturl;
	public String getPicurl() {
		return picurl;
	}
	public void setPicurl(String picurl) {
		this.picurl = picurl;
	}
	public String getContenturl() {
		return contenturl;
	}
	public void setContenturl(String contenturl) {
		this.contenturl = contenturl;
	}
}
