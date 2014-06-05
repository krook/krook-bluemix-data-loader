package net.bluemix.krook.read;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import net.bluemix.krook.City;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PostgreSQLDataReader {
	
    private static String host = "";
    private static String name = "";
    private static String user = "";
    private static String pass = "";
    private static int port = 0;
 

	public static ArrayList<City> read() {
		
		PostgreSQLDataReader.init();
		
		ArrayList<City> cities = new ArrayList<City>(); 
		
		try {
			Class.forName("org.postgresql.Driver");
		} catch (java.lang.ClassNotFoundException e) {
			System.out.println(e.getMessage());
			return null;
		}
		
		String url = "jdbc:postgresql://" + host + ":" + port + "/" + name;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
				
		try {
			
			conn = DriverManager.getConnection(url, user, pass);
            
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM city");
			while (rs.next()) {
				City city = new City(
					rs.getString("id"), 
					rs.getString("name"),
					rs.getString("countrycode"), 
					rs.getString("district"), 
					rs.getInt("population")
				);
				cities.add(city);
			}
 
		} catch (java.sql.SQLException e) {
			System.out.println(e.getMessage());
			return null;
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return cities;
		
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
