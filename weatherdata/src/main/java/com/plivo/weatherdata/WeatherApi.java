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
	public WeatherApi weaApi=null;
	Logger log = Logger.getLogger(WeatherApi.class);

	/* To get the weather info response
	 * This is the common method to get the response for both Kelvin api and Celcius api
	 * Kelvin API provides response of temperature in Kelvin
	 * Celcius API provides response of temperature in Degrees where we need to pass units=metric
	 */
	public Response getWeatherInfo(String apiId,String cityId,String baseUri,String endPoint, String units) {
		RestAssured.baseURI=baseUri;
		request=RestAssured.given();

		queryPara = new LinkedHashMap<String, String>();
		queryPara.put("appid",apiId);
		queryPara.put("id",cityId);
		queryPara.put("units",units);
		request.queryParams(queryPara);
		
		//Without headers also we can able to get response
		headerPara = new LinkedHashMap<String, String>();
		headerPara.put("Content-Type", "application/json");
		headerPara.put("Accept", "application/json");
		request.headers(headerPara);

		try {
			res=request.get(endPoint);
			log.info("Response from getWeatherInfo method: "+res.asString());
		}
		catch(Exception e) {
			Reporter.log("Exception occurs in getWeatherInfo method response, please check the response",true);
		}
		return res;
	}

	//To validate the Kelvin response
	public WeatherInfo getKelvinValueOfTemp(String apiId,String cityId,String baseUri,String endPoint) {
		try {
			weaApi= new WeatherApi();
			res=weaApi.getWeatherInfo(apiId,cityId,baseUri,endPoint, null);
			Reporter.log("Response of getKelvinValue method API: "+res.asString(),true);
			String tempInKelvin=res.jsonPath().getString("main.temp");
			String placeId=res.jsonPath().getString("id");
			String cityName=res.jsonPath().getString("name");
			String country=res.jsonPath().getString("sys.country");
			int statusCode= res.getStatusCode();
			kelvinInfo=new WeatherInfo(tempInKelvin,placeId,cityName,country,statusCode);
			log.info("created kelvinInfo reference variable to store tempInKelvin,placeId,cityName,country info");
		}
		catch(Exception e) {
			Reporter.log("Exception occurs in getKelvinValueOfTemp method, please check the response",true);
		}
		return kelvinInfo;
	}

	//To validate the degree response from API, adding query units=metric
	public WeatherInfo getDegreeValueOfTemp(String apiId,String cityId,String baseUri,String endPoint, String units) {
		try {
			weaApi= new WeatherApi();
			res=weaApi.getWeatherInfo(apiId,cityId,baseUri,endPoint, units);
			Reporter.log("Response of getDegreeValueOfTemp method API: "+res.asString(),true);
			String tempInDegree=res.jsonPath().getString("main.temp");
			String placeId=res.jsonPath().getString("id");
			String cityName=res.jsonPath().getString("name");
			String country=res.jsonPath().getString("sys.country");
			int statusCode= res.getStatusCode();
			celsiusInfo=new WeatherInfo(tempInDegree,placeId,cityName,country,statusCode);
			log.info("created celsiusInfo reference variable to store tempInDegree,placeId,cityName,country info");
		}
		catch(Exception e) {
			Reporter.log("Exception occurs in getDegreeValueOfTemp method, please check the response",true);
		}
		return celsiusInfo;
	}

}
