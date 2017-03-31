package ylc.appier.challenge.db;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class DbHandler {
	private static Connection connection = null;
	
	public static Connection getConnection(){
		try {
			if (connection != null && connection.isValid(5)){
				return connection;
			}
			
			URI dbUri;
			dbUri = new URI(System.getenv("DATABASE_URL"));
			System.out.println("dbUri = " + dbUri.getPath());
	        String username = dbUri.getUserInfo().split(":")[0];
	        String password = dbUri.getUserInfo().split(":")[1];
	        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath() + "?sslmode=require";
	        connection = DriverManager.getConnection(dbUrl, username, password);
	        
		} catch (URISyntaxException | SQLException e) {			
			e.printStackTrace();
		}		        
        return connection; 	
    }		
	
}
