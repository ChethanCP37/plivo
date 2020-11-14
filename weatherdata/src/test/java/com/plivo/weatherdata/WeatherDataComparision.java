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
	
	@Test()
	public void compareWeatherInfo() {
		WeatherApi wea= new WeatherApi();
		WeatherInfo kelvinInfo = wea.getKelvinValueOfTemp(apiId, cityId, baseUri, endPoint);
		WeatherInfo celsiusInfo = wea.getDegreeValueOfTemp(apiId, cityId, baseUri, endPoint, units);
		Map<String, Float> tempValues=Util.getMaxMinTempErrorValues(kelvinInfo, celsiusInfo);
		float maxTemp=tempValues.get("maxTemp");
		float minTemp=tempValues.get("minTemp");
		float tempInCelsius=tempValues.get("tempInCelsius");
		if(maxTemp!=0 && minTemp!=0 && tempInCelsius!=0) {
			SoftAssert asser= new SoftAssert();
			asser.assertTrue(maxTemp>tempInCelsius && minTemp<tempInCelsius, "Error temparature values are not close to temparature in celcius");
			asser.assertAll();
		}
		else {
			Assert.fail("No data returned from API");
		}
		
	}
}
