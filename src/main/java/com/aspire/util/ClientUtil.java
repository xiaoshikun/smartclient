package com.aspire.util;

import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public  class ClientUtil {


	public ClientUtil() {
	}

	public static Integer charCutChangeInteger(String contenturl){
		
		if(null!=contenturl&&!"".trim().equals(contenturl)){
			
			String id = contenturl.substring(contenturl.lastIndexOf("_")+1);
			
			return Integer.parseInt(id);
		}
		return null;
		
	}
	
	
	public static String FindAndReplaceChar(String name){
		
		String replaceName = name.replace("-", "_");
		int i = replaceName.lastIndexOf(".");
		StringBuilder sb = new StringBuilder(replaceName);	
		return sb.insert(i, System.currentTimeMillis()).toString();
	}

	/**
	 * 中文字段翻译拼音
	 * @param name
	 * @return
	 */
	public static String ChineseTranslation(String name){
		
		if(!name.trim().isEmpty()){
			String translationName = Pinyin4jUtil.converterToSpell(name);
			String[] layoutfiles = translationName.split(",");
			
			return layoutfiles[0].toLowerCase();
		}
		
		return null;
	}

	/**
	 * 用户手机短信6位确认随机码
	 * @return long
	 */
	public static String getRandom(){

			int random = (int)(Math.random() * 10000 + 99999);

		return String.valueOf(random);
	}


	/**
	 * 判断字符串是否为手机号码
	 * @param src
	 * @return
	 */
	public static boolean isMobileNo(String src){
		String regExp = "^1[3|4|5|7|8]\\d{9}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(src);
		return m.find();
	}
	
	/**
	 * 按天打印日志
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static File createFile(File f) throws IOException {
		Timer timer = new Timer(); //创建定时器对象
		int intervel = 1000*60*60*24; //时间间隔(以毫秒为单位)按天输出日志记录
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String fileName = sdf.format(new Date())+".log";
		final File file = new File(f+File.separator+fileName);
		file.createNewFile();
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				if(!file.exists()){
					try {
						file.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		},intervel,intervel);
		
		return file;
	}
	
	

	/**
	 * 将文件转换为Base64字符串
	 * @param path 文件路径
	 * @return
	 */
	@SuppressWarnings("restriction")
	public static String encodeBase64File(String path){

		FileInputStream fis = null;
		byte[] buff=null;
		try {
			File file = new File(path);
			fis = new FileInputStream(file);
		    buff = new byte[(int) file.length()];
		    fis.read(buff);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				fis.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return new BASE64Encoder().encode(buff);
	}
	/**
	 * 将base64字符解码保存图片
	 * @param base64Code
	 * @param targetPath
	 */
	public static boolean decoderBase64File(String base64Code, String targetPath){
		if(base64Code.equals("")){
			return false;
		}
		byte[] buff ;
		OutputStream os = null;
		try {
			String code = base64Code.replaceAll(" ", "+");
			buff = Base64.decodeBase64(code.getBytes());
			for(int i=0;i<buff.length;i++){
				if(buff[i]<0){
					buff[i]+=256;
				}
			}
			os = new FileOutputStream(new File(targetPath));
			os.write(buff);
			os.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally{
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	
	/**
	 * 图片重命名
	 * @param selectediconimagename
	 * @return
	 */
	public static StringBuilder modifyImageName(String selectediconimagename){
		StringBuilder sb = new StringBuilder(selectediconimagename);
		int i = sb.lastIndexOf(".");
		
		return sb.insert(i, "@2x");
		
	}

	



	/**
	 * 删除图片
	 * @param imgPath
	 */
	public static void deleteImage(String imgPath){
		File file = new File(imgPath);
		file.delete();
	}
	
	/**
	 * 删除目录下的所有文件
	 * @param  dir
	 */
	public static boolean deleteDir(File dir){
		if(dir.isDirectory()){
			String[] children = dir.list();
			for(int i=0;i<children.length;i++){
				boolean success = deleteDir(new File(dir,children[i]));
				if(!success){
					return false;
				}
			}
		}
		return dir.delete();
	}
	
	public static String getFile(String path){
		File file = new File(path);
		File[] arrays = file.listFiles();
		for (int i = 0; i < arrays.length; i++) {
			if(arrays[i].isFile()){
				return arrays[i].getName();
			}
				return arrays[i].getPath();
		}
		return "文件不存在";
	}
	

}









