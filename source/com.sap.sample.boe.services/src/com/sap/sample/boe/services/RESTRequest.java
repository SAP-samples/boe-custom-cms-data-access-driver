package com.sap.sample.boe.services;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONObject;

import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.occa.infostore.IInfoObject;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;

public class RESTRequest {

	public static final String BASE_URL = "SELECT top 1 si_access_url FROM  CI_APPOBJECTS WHERE si_kind='RestWebService'";

	public static enum TOKEN_TYPE {
		token, serializedSession
	}

	private static String LOGON_URL = "/logon/token";
	
	private SessionHandler sessionHandler;
    private String logonToken;
    private String baseUrl;
    private String responseContent;
    private int responseCode;
    private Map<String, List<String>> responseHeaders;

    public RESTRequest() {
    	
    }
    
    /**
     * URL for Web Intelligence REST API
     * @return
     */
    public String getWEBI_RWS() {
    	return "/raylight/v1";
    }

    /**
     * Query the Access URL from the CMS and connect using the serialized session
     * @param infoStore
     * @param serializedSession
     * @throws Exception
     */
    public void connect(IInfoStore infoStore, String serializedSession) throws Exception {
    	// query the url from the CMS
    	IInfoObjects infoObjects = infoStore.query(BASE_URL);
    	IInfoObject infoObject = (IInfoObject)infoObjects.get(0);
    	baseUrl = infoObject.properties().getString("SI_ACCESS_URL");
    	sessionHandler = new SessionHandler(serializedSession);
    }

    /**
     * Get the Access URL from the InfoObject and create another session using the provided serialized session
     * @param infoObjects
     * @param serializedSession
     * @throws Exception
     */
    public void connectNewSession(IInfoObjects infoObjects, String serializedSession) throws Exception {
    	// query the url from the CMS
    	IInfoObject infoObject = (IInfoObject)infoObjects.get(0);
    	baseUrl = infoObject.properties().getString("SI_ACCESS_URL");

    	sessionHandler = new SessionHandler(serializedSession, CrystalEnterprise.getSessionMgr());
    }

    /**
     * Send request to logon with session token
     * @param sessionToken
     * @throws Exception
     */
    private void logon(String sessionToken, TOKEN_TYPE tokenType) throws Exception {
    	send(baseUrl + LOGON_URL, "GET", null, false);
    	// TOKEN_TYPE tokenType = TOKEN_TYPE.serializedSession;
    	if (tokenType == TOKEN_TYPE.serializedSession) sessionToken = StringEscapeUtils.escapeXml(sessionToken);
        // Sets logon information
        Map<String, String> map = new HashMap<String, String>();
        map.put("//attr[@name='tokenType']", tokenType.toString());
        map.put("//attr[@name='logonToken']", sessionToken);
		String filledLogonResponse = responseContent.replaceFirst("<attr name=\"logonToken\" type=\"string\"></attr>", 
				                                                  "<attr name=\"logonToken\" type=\"string\">" + sessionToken + "</attr>");
		// must specify the correct token type
		filledLogonResponse = filledLogonResponse.replaceFirst(">token</attr>", ">" + tokenType.toString() + "</attr>");
        send(baseUrl + LOGON_URL, "POST", filledLogonResponse, false);
        
        logonToken = responseHeaders.get("X-SAP-LogonToken").get(0);
    }
    
    /**
     * send request using JSON
     * @param requestUrl
     * @param method
     * @param jsonContent
     * @return
     * @throws Exception
     */
	public JSONObject sendRequestJSON(String requestUrl, String method, String jsonContent) throws Exception {
		getLogonToken(requestUrl);
       	send(baseUrl + requestUrl, method, jsonContent, true);
        JSONObject document = new JSONObject(responseContent);
        if (responseCode == HTTP_OK) return document;
        if (responseCode == HTTP_NOT_FOUND) {
            // If the request returns the response error code "400"
        	// errorCode = document.getString("error_code");
            // errorMessage = document.optString("message");
        } else {
            throw new IllegalArgumentException(document.toString());
        }
        return null;
	}

	/**
	 * send GET request and return XML
	 * @param requestUrl
	 * @return
	 * @throws Exception
	 */
	public String sendRequestGet(String requestUrl) throws Exception {
		getLogonToken(requestUrl);
       	send(baseUrl + requestUrl, "GET", null, false);
        if (responseCode == HTTP_OK) return responseContent;
        return null;
	}

	
    /**
     * Send the request
     * @param url
     * @param method
     * @param content
     * @param useJSON
     * @throws Exception
     */
    private void send(String url, String method, String content, boolean useJSON) throws Exception {
		getLogonToken(url);
    	String accept = (useJSON ? "application/json" : "application/xml");
        responseContent = null;
        responseHeaders = null;
        responseCode = 0;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);

        if (accept != null) {
            connection.setRequestProperty("Accept", accept);
        }

        if (this.logonToken != null) {
            connection.setRequestProperty("X-SAP-LogonToken", this.logonToken);
            // connection.setRequestProperty("X-SAP-PVL", "de-DE");            
        }

        InputStream in = null;
        DataOutputStream out = null;
        try {
            // reads response
        	if (content == null) {
        		in = connection.getInputStream();
            // sets content and reads response
        	} else {
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", accept);   // "application/xml");
                connection.setRequestProperty("Content-Length", String.valueOf(content.getBytes("UTF-8").length));
                connection.setRequestProperty("X-SAP-PVL", "en-US");            
                out = new DataOutputStream(connection.getOutputStream());
                out.write(content.getBytes("UTF-8"));
                out.flush();
                in = (InputStream) connection.getContent();
        	}
        } catch (IOException e) {
            in = connection.getErrorStream();
        } finally {
        	if(out != null) out.close();
        }
        
        try{
        	if (in == null)
        		throw new Exception("Connection to " + url + " failed");

        	Scanner scanner = new Scanner(in, "UTF-8").useDelimiter("\\A");
        	responseContent = scanner.hasNext() ? scanner.next() : "";
        	responseHeaders = connection.getHeaderFields();
            responseCode = connection.getResponseCode();
        } finally {
        	if(in != null) in.close();
        	if(connection != null) connection.disconnect();
        }
    }

    /**
     * 
     * @param url
     * @throws Exception
     */
    private void getLogonToken(String url) throws Exception {
    	if (logonToken == null && !url.endsWith(LOGON_URL)) {
    		logon(sessionHandler.getSerializedSession(), TOKEN_TYPE.serializedSession);
    	}

    }

}
