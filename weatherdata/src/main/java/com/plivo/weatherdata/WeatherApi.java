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
	public WeatherInfo tempInfo=null;
	public WeatherApi weaApi=null;
	Logger log = Logger.getLogger(WeatherApi.class);

	//To get the weather info response
	public Response getWeatherInfo(String baseUri,String endPoint, HashMap<String, String> queryParam) {
		RestAssured.baseURI=baseUri;
		request=RestAssured.given();

		//To assign query parameters
		request.queryParams(queryParam);

		try {
			res=request.get(endPoint);
			log.info("Response from getWeatherInfo method: "+res.asString());
		}
		catch(Exception e) {
			Reporter.log("Exception occurs in getWeatherInfo method response, please check the response",true);
		}
		return res;
	}

	//To get the cityId, cityName, country and status code from response 
	public WeatherInfo getValueOfTemp(String baseUri,String endPoint, HashMap<String, String>  queryparam) {
		try {
			weaApi= new WeatherApi();
			res=weaApi.getWeatherInfo(baseUri,endPoint,queryparam);
			
			String tempInDegree=res.jsonPath().getString("main.temp");
			String placeId=res.jsonPath().getString("id");
			String cityName=res.jsonPath().getString("name");
			String country=res.jsonPath().getString("sys.country");
			int statusCode= res.getStatusCode();
			tempInfo=new WeatherInfo(tempInDegree,placeId,cityName,country,statusCode);
			log.info("created tempInfo reference variable to store tempInDegree, placeId, cityName, country information");
		}
		catch(Exception e) {
			Reporter.log("Exception occurs in getValueOfTemp method, please check the response",true);
		}
		return tempInfo;
	}

	//Returns queryPara which accepts apiId, cityId and units
	public HashMap<String, String> getQueryParam(String apiId, String cityId, String units) {

		queryPara = new LinkedHashMap<String, String>();
		queryPara.put("appid",apiId);
		queryPara.put("id",cityId);
		queryPara.put("units",units);
		return queryPara;
	}
	//Returns queryPara which accepts apiId and cityId only
	public HashMap<String, String> getQueryParamWithoutUnits(String apiId, String cityId) {
		queryPara = new LinkedHashMap<String, String>();
		queryPara.put("appid",apiId);
		queryPara.put("id",cityId);
		return queryPara;
	}
}
