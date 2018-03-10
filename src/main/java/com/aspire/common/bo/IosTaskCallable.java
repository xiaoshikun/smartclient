package com.aspire.common.bo;

import com.aspire.common.Command;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * @author xiaos
 * @create 2017-11-19-14:29
 */
public class IosTaskCallable implements Callable<String> {


    Logger logger = Logger.getLogger(IosTaskCallable.class);

    private  int projectId;
    private String cmd;
    private String mcLogfile;
    public  IosTaskCallable(int projectId,String cmd,String mcLogfile){
        this.projectId = projectId;
        this.cmd = cmd;
        this.mcLogfile = mcLogfile;
    }

    public IosTaskCallable() {
    }

    @Override
    public String call() throws Exception {
        Command command = new Command();
        //加载cmd命令
        //String cmd = ClientUtil.loadFilePath("iosCmd");
        logger.info(cmd);
        command.setCmd(cmd);
        command.writeCmd(cmd);
        String message = command.readCmd();
        //apk生成日志输出文件保存,以工程ID创建目录
        File apkLogOutDir = new File(mcLogfile+ File.separator+projectId);
        if(!apkLogOutDir.exists()){
            apkLogOutDir.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        File apkLogFile = new File(apkLogOutDir+File.separator+sdf.format(new Date())+"_"+projectId+".log");

        logger.info("apkLogFile:"+apkLogFile);
        if(!apkLogFile.exists()){
            try {
                apkLogFile.createNewFile();
            } catch (IOException e1) {
                logger.error(e1.getMessage(),e1);
            }
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(apkLogFile,true);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }
        PrintWriter pw= new PrintWriter(fw);
        String time = sdf.format(new Date());
        pw.println("**********************");
        pw.println("*生成安装包时间:"+time);
        pw.println("**********************");
        //apk日志记录打印
        pw.println("**********start*********");
        pw.println("*生成安装包消息:"+message);
        pw.println("**********end***********");
        pw.close();
        try {
            fw.close();
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        }


        //读取安装包生成日志判断成功，失败
        StringBuffer sb = new StringBuffer();

//        File file = new File("C:\\Users\\xiaos\\Desktop\\2017-11-17 16-50-50_5.log");

        FileReader fr = null;
        BufferedReader br = null;
        try {
//            fr = new FileReader(file);
            fr = new FileReader(apkLogFile);
            br = new BufferedReader(fr);
            String line = null;
            try {
                while ((line=br.readLine())!=null){
                    sb.append(line+"\n");
                }
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }

            String str = sb.toString();
            String success = "finished successfully";
            int i = str.lastIndexOf(success);
            if(i==-1)
            {
                return "ERROR";
            }else{
                return "SUCCESS";
            }

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(),e);
        }finally {
            try {
                br.close();
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
            }
        }
        return null;
    }


}
