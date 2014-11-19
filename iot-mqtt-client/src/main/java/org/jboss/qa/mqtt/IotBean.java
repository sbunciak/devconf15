package org.jboss.qa.mqtt;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

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
