Example of Cubieboard GPIO Camel Component usage
================================================

Requirements
------------
For this example to work following requirements must be filled:
* [Cubieboard GPIO Camel Component](https://github.com/pmacik/camel-gpio-cubieboard) should be installed in local Maven repository for this example to work.
* [libbuldog](http://libbulldog.org/bulldog/) native (only *.so) library should be installed.
* The example should be executed on Cubieboard with [sunxi](http://linux-sunxi.org/) linux kernel (tested with [Cubian](http://cubian.org/))

Build
-----
To build this project use

	mvn install

To run the project you can execute the following Maven goal

	mvn camel:run

To produce executable jar use
	
	mvn clean package -Pdistro 