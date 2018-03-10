package com.aspire.mapper;

import com.aspire.pojo.PageList;
import com.aspire.pojo.ProjectList;
import com.aspire.pojo.out.ProjectListJson;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ProjectListDao {

	int addProjectList(ProjectList projectList);

	List<Map<String, ProjectList>> findProjectListByUserId(Integer userId);

	List<Map<String, ProjectList>> findProjectListBykeyWord(String title);

	List<PageList> openMainProjectListByProjectId(Integer projectId);
	
	int updateProjectList(ProjectList projectList);

	@Select(value="SELECT app_icon FROM project_list WHERE project_id = #{projectId}")
	String findProjectListAppiconPath(@Param("projectId") int projectId);

	ProjectList getProjectListDetail(Integer projectId);

	@Delete(value="DELETE FROM project_list WHERE project_id = #{projectId}")
	int deleteProjectListByProjectId(@Param("projectId") Integer projectId);

	ProjectListJson findProjectListJson(@Param("projectId") Integer projectId);

	@Select(value="SELECT scheme FROM project_list WHERE project_id = #{projectId}")
	String findProjectListSchemeByProjectId(@Param("projectId") Integer projectId);

	@Select(value="SELECT IFNULL(page_id,-1) AS pageId FROM page_list  WHERE project_id = #{projectId} AND pagetype = #{pagetype}")
	Integer findPageByPageType(@Param("projectId") Integer projectId, @Param("pagetype") Integer pagetype);
	
}
