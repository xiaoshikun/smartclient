package com.aspire.component.tabnavigator;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class TabSpec implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3404266538794448173L;
	private String label;
	private String caption;
	private String captionTrans;
	private String selectedicon;
	private String unselectedicon;
	private String contenturl;
	public String getLabel() {
		return label;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getCaptionTrans() {
		return captionTrans;
	}
	public void setCaptionTrans(String captionTrans) {
		this.captionTrans = captionTrans;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getSelectedicon() {
		return selectedicon;
	}
	public void setSelectedicon(String selectedicon) {
		this.selectedicon = selectedicon;
	}
	public String getUnselectedicon() {
		return unselectedicon;
	}
	public void setUnselectedicon(String unselectedicon) {
		this.unselectedicon = unselectedicon;
	}
	public String getContenturl() {
		return contenturl;
	}
	public void setContenturl(String contenturl) {
		this.contenturl = contenturl;
	}
	@Override
	public String toString() {
		return "TabSpec [label=" + label + ", caption=" + caption + ", captionTrans=" + captionTrans + ", selectedicon="
				+ selectedicon + ", unselectedicon=" + unselectedicon + ", contenturl=" + contenturl + "]";
	}

	

}
