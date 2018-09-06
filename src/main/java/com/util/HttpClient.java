package com.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpClient {

	public static String triggerGet(String endpointUrl){
		
		StringBuffer responseBuffer = new StringBuffer();
		try {
			URL urlObj = new URL(endpointUrl);
			System.out.println("search_url : "+ endpointUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) urlObj.openConnection();
			httpUrlConn.setRequestMethod("GET");
			System.out.println("ResponseCode : "+ httpUrlConn.getResponseCode());
			if(httpUrlConn.getResponseCode() == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConn.getInputStream()));
				String inputLine;
				while((inputLine = in.readLine()) != null){
					responseBuffer.append(inputLine);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 
		return responseBuffer.toString();
	}
	
	public static boolean triggerPut(String endpointUrl, String payload){

		try {
			URL urlObj = new URL(endpointUrl);
			System.out.println("endpointUrl : "+ endpointUrl);
			System.out.println("payload : "+ payload);
			HttpURLConnection httpUrlConn = (HttpURLConnection) urlObj.openConnection();
			httpUrlConn.setRequestMethod("PUT");
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setRequestProperty("Content-Type", "application/json");
			httpUrlConn.setRequestProperty("Accept", "application/json");
			OutputStreamWriter osw = new OutputStreamWriter(httpUrlConn.getOutputStream());
            osw.write(payload);
            osw.flush();
            osw.close();
			System.out.println("ResponseCode : "+ httpUrlConn.getResponseCode());
			if(httpUrlConn.getResponseCode() == 200 || httpUrlConn.getResponseCode() == 201) {
				 return true;	
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return false;	
	}
}
