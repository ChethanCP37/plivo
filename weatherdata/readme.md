#Project Info:
Created a framework to compare the weather information from standard api (kelvin response) with converted units (metric) api. Standard api response provides the temperature information in kelvin and this data is converted into degree with 2% error and then compared these two min and max values of temperature with degree temperature of converted units (metric) api.

> Standard API (Kelvin Response): https://api.openweathermap.org/data/2.5/weather?{id=cityId}&{appid=apiIdFromPortal}

> Converted Metric Units API (Celcius Response): https://api.openweathermap.org/data/2.5/weather?{id=cityId}&{units=metric}&{appid=apiIdFromPortal}

#Tools and Technology Used:
Java, TestNG, Maven, RestAssured, Log4j

#Framework Used:
Customised Rest Assured Framework

#Setup:
1. You need to download the zip file, unzip it and open in eclipse.
2. Navigate to project *weatherdata* > right click on it > select *Maven* > select *Update Project* so that all maven dependencies will be downloaded. 
3. To run the project navigate to *testng.xml* > right click on xml file > select *Run As* > select *TestNg Suite* (OR) Navigate to *WeatherDataComp* testNG class present under *src/test/java/com/plivo/weatherdata/WeatherDataComparision.java*  > Right click > select Run As > select TestNg Test
4. cityId, apiId, units, errorPercentage, baseUri and endPoint are present in *api.properties* file which is present under *src/main/resources/api.properties* path.
Ex: Change the cityId which are commented in *api.properties* file.
5. To check the execution logs, open *application.log* present under weatherdata project

#Note: 
1. Input parameters to methods like cityId, apiId and errorPercentage are passed from *api.properties*.
2. There are 4 test methods (compareTemparatureInfo, invalidApiId, invalidNumericToCityId and invalidStringToCityId)
3. compareTemparatureInfo test method is to compare 2% error of max and min temperature of standard api with degree temperature of converted units (metric) api.
4. invalidApiId, invalidNumericToCityId and invalidStringToCityId are used to validate negative scenarios.
5. Error comparison logic is available in Util.java file present under *src/main/java/com/plivo/weather/util/Util.java*
6. WeatherInfo.java file present under *src/main/java/com/plivo/weather/util/WeatherInfo.java* is to store the response parameters (temperature values, cityName, cityCode etc..) in the form of object.




