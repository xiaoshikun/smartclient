package com.aspire.pojo.out;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_EMPTY)
public class ProjectListJson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3547669237083863226L;

	private Integer projectId;
	private String application;
	private String packageName;
	private String label;
	private String launchicon;
	private String scheme;
	private Integer versioncode;
	private List<String> imageNames;
	private SplashInterface splash_interface;
	private LoginInterface login_interface;
	private List<CommonInterface> common_interfaces;
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getLaunchicon() {
		return launchicon;
	}
	public void setLaunchicon(String launchicon) {
		this.launchicon = launchicon;
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
	public List<String> getImageNames() {
		return imageNames;
	}
	public void setImageNames(List<String> imageNames) {
		this.imageNames = imageNames;
	}
	public SplashInterface getSplash_interface() {
		return splash_interface;
	}
	public void setSplash_interface(SplashInterface splash_interface) {
		this.splash_interface = splash_interface;
	}
	public LoginInterface getLogin_interface() {
		return login_interface;
	}
	public void setLogin_interface(LoginInterface login_interface) {
		this.login_interface = login_interface;
	}
	public List<CommonInterface> getCommon_interfaces() {
		return common_interfaces;
	}
	public void setCommon_interface(List<CommonInterface> common_interfaces) {
		this.common_interfaces = common_interfaces;
	}
	@Override
	public String toString() {
		return "ProjectListJson [projectId=" + projectId + ", application=" + application + ", packageName="
				+ packageName + ", label=" + label + ", launchicon=" + launchicon + ", scheme=" + scheme
				+ ", versioncode=" + versioncode + ", imageNames=" + imageNames + ", splash_interface="
				+ splash_interface + ", login_interface=" + login_interface + ", common_interfaces=" + common_interfaces
				+ "]";
	}



	
}
