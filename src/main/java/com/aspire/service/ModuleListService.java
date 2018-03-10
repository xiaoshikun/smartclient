package com.aspire.service;

import com.aspire.pojo.ModuleList;
import com.aspire.util.ResultJson;


public interface ModuleListService {

	void saveModuleList(String saveModule, ResultJson result)throws Exception;
	
	void findModuleList(ResultJson result)throws  Exception;

	void findModuleListBykeyword(String keyword, ResultJson result)throws  Exception;

	ModuleList findModuleDetail(Integer moduleId)throws  Exception;

	void copyModuleList(Integer moduleId, String moduleName, ResultJson result)throws  Exception;

	void deleteModuleList(Integer moduleId, ResultJson result)throws  Exception;

	void addModuleListType(String name, ResultJson result)throws  Exception;

	void getModuleListTypes(ResultJson result)throws  Exception;

	void updateModuleListType(Integer id, Integer moduleId, ResultJson result)throws  Exception;

	void findModuleListTypeByModuleId(Integer moduleId, ResultJson result)throws  Exception;

	void deleteModuleListType(Integer id, ResultJson result)throws  Exception;

	void updateModuleList(ModuleList moduleList, ResultJson result)throws  Exception;


}


















