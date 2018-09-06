package com.aws.lambda.function.plans;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class PlansSearch implements RequestHandler<Request, Response>  {
	
	public Response handleRequest(Request request, Context context) {

		String elastic_search_url = "https://search-pc-domain-dtmuia33nuk4lskeequrpnizsu.us-east-2.es.amazonaws.com/plans/_search?q=";
		
		if(request.planName!=null){
			elastic_search_url+="PLAN_NAME:\""+getEncodedString(request.planName)+"\"";					
		}
		if(request.sponsorName!=null){
			elastic_search_url+="SPONSOR_DFE_NAME:\""+getEncodedString(request.sponsorName)+"\"";
		}
		if(request.sponsorState!=null){
			elastic_search_url+="SPONS_DFE_MAIL_US_STATE:\""+getEncodedString(request.sponsorState)+"\"";
		}

		StringBuffer responseBuffer = new StringBuffer();
		JSONObject jsonObj = null;
		try {
			URL urlObj = new URL(elastic_search_url);
			System.out.println("search_url : "+ elastic_search_url);
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

		if(responseBuffer.length() > 0) {
			JSONParser parser = new JSONParser();
			try {
				jsonObj = (JSONObject) parser.parse(responseBuffer.toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	  return new Response(jsonObj);
	}
	
	
	private String getEncodedString(String inputStr) {
		try {
			return URLEncoder.encode(inputStr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}

class Request {
	String planName;
	String sponsorName;
	String sponsorState;
	
	public String getPlanName() {
		return planName;
	}
	public void setPlanName(String planName) {
		this.planName = planName;
	}
	public String getSponsorName() {
		return sponsorName;
	}
	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}
	public String getSponsorState() {
		return sponsorState;
	}
	public void setSponsorState(String sponsorState) {
		this.sponsorState = sponsorState;
	}
	
	public Request(String planName, String sponsorName, String sponsorState){
		this.planName = planName;
		this.sponsorName = sponsorName;
		this.sponsorState = sponsorState;
	}
	
	public Request(){}
	
}

class Response {
	
	JSONObject jsonObj;

	public JSONObject getJsonObj() {
		return jsonObj;
	}

	public void setJsonObj(JSONObject jsonObj) {
		this.jsonObj = jsonObj;
	}
	
   public Response(JSONObject jsonObj) {
	   this.jsonObj = jsonObj;
   }
   
   public Response(){
   }
}
