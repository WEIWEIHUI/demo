import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

class StreamDrainer implements Runnable {
    private InputStream ins;

    public StreamDrainer(InputStream ins) {
        this.ins = ins;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(ins,"gb2312"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

public class Main {
	public static void writeToFile(String info,String filename) throws IOException {
		
		File file =new File(filename);
		Writer out =new FileWriter(file);
		out.write(info);
		out.close();
	}

	public static int call_spider(String date ,String filename){
		   String info = "cd /home/louyu/ && source env/bin/activate && cd spider/tutorial && scrapy crawl build -a date="+date +" -a filename="+filename;
	       String shellname = "//home/louyu/shell.sh"
	       // String info ="cd /home/hadoop/weilinhui/liantongSpider/tutorial && scrapy crawl build -a date="+date +" -a filename="+filename;
           // String call ="/bin/echo \""+info + "\" > /home/hadoop/weilinhui/liantongSpider/a.sh  &&/bin/sh /home/hadoop/weilinhui/liantongSpider/a.sh";
           // String shellname = "/home/hadoop/weilinhui/liantongSpider/a.sh"
            String call="/bin/sh " +shellname;
          
            System.out.println("call：" + call);
        	Runtime runtime = Runtime.getRuntime();
    	
		try {
			writeToFile(info,shellname);
			Process process = runtime.exec(call);
			
			new Thread(new StreamDrainer(process.getInputStream())).start();
			new Thread(new StreamDrainer(process.getErrorStream())).start();
	    	process.getOutputStream().close();
			int exitValue = process.waitFor();//0运行正确
			System.out.println("返回值：" + exitValue);
			return exitValue;
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		   e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return 1;
	}
    
    public static void main(String[] args) {
    	long startTime = System.currentTimeMillis();//获取当前时间
    	call_spider("2018-05","/home/louyu/info/a.xls");
       // call_spider("2018-05","/home/hadoop/weilinhui/liantongSpider/info/a.xls");   
 	    long endTime = System.currentTimeMillis();
    	System.out.println("程序运行时间："+(endTime-startTime)+"ms");
		
	}
}
