package com.ibm.w3.search.mockserver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.mockserver.integration.ClientAndServer;

import com.ibm.w3.search.mockserver.expectations.ExpectationsLoader;

public class MockServerW3Search {
    
    private ClientAndServer mockServer;
    private int port;
    private String host;
    
    private static Logger log = Logger.getLogger(MockServerW3Search.class);
    ClassLoader classLoader;
    private String properties_file = "properties/mockserver.properties"; 

    public MockServerW3Search() {	
	classLoader = getClass().getClassLoader();
	Properties prop = new Properties();
	InputStream input;
	try {
	    input = classLoader.getResourceAsStream(properties_file);
	    prop.load(input);
	    this.host = prop.getProperty("host");
	    this.port = Integer.parseInt(prop.getProperty("port"));
	    log.info("mockserver host: "+this.host+" using port:"+this.port);
	} catch (FileNotFoundException e) {	    
	    log.error("Failed to load properties file for mock server, file not found "+properties_file);
	} catch (IOException e) {
	    log.error("Failed to load properties file for mock server");
	}
	
    }
    public int getPort() {
	 return port;
    }
    
    public String getHost() {
	return  host;
    }
    
    @Before
    public void startMockServer() {   
	
        if (mockServer == null) {
            mockServer = ClientAndServer.startClientAndServer(port);
            
        }
    }
    

    @After
    public void stopMockServer() {
        mockServer.stop();
    }
   
    public void loadExpectations() {
	ExpectationsLoader loader = new ExpectationsLoader(host, port);
	loader.loadExpectations();
    }
    
    public static void main(String[] args) {	
	MockServerW3Search mockServer = new MockServerW3Search();
	mockServer.startMockServer();
	mockServer.loadExpectations();
    }
}
