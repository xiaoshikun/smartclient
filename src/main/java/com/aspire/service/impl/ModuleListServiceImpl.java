package com.aspire.service.impl;

import com.aspire.mapper.ComponentListDao;
import com.aspire.mapper.ModuleListDao;
import com.aspire.pojo.ComponentList;
import com.aspire.pojo.ModuleList;
import com.aspire.pojo.ModuleListType;
import com.aspire.service.ModuleListService;
import com.aspire.util.ResultJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class ModuleListServiceImpl implements ModuleListService {

	@Resource
	private ModuleListDao moduleListDao;
	@Resource
	private ComponentListDao componentListDao;

	Logger logger = Logger.getLogger(ModuleListServiceImpl.class);

	
	@Override
	@Transactional
	public void saveModuleList(String modcomSort, ResultJson result) throws Exception {
		ComponentList component = new ComponentList();
		ObjectMapper mapper = new ObjectMapper();  
		JsonNode jsonNode =  mapper.readTree(modcomSort);
		JsonNode module = jsonNode.findValue("moduleId");
		if(null==module){
			result.setCode(1);
			result.setMsg("模板ID空");
		}
		//保存新的排序号前先根据moduleId清空
		component.setModuleId(module.asInt());
		component.setSort(0);
		componentListDao.updateComponentList(component);
		//更新模板排序
		int sort = 0;
		List<JsonNode> ids = jsonNode.findValues("comSorts");
		for (JsonNode id : ids) {
			sort ++;
			component.setComponentId(id.asInt());
			component.setSort(sort);
			componentListDao.updateComponentList(component);
		}
		result.setCode(0);
		result.setMsg("SUCCESS");
	}
	
	@Override
	public void updateModuleList(ModuleList moduleList, ResultJson result)throws Exception {
		if(null==moduleList.getModuleId()){
			result.setCode(1);
			result.setMsg("模板ID为空");
			return;
	    }
		moduleListDao.updateModuleList(moduleList);
		result.setCode(0);
		result.setMsg("SUCCESS");
	}
	
	/**
	 * 查询所有模板列表
	 */
	@Override
	public void findModuleList(ResultJson result)throws  Exception{
		List<ModuleList> moduleList = moduleListDao.findModuleList();
		if(null==moduleList||moduleList.size()==0){
			result.setCode(0);
			result.setMsg("没有模板");
			return;
		}
		result.setCode(0);
		result.setMsg("SUCCESS");
		result.setData(moduleList);
	}
	
	/**
	 * 根据关键字搜索模板列表
	 */
	@Override
	public void findModuleListBykeyword(String keyword,ResultJson result)throws  Exception {
		if(""==keyword||keyword.trim().isEmpty()){
			result.setCode(1);
			result.setMsg("请求关键字为空");
			return;
		}
			String moduleName = "%"+keyword+"%";
			List<ModuleList> moduleLists = moduleListDao.findModuleListByKeyword(moduleName);
			 if(null==moduleLists||moduleLists.size()==0){
				 	result.setCode(0);
					result.setMsg("关键字不存在");
					return;
			 }	
			for(ModuleList moduleList : moduleLists){
				moduleList.setLaunchicon("");
			}
			result.setCode(0);
			result.setMsg("SUCCESS");
			result.setData(moduleLists);
	}
	
	/**
	 * 获取模板详情
	 */
	@Override
	public ModuleList findModuleDetail(Integer moduleId)throws  Exception {
		//根据模板ID查询模板列表
		ModuleList moduleList = moduleListDao.findModuleDetail(moduleId);
		//根据模板ID查询控件列表
		List<ComponentList> componentLists = componentListDao.findComponentByModuleId(moduleId);
		moduleList.setComponentLists(componentLists);
		return moduleList;
	}

	@Override
	@Transactional
	public void copyModuleList(Integer moduleId, String moduleName, ResultJson result) throws  Exception{
		if(null==moduleId||"".equals(moduleId)){
			result.setCode(1);
			result.setMsg("请求模板ID为空");
			return;
	    }
		if(null==moduleName||moduleName.trim().equals("")){
			result.setCode(1);
			result.setMsg("请求模板名字为空");
			return;
	    }
		ModuleList moduleList = moduleListDao.findModuleDetail(moduleId);
		if(moduleList==null){
			result.setCode(1);
			result.setMsg("复制模板错误");
			return;
		}
		//判断页面名称是否重复
		Map<String,Object> map = new HashMap<String,Object>();
		if(moduleName.equals(moduleList.getModuleName())){
			map.put("moduleName", "模板名重复");
		}
		//复制模板
		moduleList.setModuleName(moduleName);
		moduleListDao.copyModuleList(moduleList);
		//复制后的模板ID
		Integer newModuleId = moduleList.getModuleId();
		//复制模板页面控件
		List<ComponentList> componentLists = componentListDao.findComponentByModuleId(moduleId);
		for (ComponentList componentList : componentLists) {
			Integer componentId = componentList.getComponentId();
			if(componentId!=null){
				componentList.setModuleId(newModuleId);
				componentList.setPageId(null);
				componentList.setUpdated(new Date());
				componentListDao.addComponentList(componentList);
			}
		}
		map.put("moduleId",newModuleId);
		result.setCode(0);
		result.setMsg("SUCCESS");
		result.setData(map);
	}

	@Override
	@Transactional
	public void deleteModuleList(Integer moduleId, ResultJson result)throws  Exception {
		if(moduleId==null||"".equals(moduleId)){
			result.setCode(1);
			result.setMsg("请求模板ID为空");
			return;
		}
		ModuleList moduleList = moduleListDao.findModuleDetail(moduleId);
		//删除模板控件列表
		List<ComponentList> componentLists = componentListDao.findComponentByModuleId(moduleId);
		for (ComponentList componentList : componentLists) {
			componentListDao.deleteComponentList(componentList.getComponentId());
		}
		//删除模板分类
		Integer typeId = moduleList.getTypeId();
		if(null!=typeId){
			moduleListDao.deleteModuleListType(typeId);
		}
		//删除模板列表
		moduleListDao.deleteModuleListByModuleId(moduleId);
		result.setCode(0);
		result.setMsg("SUCCESS");
	}

	@Override
	@Transactional
	public void addModuleListType(String name, ResultJson result)throws  Exception {
		if(name==null||name.trim().equals("")){
			result.setCode(1);
			result.setMsg("请求模板分类名称为空");
			return;
		}
		//判断模板分类是否重复
		List<ModuleListType> moduleListTypes = moduleListDao.getModuleListTypes();
		if(moduleListTypes.size()!=0){
			for (ModuleListType moduleListType : moduleListTypes) {
				if(name.equals(moduleListType.getName())){
					result.setCode(0);
					result.setMsg("模板分类名重复");
				}
			}
		}
		ModuleListType mt = new ModuleListType();
		mt.setName(name);
		moduleListDao.addModuleListType(mt);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("typeId", mt.getId());
		result.setCode(0);
		result.setMsg("SUCCESS");
		result.setData(map);
	}

	@Override
	public void getModuleListTypes(ResultJson result)throws  Exception {
		List<ModuleListType> moduleListType = moduleListDao.getModuleListTypes();
		if(moduleListType==null){
			result.setCode(1);
			result.setMsg("获取分类错误");
			return;
		}
		result.setCode(0);
		result.setMsg("SUCCESS");
		result.setData(moduleListType);
	}

	@Override
	public void updateModuleListType(Integer id, Integer moduleId, ResultJson result)throws  Exception {
		if(id==null||"".equals(id)){
			result.setCode(1);
			result.setMsg("请求分类ID为空");
			return;
		}
		if(moduleId==null||"".equals(moduleId)){
			result.setCode(1);
			result.setMsg("请求模板ID为空");
			return;
		}
		ModuleList moduleList = new ModuleList();
		moduleList.setModuleId(moduleId);
		moduleList.setTypeId(id);
		moduleListDao.updateModuleList(moduleList);
		result.setCode(0);
		result.setMsg("SUCCESS");
	}

	@Override
	public void findModuleListTypeByModuleId(Integer moduleId, ResultJson result)throws  Exception {
		if(moduleId==null){
			result.setCode(1);
			result.setMsg("请求模板ID为空");
			return;
		}
			int moduleListTypeId = moduleListDao.findModuleListTypeByModuleId(moduleId);
			if(moduleListTypeId==0){
				result.setCode(0);
				result.setMsg("当前模板未分类");
				return;
			}
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("typeId", moduleListTypeId);
		result.setCode(0);
		result.setMsg("SUCCESS");
		result.setData(map);
	}

	@Override
	public void deleteModuleListType(Integer id, ResultJson result)throws  Exception {
		if(id==null||"".equals(id)){
			result.setCode(1);
			result.setMsg("请求模板分类ID为空");
			return;
		}
		int n = moduleListDao.deleteModuleListType(id);
		if(n==0||n==-1){
			result.setCode(0);
			result.setMsg("分类ID不存在");
			return;
		}
		result.setCode(0);
		result.setMsg("SUCCESS");
	}





	
	
	
	
}















