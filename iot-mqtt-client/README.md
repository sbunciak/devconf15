#PoC Project for DevConf 2015 

This project contains simple Camel route *template* configured to communicate using MQTT protocol with remote MQTT broker.

##Libraries

There are 2 libraries you can pick to control GPIOs on your device.

###Pi4J

For Raspberry Pi only. Just uncomment pi4j dependency in pom.xml and you are all set. More at [pi4j.com](http://pi4j.com/)

###libbulldog

This library provides unified api for additional devices (including Raspberry Pi, BeagleBoneBlack, CubieBoard). To use this library you need to locally install binary distribution obtained from [downloads](http://libbulldog.org/bulldog/downloads/).
Before building this project you have to locally install [libbulldog](http://libbulldog.org/bulldog) library which encapsulated interaction with GPIOs. 

	mvn install:install-file -Dfile=bulldog.*board*.jar -DgroupId=org.bulldog -DartifactId=cubieboard -Dversion=0.1.0 -Dpackaging=jar

libbulldog also requires to have its native part to be installed on system with name "bulldog-linux". How to install system library: http://blog.andrewbeacock.com/2007/10/how-to-add-shared-libraries-to-linuxs.html
More about [libbulldog.org](http://libbulldog.org/bulldog/tutorial/).

##Build

To build this project use

	mvn install

To run the project you can execute the following Maven goal

	mvn camel:run

To deploy the project in OSGi. For example using Apache ServiceMix
or Apache Karaf. You can run the following command from its shell:

    osgi:install -s mvn:org.blogdemo/mqtt-consumer/1.0.0-SNAPSHOT
    
To produce executable jar use
	
	mvn clean package -Pdistro 