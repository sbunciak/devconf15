package org.jboss.qa.mqtt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.silverspoon.bulldog.devices.sensors.DS18B20TemperatureSensor;

/**
 * A bean which we use in the route
 */
public class TemperatureBean {

	private static List<DS18B20TemperatureSensor> sensors = new ArrayList<DS18B20TemperatureSensor>();

	static {
		// Load sensors
		File sensorDir = new File("/sys/bus/w1/devices/");
		
		for (File sensorFile : sensorDir.listFiles()) {
			if (!sensorFile.getName().startsWith("w1_bus_master")) {
				System.out.println("Adding sensor file: " + sensorFile);
				sensors.add(new DS18B20TemperatureSensor(sensorFile));
			}
		}
		
	}

	public float getTemperature() {
		float res = 0.00f;

		try {
			res = sensors.get(0).readTemperature();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res;
	}
}
