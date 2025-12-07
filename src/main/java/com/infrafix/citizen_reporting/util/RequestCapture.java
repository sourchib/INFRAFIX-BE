package com.infrafix.citizen_reporting.util;

import jakarta.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class RequestCapture {

    private static String processingData(HttpServletRequest request){
        String headerName="";
        String paramName = "";
        Map<String,Object> requestBuild = new HashMap<>();
        Map<String,Object> reqParam = new HashMap<>();
        Map<String,Object> reqHeader =  new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            headerName = headerNames.nextElement();
            reqHeader.put(headerName,request.getHeader(headerName));
        }

        Enumeration<String> params = request.getParameterNames();
        while(params.hasMoreElements()){
            paramName = params.nextElement();
            reqParam.put(paramName,request.getParameter(paramName));
        }
        requestBuild.put("authType",request.getAuthType());
        requestBuild.put("method",request.getMethod());
        requestBuild.put("serverName",request.getServerName());
        requestBuild.put("session",request.getSession());
        requestBuild.put("queryString",request.getQueryString());
        requestBuild.put("remoteAddr",request.getRemoteAddr());
        requestBuild.put("requestSessionId",request.getRequestedSessionId());
        requestBuild.put("serverPort",request.getServerPort());
        requestBuild.put("pathInfo",request.getPathInfo());
        requestBuild.put("remoteHost",request.getRemoteHost());
        requestBuild.put("locale",request.getLocale());
        requestBuild.put("principal",request.getUserPrincipal());
        requestBuild.put("isSecure",request.isSecure());
        requestBuild.put("reqHeader",reqHeader);
        requestBuild.put("reqParam",reqParam);
        try{
            System.out.println("Request Get Method: " + request.getMethod());
            if ("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod()) || "PATCH".equalsIgnoreCase(request.getMethod()))
            {
                requestBuild.put("reqBody",readInputString(request));
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        String strValue = new JSONObject(requestBuild).toString();
        return strValue;
    }

    private static String readInputString(HttpServletRequest request){
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        String requestBody;

        try{
            reader = request.getReader();
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            requestBody = sb.toString();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        return requestBody;
    }

    public static String allRequest(HttpServletRequest requestIn){
        ContentCachingRequestWrapper request = new ContentCachingRequestWrapper(requestIn);
        return processingData(request);
    }
}
