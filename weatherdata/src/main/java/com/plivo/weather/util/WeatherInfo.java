package com.plivo.weather.util;

public class WeatherInfo {
	public String temp=null;
	public String placeId=null;
	public String cityName=null;
	public String country=null;
	public int statusCode=0;
	public WeatherInfo(String temp, String placeId, String cityName, String country, int statusCode) {
		this.temp=temp;
		this.placeId=placeId;
		this.cityName=cityName;
		this.country=country;
		this.statusCode=statusCode;
	}
}
