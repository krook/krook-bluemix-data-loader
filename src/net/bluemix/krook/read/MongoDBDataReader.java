package net.bluemix.krook.read;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import net.bluemix.krook.City;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoDBDataReader {
	
    private static String host = "";
    private static String name = "";
    private static String user = "";
    private static String pass = "";
    private static int port = 0;

	public static ArrayList<City> read() {
		
		MongoDBDataReader.init();
		
		ArrayList<City> cities = new ArrayList<City>(); 
		
		MongoCredential credential = MongoCredential.createMongoCRCredential(user, name, pass.toCharArray());
		
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient(new ServerAddress(host, port), Arrays.asList(credential));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
		DB db = mongoClient.getDB(name);
		
		DBCollection citiesColl = db.getCollection("cities");
		
		DBCursor cursor = citiesColl.find();
		try {
			while (cursor.hasNext()) {
				DBObject obj = cursor.next();
				City city = new City(
					(String) obj.get("_id"), 
					(String) obj.get("city"),
					"", 
					(String) obj.get("state"), 
					(Integer) obj.get("pop")
				);
				cities.add(city);
			}
		} finally {
			cursor.close();
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

            JsonNode pgsqlNode = rootNode.path("mongodb-2.2");
            JsonNode credentials = pgsqlNode.get(0).get("credentials");

            name = credentials.get("db").textValue();
            host = credentials.get("host").textValue();
            user = credentials.get("username").textValue();
            pass = credentials.get("password").textValue();
            port = credentials.get("port").intValue();
        }

    }

}
