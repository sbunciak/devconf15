package io.silverspoon.iotdemo.camel;

import java.io.StringWriter;
import java.io.IOException;
import java.io.File;
import java.util.Date;

import java.lang.InterruptedException;

import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

public class WebCamBean {
	private File picturesDirectory = new File ("/var/www/pictures");
	private String outName = "out";
	private String baseUrl = "http://localhost";
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	public String getPicture(String in) throws InterruptedException, IOException {
		System.out.println("Capturing image...");
		final String outFileName = outName + "-" + format.format(new Date());
		final File outFile = new File(picturesDirectory, outFileName + ".jpeg");
		final Process process = Runtime.getRuntime().exec("streamer -c /dev/video0 -s 800x600 -o " + outFile.getAbsolutePath());
		process.waitFor();
		return baseUrl + "/" + outFileName;
	}
	public void setPicturesDirectory(String picturesDirectory){
		this.picturesDirectory = new File(picturesDirectory);
	}
	public String getPicturesDirectory(){
		return this.picturesDirectory.toString();
	}
	public void setOutName(String name){
		this.outName = name;
	}
	public String getOutName(){
		return this.outName;
	}
	public void setBaseUrl(String baseUrl){
		this.baseUrl = baseUrl;
	}
	public String getBaseUrl(){
		return this.baseUrl;
	}
}