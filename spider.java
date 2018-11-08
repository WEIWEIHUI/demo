

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public static void main（）{
	call_spider("2018-05","/home/loyu/info/a.xls");
}



public static int call_spider(String date ,String filename){
    	//scrapy crawl build -a date=2018-05 -a filename=D:\\c.xls

		String call = "cd /home/louyu/ && source env/bin/activate && cd spider/tutorial && scrapy crawl build -a date="+date +" -a filename="+filename;
		//String call = "cmd.exe /c D: &&  echo 1234 > " + filename ;
		Runtime runtime = Runtime.getRuntime();
    	
		try {
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
