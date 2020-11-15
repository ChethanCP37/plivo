package com.plivo.weather.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileReader {
	public FileInputStream fis=null;
	public Properties apiProd=null;
	
	public void loadProperty() {
		try {
			apiProd= new Properties();
			fis = new FileInputStream("src/main/resources/api.properties");
			apiProd.load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
