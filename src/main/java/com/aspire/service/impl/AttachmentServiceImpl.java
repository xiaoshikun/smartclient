package com.aspire.service.impl;

import com.aspire.config.AndroidConfig;
import com.aspire.service.AttachmentService;
import com.aspire.util.ClientUtil;
import com.aspire.util.ResultJson;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

@Service
public class AttachmentServiceImpl  implements AttachmentService{

	@Autowired
	private AndroidConfig androidConfig;

	Logger logger = Logger.getLogger(AttachmentServiceImpl.class);

	@Value("${ftp.host}")
	private String host;
	@Value("${ftp.port}")
	private Integer port;
	@Value("${ftp.username}")
	private String username;
	@Value("${ftp.password}")
	private String password;
	@Value("${ftp.apkCallPath}")
	private String apkCallPath;
	@Value("${ftp.imageCallPath}")
	private String imageCallPath;
	@Value("${ftp.uploadApkUrl}")
	private String uploadApkUrl;
	@Value("${ftp.uploadImageUrl}")
	private String uploadImageUrl;


	@Override
	public void saveImageToFtp(String image, ResultJson result) throws Exception {
		// 无上传图片
		if(null==image || image.equals("")){
			result.setCode(1);
			result.setMsg("请选择图片");
			return ;
		}
		String code = image.replaceAll(" ", "+");
		//String name = "i"+System.currentTimeMillis()+".png";  //图片命名满足安装包格式要求
		//String target =uploadImageUrl+ File.separator+"icon"+File.separator+name;
		String filePath = getFilePath();
		ClientUtil.decoderBase64File(code,filePath);

		// 生成图片的绝对引用地址
		String callUrl = filePath.replace(uploadImageUrl,imageCallPath);

		//String callUrl = imageCallPath+"icon/"+name;
		result.setCode(0);
		result.setMsg("SUCCESS");
		result.setData(callUrl);
	}

	@Override
	public String saveApkToFtp(String apkName) throws Exception {
		//String apkPath = ClientUtil.loadFilePath("apkout")+File.separator+apkName;
		String apkPath = androidConfig.getApkout()+File.separator+apkName;
		Path source = Paths.get(apkPath);
		logger.info("获取安装包上传资源源路径"+source);
		Path target = Paths.get(uploadApkUrl+File.separator+apkName);
		logger.info("获取安装包目标资源路径"+target);
		Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
		String downloadlUrl =apkCallPath+"apk/"+apkName;
		logger.info("返回安装包访问路径"+downloadlUrl);
		return downloadlUrl;
	}

	public String getFilePath(){
		Date nowDate = new Date();
		// yyyy/MM/dd
		String fileFolder = uploadImageUrl + File.separator +"icon"+File.separator+ new DateTime(nowDate).toString("yyyy")
				+ File.separator + new DateTime(nowDate).toString("MM") + File.separator
				+ new DateTime(nowDate).toString("dd");
		File file = new File(fileFolder);
		if (!file.isDirectory()) {
			// 如果目录不存在，则创建目录
			file.mkdirs();
		}
		// 生成新的文件名 图片命名满足安装包格式要求
		String fileName ="i"+ new DateTime(nowDate).toString("yyyyMMddhhmmssSSSS")+".png";
		return fileFolder + File.separator + fileName;
	}

}








