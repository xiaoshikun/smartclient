package com.aspire.mapper;

import com.aspire.pojo.PageList;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PageListDao {

	int addPageList(PageList pageList);

	int updatePageList(PageList pageList);

	PageList findPageList(PageList pageList);
	
	@Select(value="SELECT page_id AS pageId FROM page_list WHERE project_id = #{projectId}")
	List<Integer> findPageIdByProjectId(@Param("projectId") Integer projectId);

	@Delete("DELETE FROM page_list WHERE project_id=#{projectId} AND page_id=#{pageId}")
	void deletePageListByPageId(@Param("projectId") Integer projectId, @Param("pageId") Integer pageId);

	@Select(value="SELECT layoutfile FROM page_list WHERE page_id = #{pageId}")
	String findPagelayoutByPageId(@Param("pageId") int pageId);

	@Select(value="SELECT page_id AS pageId,page_title AS pageTitle,uitype,layoutfile,pagepath,navigation AS navigation FROM page_list WHERE project_id = #{projectId} AND isindex = 1")
	PageList findPageIndexIdByProjectId(@Param("projectId") Integer projectId);
	
	@Select(value="SELECT scheme FROM project_list WHERE project_id = #{projectId} ")
	String findSchemeByProjectId(@Param("projectId") Integer projectId);

	@Select(value="SELECT project_id FROM page_list WHERE page_id = #{pageId}")
	Integer findProjectIdByPageId(@Param("pageId") Integer pageId);

	@Select(value="SELECT uitype FROM page_list WHERE page_id = #{pageId}")
	String findUitypeByPageId(@Param("pageId") Integer pageId);

	@Select(value="SELECT page_id AS pageId FROM page_list WHERE page_id = #{pageId}  AND navigation = 2 ")
	PageList findPageListByPageIdIsBottomNavigation(@Param("pageId") Integer pageId);

	@Select(value="SELECT page_id AS pageId FROM page_list WHERE page_id = #{pageId}  AND navigation = 1 ")
	PageList findPageListByPageIdIsTopNavigation(@Param("pageId") Integer pageId);
	
	@Select(value="SELECT navigation FROM page_list WHERE page_id = #{id}")
	Integer findNavigationByPageId(@Param("id") Integer id);

	@Select("SELECT page_id AS pageId,page_name AS pageName,pagepath,page_title AS pageTitle,uitype,layoutfile,isindex,sort,pagetype AS pageType,navigation AS navigation FROM page_list WHERE project_id = #{projectId}")
	List<PageList> findAllPageListByProjectId(@Param("projectId") Integer oldProjectId);

	com.aspire.component.PageList findPageDetails(PageList pageList);

	//删除控件使用
	@Select(value="SELECT page_id AS pageId FROM page_list WHERE nav_pid = #{pageId}")
	List<Integer> getPageIdByPageId(@Param("pageId") Integer pageId);

	//删除控件使用
	@Delete(value="DELETE FROM page_list WHERE page_id = #{pageId}")
	int deletePageInfoByPageId(@Param("pageId") Integer ttPid);

	@Select(value="SELECT page_id AS pageId,project_id AS projectId,page_name AS pageName,pagepath,page_title AS pageTitle,uitype,layoutfile,isindex,sort,pagetype AS pageType,navigation AS navigation FROM page_list WHERE nav_pid = #{pageId}")
	List<PageList> getPageInfoByNavId(@Param("pageId") Integer pageId);

	@Select(value="SELECT page_id AS pageId,project_id AS projectId,page_name AS pageName,pagepath,page_title AS pageTitle,uitype,layoutfile,isindex,sort,pagetype AS pageType,navigation AS navigation FROM page_list WHERE project_id = #{projectId} AND navigation =2 ")
	List<PageList> findBottomNavPageIdByprojectId(@Param("projectId") Integer oldProjectId);

	@Select(value="SELECT page_id AS pageId,project_id AS projectId,page_name AS pageName,pagepath,page_title AS pageTitle,uitype,layoutfile,isindex,sort,pagetype AS pageType,navigation AS navigation FROM page_list WHERE project_id = #{projectId} AND navigation =1 ")
	List<PageList> findTopNavPageIdByprojectId(@Param("projectId") Integer oldProjectId);

	@Select(value="SELECT page_id AS pageId,project_id AS projectId,page_name AS pageName,pagepath,page_title AS pageTitle,uitype,layoutfile,isindex,sort,pagetype AS pageType,navigation AS navigation FROM page_list WHERE project_id = #{projectId} AND navigation =0 ")
	List<PageList> findCommonPageIdByprojectId(@Param("projectId") Integer oldProjectId);

	@Select(value="SELECT 	page_id AS pageId,project_id AS projectId,page_name AS pageName,pagepath,page_title AS pageTitle,uitype,layoutfile,isindex,sort,pagetype AS pageType,navigation,nav_pid AS navPid FROM page_list WHERE page_id = #{pageId}")
	PageList findPageListByPageId(@Param("pageId") Integer pageId);

	@Select(value="SELECT page_id AS pageId,page_name AS pageName,pagepath,page_title AS pageTitle,uitype,layoutfile,isindex,sort,pagetype AS pageType,navigation FROM page_list WHERE project_id = #{projectId} AND navigation = 2")
	List<PageList> findBottNavigatorByProjectId(@Param("projectId") Integer projectId);

	@Select(value="SELECT page_id AS pageId,page_name AS pageName,pagepath,page_title AS pageTitle,uitype,layoutfile,isindex,sort,pagetype AS pageType,navigation FROM page_list WHERE project_id = #{projectId} AND navigation = 1")
	List<PageList> findTopNavigatorByProjectId(@Param("projectId") Integer projectId);
	
	@Select(value="SELECT page_id AS pageId,page_name AS pageName,pagepath,page_title AS pageTitle,uitype,layoutfile,isindex,sort,pagetype AS pageType,navigation FROM page_list WHERE project_id = #{projectId} AND navigation = 3")
	List<PageList> findCommonNavigatorByProjectId(@Param("projectId") Integer projectId);



}
