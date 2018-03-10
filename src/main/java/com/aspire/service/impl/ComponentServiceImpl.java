package com.aspire.service.impl;

import com.aspire.mapper.ComponentListDao;
import com.aspire.mapper.PageListDao;
import com.aspire.pojo.ComponentList;
import com.aspire.pojo.PageList;
import com.aspire.service.ComponentService;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
@Service
public class ComponentServiceImpl<T> implements ComponentService {
	
	Logger logger = Logger.getLogger(ComponentServiceImpl.class);
	//JsonNodeFactory 实例，可全局共享
    private static JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
//	 //JsonFactory 实例，线程安全
//    private static JsonFactory jsonFactory = new JsonFactory();

	
	@Autowired
	private ComponentListDao componentListDao;
	@Resource
	private PageListDao pageListDao;
	
	
	@Override
	public void addComponentList(String compo, ResultJson result) throws Exception {
		
		ComponentList recomponent = new ComponentList();
		ObjectMapper mapper = new ObjectMapper();  
		JsonNode jsonNode =  mapper.readTree(compo);
		String type = jsonNode.findValue("type").asText();
		
		Integer pId = jsonNode.findValue("pageId").asInt();
		recomponent.setPageId(pId);
		recomponent.setName(jsonNode.findValue("name").asText());
		recomponent.setType(type);
		recomponent.setCreated(new Date());
		recomponent.setUpdated(recomponent.getCreated());
		
		Integer projectId = pageListDao.findProjectIdByPageId(pId);
		recomponent.setProjectId(projectId);
		
		//创建回显页面对象
		com.aspire.component.Component<T> componentList = new com.aspire.component.Component<T>();
		
		//根据工程ID查询当前工程的scheme字段
		String scheme = pageListDao.findSchemeByProjectId(projectId);

		PageList pageInfo = new PageList();
		
		if("TabNavigator".equals(type)){ //导航控件
			
			JsonNode align = jsonNode.findValue("aligntop");
			if(align!=null){
				boolean aligntop = align.asBoolean();
				
				if(!aligntop){ //false 底部导航
					//增加了底部导航，需要更新页面navigation字段为2,作为ios生成包需要固定底部导航判断依据
					pageInfo.setPageId(pId);
					pageInfo.setNavigation(2);
					pageInfo.setPagepath(scheme+"://load_hscroll_"+recomponent.getPageId());
					pageInfo.setUitype("hscroll");
					pageListDao.updatePageList(pageInfo);
					
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
			        	String label = tab.findValue("label").asText();
			        	String title = ClientUtil.ChineseTranslation(label);
						//创建新建页面对象
						PageList pageList = new PageList();
						pageList.setProjectId(projectId);
						pageList.setUitype("vscroll");
						pageList.setIsindex(0);//默认非首页
						pageList.setPageTitle(label);
						pageList.setSort(-1);
						pageList.setPageType(-1);
						pageList.setCreateDate(new Date());
						pageList.setNavigation(0);
						pageList.setNavPid(pId);
						pageListDao.addPageList(pageList);
						
						Integer pageId = pageList.getPageId();
						pageList.setPagepath(scheme+"://load_vscroll_"+pageId);
						//避免页面名称重名拼接pageId
						pageList.setPageName(title+"_"+pageId);
						pageList.setLayoutfile(title+"_"+pageId+".json");
						logger.info("添加导航控件根据label属性名新建页面成功pageList = "+pageList);
						
						//返回导航控件跳转页面地址
						String contenturl = scheme+"://load_vscroll_"+pageId;

					      ObjectNode tabNode = jsonNodeFactory.objectNode();
					      tabNode.put("label", label);
					      tabNode.put("contenturl", contenturl);
					      JsonNode selectedicon = jsonNode.findValue("selectedicon");
					      JsonNode unselectedicon = jsonNode.findValue("unselectedicon");
					      if(null!=selectedicon){
					    	  tabNode.put("selectedicon", selectedicon.asText());
					      }
					      if(null!=unselectedicon){
					    	  tabNode.put("unselectedicon", unselectedicon.asText());
					      }
					      arrayNode.add(tabNode);
						
						pageListDao.updatePageList(pageList);

					}
				      paramsNode.set("tabs", arrayNode);
				      //调用ObjectMapper的writeTree方法根据节点生成最终json字符串
				      String params = mapper.writeValueAsString(paramsNode);
				      recomponent.setParams(params);
					//增加页面控件列表
					componentListDao.addComponentList(recomponent); 
					//回显页面
					componentList.setComponentId(recomponent.getComponentId());
					componentList.setPageId(recomponent.getPageId());
					componentList.setName(recomponent.getName());
					componentList.setType(type);
					com.aspire.component.tabnavigator.Params p = JacksonUtil.readValue(params, com.aspire.component.tabnavigator.Params.class);
					componentList.setParams((T) p);
					result.setCode(0);
					result.setMsg("SUCCESS");
					result.setData(componentList);
					logger.info("新增导航 控件完毕");
					return;
				}
				if(aligntop){ //true 顶部导航
					//当前页面有添加顶部导航控件，就需要设置页面navigation字段为1,作为uitype判断依据
					pageInfo.setPageId(pId);
					pageInfo.setNavigation(1);
					pageInfo.setPagepath(scheme+"://load_hscroll_"+recomponent.getPageId());
					pageInfo.setUitype("hscroll");
					pageListDao.updatePageList(pageInfo);
					
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
			        	String label = tab.findValue("label").asText();
			        	String title = ClientUtil.ChineseTranslation(label);
						//创建新建页面对象
						PageList pageList = new PageList();
						pageList.setProjectId(projectId);
						pageList.setUitype("vscroll");
						pageList.setIsindex(0);//默认非首页
						pageList.setPageTitle(label);
						pageList.setSort(-1);
						pageList.setPageType(-1);
						pageList.setCreateDate(new Date());
						pageList.setNavigation(0);
						pageList.setNavPid(pId);
						pageListDao.addPageList(pageList);
						
						Integer pageId = pageList.getPageId();
						pageList.setPagepath(scheme+"://load_vscroll_"+pageId);
						//避免页面名称重名拼接pageId
						pageList.setPageName(title+"_"+pageId);
						pageList.setLayoutfile(title+"_"+pageId+".json");
						logger.info("添加导航控件根据label属性名新建页面成功pageList = "+pageList);
						
						//返回导航控件跳转页面地址
						String contenturl = scheme+"://load_vscroll_"+pageId;

					      ObjectNode tabNode = jsonNodeFactory.objectNode();
					      tabNode.put("label", label);
					      tabNode.put("contenturl", contenturl);
					      JsonNode selectedicon = jsonNode.findValue("selectedicon");
					      JsonNode unselectedicon = jsonNode.findValue("unselectedicon");
					      if(null!=selectedicon){
					    	  tabNode.put("selectedicon", selectedicon.asText());
					      }
					      if(null!=unselectedicon){
					    	  tabNode.put("unselectedicon", unselectedicon.asText());
					      }
					      arrayNode.add(tabNode);
						
						pageListDao.updatePageList(pageList);

					}
				      paramsNode.set("tabs", arrayNode);
				      //调用ObjectMapper的writeTree方法根据节点生成最终json字符串
				      String params = mapper.writeValueAsString(paramsNode);
				      recomponent.setParams(params);
					//增加页面控件列表
					componentListDao.addComponentList(recomponent); 
					//回显页面
					componentList.setComponentId(recomponent.getComponentId());
					componentList.setPageId(recomponent.getPageId());
					componentList.setName(recomponent.getName());
					componentList.setType(type);
					com.aspire.component.tabnavigator.Params p = JacksonUtil.readValue(params, com.aspire.component.tabnavigator.Params.class);
					componentList.setParams((T) p);
					result.setCode(0);
					result.setMsg("SUCCESS");
					result.setData(componentList);
					return;
				}
			}
			

		}
		
		JsonNode data = jsonNode.findValue("params");
		recomponent.setParams(data.toString());
		//增加页面控件列表
		componentListDao.addComponentList(recomponent); 
		
		componentList.setComponentId(recomponent.getComponentId());
		componentList.setPageId(recomponent.getPageId());
		componentList.setName(recomponent.getName());
		componentList.setType(type);
		if(type.trim().equals("SingleBanner")){
			String jsonParams = recomponent.getParams();
			com.aspire.component.singlebanner.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.singlebanner.Params.class);
			componentList.setParams((T) params);
		}else if(type.trim().equals("HorizScrollBanner")){
			String jsonParams = recomponent.getParams();
			com.aspire.component.horizScrollbanner.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.horizScrollbanner.Params.class);
			componentList.setParams((T) params);
			
		}else if(type.trim().equals("Entries")){
			String jsonParams = recomponent.getParams();
			com.aspire.component.entries.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.entries.Params.class);
			componentList.setParams((T) params);
			
		}else if(type.trim().equals("DividerLine")){
			String jsonParams = recomponent.getParams();
			com.aspire.component.dividerline.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.dividerline.Params.class);
			componentList.setParams((T) params);
			
		}else if(type.trim().equals("TripleBanner")){
			String jsonParams = recomponent.getParams();
			com.aspire.component.triplebanner.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.triplebanner.Params.class);
			componentList.setParams((T) params);
		}else if(type.trim().equals("SquareIcon1LineText")){
			String jsonParams = recomponent.getParams();		
			com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
			componentList.setParams((T) params);
		}else if(type.trim().equals("SquareIcon2LinesText")){
			String jsonParams = recomponent.getParams();		
			com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
			componentList.setParams((T) params);
		}else if(type.trim().equals("SquareIcon3LinesText")){
			String jsonParams = recomponent.getParams();		
			com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
			componentList.setParams((T) params);
		}else if(type.trim().equals("CircleIcon1LineText")){
			String jsonParams = recomponent.getParams();		
			com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
			componentList.setParams((T) params);
		}else if(type.trim().equals("CircleIcon2LinesText")){
			String jsonParams = recomponent.getParams();		
			com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
			componentList.setParams((T) params);
		}else if(type.trim().equals("CircleIcon3LinesText")){
			String jsonParams = recomponent.getParams();		
			com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
			componentList.setParams((T) params);
		}
		result.setCode(0);
		result.setMsg("SUCCESS");
		result.setData(componentList);
		logger.info("新增普通控件完毕");

	}
	
	@Override
	public void updateComponentList(String jsonCompo, ResultJson result) throws Exception {

		ComponentList component = new ComponentList();
		ObjectMapper mapper = new ObjectMapper();  
		JsonNode jsonNode =  mapper.readTree(jsonCompo);
		String type = jsonNode.findValue("type").asText();
		if(jsonNode!=null){
			
			if(jsonNode.findValue("componentId")!=null){
				component.setComponentId(jsonNode.findValue("componentId").asInt());
			}
			if(jsonNode.findValue("pageId")!=null){
				component.setPageId(jsonNode.findValue("pageId").asInt());
			}
			if(jsonNode.findValue("name")!=null){
				component.setName(jsonNode.findValue("name").asText());
			}
			if(jsonNode.findValue("params")!=null){
				component.setParams(jsonNode.findValue("params").toString());
			}
			component.setType(type);
			component.setUpdated(new Date());
			componentListDao.updateComponentList(component);
		}
		//创建回显页面对象
				com.aspire.component.Component<T> componentList = new com.aspire.component.Component<T>();
		//回显页面
			componentList.setComponentId(component.getComponentId());
			componentList.setPageId(component.getPageId());
			componentList.setName(component.getName());
			componentList.setType(type);
		if(type.trim().equals("SingleBanner")){
			String jsonParams = component.getParams();
			com.aspire.component.singlebanner.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.singlebanner.Params.class);
			componentList.setParams((T) params);
		}else if(type.trim().equals("HorizScrollBanner")){
			String jsonParams = component.getParams();
			com.aspire.component.horizScrollbanner.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.horizScrollbanner.Params.class);
			componentList.setParams((T) params);
			
		}else if(type.trim().equals("Entries")){
			String jsonParams = component.getParams();
			com.aspire.component.entries.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.entries.Params.class);
			componentList.setParams((T) params);
			
		}else if(type.trim().equals("DividerLine")){
			String jsonParams = component.getParams();
			com.aspire.component.dividerline.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.dividerline.Params.class);
			componentList.setParams((T) params);
			
		}else if(type.trim().equals("TripleBanner")){
			String jsonParams = component.getParams();
			com.aspire.component.triplebanner.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.triplebanner.Params.class);
			componentList.setParams((T) params);
		}else if(type.trim().equals("SquareIcon1LineText")){
			String jsonParams = component.getParams();		
			com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
			componentList.setParams((T) params);
		}else if(type.trim().equals("SquareIcon2LinesText")){
			String jsonParams = component.getParams();		
			com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
			componentList.setParams((T) params);
		}else if(type.trim().equals("SquareIcon3LinesText")){
			String jsonParams = component.getParams();		
			com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
			componentList.setParams((T) params);
		}else if(type.trim().equals("CircleIcon1LineText")){
			String jsonParams = component.getParams();		
			com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
			componentList.setParams((T) params);
		}else if(type.trim().equals("CircleIcon2LinesText")){
			String jsonParams = component.getParams();		
			com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
			componentList.setParams((T) params);
		}else if(type.trim().equals("CircleIcon3LinesText")){
			String jsonParams = component.getParams();		
			com.aspire.component.squareiconlinetext.Params params = JacksonUtil.readValue(jsonParams, com.aspire.component.squareiconlinetext.Params.class);
			componentList.setParams((T) params);
		}
		
		result.setCode(0);
		result.setMsg("SUCCESS");
		result.setData(componentList);


	}

	@Override
	@Transactional
	public void deleteComponentList(Integer pageId, Integer componentId, ResultJson result) throws Exception {
		logger.info("service层接收删除控件请求pageId="+pageId+"，componentId="+componentId);
		if(null==pageId||"".equals(pageId)){
			result.setCode(1);
			result.setMsg("请求页面ID空");
			return;
		}
		if(null==componentId||"".equals(componentId)){
			result.setCode(1);
			result.setMsg("请求控件ID空");
			return;
		}
	    
		//删除页面控件列表
		//根据控件ID查询是不是导航控件，
		//导航控件需要先删除该导航控件关联的页面和其它控件，
		//最后才能删除导航控件，避免僵尸数据
		ComponentList componentList = componentListDao.findComponentByComponentId(componentId);
		String type = componentList.getType();
		String params = componentList.getParams();
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
		componentListDao.deleteComponentList(componentId); 
		result.setCode(0);
		result.setMsg("SUCCESS");
		
	}
	
	@Override
	public void findComponentListByComponentId(Integer componentId, ResultJson result) {
		logger.info("根据控件Id查询控件列表");
		if(componentId!=null){
			ComponentList componentList = componentListDao.findComponentByComponentId(componentId);
			if(componentList!=null){
				result.setCode(0);
				result.setMsg("SUCCESS");
				result.setData(componentList);
			}else{
				result.setCode(0);
				result.setMsg("控件ID不存在");
			}

		}
		
		logger.info("查询控件列表完毕");
	}


}








