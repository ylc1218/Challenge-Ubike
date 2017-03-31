package ylc.appier.challenge.ubike;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import ylc.appier.challenge.db.DbHandler;

public class UbikeInfo {
	private static Connection dbConnection = DbHandler.getConnection();
	private static final String INFO_TABLE_NAME = "ubike_info"; //TODO: table name env
	private static final String STATUS_TABLE_NAME = "ubike_status"; //TODO: table name env
		
	private int id;
	private double lat, lng;
	private String sna;
	
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
	
	public UbikeStation createStation(){
		return new UbikeStation(sna, -1);
	}
	
	@Override
	public String toString(){
		return id + " " + sna;
	}
	
	public static ArrayList<UbikeInfo> getActiveStations(){
		ArrayList<UbikeInfo> stations = new ArrayList<>();		
		Statement stmt;
		try {
			stmt = dbConnection.createStatement();			
	        ResultSet rs = stmt.executeQuery("SELECT * FROM " + INFO_TABLE_NAME);
	        System.out.println("select done");
	        while (rs.next()) {
	        	UbikeInfo station = new UbikeInfo();
	        	station.setId(rs.getInt("id"));
	        	station.setLat(rs.getDouble("lat"));
	        	station.setLng(rs.getDouble("lng"));
	        	station.setSna(rs.getString("sna"));
	        	stations.add(station);
	        }
	        rs.close();
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		
		return stations;
	}
	
	public static int getStationSbi(int id){
		int sbi = -1;
		Statement stmt;
		try {
			stmt = dbConnection.createStatement();			
	        ResultSet rs = stmt.executeQuery("SELECT sbi, act FROM "+ STATUS_TABLE_NAME +" where id = " + id);
	        System.out.println("select done");
	        while (rs.next()) {
	        	if (rs.getInt("act") == 0){
	        		sbi = rs.getInt("sbi");
	        	}
	        }
	        rs.close();
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		return sbi;
	}
}
