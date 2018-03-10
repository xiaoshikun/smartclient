package com.aspire.mapper;

import com.aspire.pojo.ComponentList;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
public interface ComponentListDao {

	int addComponentList(ComponentList componentList);

	int updateComponentList(ComponentList componentList);

	int deleteComponentList(Integer componentId);

	@Select(value="SELECT 	component_id AS componentId,page_id AS pageId,NAME,TYPE, params AS params  FROM component_list WHERE page_id = #{pageId}")
	List<ComponentList> findComponentByPageId(@Param("pageId") Integer pageId);

	@Select(value="SELECT page_name FROM page_list WHERE page_id = #{pageId}")
	String findPageNameByPageId(@Param("pageId") Integer pageId);

	@Select(value="SELECT component_id AS componentId, page_id AS pageId,project_id AS projectId ,NAME, TYPE,params AS params FROM component_list WHERE component_id = #{componentId}")
	ComponentList findComponentByComponentId(@Param("componentId") Integer componentId);

	@Select(value="SELECT 	component_id AS componentId,module_id AS moduleId, NAME, TYPE,  params AS params FROM component_list WHERE module_id = #{moduleId}")
	List<ComponentList> findComponentByModuleId(@Param("moduleId") Integer moduleId);

	//查找闪屏控件
	@Select(value="SELECT  NAME, TYPE, params AS params FROM component_list WHERE page_id = #{splash}")
	ComponentList findSplashComponentByPageId(@Param("splash") Integer splash);

	@Select(value="SELECT component_id AS componentId FROM component_list WHERE page_id = #{pageId}")
	List<Integer> findComponentIdByPageId(@Param("pageId") Integer ttPid);

	@Delete(value="DELETE FROM component_list WHERE page_id=#{top_pageId}")
	int deleteComponentByPageId(@Param("top_pageId") Integer top_pageId);




	

	


	
	
	
}










