package com.aspire.service;

import com.aspire.util.ResultJson;


public interface ComponentService {

	void addComponentList(String componentList, ResultJson result) throws Exception;

	void updateComponentList(String componentList, ResultJson result) throws Exception;

	void deleteComponentList(Integer pageId, Integer componentId, ResultJson result) throws Exception;

    void findComponentListByComponentId(Integer componentId, ResultJson result) throws  Exception;
}
