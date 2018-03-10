package com.aspire.controller;

import com.aspire.pojo.PageList;
import com.aspire.service.PageListService;
import com.aspire.util.JacksonUtil;
import com.aspire.util.ResultJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/page")
public class PageListController {

	Logger logger = Logger.getLogger(PageListController.class);
	@Resource
	private PageListService pageListService;

	@RequestMapping(value="/add.do",method=RequestMethod.POST)
	@ResponseBody
	public ResultJson addPageList(PageList pageList, ResultJson result){
		logger.info("接收新增页面请求pageList = :"+pageList);
		try {
			if(null!=pageList){
				pageListService.addPageList(pageList, result);
				logger.info("新增页面成功");
				return result;
			}

		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	
	@RequestMapping("/set_index.do")
	@ResponseBody
	public ResultJson setPageIndex(PageList pageList,ResultJson result){
		logger.info("接收设置页面首页请求pageList"+pageList);
		try {
			if(pageList!=null){
				pageListService.updatePageIndex(pageList,result);
				logger.info("页面设置首页成功");
				return result;
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	@RequestMapping("/edit.do")
	@ResponseBody
	public ResultJson updatePageList(@RequestBody String json, ResultJson result){
		logger.info("接收页面更新操作pageList = "+json);
		logger.debug("json = "+json);
		if(json.equals(null)){
			result.setCode(1);
			result.setMsg("接收参数空");
			return result;
		}
		try {
		ObjectMapper mapper = new ObjectMapper();  
		JsonNode jsonNode = mapper.readTree(json);
		Integer pageId = jsonNode.findValue("pageId").asInt();
		Integer projectId = jsonNode.findValue("projectId").asInt();
		String pageName = jsonNode.findValue("pageName").asText();
		String pageTitle = jsonNode.findValue("pageTitle").asText();

		PageList pageList = new PageList();
		pageList.setPageId(pageId);
		pageList.setProjectId(projectId);
		pageList.setPageName(pageName);
		pageList.setPageTitle(pageTitle);
		logger.info("controller层接收编辑更新页面请求pageList = "+pageList);
		if(pageList!=null){
			pageListService.updatePageList(pageList,result);
			logger.info("页面编辑成功");
			return  result;
		}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	
//	@RequestMapping("/edit.do")
//	@ResponseBody
//	public ResultJson updatePageList(PageList pageList, ResultJson result){
//		
//		logger.info("controller层接收编辑更新页面请求pageLis = "+pageList);
//
//		if(pageList!=null){
//			pageListService.updatePageList(pageList,result);
//		}
//		return result;
//	}
	
//	@RequestMapping("/edit.do")
//	@ResponseBody
//	public ResultJson updatePageList(@RequestBody  String pageList, ResultJson result){
//		System.out.println("controller层接收编辑更新页面请求pageLisJson = "+pageList);
//
//		PageList page = JacksonUtil.readValue(pageList,PageList.class);
//		System.out.println("controller层接收编辑页面请求字符串映射对象pageList= "+page);
//
//
//		if(pageList!=null){
//			pageListService.updatePageList(page,result);
//		}
//		return result;
//	}

	@RequestMapping("/detail.do")
	@ResponseBody
	public ResultJson findPageListByPageId(Integer projectId,Integer pageId,ResultJson result){
		logger.info("收根据页面ID查询页面详情projectId = "+projectId+"，pageId = "+pageId);
		try {
			if(projectId!=null&&pageId!=null){
				pageListService.findPageListByPageId(projectId,pageId,result);
				logger.info("获取页面详情成功");
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	@RequestMapping(value="/save.do",method=RequestMethod.POST)
	@ResponseBody
	public ResultJson savePageListByPageId(@RequestBody String savePage,ResultJson result){
		logger.info("接收保存页面请求savePage = :"+savePage);
		try {
			if(savePage!=null){
				pageListService.savePageList(savePage,result);
				logger.info("保存页面成功");
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	@RequestMapping("/delete.do")
	@ResponseBody
	public ResultJson deletePageListByPageId(Integer projectId,Integer pageId,ResultJson result){
		logger.info("接收根据ID删除页面请求projectId = "+projectId+"，pageId = "+pageId);
		try {
			if(projectId!=null&&pageId!=null){
				pageListService.deletePageListByPageId(projectId,pageId,result);
				logger.info("删除页面成功");
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}

	@RequestMapping("/copy.do")
	@ResponseBody
	public ResultJson copyPageList(@RequestBody String request,ResultJson result){
		logger.info("接收复制页面请求pageList = " + request);
		try {
		PageList pageList = JacksonUtil.readValue(request,PageList.class);
		logger.info("接收复制页面请求转换对象pageList = " + pageList);
		Integer pageId = pageList.getPageId();
		Integer projectId = pageList.getProjectId();
		String pageName = pageList.getPageName();

			if(projectId!=null&&pageId!=null){
				pageListService.copyPageList(pageId,projectId,pageName,result);
				logger.info("复制页面成功");
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
//	@RequestMapping("/copy.do")
//	@ResponseBody
//	public ResultJson copyPageList(Integer pageId, Integer projectId,String pageName,ResultJson result){
//		System.out.println("复制页面"+pageId+"AND"+projectId+"AND"+pageName);
//		if(projectId!=null&&pageId!=null){
//	
//			try {
//				pageListService.copyPageList(pageId,projectId,pageName,result);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return result;
//	}
	
	@RequestMapping(value="/save_module.do",method=RequestMethod.POST)
	@ResponseBody
	public ResultJson savePageToModule(@RequestBody String savePageModule,ResultJson result){
		logger.info("接收页面保存为模板请求savePageModule = "+savePageModule);
		try {
			PageList pageList = JacksonUtil.readValue(savePageModule,PageList.class);
			logger.info("接收页面保存为模板请求映射对象pageList = :"+pageList);
			if(pageList!=null){
				pageListService.savePageToModule(pageList,result);
				logger.info("页面存为模板成功");
				return result;
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	
	
}














