package com.aspire.service.impl;

import com.aspire.mapper.ProjectPackageListDao;
import com.aspire.pojo.PackageList;
import com.aspire.service.ProjectPackageListService;
import com.aspire.util.ResultJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProjectPackageListServiceImpl implements ProjectPackageListService {

	@Autowired
	private ProjectPackageListDao projectPackageListDao;

	@Override
	public void findPackageListByUserId(Integer userId, ResultJson result)throws  Exception {
		if(userId==null){
			result.setCode(1);
			result.setMsg("请求用户ID为空");
			return;
		}
		List<PackageList> packageLists = projectPackageListDao.findPackageListByUserId(userId);
		if(packageLists.size()==0){
			result.setCode(0);
			result.setMsg("未生成安装包");
			return;
		}
		result.setCode(0);
		result.setMsg("SUCCESS");
		result.setData(packageLists);
	}

	@Override
	public void findPackageListByUserIdAndApkName(Integer userId, String apkName, ResultJson result) throws  Exception{
		if(userId==null){
			result.setCode(1);
			result.setMsg("请求用户ID为空");
			return;
		}
		if("".equals(apkName)&&apkName==null){
			result.setCode(1);
			result.setMsg("请求安装包名为空");
			return;
		}
		String projectName = "%"+apkName+"%";
		List<PackageList> packageLists = projectPackageListDao.findPackageListByUserIdAndApkName(userId,projectName);
		if(packageLists.size()==0){
			result.setCode(0);
			result.setMsg("安装包名不存在或用户ID不存在");
			return;
		}
		result.setCode(0);
		result.setMsg("SUCCESS");
		result.setData(packageLists);
	}

	@Override
	public void deletePackageListByUserIdAndProjectId(Integer projectId, Integer userId, ResultJson result) throws  Exception{
		if(userId==null){
			result.setCode(1);
			result.setMsg("请求用户ID为空");
			return;
		}
		if(projectId==null){
			result.setCode(1);
			result.setMsg("请求工程ID为空");
			return;
		}
		int n = projectPackageListDao.deletePackageListByUserIdAndProjectId(projectId,userId);
		if(n!=-1){
			result.setCode(0);
			result.setMsg("SUCCESS");
			return;
		}
		result.setCode(0);
		result.setMsg("删除安装包出错了");
	}

	@Override
	public boolean addPackageListByUserIdAndProjectId(PackageList packageList)throws  Exception {
		if(null==packageList){
			return false;
		}
		int n = projectPackageListDao.addPackageListByUserIdAndProjectId(packageList);
		if(n!=0){
			return true;
		}else{
			return false;
		}
	}

}
