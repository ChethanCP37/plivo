package com.plivo.weatherdata;

import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.plivo.weather.util.ApiPropertyFileReader;
import com.plivo.weather.util.Util;
import com.plivo.weather.util.WeatherInfo;

import io.restassured.response.Response;


public class WeatherDataComparision {
	public String endPoint,apiId,cityId,baseUri,units=null;
	public WeatherInfo kelvinInfo=null;
	public WeatherInfo celsiusInfo=null;
	public WeatherApi wea=null;
	public Response res=null;

	ApiPropertyFileReader api= new ApiPropertyFileReader();
	Logger log = Logger.getLogger(WeatherApi.class);

	@BeforeSuite()
	public void initialize(){
		api.loadProperty();
		apiId=api.apiProd.getProperty("apiId");
		cityId=api.apiProd.getProperty("cityId");
		baseUri=api.apiProd.getProperty("baseUri");
		endPoint=api.apiProd.getProperty("endPoint");
		units=api.apiProd.getProperty("units");
	}

	@Test(description="To check error temparature values are close to temparature in celcius or not")
	public void compareTemparatureInfo() {
		wea= new WeatherApi();
		kelvinInfo = wea.getKelvinValueOfTemp(apiId, cityId, baseUri, endPoint);
		celsiusInfo = wea.getDegreeValueOfTemp(apiId, cityId, baseUri, endPoint, units);

		Map<String, Float> tempValues=Util.getMaxMinTempErrorValues(kelvinInfo, celsiusInfo);
		float maxTemp = 0,minTemp = 0,tempInCelsius=0;
		if((tempValues.get("maxTemp"))!=null && (tempValues.get("minTemp"))!=null && (tempValues.get("tempInCelsius"))!=null) {
			maxTemp=tempValues.get("maxTemp");
			minTemp=tempValues.get("minTemp");
			tempInCelsius=tempValues.get("tempInCelsius");
		}

		if(maxTemp!=0 && minTemp!=0 && tempInCelsius!=0) {
			Assert.assertTrue(maxTemp>tempInCelsius && minTemp<tempInCelsius, "Error temparature values are not close to temparature in celcius");
			Assert.assertEquals(kelvinInfo.statusCode, 200,"Status Code is incorrect for kelvinInfo");
			Assert.assertEquals(celsiusInfo.statusCode, 200,"Status Code is incorrect for celsiusInfo");
		}
		else {
			Assert.fail("No data returned from API");
		}
	}
	@Test(description="Comparision of cityId, cityName and country name from api's which return kelvin and celcius")
	public void compareWeatherInfo() {
		if(kelvinInfo.cityName!=null && celsiusInfo.cityName!=null) {
			Assert.assertEquals(kelvinInfo.cityName, celsiusInfo.cityName, "City name from both api's which return kelvin and celcius are not correct");
			Assert.assertEquals(kelvinInfo.country,celsiusInfo.country,"country name from both api's which return kelvin and celcius are not correct");
			Assert.assertEquals(kelvinInfo.placeId,celsiusInfo.placeId,"placeId from both api's which return kelvin and celcius are not correct");
		}
		else {
			Assert.fail("Invalid API key or CityId is not correct or check the response");
		}
	}

	@Test(description="When apiId is not correct and is validated for changing unit to metric in api")
	public void invalidApiId() {
		int expectedStatusCode=401;
		String expectedErrorMsg="Invalid API key";
	
		wea= new WeatherApi();
		//Appended with 1 for apiId
		res=wea.getWeatherInfo(apiId+1, cityId, baseUri, endPoint, units);
		Reporter.log("Response for invalid api id: "+res.asString(),true);
		int actualStatusCode=res.getStatusCode();
		String actualErrorMsg= res.jsonPath().getString("message");
	
		Assert.assertEquals(actualStatusCode,expectedStatusCode,"API ID is not correct, please recheck the apiId");
		Assert.assertTrue(actualErrorMsg.contains(expectedErrorMsg), "Response message is not correct for invalidApiId");
	}
	
	@Test(description="When CityId is not correct and is validated for changing unit to metric in api")
	public void invalidCityId() {
		int expectedStatusCode=404;
		String expectedErrorMsg="city not found";
	
		wea= new WeatherApi();
		//Appended with 1 for cityId
		res=wea.getWeatherInfo(apiId, cityId+1, baseUri, endPoint, units);
		Reporter.log("Response for invalid city id: "+res.asString(),true);
		int actualStatusCode=res.getStatusCode();
		String actualErrorMsg= res.jsonPath().getString("message");
	
		Assert.assertEquals(actualStatusCode,expectedStatusCode,"City ID is not correct, please recheck the CityId");
		Assert.assertTrue(actualErrorMsg.contains(expectedErrorMsg), "Response message is not correct for invalidCityId");
	}
}
