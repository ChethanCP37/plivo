package com.plivo.weather.util;


import java.util.LinkedHashMap;
import java.util.Map;

import org.testng.Reporter;

import com.plivo.weather.util.WeatherInfo;

public class Util {
	static Map<String, Float> map= new LinkedHashMap<String,Float>();

	public static Map<String, Float> getMaxMinTempErrorValues(WeatherInfo kelvinInfo, WeatherInfo celsiusInfo) {
		float tempInKelvin,errorValue, tempInDegree,maxTemp,minTemp= 0;

		try {
			if(kelvinInfo.temp!= null && celsiusInfo.temp!=null) {
				//Converting Kelvin temperature response from string to float
				tempInKelvin = Float.parseFloat(kelvinInfo.temp);

				//Converting Temperature in degrees from kelvin
				tempInDegree = (float) (tempInKelvin-273.15);

				//getting the ErrorValue of 2%
				errorValue = (float) (tempInDegree*0.02);

				//Converting to max and min values temperature by adding error to temperature in degrees
				maxTemp=tempInDegree+errorValue;
				minTemp=tempInDegree-errorValue;

				//Converting temperature value of Celsius from string to float
				float tempInCelsius = Float.parseFloat(celsiusInfo.temp);

				Reporter.log("Max and Min error temp values are: "+maxTemp+" & "+minTemp,true);

				map.put("maxTemp",maxTemp);
				map.put("minTemp",minTemp);
				map.put("tempInCelsius",tempInCelsius);
			}
		}
		catch(Exception e) {
			Reporter.log("Response from API are not correct please check the response ",true);
		}
		return map;

	}
}
