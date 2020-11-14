package com.plivo.weatherdata;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.testng.Reporter;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class WeatherApi {
	public RequestSpecification request=null;
	public Response res=null;
	public HashMap<String, String> queryPara=null;
	public HashMap<String, String> headerPara = null;
	
	
	public void getKelvinValue(String apiId,String cityId,String baseUri,String endPoint) {
		RestAssured.baseURI=baseUri;
		request=RestAssured.given();
		
		queryPara = new LinkedHashMap<String, String>();
		queryPara.put("appid",apiId);
		queryPara.put("id",cityId);
		request.queryParams(queryPara);
		
		headerPara = new LinkedHashMap<String, String>();
		headerPara.put("Content-Type", "application/json");
		headerPara.put("Accept", "application/json");
		request.headers(headerPara);
		
		
		res=request.get(endPoint);
		Reporter.log("Response of API: "+res.asString(),true);
		String tempInKelvin=res.jsonPath().getString("main.temp");
		String placeId=res.jsonPath().getString("id");
		String cityName=res.jsonPath().getString("name");
		String country=res.jsonPath().getString("sys.country");
	}

}
