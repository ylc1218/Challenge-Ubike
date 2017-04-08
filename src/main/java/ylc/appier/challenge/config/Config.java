package ylc.appier.challenge.config;

import java.io.IOException;
import java.util.Properties;

public class Config {
	public static final String INFO_TBL_NAME = "info.table.name";
	public static final String STATUS_TBL_NAME = "status.table.name";
	public static final String HTTP_THR_NUM = "http.thread.num";
	public static final String UPDATE_INFO_MIN = "update.info.min";
	public static final String RETURN_STATION_NUM = "return.station.num";
	
	private static Properties prop = new Properties();
	
	static{
		try {
			prop.load(Config.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getString(String name){
		return prop.getProperty(name);
	}
	
	public static int getInt(String name){
		return Integer.parseInt(prop.getProperty(name));
	}
}
