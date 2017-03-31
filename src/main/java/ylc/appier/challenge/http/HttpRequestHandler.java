package ylc.appier.challenge.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import ylc.appier.challenge.db.DbHandler;
import ylc.appier.challenge.ubike.UbikeInfo;
import ylc.appier.challenge.ubike.UbikeSelector;
import ylc.appier.challenge.ubike.UbikeStation;

public class HttpRequestHandler implements HttpHandler{
	Connection dbConnection;
	public HttpRequestHandler() {
		dbConnection = DbHandler.getConnection();
	}
	
	@Override
    public void handle(HttpExchange t) throws IOException {
		System.out.println(t.getRequestURI().getQuery());
		Map<String, String> params = queryToMap(t.getRequestURI().getQuery());
		
		//TODO: check param(exist, parse)
		double lat = Double.parseDouble(params.get("lat"));
		double lng = Double.parseDouble(params.get("lng"));
		String response = "lat = " + lat + ", lng = " + lng;
		
		UbikeSelector selector = new UbikeSelector();
		ArrayList<UbikeStation> stations = selector.selectNearestStations(lat, lng, 2); // TODO: count
		System.out.println(stations.toString());
		
        
        t.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
	
	private void testDb(){
		Statement stmt;
		try {
			stmt = dbConnection.createStatement();
			stmt.executeUpdate("DROP TABLE IF EXISTS ticks");
	        stmt.executeUpdate("CREATE TABLE ticks (tick timestamp)");
	        stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
	        ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
	        while (rs.next()) {
	            System.out.println("Read from DB: " + rs.getTimestamp("tick"));
	        }
	        rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private Map<String, String> queryToMap(String query){
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            }else{
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
