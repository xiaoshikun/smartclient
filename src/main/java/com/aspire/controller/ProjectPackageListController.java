package com.aspire.controller;

import com.aspire.service.ProjectPackageListService;
import com.aspire.util.ResultJson;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/apk")
public class ProjectPackageListController {

	@Resource
	private ProjectPackageListService projectPackageListService;

	Logger logger = Logger.getLogger(ProjectPackageListController.class);
	
	@RequestMapping(value="/list.do")
	@ResponseBody
	public ResultJson findPackageListByUserId(Integer userId , ResultJson result){
		logger.info("controller层接收根据用户ID查询安装包列表请求userId = :"+userId);
		try {
			if(userId!=null){
				projectPackageListService.findPackageListByUserId(userId,result);
				logger.info("用户ID查询安装包列表成功");
				return result;
			}

		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
    }
	
	@RequestMapping(value="/search.do" )
	@ResponseBody
	public ResultJson findPackageListByUserIdAndApkName(Integer userId,String apkName,ResultJson result){
		logger.info("接收根据用户ID安装包名查询安装包列表任务请求userId = :"+userId+"，apkName = "+apkName);
		try {
			if(userId!=null&&!apkName.trim().isEmpty()){
				projectPackageListService.findPackageListByUserIdAndApkName(userId,apkName,result);
				logger.info("用户ID安装包名查询安装包列表成功");
				return result;
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
    }
	
	@RequestMapping(value="/delete.do")
	@ResponseBody
	public ResultJson deletePackageListByUserIdAndProjectId(Integer projectId,Integer userId,ResultJson result){
		logger.info("接收根据用户ID工程ID删除安装包列表任务请求userId = :"+userId+"，projectId = "+projectId);
		try {
			if(userId!=null&&projectId!=null){
				projectPackageListService.deletePackageListByUserIdAndProjectId(projectId,userId,result);
				logger.info("删除安装包成功");
				return result;
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
    }
	
}
