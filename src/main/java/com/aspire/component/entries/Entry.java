package com.aspire.component.entries;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
@JsonInclude(Include.NON_NULL)
public class Entry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5314257906951989081L;
	private String label;
	private String iconurl;
	private String contenturl;
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getIconurl() {
		return iconurl;
	}
	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}
	public String getContenturl() {
		return contenturl;
	}
	public void setContenturl(String contenturl) {
		this.contenturl = contenturl;
	}

}
