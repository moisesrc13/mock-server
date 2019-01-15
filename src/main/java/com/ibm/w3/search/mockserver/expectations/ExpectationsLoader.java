package com.ibm.w3.search.mockserver.expectations;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.Cookie;
import org.mockserver.model.Header;
import org.mockserver.model.Parameter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.ibm.w3.search.mockserver.util.ExpectationW3Search;

public class ExpectationsLoader{
    
   private static Logger log = Logger.getLogger(ExpectationsLoader.class);
   ClassLoader classLoader;
   private String expectations_path = "expectations";
   private int port;
   private String host;
   
   public ExpectationsLoader(String host, int port) {
       this.host = host;
       this.port = port;       
   }
   
   public void loadExpectations() {       
       final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());       
       try {
       if(jarFile.isFile()) {  // Run with JAR file
           final JarFile jar = new JarFile(jarFile);
           final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
           while(entries.hasMoreElements()) {
               final String name = entries.nextElement().getName();
               if (name.startsWith(expectations_path + "/")) { //filter according to the path                   
                   if (!name.equals(expectations_path+"/template.json") && !name.equals(expectations_path+"/")) {
  	             createExpectationFromJSON(name, true);
  	          }
               }
           }
           jar.close();
       } else { // Run with IDE
           final URL url = getClass().getResource("/" + expectations_path);
           if (url != null) {
               try {
                   final File dir = new File(url.toURI());
                   for (File file : dir.listFiles()) {                       
                       createExpectationFromJSON(file.getAbsolutePath(), false);
                   }
               } catch (URISyntaxException ex) {
        	   log.info("Error reading expectations dir "+ex.getMessage());
               }
           }
       }
    }catch(Exception e) {
	
    }
       
   }
      
   public <T> List<T> getListOfKeys(String type, JsonObject obj){
       List<T> list = new ArrayList<T>();
       if(obj.has(type)) {
     		JsonArray array = obj.get(type).getAsJsonArray();		
     		for (JsonElement el : array) {
     		    JsonObject child_obj = el.getAsJsonObject();
     		    String name = child_obj.get("name").getAsString();
     		    String value = child_obj.get("value").getAsString();
     		    if(type.equals("parameters")) {
     			list.add((T) new Parameter(name,value));
     		    }if(type.equals("headers")) {
     			list.add((T) new Header(name,value));
     		    }if(type.equals("cookies")) {
     			list.add((T) new Cookie(name,value));
     		    }
     		}
     	}
       return list;
   }
   
   public void createExpectationFromJSON(String expectations_file, boolean isJar) {
    
       log.info("loading expectations :"+expectations_file);
       BufferedReader bufferedReader = null;
        try {	 
    	 if (isJar) {
    	     InputStream is = getClass().getClassLoader().getResourceAsStream(expectations_file);
    	     InputStreamReader isr = new InputStreamReader(is);	
    	     bufferedReader = new BufferedReader(isr);
    	 }else {
    	     FileReader fileReader = new FileReader(expectations_file);
    	     bufferedReader = new BufferedReader(fileReader);
    	 }
        }catch(IOException e) {
    		log.error("Error reading "+expectations_file);
        }
	
	 if (bufferedReader == null) {
	     log.info("Noting to load, not able to read expecation file"+expectations_file);
	     return;
	 }else {
	   
	     try {
	     Gson gson = new Gson();
	     JsonObject json = (JsonObject)gson.fromJson(bufferedReader, JsonObject.class);
	     JsonArray expectations = (JsonArray)json.getAsJsonArray("expectations");
	     
	     String path = "";
	     String method = "";
	     ExpectationW3Search expectation_w3search = new ExpectationW3Search();
	     for (JsonElement ex : expectations) {
	         JsonObject expectationsObj = ex.getAsJsonObject();
	         
	         //------------------------------REQUEST-----------------------------------
	         JsonObject requestObj = expectationsObj.getAsJsonObject("request");
	         path = requestObj.get("path").getAsString();
	         expectation_w3search.setPath(path);
	         method = requestObj.get("method").getAsString();
	         expectation_w3search.setMethod(method);
	         
	         //check if headers	    
	         expectation_w3search.setRequestHeader(getListOfKeys("headers",requestObj));
	         
	         //get the body
	         //check if body is a json object	    
	         if(requestObj.has("body")) {		
	     	Object obj = requestObj.get("body");
	     	if (obj instanceof JsonPrimitive) {
	     	    //this is a text body
	     	    expectation_w3search.setRequest_body_text(requestObj.get("body").getAsString());
	     	    expectation_w3search.setRequest_body_type("text");
	     	}else if (obj instanceof JsonObject) {
	     	    //this is a Json object
	     	    expectation_w3search.setRequest_body_json(requestObj.get("body").getAsJsonObject());
	     	    expectation_w3search.setRequest_body_type("json");
	     	}		
	         }else {
	     	//set no body
	     	expectation_w3search.setRequest_body_text("");
	     	expectation_w3search.setRequest_body_type("text");
	         }
	         
	         //check parameters	    
	         expectation_w3search.setRequestParameter(getListOfKeys("parameters",requestObj));
	        
	         //check cookies
	         expectation_w3search.setRequestCookie(getListOfKeys("cookies",requestObj));
	         
	         
	         //------------------------------RESPONSE-----------------------------------
	         JsonObject responseObj = expectationsObj.getAsJsonObject("response");
	         int status = responseObj.get("status").getAsInt();
	         expectation_w3search.setResponse_status(status);
	         
	         //check if headers	    
	         expectation_w3search.setResponseHeader(getListOfKeys("headers",responseObj));
	         
	         //get the body
	         //check if body is a json object	    
	         if(responseObj.has("body")) {		
	     	Object obj = responseObj.get("body");
	     	if (obj instanceof JsonPrimitive) {
	     	    //this is a text body
	     	    expectation_w3search.setResponse_body_text(responseObj.get("body").getAsString());
	     	    expectation_w3search.setResponse_body_type("text");
	     	}else if (obj instanceof JsonObject) {
	     	    //this is a Json object
	     	    expectation_w3search.setResponse_body_json(responseObj.get("body").getAsJsonObject());
	     	    expectation_w3search.setResponse_body_type("json");
	     	}		
	         }else {
	     	//set no body
	     	expectation_w3search.setResponse_body_text("");
	     	expectation_w3search.setResponse_body_type("text");
	         }
	         //create the expectation	    
	         new MockServerClient(host, port)
	         .when(
	             request()
	                 .withMethod(expectation_w3search.getMethod())
	                 .withPath(expectation_w3search.getPath())
	                 .withHeaders(expectation_w3search.getRequestHeader())
	                 .withQueryStringParameters(expectation_w3search.getRequestParameter())
	                 .withCookies(expectation_w3search.getRequestCookie())
	                 
	         )
	         .respond(
	             response()
	                 .withStatusCode(expectation_w3search.getResponse_status())
	                 .withHeaders(expectation_w3search.getResponseHeader())	         
	                 .withBody(expectation_w3search.getResponseBodyText())
	                 
	         );
	     }//main loop for loading
	        log.info("expectations loaded "+expectations_file);
	        
	 	bufferedReader.close();
	     } catch (IOException e) {
	 	log.error("Error on closing bufferedReader for "+expectations_file);
	     }

	     
	     
	 }//end bufferedReader not null 
	
           
       
   }//end createExpectationFromJSON function

}//end class
