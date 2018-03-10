package com.aspire.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_EMPTY)
public class ModuleList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -130007512131901799L;

	
	private Integer moduleId;
	private Integer projectId;
	private Integer typeId;
	private String moduleName;
	private String moduleTitle;
	private String thumbnail;
	private String launchicon;
	private Date created;
	private Date updated;
	private List<ComponentList> componentLists; 
    
	public ModuleList() {
	}
	public Integer getModuleId() {
		return moduleId;
	}
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getModuleTitle() {
		return moduleTitle;
	}
	public void setModuleTitle(String moduleTitle) {
		this.moduleTitle = moduleTitle;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getLaunchicon() {
		return launchicon;
	}
	public void setLaunchicon(String launchicon) {
		this.launchicon = launchicon;
	}
	public List<ComponentList> getComponentLists() {
		return componentLists;
	}
	public void setComponentLists(List<ComponentList> componentLists) {
		this.componentLists = componentLists;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	@Override
	public String toString() {
		return "ModuleList [moduleId=" + moduleId + ", projectId=" + projectId + ", typeId=" + typeId + ", moduleName="
				+ moduleName + ", moduleTitle=" + moduleTitle + ", thumbnail=" + thumbnail + ", launchicon="
				+ launchicon + ", created=" + created + ", updated=" + updated + ", componentLists=" + componentLists
				+ "]";
	}
	
	
	
	
	
	
	
	
	
}
