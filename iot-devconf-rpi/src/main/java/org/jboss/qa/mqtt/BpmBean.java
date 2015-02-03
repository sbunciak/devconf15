package org.jboss.qa.mqtt;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Class to start remote BPMN process in JBoss BPM Suite.
 * 
 * @author sbunciak
 *
 */
public class BpmBean {

	private Logger log = Logger.getLogger(BpmBean.class);

	private String deploymentId = "org.jboss.qa:doorkeeper:1.0";
	private String processId = "doorkeeping";

	private String username = null;
	private String password = null;
	private String bcUrl = null;

	public BpmBean() {
		Properties prop = new Properties();
		InputStream in = getClass().getResourceAsStream("/rpi.properties");
		try {
			prop.load(in);
			in.close();
		} catch (IOException e) {
			log.warn("Failed to load properties.", e);
		}

		username = prop.getProperty("bpmUser", "bpmadmin");
		password = prop.getProperty("bpmPwd", "admin1admin?");
		bcUrl = prop.getProperty("bpmUrl", "http://localhost:8080/business-central/rest/");
	}

	/**
	* Start a process using the rest api start call.
	*
	* @param String
	* @throws Exception
	*/
	public void startProcess(String imageUrl) throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put("image", imageUrl);

		String newInstanceUrl = bcUrl + "runtime/" + deploymentId + "/process/" + processId + "/start";
		log.info("BPMS Instance Url: " + newInstanceUrl);

		String dataFromService = getDataFromService(newInstanceUrl, "POST", params, true);
		log.info("BPMS Server response: " + dataFromService);
	}

	private void doAuthorization2(String url, HttpClient httpclient, PostMethod method) {
		httpclient.getState().setCredentials(
				new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
				new UsernamePasswordCredentials(username, password)
				);
		method.setDoAuthentication(true);
		httpclient.getParams().setAuthenticationPreemptive(true);
	}

	private String getDataFromService(String urlpath, String method, Map<String, String> params, boolean multipart) {
		boolean handleException = false;
		// extract required parameters
		String urlStr = urlpath;
		String result = "";
		if (urlStr == null) {
			throw new IllegalArgumentException("Url is a required parameter");
		}
		if (method == null || method.trim().length() == 0) {
			method = "GET";
		}
		HttpClient httpclient = new HttpClient();
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(3000);
		httpclient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		PostMethod theMethod = null;
		theMethod = new PostMethod(urlpath);
		theMethod.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		doAuthorization2(urlpath, httpclient, theMethod);
		try {
			Header[] headers = theMethod.getRequestHeaders();
			for (Header header : headers) {
				log.info(header.getName() + ":[" + header.getValue() + "]");
			}
			int responseCode = httpclient.executeMethod(theMethod);
			log.info("Call Restful API again authMethod responseCode:[" + responseCode + "] ");
			Map<String, Object> results = new HashMap<String, Object>();
			if (responseCode >= 200 && responseCode < 300) {
				result = theMethod.getResponseBodyAsString();
				log.info("Successfully completed :[" + result + "]");
			} else {
				if (handleException) {
					log.info("handleException responseCode:[" + responseCode
							+ "] theMethod.getResponseBodyAsString():[" + theMethod.getResponseBodyAsString()
							+ "] urlStr:[" + urlStr + "]");
				} else {
					log.info("Unsuccessful responseCode:[" + responseCode
							+ "] theMethod.getResponseBodyAsString():[" + theMethod.getResponseBodyAsString()
							+ "] urlStr:[" + urlStr + "]");
					results.put("StatusMsg",
							"endpoint " + urlStr + " could not be reached: " + theMethod.getResponseBodyAsString());
				}
			}
			results.put("Status", responseCode);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			theMethod.releaseConnection();
		}
		return result;
	}
}
