package com.plivo.weatherdata;

import java.util.HashMap;
import java.util.LinkedHashMap;
import org.testng.Reporter;
import org.testng.log4testng.Logger;

import com.plivo.weather.util.WeatherInfo;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class WeatherApi {
	public RequestSpecification request=null;
	public Response res=null;
	public HashMap<String, String> queryPara=null;
	public HashMap<String, String> headerPara = null;
	public WeatherInfo kelvinInfo=null;
	public WeatherInfo celsiusInfo=null;
	Logger log = Logger.getLogger(WeatherApi.class);
	
	//To get the response in kelvin
	public WeatherInfo getKelvinValueOfTemp(String apiId,String cityId,String baseUri,String endPoint) {
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
		Reporter.log("Response of getKelvinValue method API: "+res.asString(),true);
		String tempInKelvin=res.jsonPath().getString("main.temp");
		String placeId=res.jsonPath().getString("id");
		String cityName=res.jsonPath().getString("name");
		String country=res.jsonPath().getString("sys.country");
		kelvinInfo=new WeatherInfo(tempInKelvin,placeId,cityName,country);
		Reporter.log("created kelvinInfo reference variable to store tempInKelvin,placeId,cityName,country info",true);
		return kelvinInfo;
	}
	//To get the response in degree adding query units=metric
	public WeatherInfo getDegreeValueOfTemp(String apiId,String cityId,String baseUri,String endPoint, String units) {
		RestAssured.baseURI=baseUri;
		request=RestAssured.given();
		
		log.info("adding query parameters apiId, cityId and conversion units=metric under getDegreeValueOfTemp method");
		queryPara = new LinkedHashMap<String, String>();
		queryPara.put("appid",apiId);
		queryPara.put("id",cityId);
		queryPara.put("units",units);
		request.queryParams(queryPara);
		
		headerPara = new LinkedHashMap<String, String>();
		headerPara.put("Content-Type", "application/json");
		headerPara.put("Accept", "application/json");
		request.headers(headerPara);
		
		log.info("To get the response using get api");
		res=request.get(endPoint);
		Reporter.log("Response of getDegreeValueOfTemp method API: "+res.asString(),true);
		String tempInDegree=res.jsonPath().getString("main.temp");
		String placeId=res.jsonPath().getString("id");
		String cityName=res.jsonPath().getString("name");
		String country=res.jsonPath().getString("sys.country");
		celsiusInfo=new WeatherInfo(tempInDegree,placeId,cityName,country);
		Reporter.log("created celsiusInfo reference variable to store tempInDegree,placeId,cityName,country info",true);
		return celsiusInfo;
	}
}
