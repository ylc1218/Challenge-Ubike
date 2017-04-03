package ylc.appier.challenge.db;

import java.beans.PropertyVetoException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DbHandler {
	private static ComboPooledDataSource cpds = null;
	
	public static void init(){
		cpds = new ComboPooledDataSource();
		try {
			// get db path
			URI dbUri;
			dbUri = new URI(System.getenv("DATABASE_URL"));
	        String username = dbUri.getUserInfo().split(":")[0];
	        String password = dbUri.getUserInfo().split(":")[1];
	        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath() + "?sslmode=require";
	        
			//loads the jdbc driver
			cpds.setDriverClass("org.postgresql.Driver");				        
			cpds.setJdbcUrl(dbUrl);
			cpds.setUser(username);
			cpds.setPassword(password);

			// settings
			cpds.setMinPoolSize(3);
			cpds.setAcquireIncrement(5);
			cpds.setMaxPoolSize(10);
						
		} catch (PropertyVetoException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() throws SQLException{
		return cpds.getConnection();		
    }	
	
	@Override
	protected void finalize(){
		if (cpds != null){
			cpds.close();
		}
	}
}
