package ylc.appier.challenge.ubike;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ylc.appier.challenge.config.Config;
import ylc.appier.challenge.db.DbHandler;

public class UbikeDbHelper {
	private Connection dbConnection = null;
	private static final String INFO_TABLE_NAME = Config.getString(Config.INFO_TBL_NAME);
	private static final String STATUS_TABLE_NAME = Config.getString(Config.STATUS_TBL_NAME);
	
	public UbikeDbHelper() throws SQLException{
		dbConnection = DbHandler.getConnection();
	}
	
	public List<UbikeInfo> getStations() throws SQLException {
		ArrayList<UbikeInfo> stations = new ArrayList<>();		
		Statement stmt = dbConnection.createStatement();			
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + INFO_TABLE_NAME);
        while (rs.next()) {
        	UbikeInfo station = new UbikeInfo();
        	station.setId(rs.getInt("id"));
        	station.setLat(rs.getDouble("lat"));
        	station.setLng(rs.getDouble("lng"));
        	station.setSna(rs.getString("sna"));
        	stations.add(station);
        }
        rs.close();
		
		return stations;
	}
	
	public int getStationSbi(int id){
		int sbi = -1;
		Statement stmt;
		try {
			stmt = dbConnection.createStatement();			
	        ResultSet rs = stmt.executeQuery("SELECT sbi, act FROM "+ STATUS_TABLE_NAME +" where id = " + id);	        
	        while (rs.next()) {
	        	if (rs.getInt("act") == 1){
	        		sbi = rs.getInt("sbi");
	        	}
	        }
	        rs.close();
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		return sbi;
	}
	
	public void close(){
		finalize();
	}
	
	@Override
	protected void finalize(){
		if (dbConnection != null){
			try {
				dbConnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}

