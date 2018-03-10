package com.aspire.service.impl;

import com.aspire.common.JsonFormatTool;
import com.aspire.common.bo.AndroidTaskCallable;
import com.aspire.common.bo.IosTaskCallable;
import com.aspire.config.AndroidConfig;
import com.aspire.config.MacConfig;
import com.aspire.mapper.ComponentListDao;
import com.aspire.mapper.PageListDao;
import com.aspire.mapper.ProjectListDao;
import com.aspire.mapper.ProjectPackageListDao;
import com.aspire.pojo.*;
import com.aspire.pojo.out.*;
import com.aspire.service.AttachmentService;
import com.aspire.service.ProjectListService;
import com.aspire.util.ClientUtil;
import com.aspire.util.JacksonUtil;
import com.aspire.util.ResultJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class ProjectListServiceImpl<T> implements ProjectListService {
	//JsonNodeFactory 实例，可全局共享
    private static JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;

    Logger logger = Logger.getLogger(ProjectListServiceImpl.class);

	@Autowired
	private AndroidConfig androidConfig;
	@Autowired
	private MacConfig macConfig;

	@Value("${ftp.imageCallPath}")
	private String imageCallPath;
	@Value("${ftp.uploadImageUrl}")
	private String uploadImageUrl;
	
	@Autowired
	private ProjectListDao projectListDao;
	@Autowired
	private PageListDao pageListDao;
	@Autowired
	private ComponentListDao componentListDao;
	@Autowired
	private AttachmentService attachmentService;
	@Resource
	private ProjectPackageListDao projectPackageListDao;

	@Override
	public void addProjectList(ProjectList projectList, ResultJson result) throws Exception{
		//非空校验
		checkProjectListNotNull(projectList,result);
		String appIconPicPath = androidConfig.getAppicon()+"a"+System.currentTimeMillis()+".png";
		ClientUtil.decoderBase64File(projectList.getAppIcon(), appIconPicPath);

		projectList.setAppIcon(appIconPicPath);  //设置应用图标路径
		projectList.setLastupdated(projectList.getCreater());
		projectList.setLastupdatedTime(new Date());
		projectListDao.addProjectList(projectList);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("projectId", projectList.getProjectId());
		result.setCode(0);
		result.setMsg("SUCCESS");
		result.setData(map);
	}

	@Override
	public void findProjectListByUserId(Integer userId, ResultJson result)throws Exception {
		if(userId==null){
			result.setCode(1);
			result.setMsg("请求用户ID为空");
			return ;
		}
		List<Map<String,ProjectList>> projectLists = projectListDao.findProjectListByUserId(userId);
		if(projectLists.isEmpty()){
			result.setCode(0);
			result.setMsg("当前用户还没有创建工程");
			result.setData(projectLists);
			return;
		}
		result.setData(projectLists);
		result.setCode(0);
		result.setMsg("SUCCESS");
	}

	@Override
	public void findProjectListByKeyWord(String keyword, ResultJson result) throws Exception{
		if(keyword.isEmpty()){
			result.setCode(1);
			result.setMsg("请求关键字为空");
			return;
		}
		String title = "%"+keyword+"%";
		List<Map<String,ProjectList>> projectLists = projectListDao.findProjectListBykeyWord(title);
		if(projectLists.size()==0||projectLists.isEmpty()){
			result.setCode(0);
			result.setMsg("关键字没有匹配");
			return ;
		}
		result.setCode(0);
		result.setMsg("SUCCESS");
		result.setData(projectLists);
	}

	@Override
	public void openMainProjectListByProjectId(Integer projectId, ResultJson result) throws Exception{
		if(null==projectId||"".equals(projectId)){
			result.setCode(1);
			result.setMsg("请求工程ID为 空");
			return;
		}
		List<PageList> pageLists = projectListDao.openMainProjectListByProjectId(projectId);
		if(pageLists.size()==0){
			result.setCode(1);
			result.setMsg("获取工程主界面错误");
		}
		result.setCode(0);
		result.setMsg("SUCCESS");
		result.setData(pageLists);
	}

	@Override
	public void updateProjectList(ProjectList projectList, ResultJson result) throws Exception{
		checkProjectListNotNull(projectList, result); //非空检查
		projectList.setLastupdatedTime(new Date());
		int projectId = projectList.getProjectId();
		String oldAppIconPath = projectListDao.findProjectListAppiconPath(projectId);

		if(!"".trim().equals(oldAppIconPath)){
			ClientUtil.deleteImage(oldAppIconPath);//删除原应用图标
			//获取应用图标的上传路径
			String appIconUploadPath =androidConfig.getAppicon()+"a"+System.currentTimeMillis()+".png";
			//获取新的Base64图标字符串
			String appIcon = projectList.getAppIcon();
			//解析Base64保存
			ClientUtil.decoderBase64File(appIcon,appIconUploadPath);
			//保存上传路径数据库存档备用
			projectList.setAppIcon(appIconUploadPath);
			projectListDao.updateProjectList(projectList);
			result.setCode(0);
			result.setMsg("SUCCESS");
		}else{
			result.setCode(1);
			result.setMsg("更新工程异常");
		}
	}

	/**
	 * 工程非空校验
	 * @param projectList
	 * @param result
	 */
	private void checkProjectListNotNull(ProjectList projectList, ResultJson result) {
		if(projectList==null){
			result.setCode(1);
			result.setMsg("请求参数空");
			return;
		}
		if(null==projectList.getProjectId()||"".equals(projectList.getProjectId())){
			result.setCode(1);
			result.setMsg("请求工程ID为空");
			return ;
		}
		if(null==projectList.getProjectName()||projectList.getProjectName().trim().isEmpty()){
			result.setCode(1);
			result.setMsg("请求工程名为空");
			return ;
		}
		if(null==projectList.getAppProjectName()||projectList.getAppProjectName().trim().isEmpty()){
			result.setCode(1);
			result.setMsg("请求工程应用名为空");
			return ;
		}
		if(null==projectList.getInstallationName()||projectList.getInstallationName().trim().isEmpty()){
			result.setCode(1);
			result.setMsg("请求安装包名为空");
			return ;
		}
		if(null==projectList.getAppName()||projectList.getAppName().trim().isEmpty()){
			result.setCode(1);
			result.setMsg("请求应用名为空");
			return ;
		}
	}

	@Override
	public void getProjectListDetail(Integer projectId, ResultJson result)throws Exception {
		if(projectId==null||"".equals(projectId)){
			result.setCode(1);
			result.setMsg("请求工程ID为空");
			return;
		}
		ProjectList projectList = projectListDao.getProjectListDetail(projectId);
		if(null!=projectList){
			//获取应用图标路径Base64解析字符串返回
			String appIconBase64 = ClientUtil.encodeBase64File(projectList.getAppIcon());
			projectList.setAppIcon(appIconBase64);
			result.setCode(0);
			result.setMsg("SUCCESS");
			result.setData(projectList);
		}else{
			result.setCode(0);
			result.setMsg("当前工程还未新建页面");
		}
	}

	@Override
	@Transactional
	public void copyProjectList(ProjectList projectList, ResultJson result) throws Exception {
		Integer oldProjectId = projectList.getProjectId();
		if(oldProjectId==null||"".equals(oldProjectId)){
			result.setCode(1);
			result.setMsg("请求工程ID为空");
			return;
		}
			checkProjectListNotNull(projectList,result);
			//应用图标保存路径
			String appIconUploadPath =androidConfig.getAppicon()+"a"+System.currentTimeMillis()+".png";
			String appIcon = projectList.getAppIcon();
			//将base64字符解码保存图片
			ClientUtil.decoderBase64File(appIcon,appIconUploadPath);
			//应用图标路径保存数据库
			projectList.setAppIcon(appIconUploadPath);

		projectList.setLastupdated(projectList.getCreater());
		projectList.setLastupdatedTime(new Date());
		projectListDao.addProjectList(projectList);
		
		String scheme = projectList.getScheme();
		//获取新工程ID
		Integer new_project_id = projectList.getProjectId();
		//分别根据工程ID和页面类型查询页面信息，判断(1、底部嵌套顶部,2、顶部嵌套底部)
		List<PageList> bott_pageLists = pageListDao.findBottNavigatorByProjectId(oldProjectId);
		if(bott_pageLists.size()<=1){ //底部嵌套顶部
			for (PageList pageList : bott_pageLists) {
				Integer pageId = pageList.getPageId();
				//复制页面
				pageList.setProjectId(new_project_id);
				pageList.setCreateDate(new Date());
				pageListDao.addPageList(pageList);
				Integer new_page_id = pageList.getPageId();
        		String bott_pageName = pageList.getPageName();
        		String bott_pagepath = pageList.getPagepath();
        		String bott_layoutfile = pageList.getLayoutfile();
        		if(bott_pageName!=null){
        			pageList.setPageName(bott_pageName.replace(bott_pageName.substring(bott_pageName.lastIndexOf("_")+1), new_page_id.toString()));
        		}
        		if(bott_pagepath!=null){
        			pageList.setPagepath(bott_pagepath.replace(bott_pagepath.substring(bott_pagepath.lastIndexOf("_")+1), new_page_id.toString()));
        		}
        		if(bott_layoutfile!=null){
        			pageList.setLayoutfile(bott_layoutfile.replace(bott_layoutfile.substring(bott_layoutfile.lastIndexOf("_")+1), new_page_id.toString())+".json");
        		}
				pageListDao.updatePageList(pageList);
				
				//复制控件
				List<ComponentList> bott_componentLists = componentListDao.findComponentByPageId(pageId);
				for (ComponentList bott_componentList : bott_componentLists) {
					bott_componentList.setProjectId(new_project_id);
					bott_componentList.setPageId(new_page_id);
					bott_componentList.setCreated(new Date());
					bott_componentList.setUpdated(bott_componentList.getCreated());
					componentListDao.addComponentList(bott_componentList);
					ObjectMapper mapper = new ObjectMapper();  
					//获取控件contenturl,
					String bott_params = bott_componentList.getParams();
					JsonNode jsonNode =  mapper.readTree(bott_params);
					
					List<JsonNode> tabs = jsonNode.findParents("label");
				      //往根节点中添加一个子对象
				      ObjectNode paramsNode = jsonNodeFactory.objectNode();
				      paramsNode.put("aligntop",jsonNode.findValue("aligntop").asBoolean())
				              .put("horizscroll", jsonNode.findValue("horizscroll").asBoolean())
				      		  .put("underlinetab",jsonNode.findValue("underlinetab").asBoolean())
				      		  .put("defaulttab", jsonNode.findValue("defaulttab").asInt());
				      //往子对象中添加一个数组
				      ArrayNode arrayNode = paramsNode.arrayNode();
					
					//增加导航控件取出label标题中文命名转换为拼音新建页面
			        for (JsonNode tab : tabs) {
			        	
			        	//拆分contenturl跳转的页面ID复制
			        	JsonNode contenturl_jsonNode = tab.findValue("contenturl");
			        	if(contenturl_jsonNode!=null){
			        		String contenturl = contenturl_jsonNode.asText();
			        		Integer contenturl_page_id = Integer.parseInt(contenturl.substring(contenturl.lastIndexOf("_")+1));
			        		PageList content_page = pageListDao.findPageListByPageId(contenturl_page_id);
			        		content_page.setNavPid(new_page_id);
			        		content_page.setProjectId(new_project_id);
			        		content_page.setCreateDate(new Date());
			        		pageListDao.addPageList(content_page);
			        		//更新页面
			        		Integer new_content_page_id = content_page.getPageId();
			        		String pageName = content_page.getPageName();
			        		String pagepath = content_page.getPagepath();
			        		String layoutfile = content_page.getLayoutfile();
			        		if(pageName!=null){
			        			content_page.setPageName(pageName.replace(pageName.substring(pageName.lastIndexOf("_")+1), new_content_page_id.toString()));
			        		}
			        		if(pagepath!=null){
			        			content_page.setPagepath(pagepath.replace(pagepath.substring(pagepath.lastIndexOf("_")+1), new_content_page_id.toString()));
			        		}
			        		if(layoutfile!=null){
			        			content_page.setLayoutfile(layoutfile.replace(layoutfile.substring(layoutfile.lastIndexOf("_")+1), new_content_page_id.toString())+".json");
			        		}
			        		
			        		pageListDao.updatePageList(content_page);
			        		
			        		//底部导航嵌套顶部导航
			        		List<ComponentList> top_components = componentListDao.findComponentByPageId(contenturl_page_id);
			        		if(top_components.size()!=0){
			        			for (ComponentList top_component : top_components) {
									//复制控件
			        				top_component.setProjectId(new_project_id);
			        				top_component.setPageId(new_content_page_id);
			        				top_component.setCreated(new Date());
			        				top_component.setUpdated(top_component.getCreated());
			    					componentListDao.addComponentList(top_component);
			    					
			    					//获取控件contenturl,
			    					ObjectMapper top_mapper = new ObjectMapper(); 
			    					String top_params = top_component.getParams();
			    					JsonNode top_jsonNode =  top_mapper.readTree(top_params);
			  				      //往根节点中添加一个子对象
			  				      ObjectNode top_paramsNode = jsonNodeFactory.objectNode();
			  				    top_paramsNode.put("aligntop",top_jsonNode.findValue("aligntop").asBoolean())
			  				              .put("horizscroll", top_jsonNode.findValue("horizscroll").asBoolean())
			  				      		  .put("underlinetab",top_jsonNode.findValue("underlinetab").asBoolean())
			  				      		  .put("defaulttab", top_jsonNode.findValue("defaulttab").asInt());
			  				      //往子对象中添加一个数组
			  				      ArrayNode top_arrayNode = top_paramsNode.arrayNode();
			  				      List<JsonNode> top_tabs = top_jsonNode.findParents("label");
			  				      for (JsonNode top_tab : top_tabs) {
			  				    	//拆分contenturl跳转的页面ID复制
			  			        	JsonNode top_contenturl_jsonNode = top_tab.findValue("contenturl");
			  			        		if(top_contenturl_jsonNode!=null){
			  				        		String top_contenturl = top_contenturl_jsonNode.asText();
			  				        		Integer top_contenturl_page_id = Integer.parseInt(top_contenturl.substring(top_contenturl.lastIndexOf("_")+1));
			  				        		PageList top_content_page = pageListDao.findPageListByPageId(top_contenturl_page_id);
			  				        		top_content_page.setNavPid(new_content_page_id);
			  				        		top_content_page.setProjectId(new_project_id);
			  				        		top_content_page.setCreateDate(new Date());
			  				        		pageListDao.addPageList(top_content_page);
			  				        		//更新页面
			  				        		Integer top_new_content_page_id = top_content_page.getPageId();
			  				        		String top_pageName = top_content_page.getPageName();
			  				        		String top_pagepath = top_content_page.getPagepath();
			  				        		String top_layoutfile = top_content_page.getLayoutfile();
			  				        		if(top_pageName!=null){
			  				        			top_content_page.setPageName(top_pageName.replace(top_pageName.substring(top_pageName.lastIndexOf("_")+1), top_new_content_page_id.toString()));
			  				        		}
			  				        		if(pagepath!=null){
			  				        			top_content_page.setPagepath(top_pagepath.replace(top_pagepath.substring(top_pagepath.lastIndexOf("_")+1), top_new_content_page_id.toString()));
			  				        		}
			  				        		if(layoutfile!=null){
			  				        			top_content_page.setLayoutfile(top_layoutfile.replace(top_layoutfile.substring(top_layoutfile.lastIndexOf("_")+1), top_new_content_page_id.toString())+".json");
			  				        		}
			  				        		
			  				        		pageListDao.updatePageList(top_content_page);
			  				        		//返回导航控件跳转页面地址
			  				        		String top_new_contenturl = scheme+"://load_vscroll_"+top_new_content_page_id;
			  				        		
			  				        		ObjectNode top_tabNode = jsonNodeFactory.objectNode();
			  				        		top_tabNode.put("label",tab.findValue("label").asText());
			  				        		top_tabNode.put("contenturl", top_new_contenturl);
			  				        		JsonNode selectedicon = top_tabNode.findValue("selectedicon");
			  				        		JsonNode unselectedicon = top_tabNode.findValue("unselectedicon");
			  				        		if(null!=selectedicon){
			  				        			top_tabNode.put("selectedicon", selectedicon.asText());
			  				        		}
			  				        		if(null!=unselectedicon){
			  				        			top_tabNode.put("unselectedicon", unselectedicon.asText());
			  				        		}
			  				        		top_arrayNode.add(top_tabNode);
			  				        		
			  				        		//复制底部导航嵌套的控件
			  				        		List<ComponentList> nest_components = componentListDao.findComponentByPageId(top_contenturl_page_id);
			  				        		for (ComponentList nest_component : nest_components) {
			  				        			nest_component.setProjectId(new_project_id);
			  				        			nest_component.setPageId(top_new_content_page_id);
			  				        			nest_component.setCreated(new Date());
			  				        			nest_component.setUpdated(nest_component.getCreated());
						    					componentListDao.addComponentList(nest_component);
											}
			  			        		}
			  				      	}
			  				      //
			  				      top_paramsNode.set("tabs", top_arrayNode);
							      //调用ObjectMapper的writeTree方法根据节点生成最终json字符串
							      String top_write_params = top_mapper.writeValueAsString(top_paramsNode);
							      top_component.setParams(top_write_params);
							      //更新控件
							      componentListDao.updateComponentList(top_component);

								}
			        		}
			        		
			        		//返回导航控件跳转页面地址
			        		String new_contenturl = scheme+"://load_hscroll_"+new_content_page_id;
			        		
			        		ObjectNode tabNode = jsonNodeFactory.objectNode();
			        		tabNode.put("label",tab.findValue("label").asText());
			        		tabNode.put("contenturl", new_contenturl);
			        		JsonNode selectedicon = jsonNode.findValue("selectedicon");
			        		JsonNode unselectedicon = jsonNode.findValue("unselectedicon");
			        		if(null!=selectedicon){
			        			tabNode.put("selectedicon", selectedicon.asText());
			        		}
			        		if(null!=unselectedicon){
			        			tabNode.put("unselectedicon", unselectedicon.asText());
			        		}
			        		arrayNode.add(tabNode);
			        	}
						
					}
				      paramsNode.set("tabs", arrayNode);
				      //调用ObjectMapper的writeTree方法根据节点生成最终json字符串
				      String params = mapper.writeValueAsString(paramsNode);
				      bott_componentList.setParams(params);
					//更新控件
					componentListDao.updateComponentList(bott_componentList);
				}
				
			}
		}
		
		List<PageList> top_pageLists = pageListDao.findTopNavigatorByProjectId(oldProjectId);
		if(top_pageLists.size()<=1){ //顶部嵌套底部
			for (PageList pageList : top_pageLists) {
				Integer pageId = pageList.getPageId();
				//复制页面
				pageList.setProjectId(new_project_id);
				pageList.setCreateDate(new Date());
				pageListDao.addPageList(pageList);
				Integer new_page_id = pageList.getPageId();
        		String top_pageName = pageList.getPageName();
        		String top_pagepath = pageList.getPagepath();
        		String top_layoutfile = pageList.getLayoutfile();
        		if(top_pageName!=null){
        			pageList.setPageName(top_pageName.replace(top_pageName.substring(top_pageName.lastIndexOf("_")+1), new_page_id.toString()));
        		}
        		if(top_pagepath!=null){
        			pageList.setPagepath(top_pagepath.replace(top_pagepath.substring(top_pagepath.lastIndexOf("_")+1), new_page_id.toString()));
        		}
        		if(top_layoutfile!=null){
        			pageList.setLayoutfile(top_layoutfile.replace(top_layoutfile.substring(top_layoutfile.lastIndexOf("_")+1), new_page_id.toString())+".json");
        		}
				pageListDao.updatePageList(pageList);
				
				//复制控件
				List<ComponentList> top_componentLists = componentListDao.findComponentByPageId(pageId);
				for (ComponentList top_componentList : top_componentLists) {
					top_componentList.setProjectId(new_project_id);
					top_componentList.setPageId(pageList.getPageId());
					top_componentList.setCreated(new Date());
					top_componentList.setUpdated(top_componentList.getCreated());
					componentListDao.addComponentList(top_componentList);
					//获取控件contenturl,
					ObjectMapper mapper = new ObjectMapper(); 
					String top_params = top_componentList.getParams();
					JsonNode jsonNode =  mapper.readTree(top_params);
					
					List<JsonNode> tabs = jsonNode.findParents("label");
				      //往根节点中添加一个子对象
				      ObjectNode paramsNode = jsonNodeFactory.objectNode();
				      paramsNode.put("aligntop",jsonNode.findValue("aligntop").asBoolean())
				              .put("horizscroll", jsonNode.findValue("horizscroll").asBoolean())
				      		  .put("underlinetab",jsonNode.findValue("underlinetab").asBoolean())
				      		  .put("defaulttab", jsonNode.findValue("defaulttab").asInt());
				      //往子对象中添加一个数组
				      ArrayNode arrayNode = paramsNode.arrayNode();
					
					//增加导航控件取出label标题中文命名转换为拼音新建页面
			        for (JsonNode tab : tabs) {
			        	
			        	//拆分contenturl跳转的页面ID复制
			        	JsonNode contenturl_jsonNode = tab.findValue("contenturl");
			        	if(contenturl_jsonNode!=null){
			        		String contenturl = contenturl_jsonNode.asText();
			        		Integer contenturl_page_id = Integer.parseInt(contenturl.substring(contenturl.lastIndexOf("_")+1));
			        		PageList content_page = pageListDao.findPageListByPageId(contenturl_page_id);
			        		content_page.setNavPid(pageList.getPageId());
			        		content_page.setProjectId(new_project_id);
			        		content_page.setCreateDate(new Date());
			        		pageListDao.addPageList(content_page);
			        		//更新页面
			        		Integer new_content_page_id = content_page.getPageId();
			        		String pageName = content_page.getPageName();
			        		String pagepath = content_page.getPagepath();
			        		String layoutfile = content_page.getLayoutfile();
			        		if(pageName!=null){
			        			content_page.setPageName(pageName.replace(pageName.substring(pageName.lastIndexOf("_")+1), new_content_page_id.toString()));
			        		}
			        		if(pagepath!=null){
			        			content_page.setPagepath(pagepath.replace(pagepath.substring(pagepath.lastIndexOf("_")+1), new_content_page_id.toString()));
			        		}
			        		if(layoutfile!=null){
			        			content_page.setLayoutfile(layoutfile.replace(layoutfile.substring(layoutfile.lastIndexOf("_")+1), new_content_page_id.toString())+".json");
			        		}
			        		
			        		pageListDao.updatePageList(content_page);
			        		
			        		//顶部导航嵌套底部导航
			        		List<ComponentList> bott_components = componentListDao.findComponentByPageId(contenturl_page_id);
			        		if(bott_components.size()!=0){
			        			for (ComponentList bott_component : bott_components) {
									//复制控件
			        				bott_component.setProjectId(new_project_id);
			        				bott_component.setPageId(new_content_page_id);
			        				bott_component.setCreated(new Date());
			        				bott_component.setUpdated(bott_component.getCreated());
			    					componentListDao.addComponentList(bott_component);
			    					
			    					//获取控件contenturl,
			    					ObjectMapper bott_mapper = new ObjectMapper(); 
			    					String bott_params = bott_component.getParams();
			    					JsonNode bott_jsonNode =  bott_mapper.readTree(bott_params);
			  				      //往根节点中添加一个子对象
			  				      ObjectNode bott_paramsNode = jsonNodeFactory.objectNode();
			  				    bott_paramsNode.put("aligntop",bott_jsonNode.findValue("aligntop").asBoolean())
			  				              .put("horizscroll", bott_jsonNode.findValue("horizscroll").asBoolean())
			  				      		  .put("underlinetab",bott_jsonNode.findValue("underlinetab").asBoolean())
			  				      		  .put("defaulttab", bott_jsonNode.findValue("defaulttab").asInt());
			  				      //往子对象中添加一个数组
			  				      ArrayNode bott_arrayNode = bott_paramsNode.arrayNode();
			  				      List<JsonNode> bott_tabs = bott_jsonNode.findParents("label");
			  				      for (JsonNode bott_tab : bott_tabs) {
			  				    	//拆分contenturl跳转的页面ID复制
			  			        	JsonNode bott_contenturl_jsonNode = bott_tab.findValue("contenturl");
			  			        		if(bott_contenturl_jsonNode!=null){
			  				        		String bott_contenturl = bott_contenturl_jsonNode.asText();
			  				        		Integer bott_contenturl_page_id = Integer.parseInt(bott_contenturl.substring(bott_contenturl.lastIndexOf("_")+1));
			  				        		PageList bott_content_page = pageListDao.findPageListByPageId(bott_contenturl_page_id);
			  				        		bott_content_page.setNavPid(new_content_page_id);
			  				        		bott_content_page.setProjectId(new_project_id);
			  				        		bott_content_page.setCreateDate(new Date());
			  				        		pageListDao.addPageList(bott_content_page);
			  				        		//更新页面
			  				        		Integer bott_new_content_page_id = bott_content_page.getPageId();
			  				        		String bott_pageName = bott_content_page.getPageName();
			  				        		String bott_pagepath = bott_content_page.getPagepath();
			  				        		String bott_layoutfile = bott_content_page.getLayoutfile();
			  				        		if(pageName!=null){
			  				        			bott_content_page.setPageName(bott_pageName.replace(bott_pageName.substring(bott_pageName.lastIndexOf("_")+1), bott_new_content_page_id.toString()));
			  				        		}
			  				        		if(pagepath!=null){
			  				        			bott_content_page.setPagepath(bott_pagepath.replace(bott_pagepath.substring(bott_pagepath.lastIndexOf("_")+1), bott_new_content_page_id.toString()));
			  				        		}
			  				        		if(layoutfile!=null){
			  				        			bott_content_page.setLayoutfile(bott_layoutfile.replace(bott_layoutfile.substring(bott_layoutfile.lastIndexOf("_")+1), bott_new_content_page_id.toString())+".json");
			  				        		}
			  				        		
			  				        		pageListDao.updatePageList(bott_content_page);
			  				        		//返回导航控件跳转页面地址
			  				        		String bott_new_contenturl = scheme+"://load_vscroll_"+bott_new_content_page_id;
			  				        		
			  				        		ObjectNode bott_tabNode = jsonNodeFactory.objectNode();
			  				        		bott_tabNode.put("label",tab.findValue("label").asText());
			  				        		bott_tabNode.put("contenturl", bott_new_contenturl);
			  				        		JsonNode selectedicon = bott_tabNode.findValue("selectedicon");
			  				        		JsonNode unselectedicon = bott_tabNode.findValue("unselectedicon");
			  				        		if(null!=selectedicon){
			  				        			bott_tabNode.put("selectedicon", selectedicon.asText());
			  				        		}
			  				        		if(null!=unselectedicon){
			  				        			bott_tabNode.put("unselectedicon", unselectedicon.asText());
			  				        		}
			  				        		bott_arrayNode.add(bott_tabNode);
			  				        		
			  				        		//复制底部导航嵌套的控件
			  				        		List<ComponentList> nest_components = componentListDao.findComponentByPageId(bott_contenturl_page_id);
			  				        		for (ComponentList nest_component : nest_components) {
			  				        			nest_component.setProjectId(new_project_id);
			  				        			nest_component.setPageId(bott_new_content_page_id);
			  				        			nest_component.setCreated(new Date());
			  				        			nest_component.setUpdated(nest_component.getCreated());
						    					componentListDao.addComponentList(nest_component);
											}
			  			        		}
			  				      	}
			  				      //
			  				      bott_paramsNode.set("tabs", bott_arrayNode);
							      //调用ObjectMapper的writeTree方法根据节点生成最终json字符串
							      String bott_write_params = bott_mapper.writeValueAsString(bott_paramsNode);
							      bott_component.setParams(bott_write_params);
							      //更新控件
							      componentListDao.updateComponentList(bott_component);

								}
			        		}
			        		
			        		//返回导航控件跳转页面地址
			        		String new_contenturl = scheme+"://load_vscroll_"+new_content_page_id;
			        		
			        		ObjectNode tabNode = jsonNodeFactory.objectNode();
			        		tabNode.put("label",tab.findValue("label").asText());
			        		tabNode.put("contenturl", new_contenturl);
			        		JsonNode selectedicon = jsonNode.findValue("selectedicon");
			        		JsonNode unselectedicon = jsonNode.findValue("unselectedicon");
			        		if(null!=selectedicon){
			        			tabNode.put("selectedicon", selectedicon.asText());
			        		}
			        		if(null!=unselectedicon){
			        			tabNode.put("unselectedicon", unselectedicon.asText());
			        		}
			        		arrayNode.add(tabNode);
			        	}
						
					}
				      paramsNode.set("tabs", arrayNode);
				      //调用ObjectMapper的writeTree方法根据节点生成最终json字符串
				      String params = mapper.writeValueAsString(paramsNode);
				      top_componentList.setParams(params);
					//更新控件
					componentListDao.updateComponentList(top_componentList);
				}
				
			}
		}
		
		List<PageList> common_pageLists = pageListDao.findCommonNavigatorByProjectId(oldProjectId);
		if(common_pageLists.size()!=0){
			for (PageList pageList : common_pageLists) {
				Integer pageId = pageList.getPageId();
				//复制页面
				pageList.setProjectId(new_project_id);
				pageList.setCreateDate(new Date());
				pageListDao.addPageList(pageList);
				Integer new_page_id = pageList.getPageId();
        		String pageName = pageList.getPageName();
        		String pagepath = pageList.getPagepath();
        		String layoutfile = pageList.getLayoutfile();
        		if(pageName!=null){
        			pageList.setPageName(pageName.replace(pageName.substring(pageName.lastIndexOf("_")+1), new_page_id.toString()));
        		}
        		if(pagepath!=null){
        			pageList.setPagepath(pagepath.replace(pagepath.substring(pagepath.lastIndexOf("_")+1), new_page_id.toString()));
        		}
        		if(layoutfile!=null){
        			pageList.setLayoutfile(layoutfile.replace(layoutfile.substring(layoutfile.lastIndexOf("_")+1), new_page_id.toString())+".json");
        		}
				pageListDao.updatePageList(pageList);
				
				
				//复制控件
				List<ComponentList> common_componentLists = componentListDao.findComponentByPageId(pageId);
				for (ComponentList common_componentList : common_componentLists) {
					common_componentList.setProjectId(new_project_id);
					common_componentList.setPageId(new_page_id);
					common_componentList.setCreated(new Date());
					common_componentList.setUpdated(common_componentList.getCreated());
					componentListDao.addComponentList(common_componentList);
				}
				
			}
		}

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("projectId", new_project_id);
		result.setCode(0);
		result.setMsg("SUCCESS");
		result.setData(map);
	}

	@Override
	@Transactional
	public void deleteProjectList(Integer projectId, ResultJson result) throws Exception {
		if(projectId==0||"".equals(projectId)){
			result.setCode(1);
			result.setMsg("请求工程ID为空");
			return;
		}
		ProjectList projectList = projectListDao.getProjectListDetail(projectId);
		if(projectList!=null){
			//删除图标
			String appIconPath = projectList.getAppIcon();
			if(!"".trim().equals(appIconPath)&&appIconPath!=null){
				ClientUtil.deleteImage(appIconPath);
			}
		}
		//分别根据工程ID和页面类型查询页面信息，判断(1、底部嵌套顶部,2、顶部嵌套底部)
		List<PageList> bott_pageLists = pageListDao.findBottNavigatorByProjectId(projectId);
		if(bott_pageLists.size()!=0){
			for (PageList pageList : bott_pageLists) {
				Integer pageId = pageList.getPageId();
				//删除页面先删除页面下控件
				List<ComponentList> components = componentListDao.findComponentByPageId(pageId);
				for (ComponentList component : components) {
					String params = component.getParams();
					ObjectMapper mapper = new ObjectMapper();  
			        JsonNode jsonNode =  mapper.readTree(params);
					//底部导航false
			        if(jsonNode.findValue("aligntop")!=null&&!jsonNode.findValue("aligntop").asBoolean()){
						//获得底部导航所新建的页面
						List<Integer> bott_pageIds = pageListDao.getPageIdByPageId(pageId);
						if(bott_pageIds.size()!=0){
							for (Integer bott_pageId : bott_pageIds) {
								//查询控件
								List<ComponentList> componentLists = componentListDao.findComponentByPageId(bott_pageId);
								//判断底部导航是否嵌套顶部导航控件
								for (ComponentList nest_component : componentLists) {
									JsonNode nest_jsonNode =  mapper.readTree(nest_component.getParams());
									if(nest_jsonNode.findValue("aligntop")!=null&&nest_jsonNode.findValue("aligntop").asBoolean()){ 
										//true 有底部嵌套顶部导航控件
										//获得顶部导航所新建的页面
										List<Integer> top_pageIds = pageListDao.getPageIdByPageId(bott_pageId);
										for (Integer top_pageId: top_pageIds) {
											//根据页面ID删除控件
											componentListDao.deleteComponentByPageId(top_pageId);
											//删除顶部导航所新建页面
											pageListDao.deletePageInfoByPageId(top_pageId);
										}
									}
									//否则底部导航没有嵌套顶部导航
									componentListDao.deleteComponentList(nest_component.getComponentId());
								}
								//删除底部导航所新建页面
								pageListDao.deletePageInfoByPageId(bott_pageId);
							}
							
						}
						
			        }
			        componentListDao.deleteComponentList(component.getComponentId());
				}
				//删除页面
				pageListDao.deletePageListByPageId(projectId, pageId);
			}
		}
		List<PageList> top_pageLists = pageListDao.findTopNavigatorByProjectId(projectId);
		if(top_pageLists.size()!=0){
			for (PageList pageList : top_pageLists) {
				Integer pageId = pageList.getPageId();
				//删除页面先删除页面下控件
				List<ComponentList> components = componentListDao.findComponentByPageId(pageId);
				for (ComponentList component : components) {
					String params = component.getParams();
					ObjectMapper mapper = new ObjectMapper();  
			        JsonNode jsonNode =  mapper.readTree(params);
					//顶部导航true
			        if(jsonNode.findValue("aligntop")!=null&&jsonNode.findValue("aligntop").asBoolean()){
			        	//获得顶部导航所新建的页面
						List<Integer> top_pageIds = pageListDao.getPageIdByPageId(pageId);
						if(top_pageIds.size()!=0){
							for (Integer top_pageId : top_pageIds) {
								//查询控件
								List<ComponentList> componentLists = componentListDao.findComponentByPageId(top_pageId);
								//判断底部导航是否嵌套顶部导航控件
								for (ComponentList nest_component : componentLists) {
									JsonNode nest_jsonNode =  mapper.readTree(nest_component.getParams());
									if(nest_jsonNode.findValue("aligntop")!=null&&!nest_jsonNode.findValue("aligntop").asBoolean()){ 
										//false 有顶部嵌套底部导航控件
										//获得底部导航所新建的页面
										List<Integer> bott_pageIds = pageListDao.getPageIdByPageId(top_pageId);
										for (Integer bott_pageId: bott_pageIds) {
											//根据页面ID删除控件
											componentListDao.deleteComponentByPageId(bott_pageId);
											//删除底部导航所新建页面
											pageListDao.deletePageInfoByPageId(bott_pageId);
										}
									}
									//否则顶部导航没有嵌套底部导航
									componentListDao.deleteComponentList(nest_component.getComponentId());
								}
								//删除顶部导航所新建页面
								pageListDao.deletePageInfoByPageId(top_pageId);
							}
							
						}
			        }
			        componentListDao.deleteComponentList(component.getComponentId());
				}
				//删除页面
				pageListDao.deletePageListByPageId(projectId, pageId);
			}
		}
		List<PageList> common_pageLists = pageListDao.findCommonNavigatorByProjectId(projectId);
		if(common_pageLists.size()!=0){
			for (PageList pageList : common_pageLists) {
				Integer pageId = pageList.getPageId();
				//删除页面先删除页面下控件
				List<ComponentList> components = componentListDao.findComponentByPageId(pageId);
				for (ComponentList component : components) {
					componentListDao.deleteComponentList(component.getComponentId());
				}
				//删除页面
				pageListDao.deletePageListByPageId(projectId, pageId);
			}
		}
		//删除工程列表
		projectListDao.deleteProjectListByProjectId(projectId);

		result.setCode(0);
		result.setMsg("SUCCESS");

	}


	/**
	 * 生成Android安装包
	 * @param projectId
	 * @param result
	 */
	@Override
	public void ProjectGenerateAndroidAppPackage(final Integer projectId, String username,ResultJson result) throws Exception{
		//生成安装包前先清空原来生成的工程的数据
		//删除工程数据包
		File project_zip_Path = new File(androidConfig.getDeleteProjectZipPath());
		if(project_zip_Path.exists()){
			boolean p = ClientUtil.deleteDir(project_zip_Path);
			if(!p){
				logger.info("清空工程数据包");
			}
		}
		//删除安卓解压后的工程数据包
		File project_Path = new File(androidConfig.getDeleteProjectPath());
		if(project_Path.exists()){
			boolean p = ClientUtil.deleteDir(project_Path);
			if(!p){
				logger.info("清空解压工程数据");
			}
		}
		//删除目标工程
		File target_ProjectPath = new File(androidConfig.getDesOut());
		if(target_ProjectPath.exists()){
			boolean target = ClientUtil.deleteDir(target_ProjectPath);
			if(!target){
				logger.info("清空目标工程");
			}
		}
		//删除资源工程路径
		File resources_Path = new File(androidConfig.getProjectresourcesPath());
		if(resources_Path.exists()){
			boolean p = ClientUtil.deleteDir(resources_Path);
			if(!p){
				logger.info("清空源工程数据包");
			}
		}
		//删除工程已生成安装包路径
		File oldapk_Path = new File(androidConfig.getDeleteProjectApkPackagePath());
		if(oldapk_Path.exists()){
			boolean p = ClientUtil.deleteDir(oldapk_Path);
			if(!p){
				logger.info("清空工程安装包");
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		String scheme = projectListDao.findProjectListSchemeByProjectId(projectId);
		//查询页面首页
		PageList shouyePage = pageListDao.findPageIndexIdByProjectId(projectId);
		Integer shouyePageId = shouyePage.getPageId();
		//获取工程页面输出pagelayout目录
		getProjectpagesByprojectId(projectId,scheme);
		//根据工程ID查询当前工程下所有普通页面,不包括首页
		ProjectListJson projectListjson = projectListDao.findProjectListJson(projectId);
		//此名字应转换为大写，满足java类名命名规范
		projectListjson.setApplication( projectListjson.getApplication().toUpperCase());
		List<CommonInterface> commonInterfaces = projectListjson.getCommon_interfaces();
		//输出project.json文件的普通页面集合定义
		List<CommonInterface> common_Interfaces = new ArrayList<CommonInterface>();
		//判断首页是否存在
		if(null!=shouyePage){
			CommonInterface shouCommonInterface = new CommonInterface();
			shouCommonInterface.setCaption(shouyePage.getPageTitle());
			shouCommonInterface.setCaptionTrans(ClientUtil.ChineseTranslation(shouyePage.getPageTitle()));
			shouCommonInterface.setIsindex(true);
			//判断layoutfile是否为空
			if(!"".trim().equals(shouyePage.getLayoutfile())||!shouyePage.getLayoutfile().equals(null)){
				shouCommonInterface.setLayoutfile(shouyePage.getLayoutfile());
			}
			shouCommonInterface.setLayoutfile(ClientUtil.ChineseTranslation(shouyePage.getPageTitle())+"_"+shouyePageId+".json");
			shouCommonInterface.setPageid(shouyePage.getPageId());
			if(!"".trim().equals(shouyePage.getPagepath())||!shouyePage.getPagepath().equals(null)){

				shouCommonInterface.setPagepath(shouyePage.getPagepath());
			}
			if(shouyePage.getNavigation()!=0){ //首页有导航控件
				String uitype = "hscroll";
				shouCommonInterface.setUitype(uitype);
				shouCommonInterface.setPagepath(scheme+"://load_"+uitype+"_"+shouyePageId);
			}
			if(shouyePage.getNavigation()==0){ //首页没有导航
				String uitype = "vscroll";
				shouCommonInterface.setUitype(uitype);
				shouCommonInterface.setPagepath(scheme+"://load_"+uitype+"_"+shouyePageId);
			}
			shouCommonInterface.setProjectId(projectId);

			common_Interfaces.add(shouCommonInterface);
		}

		if(commonInterfaces.size()!=0){
			for (CommonInterface commonInterface : commonInterfaces) {
				if(commonInterface!=null){
					String caption = ClientUtil.ChineseTranslation(commonInterface.getCaption());
					//根据首页ID查询控件，判断首页里面的控件有没有底部导航，true
					//更新uitype\pagepath\layoutfile
					Integer id = commonInterface.getPageid();
					commonInterface.setCaptionTrans(caption.toUpperCase()+"_"+id);
					if(commonInterface.getNavigation()==1){ //有导航控件
						commonInterface.setUitype("hscroll");
						commonInterface.setLayoutfile(caption+"_"+id+".json");
						commonInterface.setPagepath(scheme+"://load_hscroll_"+id);
						commonInterface.setIsindex(false);
					}
					if(commonInterface.getNavigation()==0){ //没有导航控件
						commonInterface.setUitype("vscroll");
						commonInterface.setLayoutfile(caption+"_"+id+".json");
						commonInterface.setPagepath(scheme+"://load_vscroll_"+id);
						commonInterface.setIsindex(false);
					}
					common_Interfaces.add(commonInterface);
				}
			}
		}
		projectListjson.setCommon_interface(common_Interfaces);
		//获取应用图标path
		File launchiconPath = new File(projectListjson.getLaunchicon());
		String appImgName = launchiconPath.getName();
		copyAppIconImage(launchiconPath, appImgName);
		//过滤png结尾字符串
		String imgName = appImgName.substring(0,appImgName.lastIndexOf("."));
		projectListjson.setLaunchicon(imgName);
		String packageName = projectListjson.getPackageName();
		//判断安装包名是否符合安卓包规范
		int n = packageName.indexOf(".",0);
		if(n==-1){
			String modifyPackageName = "com.aspire."+packageName;
			projectListjson.setPackageName(modifyPackageName);
		}
		SplashInterface splashInterface = new SplashInterface();
		LoginInterface loginInterface = new LoginInterface();
		//登录
		Integer login = projectListDao.findPageByPageType(projectId,1);
		//闪屏
		Integer splash= projectListDao.findPageByPageType(projectId,2);
		if(null!=login&&login!=-1){ //用户自定义登录，闪屏跳转登录
			loginInterface.setPagepath(scheme+"://login_userlogin");
			loginInterface.setLoginurl("");
			loginInterface.setJumpurl(scheme+"://load_hscroll_"+shouyePageId);
			if(null!=splash&&splash!=-1){ //用户自定义闪屏
				ComponentList componentList = componentListDao.findSplashComponentByPageId(splash);
				String params = componentList.getParams();
				JsonNode jsonNode =  mapper.readTree(params);
				String splashimage = jsonNode.findValue("splashimage").asText();
				String filterImageName = "";
				if(!splashimage.trim().isEmpty()){
					//自定义闪屏图片
					//去资源共享路径下复制闪屏图
					//截取文件名
					String imageName = splashimage.substring(splashimage.lastIndexOf("/")+1);
					String _dateStr = splashimage.substring((splashimage.lastIndexOf("icon/")+5),splashimage.lastIndexOf("/i"));

					File splashImagePath = new File( androidConfig.getSplashImagePath()+File.separator+_dateStr+File.separator+imageName);
					copyAppIconImage(splashImagePath, imageName);
					//过滤掉png字符设置闪屏接口返回
					filterImageName = imageName.substring(0,imageName.lastIndexOf("."));
					splashInterface.setSplashimage(filterImageName);
				}
				if(splashimage.trim().equals("")){
					splashInterface.setSplashimage("splashimage_default");//闪屏图为空，提供项目下默认图
				}
				splashInterface.setSplashimage(filterImageName);
				splashInterface.setJumpurl(scheme+"://login_userlogin");
				splashInterface.setSplashtime(1000);
			}

			if(splash==null||splash==-1){
				splashInterface.setSplashimage("splashimage_default");
				splashInterface.setJumpurl(scheme+"://load_hscroll_"+shouyePageId);
				splashInterface.setSplashtime(1000);
			}
		}

		if(null==login){
			loginInterface.setPagepath("");
			loginInterface.setLoginurl("");
			loginInterface.setJumpurl("");
			loginInterface.setLayout("");
			if(null!=splash&&splash!=-1){ //用户自定义闪屏
				ComponentList componentList = componentListDao.findSplashComponentByPageId(splash);
				String params = componentList.getParams();
				JsonNode jsonNode =  mapper.readTree(params);
				String splashimage = jsonNode.findValue("splashimage").asText();
				String filterImageName = "";
				if(!splashimage.trim().isEmpty()){
					//自定义闪屏图片
					//去资源共享路径下复制闪屏图
					//截取文件名
					String imageName = splashimage.substring(splashimage.lastIndexOf("/")+1);
					String _dateStr = splashimage.substring((splashimage.lastIndexOf("icon/")+5),splashimage.lastIndexOf("/i"));
					File splashImagePath = new File( androidConfig.getSplashImagePath()+File.separator+_dateStr+File.separator+imageName);
					copyAppIconImage(splashImagePath, imageName);
					//过滤掉png字符设置闪屏接口返回
					filterImageName = imageName.substring(0,imageName.lastIndexOf("."));
					splashInterface.setSplashimage(filterImageName);
				}
				if(splashimage.trim().equals("")){
					splashInterface.setSplashimage("splashimage_default");//闪屏图空，提供默认
				}
				splashInterface.setSplashimage(filterImageName);
				splashInterface.setJumpurl(scheme+"://load_hscroll_"+shouyePageId);
				splashInterface.setSplashtime(1000);
			}
			if(splash==null||splash==-1){
				splashInterface.setSplashimage("splashimage_default");
				splashInterface.setJumpurl(scheme+"://load_hscroll_"+shouyePageId);
				splashInterface.setSplashtime(1000);
			}
		}
		projectListjson.setLogin_interface(loginInterface);
		projectListjson.setSplash_interface(splashInterface);
		//输出project.json
		boolean  projectIsgenratok = outProjectJsonInfo(projectListjson);
		if(!projectIsgenratok){
			result.setCode(1);
			result.setMsg("生成工程数据包异常");
			return;
		}


		String cmd = androidConfig.getCmd();
		String anLogFile = androidConfig.getLogfile();
		logger.info(cmd);

		ExecutorService threadPool = Executors.newCachedThreadPool();
		Future<String> future = threadPool.submit(new AndroidTaskCallable(projectId,cmd,anLogFile));

		String message = future.get();

		logger.info("str = "+message);

		threadPool.shutdown();

		if (message.equals("SUCCESS")){
					//安装包保存数据库
					getApkPackageInfo(projectId,username);

					result.setCode(0);
					result.setMsg("SUCCESS");
				}else {
					result.setCode(1);
					result.setMsg("ERROR");
				}


	}
	
	/**
	 * 根据工程ID生成安装包获取信息上传并保存数据库
	 * @param projectId
	 */
	private void getApkPackageInfo(Integer projectId,String username) throws Exception{
		//String apkoutPath = ClientUtil.loadFilePath("apkout");
		String apk_Name = ClientUtil.getFile(androidConfig.getApkout());
		//获取apk包上传ftp
		String apkdownUrl = attachmentService.saveApkToFtp(apk_Name);
		//安装包信息保存数据库
		ProjectList plist = projectListDao.getProjectListDetail(projectId);
		PackageList packageList = new PackageList();
		packageList.setProjectId(projectId);
		packageList.setApkName(plist.getInstallationName());
		packageList.setProjectName(plist.getProjectName());
		packageList.setCreater(username);
		packageList.setLastModifier(packageList.getCreater());
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sdf.format(date);
		packageList.setPackTime(str);
		packageList.setAndroidDownloadUrl(apkdownUrl);
		packageList.setUserId(plist.getUserId());
		projectPackageListDao.addPackageListByUserIdAndProjectId(packageList);
	}

	/**
	 * android 规范
	 * 根据工程ID获取所有的页面控件信息
	 * @param projectId
	 * @throws IOException 
	 * @throws Exception
	 */
	private void getProjectpagesByprojectId(Integer projectId,String scheme) throws IOException {
		//获取工程ID下的所有页面数据
		List<PageList> allPage = pageListDao.findAllPageListByProjectId(projectId);
		//获取页面输出路径
		//File pageOutDir = new File(ClientUtil.loadFilePath("pagelayoutPath"));
		File pageOutDir = new File(androidConfig.getPagelayoutPath());
		//判断页面路径是否存在
		if(!pageOutDir.exists()){
			pageOutDir.mkdirs();
		}
		//工程ID页面数据封装pagelayout对象中
		for (PageList pageList : allPage) {
			Integer pageId = pageList.getPageId();
			String caption = pageList.getPageTitle();
			if(pageList.getNavigation()==0){
				pageList.setPagepath(pageList.getPagepath().replace("hscroll", "vscroll"));
			}
			if(null!=pageList.getNavigation()&&pageList.getNavigation()!=0){
				pageList.setPagepath(pageList.getPagepath().replace("vscroll", "hscroll"));
			}
			String changeCaption = ClientUtil.ChineseTranslation(caption);
			//排除登录闪屏界面输出到pagelayout目录下
			int pageType = pageList.getPageType();
			if(null==pageId){
				continue;
			}
			if(pageType==1||pageType==2){
				continue;
			}
			//创建页面输出对象的所有控件集合
			List<com.aspire.component.ComponentList> componentLists = new ArrayList<com.aspire.component.ComponentList>();
			//根据页面ID获取页面下所有控件
			List<ComponentList> components = componentListDao.findComponentByPageId(pageId);
			for (ComponentList component : components) {
				String type = component.getType();
				com.aspire.component.ComponentList<T> viewComponent = new com.aspire.component.ComponentList<T>();
				viewComponent.setName("");
				viewComponent.setType(type);
				if(type.trim().equals("SingleBanner")){
					String jsonParams = component.getParams();
					com.aspire.component.singlebanner.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.singlebanner.Params.class);
					String contenturl = params.getContenturl();
					if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
						Integer id =ClientUtil.charCutChangeInteger(contenturl);
						if(id!=null){
							String uitype = pageListDao.findUitypeByPageId(id);
							if(uitype.trim().equals("hscroll")){
								if(contenturl.contains("vscroll")){
									params.setContenturl(contenturl.replace("vscroll", "hscroll"));
								}
							}
						}
					}
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("HorizScrollBanner")){
					String jsonParams = component.getParams();
					com.aspire.component.horizScrollbanner.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.horizScrollbanner.Params.class);
					List<com.aspire.component.horizScrollbanner.Banner> banners = params.getItems();
					for (com.aspire.component.horizScrollbanner.Banner banner : banners) {
						String contenturl = banner.getContenturl();
						if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
							Integer id =ClientUtil.charCutChangeInteger(banner.getContenturl());
							if(id!=null){
								String uitype = pageListDao.findUitypeByPageId(id);
								if(uitype.trim().equals("hscroll")){
									if(contenturl.contains("vscroll")){
										banner.setContenturl(contenturl.replace("vscroll", "hscroll"));
									}
								}
							}
						}
					}
					params.setItems(banners);
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("Entries")){
					String jsonParams = component.getParams();
					com.aspire.component.entries.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.entries.Params.class);
					List<com.aspire.component.entries.Entry> entrys = params.getItems();
					for (com.aspire.component.entries.Entry entry : entrys) {
						String contenturl = entry.getContenturl();
						if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
							Integer id =ClientUtil.charCutChangeInteger(entry.getContenturl());
							if(id!=null){
								String uitype = pageListDao.findUitypeByPageId(id);
								if(uitype.trim().equals("hscroll")){
									if(contenturl.contains("vscroll")){
										entry.setContenturl(contenturl.replace("vscroll", "hscroll"));
									}
								}
							}
						}
					}
					params.setItems(entrys);
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("DividerLine")){
					String jsonParams = component.getParams();
					com.aspire.component.dividerline.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.dividerline.Params.class);
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("TripleBanner")){
					String jsonParams = component.getParams();
					com.aspire.component.triplebanner.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.triplebanner.Params.class);
					List<com.aspire.component.triplebanner.Banner> banners = params.getItems();
					for (com.aspire.component.triplebanner.Banner banner : banners) {
						String contenturl = banner.getContenturl();
						if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
							Integer id =ClientUtil.charCutChangeInteger(banner.getContenturl());
							if(id!=null){
								String uitype = pageListDao.findUitypeByPageId(id);
								if(uitype.trim().equals("hscroll")){
									if(contenturl.contains("vscroll")){
										banner.setContenturl(contenturl.replace("vscroll", "hscroll"));
									}
								}
							}
						}
					}
					params.setItems(banners);
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("SquareIcon1LineText")){
					String jsonParams = component.getParams();		
					com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
					String contenturl = params.getContenturl();
					if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
						Integer id =ClientUtil.charCutChangeInteger(params.getContenturl());
						if(id!=null){
							String uitype = pageListDao.findUitypeByPageId(id);
							if(uitype.trim().equals("hscroll")){
								if(contenturl.contains("vscroll")){
									params.setContenturl(contenturl.replace("vscroll", "hscroll"));
								}
							}
						}
					}
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("SquareIcon2LinesText")){
					String jsonParams = component.getParams();		
					com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
					String contenturl = params.getContenturl();
					if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
						Integer id =ClientUtil.charCutChangeInteger(params.getContenturl());
						if(id!=null){
							String uitype = pageListDao.findUitypeByPageId(id);
							if(uitype.trim().equals("hscroll")){
								if(contenturl.contains("vscroll")){
									params.setContenturl(contenturl.replace("vscroll", "hscroll"));
								}
							}
						}
					}
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("SquareIcon3LinesText")){
					String jsonParams = component.getParams();		
					com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
					String contenturl = params.getContenturl();
					if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
						Integer id =ClientUtil.charCutChangeInteger(params.getContenturl());
						if(id!=null){
							String uitype = pageListDao.findUitypeByPageId(id);
							if(uitype.trim().equals("hscroll")){
								if(contenturl.contains("vscroll")){
									params.setContenturl(contenturl.replace("vscroll", "hscroll"));
								}
							}
						}
					}
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("CircleIcon1LineText")){
					String jsonParams = component.getParams();		
					com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
					String contenturl = params.getContenturl();
					if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
						Integer id =ClientUtil.charCutChangeInteger(params.getContenturl());
						if(id!=null){
							String uitype = pageListDao.findUitypeByPageId(id);
							if(uitype.trim().equals("hscroll")){
								if(contenturl.contains("vscroll")){
									params.setContenturl(contenturl.replace("vscroll", "hscroll"));
								}
							}
						}
					}
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("CircleIcon2LinesText")){
					String jsonParams = component.getParams();		
					com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
					String contenturl = params.getContenturl();
					if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
						Integer id =ClientUtil.charCutChangeInteger(params.getContenturl());
						if(id!=null){
							String uitype = pageListDao.findUitypeByPageId(id);
							if(uitype.trim().equals("hscroll")){
								if(contenturl.contains("vscroll")){
									params.setContenturl(contenturl.replace("vscroll", "hscroll"));
								}
							}
						}
					}
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("CircleIcon3LinesText")){
					String jsonParams = component.getParams();		
					com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
					String contenturl = params.getContenturl();
					if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
						Integer id =ClientUtil.charCutChangeInteger(params.getContenturl());
						if(id!=null){
							String uitype = pageListDao.findUitypeByPageId(id);
							if(uitype.trim().equals("hscroll")){
								if(contenturl.contains("vscroll")){
									params.setContenturl(contenturl.replace("vscroll", "hscroll"));
								}
							}
						}
					}
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
					
				}else if(type.trim().equals("TabNavigator")){
					String jsonParams = component.getParams();	
					com.aspire.component.tabnavigator.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.tabnavigator.Params.class);
					List<com.aspire.component.tabnavigator.TabSpec> tabs = params.getTabs();
					//判断selected和unselected选中未选中图片地址是否提供
					if(null!=tabs&&tabs.size()!=0){
						for (com.aspire.component.tabnavigator.TabSpec tab : tabs) {
							String contenturl = tab.getContenturl();
							if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
								Integer id =ClientUtil.charCutChangeInteger(contenturl);
								if(id!=null){
									String uitype = pageListDao.findUitypeByPageId(id);
									if(uitype.trim().equals("hscroll")){
										if(contenturl.contains("vscroll")){
											String newContenturl = scheme+"://load_hscroll_"+id;
											tab.setContenturl(newContenturl);
										}
									}
								}
							}
							String selectedicon = tab.getSelectedicon();
							String unselectedicon = tab.getUnselectedicon();
							if(null!=selectedicon&&!selectedicon.isEmpty()){
								if(selectedicon.startsWith("/smartclient/")||selectedicon.startsWith("/static/")){ //true默认提供
									//String iconImagePath = ClientUtil.loadFilePath("defaultSelectedIconImagePath");
									File source = new File(androidConfig.getDefaultSelectedIconImagePath());
									String iconName = ClientUtil.FindAndReplaceChar(source.getName());
									copyAppIconImage(source,iconName );
									//过滤.png结尾，只保留图片名称写入
									tab.setSelectedicon(iconName.substring(0, iconName.lastIndexOf(".")));
								}
								if(selectedicon.startsWith("http://")){ //自定义图片
									String _selIconPath = tab.getSelectedicon();
									//String iconImagepath = androidConfig.getUserDefinedSelectedIconImagePath()+selectediconimagename;
									String abs_sel_ico_path = _selIconPath.replace(imageCallPath,uploadImageUrl);
									String sel_ico_name = abs_sel_ico_path.substring(abs_sel_ico_path.lastIndexOf(String.valueOf(File.separator))+1);
									File sourceImagepath = new File(abs_sel_ico_path);
									copyAppIconImage(sourceImagepath, sel_ico_name);
									tab.setSelectedicon(sel_ico_name.substring(0,sel_ico_name.lastIndexOf(".")));
								}
							}
							if(null!=unselectedicon&&!unselectedicon.isEmpty()){
								if(unselectedicon.startsWith("/smartclient/")||unselectedicon.startsWith("/static/")){ //true默认提供
									//String iconImagePath = ClientUtil.loadFilePath("defaultSelectedIconImagePath");
									File source = new File(androidConfig.getDefaultSelectedIconImagePath());
									String iconName =ClientUtil.FindAndReplaceChar(source.getName());
									copyAppIconImage(source,iconName );
									//过滤.png结尾，只保留图片名称写入
									tab.setUnselectedicon(iconName.substring(0, iconName.lastIndexOf(".")));
								}
								if(unselectedicon.startsWith("http://")){ //自定义图片
									String unselIconPath = tab.getUnselectedicon();
									String abs_unsel_icon_path = unselIconPath.replace(imageCallPath,uploadImageUrl);
									String unsel_icon_name = abs_unsel_icon_path.substring(abs_unsel_icon_path.lastIndexOf(String.valueOf(File.separator))+1);
									File unsourceImagepath = new File(abs_unsel_icon_path);
									copyAppIconImage(unsourceImagepath, unsel_icon_name);
									tab.setUnselectedicon(unsel_icon_name.substring(0,unsel_icon_name.lastIndexOf(".")));
								}
							}
						}
					}
					params.setTabs(tabs);
					viewComponent.setParams((T) params);
					//导航控件单独封装输出不用输出页面标题信息
					//对象解析json
					String bottomNavigatorjson = JacksonUtil.toJSon(viewComponent);
					//格式化json
					String bottompageJsonFormatOut = new JsonFormatTool().formatJson(bottomNavigatorjson);
					File bottomNavigatorOutFile = new File(pageOutDir+File.separator+changeCaption+"_"+pageId+".json");
					FileWriter	fw = new FileWriter(bottomNavigatorOutFile);
					PrintWriter	pw= new PrintWriter(fw);
					pw.println(bottompageJsonFormatOut);
					pw.flush();
					fw.close();
					pw.close();
					//更新页面数据
					pageList.setUitype("hscroll");
					pageList.setPagepath(scheme+"://load_hscroll_"+pageId);
					//保存页面输出路径的文件名
					pageList.setLayoutfile(changeCaption+"_"+pageId+".json");
					pageListDao.updatePageList(pageList);
			}

		}	
			//排除当前页面下导航控件再次输出
			PageList bottomNavComponent = pageListDao.findPageListByPageIdIsBottomNavigation(pageId);
			if(null!=bottomNavComponent&&bottomNavComponent.getPageId().equals(pageId)){
				continue;
			}
			PageList topNavComponent = pageListDao.findPageListByPageIdIsTopNavigation(pageId);
			if(null!=topNavComponent&&topNavComponent.getPageId().equals(pageId)){
				continue;
			}
			//创建输出打包页面对象
			Pagelayout pagelayout = new Pagelayout();
			pagelayout.setPageId(pageId);
			pagelayout.setCaption(caption);
			pagelayout.setItems(componentLists);
			//普通页面布局设置为vscroll
			pageList.setUitype("vscroll");
			//设置页面路径
			pageList.setPagepath(scheme+"://load_vscroll_"+pageId);
			String commonPageFileName = changeCaption.toLowerCase()+"_"+pageId+".json";
			File PageOutFile = new File(pageOutDir+File.separator+commonPageFileName);
			pageList.setLayoutfile(commonPageFileName);
			pageListDao.updatePageList(pageList);
			//对象解析json
			String pageJson = JacksonUtil.toJSon(pagelayout);
			//格式化json
			String pageJsonFormatOut = new JsonFormatTool().formatJson(pageJson);
			FileWriter	fw = new FileWriter(PageOutFile);
			PrintWriter	pw= new PrintWriter(fw);
			pw.println(pageJsonFormatOut);
			pw.flush();
			fw.close();
			pw.close();
		}
	}

	/**
	 * 根据工程车ID输出工程Json信息 ，并生成工程zip包
	 * @param projectListjson
	 * @return
	 */
	private boolean outProjectJsonInfo(ProjectListJson projectListjson)throws Exception {
			//File projectPath = new File(ClientUtil.loadFilePath("projectjsonOutPath"));
			File projectPath = new File(androidConfig.getProjectjsonOutPath());
			//对象解析json
			String projectJson = JacksonUtil.toJSon(projectListjson);
			//格式化json
			String projectJsonFormatOut = new JsonFormatTool().formatJson(projectJson);
			FileWriter fw = new FileWriter(projectPath);
			PrintWriter pw= new PrintWriter(fw);
			pw.println(projectJsonFormatOut);
			pw.flush();
			fw.close();
			pw.close();
		//生成工程zip包

		CompressZipFolder compressZipFolder = new CompressZipFolder(androidConfig.getProjectZIPtargetPath(),androidConfig.getProjectresourcesPath());

		compressZipFolder.generateFileList(new File(androidConfig.getProjectresourcesPath()));
		compressZipFolder.zipIt(androidConfig.getProjectZIPtargetPath());
		return true;
	}
	
	/**
	 * 生成IOS安装包
	 * @param projectId
	 * @param result
	 */
	@Override
	public void ProjectGenerateIosAppPackage(Integer projectId, ResultJson result) throws Exception{
		//生成ios包前先删除资源工程路径
		//File deleteiosContents = new File(ClientUtil.loadFilePath("deleteiosContents"));
		File deleteiosContents = new File(macConfig.getDeleteiosContents());
		if(deleteiosContents.exists()){
			boolean p = ClientUtil.deleteDir(deleteiosContents);
			if(!p){
				logger.info("清空工程数据包");
			}
		}
		//删除资源工程路径
		//File deleteiosshell = new File(ClientUtil.loadFilePath("deleteiosshell"));
		File deleteiosshell = new File(macConfig.getDeleteiosshell());
		if(deleteiosshell.exists()){
			boolean p = ClientUtil.deleteDir(deleteiosshell);
			if(!p){
				logger.info("清空解压工程数据");
			}
		}
		//删除资源工程路径
		//File deleteiosMyApplicationxcodeproj = new File(ClientUtil.loadFilePath("deleteiosMyApplicationxcodeproj"));
		File deleteiosMyApplicationxcodeproj = new File(macConfig.getDeleteiosMyApplicationxcodeproj());
		if(deleteiosMyApplicationxcodeproj.exists()){
			boolean target = ClientUtil.deleteDir(deleteiosMyApplicationxcodeproj);
			if(!target){
				logger.info("清空目标工程");
			}
		}
		//删除资源工程路径
		//File deleteiosaa = new File(ClientUtil.loadFilePath("deleteiosaa"));
		File deleteiosaa = new File(macConfig.getDeleteiosaa());
		if(deleteiosaa.exists()){
			boolean p = ClientUtil.deleteDir(deleteiosaa);
			if(!p){
				logger.info("清空源工程数据包");
			}
		}
		//删除资源工程路径
		//File deleteiosMyApplication = new File(ClientUtil.loadFilePath("deleteiosMyApplication"));
		File deleteiosMyApplication = new File(macConfig.getDeleteiosMyApplication());
		if(deleteiosMyApplication.exists()){
			boolean p = ClientUtil.deleteDir(deleteiosMyApplication);
			if(!p){
				logger.info("清空源工程数据包");
			}
		}
		//删除资源工程路径
		//File iosprojectresourcesPath = new File(ClientUtil.loadFilePath("iosprojectresourcesPath"));
		File iosprojectresourcesPath = new File(macConfig.getIosprojectresourcesPath());
		if(iosprojectresourcesPath.exists()){
			boolean p = ClientUtil.deleteDir(iosprojectresourcesPath);
			if(!p){
				logger.info("清空源工程数据包");
			}
		}
		ObjectMapper mapper = new ObjectMapper();
		String scheme = projectListDao.findProjectListSchemeByProjectId(projectId);
		//查询页面首页
		PageList shouyePage = pageListDao.findPageIndexIdByProjectId(projectId);
		Integer shouyePageId = shouyePage.getPageId();

		//根据工程ID查询当前工程下所有普通页面,不包括首页
		ProjectListJson projectListjson = projectListDao.findProjectListJson(projectId);
		//应用名ios客户端要求固定住
		projectListjson.setApplication( "MyApplication");
		List<CommonInterface> commonInterfaces = projectListjson.getCommon_interfaces();
		//输出project.json文件的普通页面集合定义
		List<CommonInterface> common_Interfaces = new ArrayList<CommonInterface>();

		Map<Integer,String> transMap = new HashMap<Integer, String>();

		//判断首页是否存在
		if(null!=shouyePage){
			CommonInterface shouCommonInterface = new CommonInterface();
			shouCommonInterface.setCaption(shouyePage.getPageTitle());
			shouCommonInterface.setCaptionTrans(ClientUtil.ChineseTranslation(shouyePage.getPageTitle()).toUpperCase()+"_"+shouyePage.getPageId());
			transMap.put(shouyePageId,shouCommonInterface.getCaptionTrans());
			shouCommonInterface.setIsindex(true);
			shouCommonInterface.setLayoutfile(ClientUtil.ChineseTranslation(shouyePage.getPageTitle())+"_"+shouyePageId);
			shouCommonInterface.setPageid(shouyePage.getPageId());
			if(shouyePage.getNavigation()==0){ //首页没有导航控件
				String uitype = "vscroll";
				shouCommonInterface.setPagepath(scheme+"://load_"+uitype+"_"+shouyePage.getPageId());
				shouCommonInterface.setUitype(uitype);
			}
			if(shouyePage.getNavigation()==2){ //首页有底部导航控件固定uitype
				String uitype = "tabVC";
				shouCommonInterface.setPagepath(scheme+"://load_"+uitype+"_"+shouyePage.getPageId());
				shouCommonInterface.setUitype(uitype);
				shouCommonInterface.setPagepath(scheme+"://load_"+uitype+"_"+shouyePageId);
			}
			if(shouyePage.getNavigation()==1){ //首页有顶部导航
				String uitype = "hscroll";
				shouCommonInterface.setUitype(uitype);
				shouCommonInterface.setPagepath(scheme+"://load_"+uitype+"_"+shouyePageId);
			}
			shouCommonInterface.setProjectId(projectId);
			common_Interfaces.add(shouCommonInterface);
		}
		if(commonInterfaces.size()!=0){
			for (CommonInterface commonInterface : commonInterfaces) {
				if(commonInterface!=null){
					String caption = ClientUtil.ChineseTranslation(commonInterface.getCaption());
					//根据首页ID查询控件，判断首页里面的控件有没有底部导航，true
					//更新uitype\pagepath\layoutfile
					Integer id = commonInterface.getPageid();
					commonInterface.setCaptionTrans(caption.toUpperCase()+"_"+id);

					transMap.put(id,commonInterface.getCaptionTrans());

					if(commonInterface.getNavigation()==1){ //有顶部导航控件
						String uitype = "hscroll";
						commonInterface.setUitype(uitype);
						commonInterface.setLayoutfile(caption+"_"+id);
						commonInterface.setPagepath(scheme+"://load_"+uitype+"_"+id);
						commonInterface.setIsindex(false);
					}
					if(commonInterface.getNavigation()==0){ //没有导航控件
						String uitype = "vscroll";
						commonInterface.setUitype(uitype);
						commonInterface.setLayoutfile(caption+"_"+id);
						commonInterface.setPagepath(scheme+"://load_"+uitype+"_"+id);
						commonInterface.setIsindex(false);
					}
					if(commonInterface.getNavigation()==2){ //有底部导航
						String uitype = "tabVC";
						commonInterface.setUitype(uitype);
						commonInterface.setLayoutfile(caption+"_"+id);
						commonInterface.setPagepath(scheme+"://load_"+uitype+"_"+id);
						commonInterface.setIsindex(false);
					}
					common_Interfaces.add(commonInterface);
				}
			}
		}
		projectListjson.setCommon_interface(common_Interfaces);
		//获取应用图标path
		File launchiconPath = new File(projectListjson.getLaunchicon());
		String appImgName = launchiconPath.getName();
		copyiosAppIconImage(launchiconPath, appImgName);
		//过滤png结尾字符串
		String imgName = appImgName.substring(0,appImgName.lastIndexOf("."));
		projectListjson.setLaunchicon(imgName);

		//获取工程页面输出pagelayout目录
		List<String> imageNames = getiosProjectpagesByprojectId(projectId,scheme,transMap);

		imageNames.add(imgName);
		String packageName = projectListjson.getPackageName();
		//ios安装包名固定
		projectListjson.setPackageName("com.aspire.smartclient.fastlane");
		projectListjson.setImageNames(imageNames);
//			int n = packageName.indexOf(".",0);
//			if(n==-1){
//				String modifyPackageName = "com.aspire."+packageName;
//				projectListjson.setPackageName(modifyPackageName);
//			}
		SplashInterface splashInterface = new SplashInterface();
		LoginInterface loginInterface = new LoginInterface();
		//登录
		Integer login = projectListDao.findPageByPageType(projectId,1);
		//闪屏
		Integer splash= projectListDao.findPageByPageType(projectId,2);
		if(null!=login&&login!=-1){ //用户自定义登录，闪屏跳转登录
			loginInterface.setPagepath(scheme+"://login_userlogin");
			loginInterface.setLoginurl("");
			loginInterface.setJumpurl(scheme+"://load_hscroll_"+shouyePageId);
			if(null!=splash&&splash!=-1){ //用户自定义闪屏
				ComponentList componentList = componentListDao.findSplashComponentByPageId(splash);
				String params = componentList.getParams();
				JsonNode jsonNode =  mapper.readTree(params);
				String splashimage = jsonNode.findValue("splashimage").asText();
				String filterImageName = "";
				if(!splashimage.trim().isEmpty()){
					//自定义闪屏图片
					//去资源共享路径下复制闪屏图
					//截取文件名
					String imageName = splashimage.substring(splashimage.lastIndexOf("/")+1);
					//File splashImagePath = new File( ClientUtil.loadFilePath("splashImagePath")+File.separator+imageName);
					String _dateStr = splashimage.substring((splashimage.lastIndexOf("icon/")+5),splashimage.lastIndexOf("/i"));

					File splashImagePath = new File( androidConfig.getSplashImagePath()+File.separator+_dateStr+File.separator+imageName);
					copyiosAppIconImage(splashImagePath, imageName);
					//过滤掉png字符设置闪屏接口返回
					filterImageName = imageName.substring(0,imageName.lastIndexOf("."));
					splashInterface.setSplashimage(filterImageName);
				}
				if(splashimage.trim().equals("")){
					splashInterface.setSplashimage("splashimage_default");//闪屏图空，提供默认
				}
				splashInterface.setSplashimage(filterImageName);
				splashInterface.setJumpurl(scheme+"://login_userlogin");
				splashInterface.setSplashtime(1000);
			}
			if(splash==null||splash==-1){
				splashInterface.setSplashimage("splashimage_default");
				splashInterface.setJumpurl(scheme+"://load_hscroll_"+shouyePageId);
				splashInterface.setSplashtime(1000);
			}
		}
		if(null==login){
			loginInterface.setPagepath("");
			loginInterface.setLoginurl("");
			loginInterface.setJumpurl("");
			loginInterface.setLayout("");
			if(null!=splash&&splash!=-1){ //用户自定义闪屏
				ComponentList componentList = componentListDao.findSplashComponentByPageId(splash);
				String params = componentList.getParams();
				JsonNode jsonNode =  mapper.readTree(params);
				String splashimage = jsonNode.findValue("splashimage").asText();
				String filterImageName = "";
				if(!splashimage.trim().isEmpty()){
					//自定义闪屏图片
					//去资源共享路径下复制闪屏图
					//截取文件名
					String imageName = splashimage.substring(splashimage.lastIndexOf("/")+1);
					//File splashImagePath = new File( ClientUtil.loadFilePath("splashImagePath")+File.separator+imageName);
					String _dateStr = splashimage.substring((splashimage.lastIndexOf("icon/")+5),splashimage.lastIndexOf("/i"));
					File splashImagePath = new File( androidConfig.getSplashImagePath()+File.separator+_dateStr+File.separator+imageName);
					copyiosAppIconImage(splashImagePath, imageName);
					//过滤掉png字符设置闪屏接口返回
					filterImageName = imageName.substring(0,imageName.lastIndexOf("."));
					splashInterface.setSplashimage(filterImageName);
				}
				if(splashimage.trim().equals("")){
					splashInterface.setSplashimage("splashimage_default");//闪屏图空，提供项目下默认图
				}
				splashInterface.setSplashimage(filterImageName);
				splashInterface.setJumpurl(scheme+"://load_hscroll_"+shouyePageId);
				splashInterface.setSplashtime(1000);
			}
			if(splash==null||splash==-1){
				splashInterface.setSplashimage("splashimage_default");
				splashInterface.setJumpurl(scheme+"://load_hscroll_"+shouyePageId);
				splashInterface.setSplashtime(1000);
			}
		}
		projectListjson.setLogin_interface(loginInterface);
		projectListjson.setSplash_interface(splashInterface);
		//输出project.json
		boolean  projectIsgenratok = outiosProjectJsonInfo(projectListjson);
		if(!projectIsgenratok){
			result.setCode(1);
			result.setMsg("输出ios工程zip包异常");
			return;
		}

		String cmd = macConfig.getIosCmd();
		String mcLogfile = macConfig.getIoslogfile();
		logger.info(cmd);

		//创建一个线程池
		ExecutorService exec = Executors.newCachedThreadPool();
		//创建有返回值的任务
		//submit返回一个Future，代表了即将要返回的结果
		IosTaskCallable iostaskCallable = new IosTaskCallable(projectId,cmd,mcLogfile);
		//执行任务并获取Future对象
		Future<String> f1 = exec.submit(iostaskCallable);
		//从Future对象上获取任务的返回值，并输出到控制台
		String cmdStr = f1.get().toString();
		if (cmdStr.equals("SUCCESS")){
			//ios安装包不需要读取保存数据库，直接上传至蒲公英网站扫描二维码下载
//				getApkPackageInfo(projectId);
			result.setCode(0);
			result.setMsg("SUCCESS");
		}else {
			result.setCode(1);
			result.setMsg("ERROR");
		}
		//关闭线程池
		exec.shutdown();

	}
	
	/**
	 * 此方法满足ios要求标准
	 * 根据工程ID获取所有的页面控件信息
	 * @param projectId
	 * @throws IOException 
	 * @throws Exception
	 */
	private List<String> getiosProjectpagesByprojectId(Integer projectId,String scheme,Map<Integer,String> transMap) throws IOException {
		List<String> imageNames = new ArrayList<String>();
		//获取工程ID页面数据
		List<PageList> allPage = pageListDao.findAllPageListByProjectId(projectId);
		//获取页面输出路径
		//File pageOutDir = new File(ClientUtil.loadFilePath("iospagelayoutPath"));
		File pageOutDir = new File(macConfig.getIospagelayoutPath());
		//判断页面路径是否存在
		if(!pageOutDir.exists()){
			pageOutDir.mkdirs();
		}
		//工程ID页面数据封装pagelayout对象中
		for (PageList pageList : allPage) {
			Integer pageId = pageList.getPageId();
			String caption = pageList.getPageTitle();
			String changeCaption = ClientUtil.ChineseTranslation(caption);
			//不用输出登录闪屏界面到pagelayout目录下
			int pageType = pageList.getPageType();
			if(null==pageId){
				continue;
			}
			if(pageType==1||pageType==2){
				continue;
			}
			//创建页面输出对象的所有控件集合
			List<com.aspire.component.ComponentList> componentLists = new ArrayList<com.aspire.component.ComponentList>();
			//根据页面ID获取页面下所有控件
			List<ComponentList> components = componentListDao.findComponentByPageId(pageId);
			for (ComponentList component : components) {
				String type = component.getType();
				com.aspire.component.ComponentList<T> viewComponent = new com.aspire.component.ComponentList<T>();
				viewComponent.setName("");
				viewComponent.setType(type);
				if(type.trim().equals("SingleBanner")){
					String jsonParams = component.getParams();
					com.aspire.component.singlebanner.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.singlebanner.Params.class);
					String contenturl = params.getContenturl();
					if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
						Integer id =ClientUtil.charCutChangeInteger(contenturl);
						if(id!=null){
							Integer navigation = pageListDao.findNavigationByPageId(id);
							if(navigation==2){ //底部
								params.setContenturl(scheme+"://load_tabVC_"+id);
							}
							if(navigation==1){ //顶部
								params.setContenturl(scheme+"://load_hscroll_"+id);
							}
							if(navigation==0){ //普通
								params.setContenturl(scheme+"://load_vscroll_"+id);
							}
						}
					}
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("HorizScrollBanner")){
					String jsonParams = component.getParams();
					com.aspire.component.horizScrollbanner.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.horizScrollbanner.Params.class);
					List<com.aspire.component.horizScrollbanner.Banner> banners = params.getItems();
					for (com.aspire.component.horizScrollbanner.Banner banner : banners) {
						String contenturl = banner.getContenturl();
						if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
							Integer id =ClientUtil.charCutChangeInteger(banner.getContenturl());
							if(id!=null){
								Integer navigation = pageListDao.findNavigationByPageId(id);
								if(navigation==2){ //底部
									banner.setContenturl(scheme+"://load_tabVC_"+id);
								}
								if(navigation==1){ //顶部
									banner.setContenturl(scheme+"://load_hscroll_"+id);
								}
								if(navigation==0){ //普通
									banner.setContenturl(scheme+"://load_vscroll_"+id);
								}
							}
						}
					}
					params.setItems(banners);
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("Entries")){
					String jsonParams = component.getParams();
					com.aspire.component.entries.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.entries.Params.class);
					List<com.aspire.component.entries.Entry> entrys = params.getItems();
					for (com.aspire.component.entries.Entry entry : entrys) {
						String contenturl = entry.getContenturl();
						if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
							Integer id =ClientUtil.charCutChangeInteger(entry.getContenturl());
							if(id!=null){
								Integer navigation = pageListDao.findNavigationByPageId(id);
								if(navigation==2){ //底部
									entry.setContenturl(scheme+"://load_tabVC_"+id);
								}
								if(navigation==1){ //顶部
									entry.setContenturl(scheme+"://load_hscroll_"+id);
								}
								if(navigation==0){ //普通
									entry.setContenturl(scheme+"://load_vscroll_"+id);
								}
							}
						}
					}
					params.setItems(entrys);
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("DividerLine")){
					String jsonParams = component.getParams();
					com.aspire.component.dividerline.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.dividerline.Params.class);
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("TripleBanner")){
					String jsonParams = component.getParams();
					com.aspire.component.triplebanner.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.triplebanner.Params.class);
					List<com.aspire.component.triplebanner.Banner> banners = params.getItems();
					for (com.aspire.component.triplebanner.Banner banner : banners) {
						String contenturl = banner.getContenturl();
						if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
							Integer id =ClientUtil.charCutChangeInteger(banner.getContenturl());
							if(id!=null){
								Integer navigation = pageListDao.findNavigationByPageId(id);
								if(navigation==2){ //底部
									banner.setContenturl(scheme+"://load_tabVC_"+id);
								}
								if(navigation==1){ //顶部
									banner.setContenturl(scheme+"://load_hscroll_"+id);
								}
								if(navigation==0){ //普通
									banner.setContenturl(scheme+"://load_vscroll_"+id);
								}
							}
						}
					}
					params.setItems(banners);
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("SquareIcon1LineText")){
					String jsonParams = component.getParams();		
					com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
					String contenturl = params.getContenturl();
					if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
						Integer id =ClientUtil.charCutChangeInteger(params.getContenturl());
						if(id!=null){
							Integer navigation = pageListDao.findNavigationByPageId(id);
							if(navigation==2){ //底部
								params.setContenturl(scheme+"://load_tabVC_"+id);
							}
							if(navigation==1){ //顶部
								params.setContenturl(scheme+"://load_hscroll_"+id);
							}
							if(navigation==0){ //普通
								params.setContenturl(scheme+"://load_vscroll_"+id);
							}
						}
					}
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("SquareIcon2LinesText")){
					String jsonParams = component.getParams();		
					com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
					String contenturl = params.getContenturl();
					if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
						Integer id =ClientUtil.charCutChangeInteger(params.getContenturl());
						if(id!=null){
							Integer navigation = pageListDao.findNavigationByPageId(id);
							if(navigation==2){ //底部
								params.setContenturl(scheme+"://load_tabVC_"+id);
							}
							if(navigation==1){ //顶部
								params.setContenturl(scheme+"://load_hscroll_"+id);
							}
							if(navigation==0){ //普通
								params.setContenturl(scheme+"://load_vscroll_"+id);
							}
						}
					}
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("SquareIcon3LinesText")){
					String jsonParams = component.getParams();		
					com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
					String contenturl = params.getContenturl();
					if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
						Integer id =ClientUtil.charCutChangeInteger(params.getContenturl());
						if(id!=null){
							Integer navigation = pageListDao.findNavigationByPageId(id);
							if(navigation==2){ //底部
								params.setContenturl(scheme+"://load_tabVC_"+id);
							}
							if(navigation==1){ //顶部
								params.setContenturl(scheme+"://load_hscroll_"+id);
							}
							if(navigation==0){ //普通
								params.setContenturl(scheme+"://load_vscroll_"+id);
							}
						}
					}
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("CircleIcon1LineText")){
					String jsonParams = component.getParams();		
					com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
					String contenturl = params.getContenturl();
					if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
						Integer id =ClientUtil.charCutChangeInteger(params.getContenturl());
						if(id!=null){
							Integer navigation = pageListDao.findNavigationByPageId(id);
							if(navigation==2){ //底部
								params.setContenturl(scheme+"://load_tabVC_"+id);
							}
							if(navigation==1){ //顶部
								params.setContenturl(scheme+"://load_hscroll_"+id);
							}
							if(navigation==0){ //普通
								params.setContenturl(scheme+"://load_vscroll_"+id);
							}
						}
					}
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("CircleIcon2LinesText")){
					String jsonParams = component.getParams();		
					com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
					String contenturl = params.getContenturl();
					if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
						Integer id =ClientUtil.charCutChangeInteger(params.getContenturl());
						if(id!=null){
							Integer navigation = pageListDao.findNavigationByPageId(id);
							if(navigation==2){ //底部
								params.setContenturl(scheme+"://load_tabVC_"+id);
							}
							if(navigation==1){ //顶部
								params.setContenturl(scheme+"://load_hscroll_"+id);
							}
							if(navigation==0){ //普通
								params.setContenturl(scheme+"://load_vscroll_"+id);
							}
						}
					}
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("CircleIcon3LinesText")){
					String jsonParams = component.getParams();		
					com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
					String contenturl = params.getContenturl();
					if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
						Integer id =ClientUtil.charCutChangeInteger(params.getContenturl());
						if(id!=null){
							Integer navigation = pageListDao.findNavigationByPageId(id);
							if(navigation==2){ //底部
								params.setContenturl(scheme+"://load_tabVC_"+id);
							}
							if(navigation==1){ //顶部
								params.setContenturl(scheme+"://load_hscroll_"+id);
							}
							if(navigation==0){ //普通
								params.setContenturl(scheme+"://load_vscroll_"+id);
							}
						}
					}
					viewComponent.setParams((T) params);
					componentLists.add(viewComponent);
				}else if(type.trim().equals("TabNavigator")){
					String jsonParams = component.getParams();	
					com.aspire.component.tabnavigator.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.tabnavigator.Params.class);
					List<com.aspire.component.tabnavigator.TabSpec> tabs = params.getTabs();
					//判断selected和unselected选中未选中图片地址是否提供
					if(null!=tabs&&tabs.size()!=0){
						for (com.aspire.component.tabnavigator.TabSpec tab : tabs) {
							String contenturl = tab.getContenturl();

							if(!"".trim().equals(contenturl)&&null!=contenturl&&contenturl.startsWith(scheme)){
								Integer id =ClientUtil.charCutChangeInteger(contenturl);

								Set<Integer> keys = transMap.keySet();
								for(Integer key : keys){
									if(id.equals(key)){
										tab.setCaptionTrans(transMap.get(key));
									}
								}

								if(id!=null){
									Integer navigation = pageListDao.findNavigationByPageId(id);
									if(navigation==2){ //底部
										tab.setContenturl(scheme+"://load_tabVC_"+id);
									}
									if(navigation==1){ //顶部
										tab.setContenturl(scheme+"://load_hscroll_"+id);
									}
									if(navigation==0){ //普通
										tab.setContenturl(scheme+"://load_vscroll_"+id);
									}
								}
							}
							String selectedicon = tab.getSelectedicon();
							String unselectedicon = tab.getUnselectedicon();
							if(null!=selectedicon&&!selectedicon.isEmpty()){
								if(selectedicon.startsWith("/smartclient/")||selectedicon.startsWith("/static/")){ //true默认提供
									//String iconImagePath = ClientUtil.loadFilePath("defaultSelectedIconImagePath");
									File source = new File(androidConfig.getDefaultSelectedIconImagePath());
									String iconName = source.getName();
									String chageIconName =  ClientUtil.FindAndReplaceChar(iconName);
									copyiosAppIconImage(source,chageIconName );
									//过滤.png结尾，只保留图片名称写入
									tab.setSelectedicon(chageIconName.substring(0,chageIconName.lastIndexOf(".")));
									imageNames.add(chageIconName.substring(0,chageIconName.lastIndexOf(".")));
								}
								if(selectedicon.startsWith("http://")){ //自定义图片
									String _sel_icon_path = tab.getSelectedicon();
									String abs_sel_icon_path = _sel_icon_path.replace(imageCallPath,uploadImageUrl);
									String sel_icon_name = abs_sel_icon_path.substring(abs_sel_icon_path.lastIndexOf(String.valueOf(File.separator))+1);
									File sourceImagepath = new File(abs_sel_icon_path);
									copyiosAppIconImage(sourceImagepath, sel_icon_name);
									tab.setSelectedicon(sel_icon_name.substring(0,sel_icon_name.lastIndexOf(".")));
									imageNames.add(sel_icon_name.substring(0,sel_icon_name.lastIndexOf(".")));
								}
							}
							if(null!=unselectedicon&&!unselectedicon.isEmpty()){
								if(unselectedicon.startsWith("/smartclient/")||unselectedicon.startsWith("/static/")){ //true默认提供
									//String iconImagePath = ClientUtil.loadFilePath("defaultSelectedIconImagePath");
									File source = new File(androidConfig.getDefaultSelectedIconImagePath());
									String iconName = source.getName();
									String changeIconName = ClientUtil.FindAndReplaceChar(iconName);
									copyiosAppIconImage(source,changeIconName);
									//过滤.png结尾，只保留图片名称写入
									tab.setUnselectedicon(changeIconName.substring(0,changeIconName.lastIndexOf(".")));
									imageNames.add(changeIconName.substring(0,changeIconName.lastIndexOf(".")));
								}
								if(unselectedicon.startsWith("http://")){ //自定义图片
									String _unsel_icon_path = tab.getUnselectedicon();
									String abs_unsel_icon_path = _unsel_icon_path.replace(imageCallPath,uploadImageUrl);
									String unsel_icon_name = abs_unsel_icon_path.substring(abs_unsel_icon_path.lastIndexOf(String.valueOf(File.separator))+1);
									File unsourceImagepath = new File(abs_unsel_icon_path);
									copyiosAppIconImage(unsourceImagepath, unsel_icon_name);
									tab.setUnselectedicon(unsel_icon_name.substring(0,unsel_icon_name.lastIndexOf(".")));
									imageNames.add(unsel_icon_name.substring(0,unsel_icon_name.lastIndexOf(".")));
								}
							}
						}

					}
					params.setTabs(tabs);
					viewComponent.setParams((T) params);
					//导航控件单独封装输出不用输出页面标题信息
					//对象解析json
					String bottomNavigatorjson = JacksonUtil.toJSon(viewComponent);
					//格式化json
					String bottompageJsonFormatOut = new JsonFormatTool().formatJson(bottomNavigatorjson);
					File bottomNavigatorOutFile = new File(pageOutDir+File.separator+changeCaption+"_"+pageId+".json");
					FileWriter	fw = new FileWriter(bottomNavigatorOutFile);
					PrintWriter	pw= new PrintWriter(fw);
					pw.println(bottompageJsonFormatOut);
					pw.flush();
					fw.close();
					pw.close();
			}
		}
			//排除当前页面下导航控件再次输出
			PageList bottomNavComponent = pageListDao.findPageListByPageIdIsBottomNavigation(pageId);
			if(null!=bottomNavComponent&&bottomNavComponent.getPageId().equals(pageId)){
				continue;
			}
			PageList topNavComponent = pageListDao.findPageListByPageIdIsTopNavigation(pageId);
			if(null!=topNavComponent&&topNavComponent.getPageId().equals(pageId)){
				continue;
			}
			//创建输出打包页面对象
			Pagelayout pagelayout = new Pagelayout();
			pagelayout.setPageId(pageId);
			pagelayout.setCaption(caption);
			pagelayout.setItems(componentLists);
			String commonPageFileName = changeCaption.toLowerCase()+"_"+pageId+".json";
			File PageOutFile = new File(pageOutDir+File.separator+commonPageFileName);
			//对象解析json
			String pageJson = JacksonUtil.toJSon(pagelayout);
			//格式化json
			String pageJsonFormatOut = new JsonFormatTool().formatJson(pageJson);
			FileWriter	fw = new FileWriter(PageOutFile);
			PrintWriter	pw= new PrintWriter(fw);
			pw.println(pageJsonFormatOut);
			pw.flush();
			fw.close();
			pw.close();
		}
		return imageNames;
	}
	
	
	/**
	 * 此方法应满足ios规范
	 * 根据工程车ID输出工程Json信息 ，并生成工程zip包
	 * @param projectListjson
	 * @return
	 */
	private boolean outiosProjectJsonInfo(ProjectListJson projectListjson)throws Exception {
		//File projectPath = new File(ClientUtil.loadFilePath("iosprojectjsonOutPath"));
		File projectPath = new File(macConfig.getIosprojectjsonOutPath());
		//对象解析json
		String projectJson = JacksonUtil.toJSon(projectListjson);
		//格式化json
		String projectJsonFormatOut = new JsonFormatTool().formatJson(projectJson);
		FileWriter fw = new FileWriter(projectPath);
		PrintWriter pw= new PrintWriter(fw);
		pw.println(projectJsonFormatOut);
		pw.flush();
		fw.close();
		pw.close();
		//生成工程zip包
		CompressZipFolder compressZipFolder = new CompressZipFolder(macConfig.getIosprojectZIPtargetPath(),macConfig.getIosprojectresourcesPath());
		compressZipFolder.generateFileList(new File(macConfig.getIosprojectresourcesPath()));
		compressZipFolder.zipIt(macConfig.getIosprojectZIPtargetPath());
		return true;
	}



	/**
	 * 复制工程应用图标文件到相应目标工程要求的目录下。
	 * Android从res/drawable-hdpi/xhdpi/xxhdpi/xxxhdpi取，
	 * 分别对应72x72/96x96/144x144/192x192的图标，
	 * ios从res/ios取。
	 * @param path 源图片路径位置
	 * @param imgName 图片名
	 */
	public  boolean copyAppIconImage(File path,String imgName){
//		File file = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
//		File iconurl = null;
		try {
//			file = new File(path);
			fis = new FileInputStream(path);
			BufferedImage bufferedImage = ImageIO.read(fis);
			int width = bufferedImage.getWidth();
//			System.out.println("imagewidth:"+width);
			File  iconurl = null;
			switch (width) {
				case 72:
					iconurl= new File(androidConfig.getAndroidDrawableHdpi());

					if(!iconurl.exists()){
						if(!iconurl.isDirectory()){
							iconurl.mkdirs();
						}
					}
					fos = new FileOutputStream(iconurl+File.separator+imgName);
					ImageIO.write(bufferedImage, "png", fos);
					fos.flush();
					break;
				case 96:
					iconurl = new File(androidConfig.getAndroidXhdpi());
					if(!iconurl.exists()){
						if(!iconurl.isDirectory()){
							iconurl.mkdirs();
						}
					}
					fos = new FileOutputStream(iconurl+File.separator+imgName);
					ImageIO.write(bufferedImage, "png", fos);
					fos.flush();
					break;
				case 144:
					iconurl = new File(androidConfig.getAndroidXxhdpi());
					if(!iconurl.exists()){
						if(!iconurl.isDirectory()){
							iconurl.mkdirs();
						}
					}
					fos = new FileOutputStream(iconurl+File.separator+imgName);
					ImageIO.write(bufferedImage, "png", fos);
					fos.flush();
					break;
				case 180:
					iconurl = new File(androidConfig.getAndroidXxxhdpi());
					if(!iconurl.exists()){
						if(!iconurl.isDirectory()){
							iconurl.mkdirs();
						}
					}
					fos = new FileOutputStream(iconurl+File.separator+imgName);
					ImageIO.write(bufferedImage, "png", fos);
					fos.flush();
					break;
				case 192:
					iconurl = new File(androidConfig.getAndroidXxxhdpi());
					if(!iconurl.exists()){
						if(!iconurl.isDirectory()){
							iconurl.mkdirs();
						}
					}
					fos = new FileOutputStream(iconurl+File.separator+imgName);
					ImageIO.write(bufferedImage, "png", fos);
					fos.flush();
					break;
				default:
					iconurl = new File(androidConfig.getAndroidXhdpi());
					if(!iconurl.exists()){
						if(!iconurl.isDirectory()){
							iconurl.mkdirs();
						}
					}
					fos = new FileOutputStream(iconurl+File.separator+imgName);
					ImageIO.write(bufferedImage, "png", fos);
					fos.flush();
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				fis.close();
				fos.close();
			} catch (Exception ex) {
			}
		}
		return true;
	}

	/**
	 * 此方法满足IOS标准
	 * 复制工程应用图标文件到相应目标工程要求的目录下。
	 * Android从res/drawable-hdpi/xhdpi/xxhdpi/xxxhdpi取，
	 * 分别对应72x72/96x96/144x144/192x192的图标，
	 * ios从res/ios取。
	 * @param path 源图片路径位置
	 * @param selectediconimagename 图片名
	 */
	public  boolean copyiosAppIconImage(File path,String selectediconimagename){
//		File file = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
//		File iconurl = null;
		try {
//			file = new File(path);
			fis = new FileInputStream(path);
			BufferedImage bufferedImage = ImageIO.read(fis);
//			System.out.println("imagewidth:"+width);
			File iconurl= new File(macConfig.getIosRes());
			if(!iconurl.exists()){
				if(!iconurl.isDirectory()){
					iconurl.mkdirs();
				}
			}
			fos = new FileOutputStream(iconurl+File.separator+ClientUtil.modifyImageName(selectediconimagename));
			ImageIO.write(bufferedImage, "png", fos);
			fos.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				fis.close();
				fos.close();
			} catch (Exception ex) {
			}
		}
		return true;
	}

}











