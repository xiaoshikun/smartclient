package com.aspire.service;

import com.aspire.pojo.PackageList;
import com.aspire.util.ResultJson;



public interface ProjectPackageListService {

	void findPackageListByUserId(Integer userId, ResultJson result)throws  Exception;

	void findPackageListByUserIdAndApkName(Integer userId, String apkName, ResultJson result)throws  Exception;

	void deletePackageListByUserIdAndProjectId(Integer projectId, Integer userId, ResultJson result)throws  Exception;
	
	boolean addPackageListByUserIdAndProjectId(PackageList packageList)throws  Exception;

}
