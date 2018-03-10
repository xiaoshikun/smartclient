package com.aspire.controller;

import com.aspire.service.ComponentService;
import com.aspire.util.ResultJson;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/component")
public class ComponentController {
	
	Logger logger = Logger.getLogger(ComponentController.class);

	@Resource
	private ComponentService componentService;
	
	@RequestMapping(value="/add.do")
	public ResultJson addComponentList(@RequestBody String componentList, ResultJson result){
		
		logger.info("接收新建控件请求:"+componentList);
		if(componentList!=null&&!"".equals(componentList)){
			try {
				componentService.addComponentList(componentList,result);
				logger.info("新建控件成功");
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(),e);
				return result;
			}
	        
		}
		return null;
	}
	
	@RequestMapping(value="/edit.do")
	public ResultJson updateComponentList(@RequestBody String comInfo,ResultJson result){
		logger.info("接收编辑控件请求:"+comInfo);
		if(comInfo!=null&&!"".equals(comInfo)){
			try {
				componentService.updateComponentList(comInfo,result);
				return result;
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				return result;
			}
		}
		return null;
	}
	
	@RequestMapping(value="/delete.do")
	public ResultJson deleteComponentList(Integer pageId,Integer componentId,ResultJson result){
		logger.info("controller层接收删除控件请求pageId=:"+pageId+"，componentId="+componentId);
		if(pageId!=null){
			try {
				componentService.deleteComponentList(pageId,componentId,result);
				logger.info("删除控件成功");
				return result;
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				return result;
			}
		}
		return null;
	}

	@RequestMapping(value="/detail.do")
	public ResultJson findComponentListByComponentId(Integer componentId,ResultJson result){
		logger.info("接收控件ID查询控件请求componentId="+componentId);
		try {
			if(componentId!=null){
				componentService.findComponentListByComponentId(componentId,result);
				logger.info(" 根据控件ID查询控件详情成功");
				return result;
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
}















