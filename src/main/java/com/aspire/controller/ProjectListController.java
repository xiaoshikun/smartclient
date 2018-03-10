package com.aspire.controller;

import com.aspire.pojo.ProjectList;
import com.aspire.pojo.User;
import com.aspire.service.ProjectListService;
import com.aspire.util.JacksonUtil;
import com.aspire.util.ResultJson;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/project")
public class ProjectListController {
	Logger logger = Logger.getLogger(ProjectListController.class);
	@Resource
	private ProjectListService projectListService;

	@RequestMapping(value="/add.do" ,method=RequestMethod.POST)
	@ResponseBody
	public ResultJson addProject(ProjectList projectList , ResultJson result, HttpSession session){
		logger.info("接收新建工程请求projectList = :"+projectList);
		try {
			if(projectList!=null){
				User user = (User) session.getAttribute("user");
				projectList.setUserId(user.getUserId());
				projectList.setCreater(user.getUsername());
				projectListService.addProjectList(projectList,result);
				logger.info("新建工程成功");
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
	public ResultJson projectListByUserId(Integer userId,ResultJson result){
		logger.info("接收根据用户ID查询所有工程列表请求userId = :"+userId);
		try {
			if(userId!=null){
				projectListService.findProjectListByUserId(userId,result);
				logger.info("用户ID查询工程列表成功");
				return result;
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	@RequestMapping("/search.do")
	@ResponseBody
	public ResultJson projectListByKeyWord(String keyword,ResultJson result){
		logger.info("接收根据关键字搜索工程列表请求keyword = :"+keyword);
		try {
			if(!keyword.trim().isEmpty()){
				projectListService.findProjectListByKeyWord(keyword,result);
				logger.info("关键字搜索工程列表成功");
				return result;
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	@RequestMapping(value="/index.do")
	@ResponseBody
	public ResultJson openMainProjectListByProjectId(Integer projectId,ResultJson result){
		logger.info("接收根据工程ID进入工程主界面projectId = :\"+projectId");
		try {
			if(projectId!=null){
				projectListService.openMainProjectListByProjectId(projectId,result);
				logger.info("进入工程主界面成功");
				return result;
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	@RequestMapping(value="/edit.do",method=RequestMethod.POST)
	@ResponseBody
	public ResultJson updateProjectList(@RequestBody String projectList,ResultJson result,HttpSession session){
		logger.info("接收编辑更新工程请求projectList = :"+projectList);
		ProjectList pl = JacksonUtil.readValue(projectList, ProjectList.class);
		logger.info("接收编辑更新工程请求映射对象projectList = :"+pl);
		try {
			if(pl!=null){
				User user = (User)session.getAttribute("user");
				pl.setLastupdated(user.getUsername());
				projectListService.updateProjectList(pl,result);
				logger.info("更新工程成功");
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
	public ResultJson getProjectListDetail(Integer projectId,ResultJson result){
		logger.info("接收根据工程ID查询工程详情请求projectId = :"+projectId);
		try {
			if(projectId!=null){
				projectListService.getProjectListDetail(projectId,result);
				logger.info("根据工程ID查询工程详情成功");
				return result;
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	
	@RequestMapping("/copy.do")
	@ResponseBody
	public ResultJson copyProjectList(ProjectList projectList,ResultJson result,HttpSession session){
		logger.info("接收复制工程请求projectList = :"+projectList);
		try {
			if(projectList!=null){
				User user = (User)session.getAttribute("user");
				projectList.setCreater(user.getUsername());
				projectList.setUserId(user.getUserId());
				projectListService.copyProjectList(projectList,result);
				logger.info("复制工程成功");
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
	public ResultJson deleteProject(Integer projectId,ResultJson result){
		logger.info("接收根据工程ID删除工程projectId = "+projectId);
		try {
			if(projectId!=null){
				projectListService.deleteProjectList(projectId,result);
				logger.info("工程ID删除工程");
				return result;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	@RequestMapping("/pack.do")
	@ResponseBody
	public ResultJson ProjectPackage(final Integer projectId,Integer packageType ,final ResultJson result,HttpSession session){
		logger.info("接收工程打包请求projectId = "+projectId+",packageType = " +packageType);
		try {
			User user = (User)session.getAttribute("user");
			String username = user.getUsername();
			if(packageType.equals(1)){
				//生成Android应用包

				projectListService.ProjectGenerateAndroidAppPackage(projectId,username,result);
				logger.info("生成Android成功");
				return result;
			}
			if(packageType.equals(2)){
				//生成ios应用包
				projectListService.ProjectGenerateIosAppPackage(projectId,result);
				logger.info("生成ios成功");
				return result;
			}
			//同时生成Android和ios
			if(packageType.equals(0)){
				//生成Android
				projectListService.ProjectGenerateAndroidAppPackage(projectId,username,result);
				//生成ios
				projectListService.ProjectGenerateIosAppPackage(projectId,result);
				logger.info("同时生成Android和ios成功");
				return result;
			}
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return result;
		}
		return null;
	}
	
	
	
	
}
	
	











