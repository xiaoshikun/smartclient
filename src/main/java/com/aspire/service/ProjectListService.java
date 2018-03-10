package com.aspire.service;

import com.aspire.pojo.ProjectList;
import com.aspire.util.ResultJson;



public interface ProjectListService {

	void addProjectList(ProjectList projectList, ResultJson result) throws Exception;

	void findProjectListByUserId(Integer userId, ResultJson result)throws Exception;

	void findProjectListByKeyWord(String keyword, ResultJson result)throws Exception;

	void openMainProjectListByProjectId(Integer projectId, ResultJson result)throws Exception;

	void updateProjectList(ProjectList projectList, ResultJson result)throws Exception;

	void getProjectListDetail(Integer projectId, ResultJson result)throws Exception;

	void copyProjectList(ProjectList projectList, ResultJson result)throws Exception;

	void deleteProjectList(Integer projectId, ResultJson result)throws Exception;

	void ProjectGenerateAndroidAppPackage(Integer projectId,String username, ResultJson result)throws Exception;

	void ProjectGenerateIosAppPackage(Integer projectId, ResultJson result)throws Exception;

}
