package com.aspire.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ComponentList  implements Serializable{

	private static final long serialVersionUID = -7946916427221803285L;
	private Integer componentId; //控件ID
	private Integer pageId;//
	private Integer projectId;//
	private Integer moduleId;//
	private String name;//控件名
	private String type;//控件类型
	private String componentTitle;//控件标题名称
	private String relativeId;//控件类名+模板名称
	private String params;
	private Integer sort; //页面控件排序
	private Date created;
	private Date updated;
	public Integer getComponentId() {
		return componentId;
	}
	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}
	public Integer getPageId() {
		return pageId;
	}
	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}
	public Integer getProjectId() {
		return projectId;
	}
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	public Integer getModuleId() {
		return moduleId;
	}
	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getComponentTitle() {
		return componentTitle;
	}
	public void setComponentTitle(String componentTitle) {
		this.componentTitle = componentTitle;
	}
	public String getRelativeId() {
		return relativeId;
	}
	public void setRelativeId(String relativeId) {
		this.relativeId = relativeId;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
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
		return "ComponentList [componentId=" + componentId + ", pageId=" + pageId + ", projectId=" + projectId
				+ ", moduleId=" + moduleId + ", name=" + name + ", type=" + type + ", componentTitle=" + componentTitle
				+ ", relativeId=" + relativeId + ", params=" + params + ", sort=" + sort + ", created=" + created
				+ ", updated=" + updated + "]";
	}



	
}
