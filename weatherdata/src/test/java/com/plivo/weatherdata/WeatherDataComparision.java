package com.plivo.weatherdata;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import com.plivo.weather.util.ApiPropertyFileReader;


public class WeatherDataComparision {
	public String endPoint,apiId,cityId,baseUri,units=null;

	ApiPropertyFileReader api= new ApiPropertyFileReader();
	
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
		wea.getKelvinValue(apiId,cityId,baseUri,endPoint);
		
		
	}
	

}
