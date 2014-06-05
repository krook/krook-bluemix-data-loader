package net.bluemix.krook.load;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PostgreSQLDataLoader {
	
    private static String host = "";
    private static String name = "";
    private static String user = "";
    private static String pass = "";
    private static int port = 0;

	public static boolean load() {
		
		PostgreSQLDataLoader.init();

		try {
			Class.forName("org.postgresql.Driver");
		} catch (java.lang.ClassNotFoundException e) {
			System.out.println(e.getMessage());
			return false;
		}
		
		String url = "jdbc:postgresql://" + host + ":" + port + "/" + name;
		Connection conn = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
				
		try {
			
			conn = DriverManager.getConnection(url, user, pass);
			
			// Drop table
			ps1 = conn.prepareStatement("DROP TABLE IF EXISTS city");
			ps1.executeUpdate();	
			
			// Create table
			ps2 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS city (id integer NOT NULL, name text NOT NULL, countrycode character(3) NOT NULL, district text NOT NULL, population integer NOT NULL)");
			ps2.executeUpdate();		
			
			// Import data
			CopyManager copyManager = new CopyManager((BaseConnection) conn);
            FileReader fileReader = new FileReader("WEB-INF/lib/cities.txt");
            copyManager.copyIn("COPY city (id, name, countrycode, district, population) FROM stdin", fileReader);
            System.err.println("Done.");
 
		}

		catch (java.sql.SQLException | IOException e) {
			System.out.println(e.getMessage());
			return false;
		} finally {
			try {
				ps1.close();
				ps2.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return true;

	}
	
    private static void init() {
    	String vcapServices = System.getenv("VCAP_SERVICES");
        
        if (vcapServices != null && vcapServices.length() > 0) {
        	
        	ObjectMapper m = new ObjectMapper();
        	JsonNode rootNode = null;
			try {
				rootNode = m.readTree(vcapServices);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

            JsonNode pgsqlNode = rootNode.path("postgresql-9.1");
            JsonNode credentials = pgsqlNode.get(0).get("credentials");

            name = credentials.get("name").textValue();
            host = credentials.get("host").textValue();
            user = credentials.get("user").textValue();
            pass = credentials.get("password").textValue();
            port = credentials.get("port").intValue();
        }

    }

}
