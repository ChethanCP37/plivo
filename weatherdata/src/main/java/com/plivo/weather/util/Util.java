package com.plivo.weather.util;

import org.testng.Reporter;
import com.plivo.weather.util.WeatherInfo;

public class Util {

	public static boolean getMaxMinTempErrorValues(WeatherInfo kelvinInfo, WeatherInfo celsiusInfo,String errorPercentage) {
		float tempInKelvin, errorValue, tempInDegree, maxTemp,minTemp,errorPer= 0;
		boolean flag=false;

		try {
			if(kelvinInfo.temp!= null && celsiusInfo.temp!=null) {
				//Converting Kelvin temperature response from string to float
				tempInKelvin = Float.parseFloat(kelvinInfo.temp);
				
				//Converting errorPercentage from string to float
				errorPer = Float.parseFloat(errorPercentage);

				//Converting Temperature in degrees from kelvin
				tempInDegree = (float) (tempInKelvin-273.15);

				//Getting the ErrorValue of 2%
				errorValue = (float) (tempInDegree*(errorPer/100));

				//To get max and min values temperature by adding error to temperature in degrees
				maxTemp=tempInDegree+errorValue;
				minTemp=tempInDegree-errorValue;

				//Converting temperature value of Celsius from string to float
				float tempInCelsius = Float.parseFloat(celsiusInfo.temp);

				Reporter.log("Max and Min error temp values are: "+maxTemp+" & "+minTemp,true);

				//Compare temperature in degree with min and max value of temperature
				if(maxTemp>tempInCelsius && minTemp<tempInCelsius) {
					flag=true;
				}
			}
		}
		catch(Exception e) {
			Reporter.log("Response from API is incorrect, please check the response ",true);
		}
		return flag;
	}
}
