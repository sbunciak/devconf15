package org.jboss.qa.mqtt;

import org.apache.camel.spring.SpringCamelContext;

public class CamelMain {

	public static void main(String[] args) throws Exception {
		// Start camel route
		SpringCamelContext.springCamelContext("META-INF/spring/camelContext.xml");
	}
}
