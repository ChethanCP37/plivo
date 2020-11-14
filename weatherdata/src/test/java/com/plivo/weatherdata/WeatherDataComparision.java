package com.plivo.weatherdata;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.testng.log4testng.Logger;

import com.plivo.weather.util.ApiPropertyFileReader;
import com.plivo.weather.util.Util;
import com.plivo.weather.util.WeatherInfo;


public class WeatherDataComparision {
	public String endPoint,apiId,cityId,baseUri,units=null;
	public WeatherInfo kelvinInfo=null;
	public WeatherInfo celsiusInfo=null;
	public WeatherApi wea=null;

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

	@Test(description="When apiId is not correct")
	public void invalidApiId() {


	}

}
