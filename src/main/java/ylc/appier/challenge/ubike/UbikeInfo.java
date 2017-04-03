package ylc.appier.challenge.ubike;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UbikeInfo {
	private static List<UbikeInfo> cachedInfos = null;
	
	private int id;
	private double lat, lng;
	private String sna;
		
	public static void init() throws SQLException{
		updateCachedInfos();
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		
		scheduler.scheduleAtFixedRate(new Runnable() { //update info cache every hour
			@Override
			public void run() {
				try {
					updateCachedInfos();
					System.out.println("Info Updated");
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			}
		}, 60, 60, TimeUnit.MINUTES);
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setLat(double lat){
		this.lat = lat;
	}
	
	public void setLng(double lng){
		this.lng = lng;
	}
	
	public void setSna(String sna){
		this.sna = sna;
	}
	
	public double dist2(double lat, double lng){
		return (this.lat - lat) * (this.lat - lat) + (this.lng - lng) * (this.lng - lng);
	}
	
	public int getId(){
		return id;
	}
	
	public String getSna(){
		return sna;
	}
	
	public static List<UbikeInfo> getCachedInfos(){
		return cachedInfos;
	}
	
	@Override
	public String toString(){
		return id + " " + sna;
	}
	
	private static void updateCachedInfos() throws SQLException{
		UbikeDbHelper dbHelper = new UbikeDbHelper();
		List<UbikeInfo> newInfos = dbHelper.getStations();
		if (newInfos != null){
			cachedInfos = newInfos;
		}	
	}
}
