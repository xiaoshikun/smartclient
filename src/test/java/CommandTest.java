import org.apache.log4j.Logger;

import java.io.*;

public  class CommandTest extends Thread {
	Logger logger = Logger.getLogger(CommandTest.class);
	 private Process p;
	 private InputStream is;
	 private OutputStream os;
	 private BufferedWriter bw;
	 private BufferedReader br;
	 private InputStream stdErr;
	 public CommandTest() {
	 }
	 //获取Process的输入，输出流
	 public void setCmd(String cmd) {
	  try {
	   p = Runtime.getRuntime().exec(cmd);
	   os = p.getOutputStream();
	   is = p.getInputStream();
	   stdErr = p.getErrorStream();
	   logger.info(stdErr);
	  } catch (IOException e) {
	   logger.error(e.getMessage(),e);
	  }
	 }
	 //向Process输出命令
	 public void writeCmd(String cmd) {
	  try {
	   bw = new BufferedWriter(new OutputStreamWriter(os));
	   bw.write(cmd);
	   bw.newLine();
	   bw.flush();
	  } catch (Exception e) {
	   logger.error(e.getMessage(),e);
	  }finally{
		  try {
			bw.close();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	  }
	 }
	 //读出Process执行的结果
	 public String readCmd() {
	  StringBuffer sb = new StringBuffer();
	  br = new BufferedReader(new InputStreamReader(is));
	  String buffer = null;
	  try {
	   while ((buffer = br.readLine()) != null) {
	    sb.append(buffer + "\n");
	   }
	   logger.info("p.waitFor():"+p.waitFor());
	  } catch (Exception e) {
	   logger.error(e.getMessage(),e);
	  }finally{
		  try {
			br.close();
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
	  }
	  return sb.toString();
	 }
	 

	 

}
