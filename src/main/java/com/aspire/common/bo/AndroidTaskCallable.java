package com.aspire.common.bo;

import com.aspire.common.Command;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * @author xiaos
 * @create 2017-11-19-14:29
 */
public class AndroidTaskCallable implements Callable<String> {

    Logger logger = Logger.getLogger(AndroidTaskCallable.class);

    private  int projectId;
    private String cmd;
    private String anLogFile;
    public AndroidTaskCallable(int projectId,String cmd,String anLogFile){
        this.projectId = projectId;
        this.cmd = cmd;
        this.anLogFile = anLogFile;
    }

    public AndroidTaskCallable() {
    }

    @Override
    public String call() throws Exception {
        Command command = new Command();
        logger.info(cmd);
        command.setCmd(cmd);
        command.writeCmd(cmd);
        String message = command.readCmd();
        //apk生成日志输出文件保存,以工程ID创建目录
        Date nowDate = new Date();
        File apkLogOutDir = new File(anLogFile+ File.separator+new DateTime(nowDate).toString("yyyy")+ File.separator + new DateTime(nowDate).toString("MM")+projectId);

        if(!apkLogOutDir.exists()){
            apkLogOutDir.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        File apkLogFile = new File(apkLogOutDir+File.separator+sdf.format(nowDate)+"_"+projectId+".log");
        logger.info("apkLogFile:"+apkLogFile);
        if(!apkLogFile.exists()){
            apkLogFile.createNewFile();
        }


        FileWriter fw = null;
        fw = new FileWriter(apkLogFile,true);

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
        fw.close();

        //读取安装包生成日志判断成功，失败
        StringBuffer sb = new StringBuffer();

        FileReader fr = null;
        BufferedReader br = null;
        fr = new FileReader(apkLogFile);
        br = new BufferedReader(fr);
        String line = null;
        while ((line=br.readLine())!=null){
            sb.append(line+"\n");
        }
        String str = sb.toString();
        String success = "BUILD SUCCESSFUL";
        int i = str.lastIndexOf(success);
        br.close();
        if(i==-1)
        {
            return "ERROR";
        }else{
            return "SUCCESS";
        }


    }


}
