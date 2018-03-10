package com.aspire.controller;

import com.aspire.pojo.ModuleList;
import com.aspire.service.ModuleListService;
import com.aspire.util.JacksonUtil;
import com.aspire.util.ResultJson;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/module")
public class ModuleListController {
	
	@Resource
	private ModuleListService moduleListService;

	Logger logger = Logger.getLogger(ModuleListController.class);

	@RequestMapping(value="/save.do",method=RequestMethod.POST)
	@ResponseBody
	public ResultJson svaeModule(@RequestBody String saveModule, ResultJson result){
		logger.info("接收保存模板请求:"+saveModule);
		if(saveModule!=null){
			try {
				moduleListService.saveModuleList(saveModule,result);
				logger.info("保存模板成功");
				return result;
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				return result;
			}
		}
		return null;
	}
	
	@RequestMapping("/edit.do")
	@ResponseBody
	public ResultJson updateModuleList(@RequestBody String editModule,ResultJson result){
		logger.info("接收编辑模板请求:"+editModule);
		try {
			ModuleList moduleList = JacksonUtil.readValue(editModule, ModuleList.class);
			logger.info("接收编辑模板请求:"+moduleList);
			if(moduleList!=null){
				moduleListService.updateModuleList(moduleList,result);
				logger.info("编辑模板成功");
				return result;
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	@RequestMapping("/list.do")
	@ResponseBody
	public ResultJson getModuleList(ResultJson result){
		logger.info("接收模板列表查询所有请求");
		try {
			moduleListService.findModuleList(result);
			logger.info("获取模板列表成功");
			return result;
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
	}
	
	@RequestMapping("/search.do")
	@ResponseBody
	public ResultJson getModuleListBykeyword(String keyword,ResultJson result){
		logger.info("根据关键字搜索查询模板列表请求"+keyword);
		try {
			if(!keyword.trim().isEmpty()){
				moduleListService.findModuleListBykeyword(keyword,result);
				logger.info("关键字查询模板成功");
				return result;
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	@RequestMapping("/detail.do")
	@ResponseBody
	public ResultJson getModuleDetail(Integer moduleId){
		logger.info("接收根据模板ID查询模板详细"+moduleId);
		try {
			if(null!=moduleId){
				ModuleList moduleList = moduleListService.findModuleDetail(moduleId);
				logger.info("模板ID查询模板详情成功");
				return ResultJson.oK(moduleList);
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return ResultJson.build(1,"查询模板详情异常");
		}
		return null;
	}
	
	
	@RequestMapping("/copy.do")
	@ResponseBody
	public ResultJson copyModuleList(Integer moduleId,String moduleName,ResultJson result){
		logger.info("接收复制模板请求moduleId="+moduleId+"，moduleName="+moduleName);
		try {
			if(moduleId!=null&&!moduleName.trim().isEmpty()){
				moduleListService.copyModuleList(moduleId,moduleName,result);
				logger.info("复制模板请求成功");
				return result;
			}

		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	@RequestMapping("/delete.do")
	@ResponseBody
	public ResultJson deleteModuleByModuleId(Integer moduleId,ResultJson result){
		logger.info("接收删除模板请求moduleId="+moduleId);
		try {
			if(moduleId!=null){
				moduleListService.deleteModuleList(moduleId,result);
				logger.info("删除模板成功");
				return result;
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	@RequestMapping("/type/list.do")
	@ResponseBody
	public ResultJson getModuleTypeList(ResultJson result){
		logger.info("接收查询所有模板分类列表请求");
		try {
			moduleListService.getModuleListTypes(result);
			logger.info("查询所有模板分类列表成功");
			return result;
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
	}
	
	@RequestMapping("/type/add.do")
	@ResponseBody
	public ResultJson addModuleType(String name,ResultJson result){
		logger.info("接收新增模板分类请求name = "+name);
		try {
			if(!name.trim().isEmpty()){
				moduleListService.addModuleListType(name,result);
				logger.info("新增模板分类成功");
				return result;
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	
	@RequestMapping("/type/edit.do")
	@ResponseBody
	public ResultJson updateModuleListType(Integer id,Integer moduleId,ResultJson result){
		logger.info("接收编辑更新模板分类请求id = "+id+"，moduleId = "+moduleId);
		try {
			if(null!=id&&null!=moduleId){
				moduleListService.updateModuleListType(id,moduleId,result);
				logger.info("更新模板分类列表成功");
				return result;
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	@RequestMapping("/type/get.do")
	@ResponseBody
	public ResultJson findModuleTypeByModuleId(Integer moduleId,ResultJson result){
		logger.info("接收根据模板ID查询模板分类请求");
		try {
			if(moduleId!=null){
				moduleListService.findModuleListTypeByModuleId(moduleId,result);
				logger.info("模板ID查询模板分类列表成功");
				return result;
			}

		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	@RequestMapping("/type/delete.do")
	@ResponseBody
	public ResultJson deleteModuleListType(Integer id,ResultJson result){
		logger.info("接收根据模板分类ID删除模板分类id = "+id);
		try {
			if(id!=null){
				moduleListService.deleteModuleListType(id,result);
				logger.info("删除模板分类列表成功");
				return result;
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	

	
}





















