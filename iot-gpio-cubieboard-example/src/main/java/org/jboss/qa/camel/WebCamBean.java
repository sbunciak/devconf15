package org.jboss.qa.camel;

import java.io.StringWriter;
import java.io.IOException;
import java.io.File;

import java.lang.InterruptedException;

import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;

public class WebCamBean {
	private File picturesDirectory = new File ("/var/www/pictures");
	private String outName = "out";
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	public String getPicture(String in) throws InterruptedException, IOException {
		System.out.println("Capturing image...");
		File out = new File(picturesDirectory, outName + ".jpeg");
		if (out.exists()) {
			File outBackup = new File(picturesDirectory, outName + "-" + format.format(out.lastModified()) + ".jpeg");
			if (!out.renameTo(outBackup)) {
				throw new IOException("Couldn't rename " + out.getAbsolutePath() + " to " + outBackup.getAbsolutePath());
			}
		}
		Process process = Runtime.getRuntime().exec("streamer -c /dev/video0 -s 800x600 -o " + (new File(picturesDirectory, outName + ".jpeg")).getAbsolutePath());
		/*StringWriter stdWriter = new StringWriter();
		StringWriter errWriter = new StringWriter();
		IOUtils.copy(process.getInputStream(), stdWriter, "UTF-8");
		IOUtils.copy(process.getErrorStream(), errWriter, "UTF-8");*/
		process.waitFor();
		return in;
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
}