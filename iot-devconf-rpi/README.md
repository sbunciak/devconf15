#IoT Project for DevConf 2015 (Raspberry Pi) 

##Use Cases
This application contains several functionalities: 

- it can interact with remote BPM Suite and trigger a new process instance 
- exposes REST interface to display current temperature (using DS18B20 sensor)
- communicates with other embedded devices via MQTT protocol 

##Technologies

This project is intended to run on *Raspberry Pi* only! It's primary functions are implemented using Apache Camel, ActiveMQ and Bulldog library.

##Build

To build this project use

	mvn install

To run the project you can execute the following Maven goal

	mvn camel:run