import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author xiaos
 * @create 2018-02-15-16:06
 */
public class CmdDemo {

    public static void main(String[] args) throws Exception{

            String cmd = "java -Dfile.encoding=utf-8 -jar D:/smartclient/smartclientTool/TemplateTool.jar D:/smartclient/smartclientTool/smartclient.json";
            String logfile = "D:/smartclient/logs";
        //创建一个线程池
		ExecutorService exec = Executors.newFixedThreadPool(2);
		//创建有返回值的任务
		AndroidTaskCallable taskCallable = new AndroidTaskCallable(cmd,logfile);//submit返回一个Future，代表了即将要返回的结果
		//执行任务并获取Future对象
		Future<String> f1 = exec.submit(taskCallable);
		//从Future对象上获取任务的返回值，并输出到控制台
        String cmdStr = f1.get().toString();

        System.out.println(cmdStr);

        exec.shutdown();



    }

}