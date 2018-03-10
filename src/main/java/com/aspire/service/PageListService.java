package com.aspire.service;

import com.aspire.pojo.PageList;
import com.aspire.util.ResultJson;


public interface PageListService {

	void addPageList(PageList pageList, ResultJson result)throws Exception;

	void updatePageList(PageList pageList, ResultJson result)throws Exception;

	void findPageListByPageId(Integer projectId, Integer pageId, ResultJson result)throws Exception;

	void savePageList(String comSort, ResultJson result) throws Exception;

	void deletePageListByPageId(Integer projectId, Integer pageId, ResultJson result)throws Exception;

	void copyPageList(Integer pageId, Integer projectId, String pageName, ResultJson result)throws Exception;

	void updatePageIndex(PageList pageList, ResultJson result)throws Exception;

	void savePageToModule(PageList pageList, ResultJson result)throws Exception;
	



}
