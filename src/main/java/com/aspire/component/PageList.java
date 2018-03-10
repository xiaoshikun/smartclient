package com.aspire.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;
import java.util.List;
@JsonInclude(Include.NON_NULL)
/**
 * 这个类是返回页面控件交互的类
 * @author xiaos
 *
 * @param <T>
 */
public class PageList<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6941800745506047751L;

	
	private Integer pageId;
	private Integer projectId;
	private String pagepath;//当前页面的路径scheme://load_uitype_pageId
	private String uitype;
	private String layoutfile;
	private Integer isindex;
	private String pageName;
	private String pageTitle;
	private Integer sort;
	private Integer pageType;//普通页面-1,登录页面1,闪屏页面 2
	private Integer navigation;//区分页面是否有导航控件，默认0:没有导航，有顶部导航1,有底部导航2.
	private List<com.aspire.component.Component<T>> componentLists;
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
	public String getPagepath() {
		return pagepath;
	}
	public void setPagepath(String pagepath) {
		this.pagepath = pagepath;
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
	public Integer getIsindex() {
		return isindex;
	}
	public void setIsindex(Integer isindex) {
		this.isindex = isindex;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Integer getPageType() {
		return pageType;
	}
	public void setPageType(Integer pageType) {
		this.pageType = pageType;
	}
	public Integer getNavigation() {
		return navigation;
	}
	public void setNavigation(Integer navigation) {
		this.navigation = navigation;
	}
	public List<com.aspire.component.Component<T>> getComponentLists() {
		return componentLists;
	}
	public void setComponentLists(List<com.aspire.component.Component<T>> componentLists) {
		this.componentLists = componentLists;
	}
	@Override
	public String toString() {
		return "PageList [pageId=" + pageId + ", projectId=" + projectId + ", pagepath=" + pagepath + ", uitype="
				+ uitype + ", layoutfile=" + layoutfile + ", isindex=" + isindex + ", pageName=" + pageName
				+ ", pageTitle=" + pageTitle + ", sort=" + sort + ", pageType=" + pageType + ", navigation="
				+ navigation + ", componentLists=" + componentLists + "]";
	}
	

	

	
	
	

}
