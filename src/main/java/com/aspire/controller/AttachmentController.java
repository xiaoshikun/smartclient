package com.aspire.controller;

import com.aspire.service.AttachmentService;
import com.aspire.util.ResultJson;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 附件上传、获取Controller
 * @author 萧杰
 *
 */
@RestController
@RequestMapping("/upload")
public class AttachmentController {
	@Autowired
	private AttachmentService attachmentService;

	Logger logger = Logger.getLogger(AttachmentController.class);

	@RequestMapping(value="/image.do")
	public ResultJson uploadBinary(String image, ResultJson result){
		logger.info("接收图片上传请求:"+image);
		try {
			if(!image.trim().isEmpty()){
				attachmentService.saveImageToFtp(image,result);
				logger.info("图片上传处理成功");
				return result;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
			return ResultJson.build(1,"图片上传出错");
		}
		return null;
}
	
	
	@RequestMapping(value="/apk.do")
	public ResultJson uploadApk(String apkName){
		logger.info("接收apk上传请求:"+apkName);
		try {
			String apkDownloadPath = attachmentService.saveApkToFtp(apkName);
			logger.info("安装包上传成功");
			return ResultJson.oK(apkDownloadPath);
		} catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
			return ResultJson.build(1,"安装包上传出错");
		}
	}
}
