package com.aspire.service;

import com.aspire.util.ResultJson;

public interface AttachmentService {

	 void saveImageToFtp(String image, ResultJson result) throws Exception;

	 String saveApkToFtp(String apkName) throws Exception;
}
