package com.ibm.w3.search.mockserver.util;

import java.util.ArrayList;
import java.util.List;

import org.mockserver.model.Cookie;
import org.mockserver.model.Header;
import org.mockserver.model.Parameter;

import com.google.gson.JsonObject;

public class ExpectationW3Search {
    private List<Header> responseHeader = new ArrayList<Header>();
    private List<Header> requestHeader = new ArrayList<Header>();
    private List<Cookie> requestCookie = new ArrayList<Cookie>();
    private List<Parameter> requestParameter = new ArrayList<Parameter>();
    private String method = "";
    private int response_status = 200;
    private String path = "";
    private String request_body_text = "";
    private String response_body_text = "";
    private String request_body_type = "";
    private String response_body_type = "";   
    private JsonObject request_body_json;
    private JsonObject response_body_json;
    
    public List<Cookie> getRequestCookie() {
        return requestCookie;
    }
    public void setRequestCookie(List<Cookie> requestCookie) {
        this.requestCookie = requestCookie;
    }
    
    public List<Parameter> getRequestParameter() {
        return requestParameter;
    }
    public void setRequestParameter(List<Parameter> requestParameter) {
        this.requestParameter = requestParameter;
    }

    
    public String getResponse_body_type() {
        return response_body_type;
    }
    public void setResponse_body_type(String response_body_type) {
        this.response_body_type = response_body_type;
    }
    
    public String getRequest_body_type() {
        return request_body_type;
    }
    public void setRequest_body_type(String request_body_type) {
        this.request_body_type = request_body_type;
    }
    
    public List<Header> getResponseHeader() {
        return responseHeader;
    }
    public void setResponseHeader(List<Header> responseHeader) {
        this.responseHeader = responseHeader;
    }
    public List<Header> getRequestHeader() {
        return requestHeader;
    }
    public void setRequestHeader(List<Header> requestHeader) {
        this.requestHeader = requestHeader;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public int getResponse_status() {
        return response_status;
    }
    public void setResponse_status(int response_status) {
        this.response_status = response_status;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getRequest_body_text() {
        return request_body_text;
    }
    public void setRequest_body_text(String request_body_text) {
        this.request_body_text = request_body_text;
    }
    public String getResponse_body_text() {
        return response_body_text;
    }
    public void setResponse_body_text(String response_body_text) {
        this.response_body_text = response_body_text;
    }
    public JsonObject getRequest_body_json() {
        return request_body_json;
    }
    public void setRequest_body_json(JsonObject request_body_json) {
        this.request_body_json = request_body_json;
    }
    public JsonObject getResponse_body_json() {
        return response_body_json;
    }
    public void setResponse_body_json(JsonObject response_body_json) {
        this.response_body_json = response_body_json;
    }
    
    public String getRequestBodyText() {
	String body = "";
	if (request_body_type.equals("text")) {
	    body =  request_body_text;
	}
	else if (request_body_type.equals("json")) {
	    body =  request_body_json.toString();
	}
	return body;
    }
    
    public String getResponseBodyText() {
	String body = "";
	if (response_body_type.equals("text")) {
	    body = response_body_text;
	}
	else if (response_body_type.equals("json")) {
	    body = response_body_json.toString();
	}
	return body;
    }
}
