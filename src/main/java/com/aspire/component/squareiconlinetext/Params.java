package com.aspire.component.squareiconlinetext;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class Params implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2902193533309222360L;
	private String title;
	private String subtitle;
	private String comment;
	private String iconurl;
	private String contenturl;
	private boolean rightarrow;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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
	public boolean isRightarrow() {
		return rightarrow;
	}
	public void setRightarrow(boolean rightarrow) {
		this.rightarrow = rightarrow;
	}

}
