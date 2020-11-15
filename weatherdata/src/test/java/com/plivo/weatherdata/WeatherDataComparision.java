package com.plivo.weatherdata;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import com.plivo.weather.util.PropertyFileReader;
import com.plivo.weather.util.Util;
import com.plivo.weather.util.WeatherInfo;

import io.restassured.response.Response;


public class WeatherDataComparision {
	public String endPoint,apiId,cityId,baseUri,units,errorPercentage=null;
	public WeatherInfo kelvinInfo=null;
	public WeatherInfo celsiusInfo=null;
	public WeatherApi wea=null;
	public Response res=null;

	PropertyFileReader api= new PropertyFileReader();
	Logger log = Logger.getLogger(WeatherApi.class);

	@BeforeSuite()
	public void initialize(){
		//Load the property values
		api.loadProperty();
		apiId=api.apiProd.getProperty("apiId");
		cityId=api.apiProd.getProperty("cityId");
		baseUri=api.apiProd.getProperty("baseUri");
		endPoint=api.apiProd.getProperty("endPoint");
		units=api.apiProd.getProperty("units");
		errorPercentage=api.apiProd.getProperty("errorPercentage");
	}

	@Test(description="To check metric unit api temperature is lies between error temperature values of standard api")
	public void compareTemparatureInfo() {
		wea= new WeatherApi();
		//To get kelvin information as an object from standard api
		kelvinInfo = wea.getValueOfTemp(baseUri, endPoint,wea.getQueryParamWithoutUnits(apiId,cityId));

		//To get celcius information as an object from metric unit api
		celsiusInfo = wea.getValueOfTemp(baseUri, endPoint,wea.getQueryParam(apiId,cityId,units));

		//Returns true if metric unit api temperature is lies between error temperature values of standard api.
		boolean tempValues=Util.getMaxMinTempErrorValues(kelvinInfo, celsiusInfo,errorPercentage);
		
		Assert.assertEquals(kelvinInfo.statusCode, 200,"Status Code is incorrect for kelvinInfo");
		Assert.assertEquals(celsiusInfo.statusCode, 200,"Status Code is incorrect for celsiusInfo");
		Assert.assertTrue(tempValues, "Error temparature values are not close to temparature in celcius");
		Assert.assertEquals(kelvinInfo.cityName, celsiusInfo.cityName, "City name from both api's which return temperature in kelvin and degrees are not correct");
		Assert.assertEquals(kelvinInfo.country,celsiusInfo.country,"country name from both api's which return temperature in kelvin and degrees are not correct");
		Assert.assertEquals(kelvinInfo.placeId,celsiusInfo.placeId,"placeId from both api's which return temperature in kelvin and degrees are not correct");
	}

	@Test(description="When apiId is not correct and is validated for metric unit api")
	public void invalidApiId() {
		int expectedStatusCode=401;
		String expectedErrorMsg="Invalid API key";

		wea= new WeatherApi();
		//Appended apiId by 1 to make invalid apiId
		res=wea.getWeatherInfo(baseUri, endPoint,wea.getQueryParamWithoutUnits(apiId+1,cityId));
		Reporter.log("Response for invalid api id: "+res.asString(),true);
		int actualStatusCode=res.getStatusCode();
		String actualErrorMsg= res.jsonPath().getString("message");

		Assert.assertEquals(actualStatusCode,expectedStatusCode,"Status code is not correct for invalidApiId");
		Assert.assertTrue(actualErrorMsg.contains(expectedErrorMsg), "Response message is not correct for invalidApiId");
	}

	@Test(description="When CityId is passed with invalid numeric id and is validated for metric unit api")
	public void invalidNumericToCityId() {
		int expectedStatusCode=404;
		String expectedErrorMsg="city not found";

		wea= new WeatherApi();
		//Appended cityId by 1 to make invalid cityId
		res=wea.getWeatherInfo(baseUri, endPoint,wea.getQueryParamWithoutUnits(apiId,cityId+1));
		Reporter.log("Response for invalid numeric to city id: "+res.asString(),true);
		int actualStatusCode=res.getStatusCode();
		String actualErrorMsg= res.jsonPath().getString("message");

		Assert.assertEquals(actualStatusCode,expectedStatusCode,"Status code is not correct for invalidApiId");
		Assert.assertTrue(actualErrorMsg.contains(expectedErrorMsg), "Response message is not correct for invalidCityId");
	}

	@Test(description="When invalid string is passed to cityId field and is validated for metric unit api")
	public void invalidStringToCityId() {
		int expectedStatusCode=400;
		String expectedErrorMsg="not a city ID";

		wea= new WeatherApi();
		//Appended cityId by string "abc"
		res=wea.getWeatherInfo(baseUri, endPoint,wea.getQueryParamWithoutUnits(apiId,cityId+"abc"));
		Reporter.log("Response for invalid string to CityId: "+res.asString(),true);
		int actualStatusCode=res.getStatusCode();
		String actualErrorMsg= res.jsonPath().getString("message");

		Assert.assertEquals(actualStatusCode,expectedStatusCode,"Status code is not correct for invalidApiId");
		Assert.assertTrue(actualErrorMsg.contains(expectedErrorMsg), "Response message is not correct for invalidStringToCityId");
	}

}
