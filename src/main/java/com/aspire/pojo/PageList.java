package com.aspire.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
/**
 * 这个类是接收页面请求信息交互的类
 * @author xiaos
 *
 */
public class PageList implements Serializable {

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
	private String thumbnail;
	private String pageName;
	private String pageTitle;
	private Integer sort;
	private Integer pageType;//普通页面-1,登录页面1,闪屏页面 2
	private  Date createDate;
	//导航控件所新建的页面默认为0,顶部导航所新建的页面值为1,底部导航所新建页面值为2,新建页面接口所建页面值为3.
	private Integer navigation;
	private Integer navPid; //识别导航控件所新建的页面ID
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
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getNavigation() {
		return navigation;
	}
	public void setNavigation(Integer navigation) {
		this.navigation = navigation;
	}
	public Integer getNavPid() {
		return navPid;
	}
	public void setNavPid(Integer navPid) {
		this.navPid = navPid;
	}
	@Override
	public String toString() {
		return "PageList [pageId=" + pageId + ", projectId=" + projectId + ", pagepath=" + pagepath + ", uitype="
				+ uitype + ", layoutfile=" + layoutfile + ", isindex=" + isindex + ", thumbnail=" + thumbnail
				+ ", pageName=" + pageName + ", pageTitle=" + pageTitle + ", sort=" + sort + ", pageType=" + pageType
				+ ", createDate=" + createDate + ", navigation=" + navigation + ", navPid=" + navPid + "]";
	}
	
	

	
	
	

}
