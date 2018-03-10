package com.aspire.service.impl;

import com.aspire.mapper.ComponentListDao;
import com.aspire.mapper.ModuleListDao;
import com.aspire.mapper.PageListDao;
import com.aspire.pojo.ComponentList;
import com.aspire.pojo.ModuleList;
import com.aspire.pojo.PageList;
import com.aspire.service.PageListService;
import com.aspire.util.ClientUtil;
import com.aspire.util.JacksonUtil;
import com.aspire.util.ResultJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class PageListServiceImpl<T> implements PageListService {
	
	Logger logger = Logger.getLogger(PageListServiceImpl.class);

	@Resource
	private PageListDao pageListDao;
	@Resource
	private ComponentListDao componentListDao;
	@Resource
	private ModuleListDao moduleListDao;
	
	@Override
	public void addPageList(PageList pageList, ResultJson result) throws Exception{
		if(pageList.getProjectId()==null||"".equals(pageList.getProjectId())){
			result.setCode(1);
			result.setMsg("工程ID为空");
			return;
		}
		if(pageList.getPageName()==null||"".equals(pageList.getPageName().trim())){
			result.setCode(1);
			result.setMsg("页面名称为空");
			return;
		}
		if(pageList.getPageType()==null){
			result.setCode(1);
			result.setData("页面类型为空");
			return;
		}
		Map<String,Object> map = new HashMap<String,Object>();
		//默认为非首页
		pageList.setIsindex(0);
		pageList.setCreateDate(new Date());
		pageList.setNavigation(3);
		//新增页面
		pageListDao.addPageList(pageList);
		//初始页面未有控件uitype未知，所有此处不能更新页面的uitype、pagepath
		Integer pageId = pageList.getPageId();
		//根据页面名称生成.json的文件名，中文需要转换拼音
		String pageName = ClientUtil.ChineseTranslation(pageList.getPageName());
		String scheme = pageListDao.findSchemeByProjectId(pageList.getProjectId());
		pageList.setLayoutfile(pageName+"_"+pageId+".json");
		pageList.setPagepath(scheme+"://load_vscroll_"+pageId);
		pageList.setUitype("vscroll");
		pageListDao.updatePageList(pageList);
		//返回新 增页面ID
		map.put("pageId", pageList.getPageId());
		result.setCode(0);
		result.setMsg("SUCCESS");
		result.setData(map);
	}

	@Override
	public void updatePageList(PageList pageList, ResultJson result)throws Exception {
		if(pageList.getPageId()==null||"".equals(pageList.getPageId())){
			result.setCode(1);
			result.setMsg("请求页面ID为空");
			return;
		}
		if(pageList.getProjectId()==null||"".equals(pageList.getProjectId())){
			result.setCode(1);
			result.setMsg("请求工程ID为空");
			return;
		}
		pageListDao.updatePageList(pageList);
		result.setCode(0);
		result.setMsg("SUCCESS");
	}

	@Override
	public void findPageListByPageId(Integer projectId, Integer pageId, ResultJson result) throws Exception {
		if(projectId==null||"".equals(projectId)){
			result.setCode(1);
			result.setMsg("pageId空");
			return;
		}
		if(pageId==null||"".equals(pageId)){
			result.setCode(1);
			result.setMsg("projectId空");
			return;
		}
		PageList pageList = new PageList();
		pageList.setProjectId(projectId);
		pageList.setPageId(pageId);
		com.aspire.component.PageList pageinfo = pageListDao.findPageDetails(pageList);
		if(null==pageinfo){
			result.setCode(1);
			result.setMsg("没有页面");
			return;
		}
		List<com.aspire.component.Component<T>> componentLists = new ArrayList<com.aspire.component.Component<T>>();
		//根据页面ID查询控件Id
		List<ComponentList> components = componentListDao.findComponentByPageId(pageId);
		for (ComponentList componentList : components) {
			String type = componentList.getType();
			com.aspire.component.Component<T> component = new com.aspire.component.Component<T>();
			component.setComponentId(componentList.getComponentId());
			component.setPageId(componentList.getPageId());
			component.setName(componentList.getName());
			component.setType(type);
			if(type.trim().equals("Login")){
				String jsonParams = componentList.getParams();
				com.aspire.component.login.Params params = JacksonUtil.readValue(jsonParams,com.aspire.component.login.Params.class);
				component.setParams((T)params);
				componentLists.add(component);
			}else if(type.trim().equals("Splash")){
				String jsonParams = componentList.getParams();
				com.aspire.component.splash.Params params = JacksonUtil.readValue(jsonParams,com.aspire.component.splash.Params.class);
				component.setParams((T)params);
				componentLists.add(component);
			}else if(type.trim().equals("TabNavigator")){
				String jsonParams = componentList.getParams();	
				com.aspire.component.tabnavigator.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.tabnavigator.Params.class);
				component.setParams((T) params);
				componentLists.add(component);
			}else if(type.trim().equals("SingleBanner")){
				String jsonParams = componentList.getParams();
				com.aspire.component.singlebanner.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.singlebanner.Params.class);
				component.setParams((T) params);
				componentLists.add(component);
			}else if(type.trim().equals("HorizScrollBanner")){
				String jsonParams = componentList.getParams();
				com.aspire.component.horizScrollbanner.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.horizScrollbanner.Params.class);
				component.setParams((T) params);
				componentLists.add(component);
			}else if(type.trim().equals("Entries")){
				String jsonParams = componentList.getParams();
				com.aspire.component.entries.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.entries.Params.class);
				component.setParams((T) params);
				componentLists.add(component);
			}else if(type.trim().equals("DividerLine")){
				String jsonParams = componentList.getParams();
				com.aspire.component.dividerline.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.dividerline.Params.class);
				component.setParams((T) params);
				componentLists.add(component);
			}else if(type.trim().equals("TripleBanner")){
				String jsonParams = componentList.getParams();
				com.aspire.component.triplebanner.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.triplebanner.Params.class);
				component.setParams((T) params);
				componentLists.add(component);
			}else if(type.trim().equals("SquareIcon1LineText")){
				String jsonParams = componentList.getParams();		
				com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
				component.setParams((T) params);
				componentLists.add(component);
			}else if(type.trim().equals("SquareIcon2LinesText")){
				String jsonParams = componentList.getParams();		
				com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
				component.setParams((T) params);
				componentLists.add(component);
			}else if(type.trim().equals("SquareIcon3LinesText")){
				String jsonParams = componentList.getParams();		
				com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
				component.setParams((T) params);
				componentLists.add(component);
			}else if(type.trim().equals("CircleIcon1LineText")){
				String jsonParams = componentList.getParams();		
				com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
				component.setParams((T) params);
				componentLists.add(component);
			}else if(type.trim().equals("CircleIcon2LinesText")){
				String jsonParams = componentList.getParams();		
				com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
				component.setParams((T) params);
				componentLists.add(component);
			}else if(type.trim().equals("CircleIcon3LinesText")){
				String jsonParams = componentList.getParams();		
				com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
				component.setParams((T) params);
				componentLists.add(component);
			}
		}
		pageinfo.setComponentLists(componentLists);
		pageinfo.setNavigation(null);

		result.setCode(0);
		result.setMsg("SUCCESS");
		result.setData(pageinfo);
	}

	@Override
	@Transactional
	public void deletePageListByPageId(Integer projectId, Integer pageId, ResultJson result) throws Exception {
		if(projectId==null||"".equals(projectId)){
			result.setCode(1);
			result.setMsg("请求页面ID为空");
			return;
		}
		if(pageId==null||"".equals(pageId)){
			result.setCode(1);
			result.setMsg("请求工程ID为空");
			return;
		}
		Integer navigation = pageListDao.findNavigationByPageId(pageId);
		if(navigation.equals(1)||navigation.equals(0)||navigation.equals(2)){
			result.setCode(1);
			result.setMsg("此页面不能单独删除，已关联导航控件，一定要删除请到导航界面删除。");
			return;
		}
		//删除页面先删除页面下控件
		List<ComponentList> components = componentListDao.findComponentByPageId(pageId);
		for (ComponentList component : components) {
			String type = component.getType();
			String params = component.getParams();
			if("TabNavigator".trim().equals(type)){ //导航控件
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
					//删除底部导航所在页面后需要更新navigation字段为3
					PageList pageList = new PageList();
					pageList.setPageId(pageId);
					pageList.setNavigation(3);
					pageListDao.updatePageList(pageList);
		        }

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
					//删除顶部导航所在页面后需要更新navigation字段为3
					PageList pageList = new PageList();
					pageList.setPageId(pageId);
					pageList.setNavigation(3);
					pageListDao.updatePageList(pageList);
		        }
		        
			}
			//否则普通控件没有新建页面直接删除即可
			componentListDao.deleteComponentList(component.getComponentId()); 
		}
		pageListDao.deletePageListByPageId(projectId,pageId);
		result.setCode(0);
		result.setMsg("SUCCESS");
	}

	@Override
	@Transactional
	public void copyPageList(Integer pageId, Integer projectId, String pageName, ResultJson result) throws Exception {
		if(projectId==null||"".equals(projectId)){
			result.setCode(1);
			result.setMsg("工程ID为空");
			return;
		}
		if(pageId==null||"".equals(pageId)){
			result.setCode(1);
			result.setMsg("页面ID为空");
			return;
		}
		if(null==pageName||pageName.trim().isEmpty()){
			result.setCode(1);
			result.setMsg("页面名称为空");
			return ;
		}
		PageList pageList = new PageList();
		pageList.setPageId(pageId);
		pageList.setProjectId(projectId);
		PageList pageInfo = pageListDao.findPageList(pageList);
		//判断页面名称是否重复
		Map<String,Object> map = new HashMap<String,Object>();
		if(pageName.equals(pageInfo.getPageName())){
			map.put("pageName", "pageName重复");
		}
		//复制页面不需要复制首页属性信息
		pageInfo.setIsindex(0);
		pageInfo.setCreateDate(new Date());
		pageListDao.addPageList(pageInfo);
		Integer newPageId = pageInfo.getPageId();
		//判断当前页面有没有导航
		if(pageInfo.getNavigation()==2){ //有底部导航
			//根据页面ID查询出底部导航所新建的页面copy
			List<PageList> bPageList = pageListDao.getPageInfoByNavId(pageId);
			for (PageList b : bPageList) {
				if(b!=null){
					Integer ob = b.getPageId();
					b.setNavPid(newPageId);
					b.setCreateDate(new Date());
					pageListDao.addPageList(b);
					Integer bPageId = b.getPageId();
					//
					List<ComponentList> bComponents = componentListDao.findComponentByPageId(ob);
					for (ComponentList bComponent : bComponents) {
						if(bComponent!=null){
							bComponent.setPageId(bPageId);
							bComponent.setProjectId(projectId);
							bComponent.setCreated(new Date());
							bComponent.setUpdated(bComponent.getCreated());
							componentListDao.addComponentList(bComponent);
						}
					}
					//获得底部导航新建的页面内有没有嵌套顶部导航所建的页面
					List<PageList>  tPageLists = pageListDao.getPageInfoByNavId(ob);
					for (PageList t : tPageLists) {
						if(t!=null){
							Integer ot = t.getPageId();
							t.setNavPid(bPageId);;
							t.setCreateDate(new Date());
							pageListDao.addPageList(t);
							Integer tPageId = t.getPageId();
							List<ComponentList> tComponents = componentListDao.findComponentByPageId(ot);
							for (ComponentList tComponent : tComponents) {
								if(tComponent!=null){
									//根据控件ID查询出控件信息复制
									tComponent.setPageId(tPageId);
									tComponent.setProjectId(projectId);
									tComponent.setCreated(new Date());
									tComponent.setUpdated(tComponent.getCreated());
									componentListDao.addComponentList(tComponent);
								}
							}
						}
					}

				}
			}
		}
		
		if(pageInfo.getNavigation()==1){ //有顶部导航
			List<PageList>  tPageLists = pageListDao.getPageInfoByNavId(pageId);
			for (PageList t : tPageLists) {
				if(t!=null){
					Integer ot = t.getPageId();
					t.setNavPid(newPageId);
					t.setCreateDate(new Date());
					pageListDao.addPageList(t);
					Integer tPageId = t.getPageId();
					List<ComponentList> tComponents = componentListDao.findComponentByPageId(ot);
					for (ComponentList tComponent : tComponents) {
						if(tComponent!=null){
							//根据控件ID查询出控件信息复制
							tComponent.setPageId(tPageId);
							tComponent.setProjectId(projectId);
							tComponent.setCreated(new Date());
							tComponent.setUpdated(tComponent.getCreated());
							componentListDao.addComponentList(tComponent);
						}
					}
				}
			}
		}
		//普通页面copy
		List<ComponentList> Components = componentListDao.findComponentByPageId(pageId);
		for (ComponentList component : Components) {
			if(component!=null){
				//根据控件ID查询出控件信息复制
				component.setPageId(newPageId);
				component.setProjectId(projectId);
				component.setCreated(new Date());
				component.setUpdated(component.getCreated());
				componentListDao.addComponentList(component);
			}
		}
		String scheme = pageListDao.findSchemeByProjectId(projectId);
		pageInfo.setPagepath(scheme+"://load_vscroll_"+newPageId);
		//避免页面名称重名拼接pageId
		pageListDao.updatePageList(pageInfo);
	
		map.put("newPageId", newPageId);
		result.setCode(0);
		result.setMsg("SUCCESS");
		result.setData(map);
	}

	@Override
	@Transactional
	public void savePageToModule(PageList pageList, ResultJson result) throws Exception{
		Integer projectId = pageList.getProjectId();
		Integer pageId = pageList.getPageId();
		String pageName = pageList.getPageName();
		if(projectId==null){
			result.setCode(1);
			result.setMsg("请求工程ID为空");
			return;
		}
		if(null==pageId){
			result.setCode(1);
			result.setMsg("请求页面ID为空");
			return ;
		}
		PageList pageInfo = pageListDao.findPageList(pageList);
		//判断模板名称是否重复
		Map<String,Object> map = new HashMap<String,Object>();
		List<ModuleList> moduleLists = moduleListDao.findModuleList();
		if(moduleLists.size()!=0){
			for (ModuleList moduleList : moduleLists) {
				if(pageName.equals(moduleList.getModuleName())){
					map.put("moduleName", "模板名重复");
				}
			}
		}
		//新增模板
		ModuleList moduleList = new ModuleList();
		moduleList.setProjectId(projectId);
		moduleList.setModuleName(pageName);
		moduleList.setModuleTitle(pageInfo.getPageTitle());
		moduleList.setCreated(new Date());
		moduleList.setUpdated(moduleList.getCreated());
		moduleListDao.copyModuleList(moduleList);
		//根据新增模板ID复制控件
		Integer moduleId = moduleList.getModuleId();
		List<ComponentList> componentLists = componentListDao.findComponentByPageId(pageId);
		for (ComponentList componentList : componentLists) {
			Integer componentId = componentList.getComponentId();
			if(componentId!=null){
				componentList.setModuleId(moduleId);
				componentList.setPageId(null);
				componentList.setUpdated(new Date());
				componentListDao.addComponentList(componentList);
			}
		}
		map.put("moduleId",moduleId );
		result.setCode(0);
		result.setMsg("SUCCESS");
		result.setData(map);
	}

	@Override
	@Transactional
	public void updatePageIndex(PageList pageList, ResultJson result) throws Exception {
		Integer projectId = pageList.getProjectId();
		Integer pageId = pageList.getPageId();
		if(pageId==null||"".equals(pageId)){
			result.setCode(1);
			result.setMsg("请求页面ID为空");
			return;
		}
		if(projectId==null||"".equals(projectId)){
			result.setCode(1);
			result.setMsg("请求工程ID为空");
			return;
		}
			//设置页面首页前先将工程下所有页面设置为非首页
			List<Integer> pids = pageListDao.findPageIdByProjectId(projectId);
			for (Integer pid : pids) {
				pageList.setPageId(pid);
				pageList.setIsindex(0);
				pageListDao.updatePageList(pageList);
			}
			//设置页面为首页
			pageList.setPageId(pageId);
			pageList.setIsindex(1);
			pageListDao.updatePageList(pageList);
			result.setCode(0);
			result.setMsg("SUCCESS");
		
	}

	@Override
	@Transactional
	public void savePageList(String comSort, ResultJson result) throws Exception {
		ComponentList component = new ComponentList();
		ObjectMapper mapper = new ObjectMapper();  
		JsonNode jsonNode =  mapper.readTree(comSort);
		JsonNode project = jsonNode.findValue("projectId");
		JsonNode page = jsonNode.findValue("pageId");
		JsonNode pageT = jsonNode.findValue("pageType");
		if(null==project){
			result.setCode(1);
			result.setMsg("工程ID空");
		}
		if(null==page){
			result.setCode(1);
			result.setMsg("页面ID空");
		}
		if(null==pageT){
			result.setCode(1);
			result.setMsg("页面类型空");
		}
		Integer pageId = page.asInt();
		//更新控件排序号之前先根据pageId情况
		component.setPageId(pageId);
		component.setSort(0);
		componentListDao.updateComponentList(component);
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


	

	
	
}












