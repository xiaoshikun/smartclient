package com.aspire.pojo;

import java.io.Serializable;

public class PackageList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1882977475889386402L;
	private Integer packageId;
	private Integer projectId;
	private Integer userId;
	private String apkName;
	private String projectName;
	private String creater;
	private String lastModifier;
	private String packTime;
	private String androidDownloadUrl;
	private String iosDownloadUrl;
	public Integer getPackageId() {
		return packageId;
	}
	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}
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
	public String getApkName() {
		return apkName;
	}
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getLastModifier() {
		return lastModifier;
	}
	public void setLastModifier(String lastModifier) {
		this.lastModifier = lastModifier;
	}
	public String getPackTime() {
		return packTime;
	}
	public void setPackTime(String packTime) {
		this.packTime = packTime;
	}
	public String getAndroidDownloadUrl() {
		return androidDownloadUrl;
	}
	public void setAndroidDownloadUrl(String androidDownloadUrl) {
		this.androidDownloadUrl = androidDownloadUrl;
	}
	public String getIosDownloadUrl() {
		return iosDownloadUrl;
	}
	public void setIosDownloadUrl(String iosDownloadUrl) {
		this.iosDownloadUrl = iosDownloadUrl;
	}
	@Override
	public String toString() {
		return "PackageList [packageId=" + packageId + ", projectId=" + projectId + ", userId=" + userId + ", apkName="
				+ apkName + ", projectName=" + projectName + ", creater=" + creater + ", lastModifier=" + lastModifier
				+ ", packTime=" + packTime + ", androidDownloadUrl=" + androidDownloadUrl + ", iosDownloadUrl="
				+ iosDownloadUrl + "]";
	}
	
	
	
	
}
