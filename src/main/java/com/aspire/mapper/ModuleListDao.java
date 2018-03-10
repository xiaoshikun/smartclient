package com.aspire.mapper;

import com.aspire.pojo.ModuleList;
import com.aspire.pojo.ModuleListType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ModuleListDao {

	int updateModuleList(ModuleList moduleList);

	ModuleList findModuleDetail(Integer moduleId);

	List<ModuleList> findModuleList();

	List<ModuleList> findModuleListByKeyword(String moduleName);

	int copyModuleList(ModuleList moduleList);

	@Select(value="SELECT id,NAME FROM module_type ")
	List<ModuleListType> getModuleListTypes();

	int addModuleListType(ModuleListType mt);

	@Select(value="SELECT id FROM module_list WHERE module_id = #{moduleId}")
	int findModuleListTypeByModuleId(@Param("moduleId") int moduleId);

	@Delete(value="DELETE FROM module_type WHERE id = #{id}")
	int deleteModuleListType(@Param("id") Integer id);

	@Delete(value="DELETE FROM module_list WHERE module_id = #{moduleId}")
	int deleteModuleListByModuleId(@Param("moduleId") Integer moduleId);

	@Select(value="SELECT  module_name AS moduleName,module_title AS moduleTitle FROM module_list WHERE module_id = #{moduleId}")
	ModuleList findModuleListByModuleId(@Param("moduleId") Integer moduleId);
	

}

















