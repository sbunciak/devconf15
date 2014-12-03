package org.jboss.qa.camel;

/**
 * A bean which we use in the route
 */
public class IotBean {

	/*
	 * Imports are used just to verify dependencies
	 */
	
	public String sayHello(String in) {
		return "Hello " + in;
	}
}
