# mock server for w3 search
[![Build Status](https://travis.ibm.com/onesearch-org/mock-server.svg?token=HwP39GU691AJ6S758MxZ)](https://travis.ibm.com/onesearch-org/mock-server)

## Overview
Using open source [netty mockserver](https://www.mock-server.com/#what-is-mockserver) to mock reuqests and responses from different w3 search applications or services
such as: push to wex, query service, fetch service, push.

## Setting up local development environment
This project requires a connection to our Artifactory [virtual](https://na.artifactory.swg-devops.com:443/artifactory/txo-onesearch-w3-maven-virtual) repository, using the local Maven settings.xml file. You can generate the settings.xml in our [Artifactory server](https://na.artifactory.swg-devops.com/artifactory/webapp/#/home). Please refer to this [box document](https://ibm.ent.box.com/notes/323393254483) for additional information.

Once you set up Maven on your local, you can proceed with next steps:

1. Clone the repository
2. Edit the file `./src/main/resources/properties/mockserver.properties` with the host and port for your mockserver
3. Build the code `mvn clean install` 
4. Move to the target folder and run `nohup java -jar mock-server-0.0.1-SNAPSHOT.jar &`

Once the mockserver is started, all expectations defined under `expectations` will be loaded.
The file `expectations/template.json` can be used as a reference to create specific expectations. You only need to create a new json file based on the template and this will be loaded after the mock server is started.
Expectations can also be created in the code directly as described [here](https://www.mock-server.com/mock_server/creating_expectations.html)


## Setting up unit testing
1. Add the following dependency to your pom.xml
```
<dependency>
     	<groupId>com.ibm.w3.search</groupId>
	  	<artifactId>mock-server</artifactId>
	  	<version>0.0.1-SNAPSHOT</version>
	  	<scope>test</scope>
 </dependency>
```
2. Initialize the mockserver as follows:
```
	MockServerW3Search mockServer = new MockServerW3Search();
	mockServer.startMockServer();
	mockServer.loadExpectations();
```

## Contributing Changes
You must issue a pull request to merge code changes into the dev branch.