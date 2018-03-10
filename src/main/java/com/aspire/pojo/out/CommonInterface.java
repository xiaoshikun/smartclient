package com.aspire.pojo.out;

import java.io.Serializable;

public class CommonInterface implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8069620586685707667L;
	private Integer projectId;
	private String pagepath;
	private Integer pageid;
	private String caption;
	private String captionTrans;
	private String uitype;
	private String layoutfile;
	private boolean isindex;
	private Integer navigation;
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public String getPagepath() {
		return pagepath;
	}
	public void setPagepath(String pagepath) {
		this.pagepath = pagepath;
	}
	public Integer getPageid() {
		return pageid;
	}
	public void setPageid(Integer pageid) {
		this.pageid = pageid;
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
	public String getUitype() {
		return uitype;
	}
	public void setUitype(String uitype) {
		this.uitype = uitype;
	}
	public String getLayoutfile() {
		return layoutfile;
	}
	public void setLayoutfile(String layoutfile) {
		this.layoutfile = layoutfile;
	}
	public boolean isIsindex() {
		return isindex;
	}
	public void setIsindex(boolean isindex) {
		this.isindex = isindex;
	}
	public Integer getNavigation() {
		return navigation;
	}
	public void setNavigation(Integer navigation) {
		this.navigation = navigation;
	}
	@Override
	public String toString() {
		return "CommonInterface [projectId=" + projectId + ", pagepath=" + pagepath + ", pageid=" + pageid
				+ ", caption=" + caption + ", captionTrans=" + captionTrans + ", uitype=" + uitype + ", layoutfile="
				+ layoutfile + ", isindex=" + isindex + ", navigation=" + navigation + "]";
	}




	
}
