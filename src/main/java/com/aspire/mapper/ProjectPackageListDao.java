package com.aspire.mapper;

import com.aspire.pojo.PackageList;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;
public interface ProjectPackageListDao {

	List<PackageList> findPackageListByUserId(@Param("userId") int userId);

	List<PackageList> findPackageListByUserIdAndApkName(@Param("userId") int userId, @Param("projectName") String projectName);

	@Delete(value="DELETE FROM package_list WHERE  project_id = #{projectId} AND user_id = #{userId}")
	int deletePackageListByUserIdAndProjectId(@Param("projectId") int projectId, @Param("userId") int userId);

	int addPackageListByUserIdAndProjectId(PackageList packageList);

}




