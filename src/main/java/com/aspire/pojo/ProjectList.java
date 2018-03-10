package com.aspire.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class ProjectList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3547669237083863226L;

	   private Integer projectId;
	   private Integer userId;
	   private String projectName;
	   private String appProjectName;
	   private String installationName;
	   private String appName;
	   private String appIcon;
	   private String scheme;
	   private Integer versioncode;
	   private String creater;
	   private String lastupdated;
	   private Date lastupdatedTime;
	   private List<PageList> pageLists;
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getAppProjectName() {
		return appProjectName;
	}
	public void setAppProjectName(String appProjectName) {
		this.appProjectName = appProjectName;
	}
	public String getInstallationName() {
		return installationName;
	}
	public void setInstallationName(String installationName) {
		this.installationName = installationName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public Integer getVersioncode() {
		return versioncode;
	}
	public void setVersioncode(Integer versioncode) {
		this.versioncode = versioncode;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getLastupdated() {
		return lastupdated;
	}
	public void setLastupdated(String lastupdated) {
		this.lastupdated = lastupdated;
	}
	public Date getLastupdatedTime() {
		return lastupdatedTime;
	}
	public void setLastupdatedTime(Date lastupdatedTime) {
		this.lastupdatedTime = lastupdatedTime;
	}
	public List<PageList> getPageLists() {
		return pageLists;
	}
	public void setPageLists(List<PageList> pageLists) {
		this.pageLists = pageLists;
	}
	@Override
	public String toString() {
		return "ProjectList [projectId=" + projectId + ", userId=" + userId + ", projectName=" + projectName
				+ ", appProjectName=" + appProjectName + ", installationName=" + installationName + ", appName="
				+ appName + ", appIcon=" + appIcon + ", scheme=" + scheme + ", versioncode=" + versioncode
				+ ", creater=" + creater + ", lastupdated=" + lastupdated + ", lastupdatedTime=" + lastupdatedTime
				+ ", pageLists=" + pageLists + "]";
	}


	
	
}
