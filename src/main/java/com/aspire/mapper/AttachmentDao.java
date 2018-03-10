package com.aspire.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface AttachmentDao {
	

	@Insert(value="INSERT INTO icon_imgurl (name,path)VALUES(outFileName,url)")
	public void saveIconPath(@Param("outFileName") String outFileName, @Param("url") String url);


}
