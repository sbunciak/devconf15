package org.jboss.qa.mqtt;

import org.apache.log4j.Logger;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.services.client.api.RemoteRestRuntimeEngineFactory;
import org.kie.services.client.api.command.RemoteRuntimeEngine;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
		bcUrl = prop.getProperty("bpmUrl", "http://localhost:8080/business-central");
	}

	/**
	* Start a process using the rest api start call.
	*
	* @param String
	* @throws Exception
	*/
	public void startProcess(String imageUrl) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("image", imageUrl);
		params.put("requester", "http://sbunciak.brq.redhat.com:8080/rest/doorLock");

		RemoteRestRuntimeEngineFactory remoteRestRuntimeEngineFactory = RemoteRestRuntimeEngineFactory.newBuilder()
				.addDeploymentId(deploymentId).addUrl(new URL(bcUrl))
				.addUserName(username).addPassword(password).addTimeout(60).build();

		RemoteRuntimeEngine engine = remoteRestRuntimeEngineFactory.newRuntimeEngine();
		// Create KieSession and TaskService instances and use them
		KieSession ksession = engine.getKieSession();

		// Each opertion on a KieSession, TaskService or AuditLogService (client) instance
		// sends a request for the operation to the server side and waits for the response
		// If something goes wrong on the server side, the client will throw an exception.
		ProcessInstance processInstance = ksession.startProcess(processId, params);

		log.info("BPMS Process ID: " + processInstance.getProcessId());
		log.info("BPMS Server response: " + processInstance);
	}
}
