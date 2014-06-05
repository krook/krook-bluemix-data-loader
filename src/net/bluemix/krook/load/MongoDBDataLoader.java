package net.bluemix.krook.load;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;

public class MongoDBDataLoader {
	
    private static String host = "";
    private static String name = "";
    private static String user = "";
    private static String pass = "";
    private static int port = 0;

	public static boolean load() {
		
		MongoDBDataLoader.init();

		MongoCredential credential = MongoCredential.createMongoCRCredential(user, name, pass.toCharArray());
		
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient(new ServerAddress(host, port), Arrays.asList(credential));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		}
		DB db = mongoClient.getDB(name);

		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream("WEB-INF/lib/cities.json");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("file not exist, exiting");
			return false;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		// Read import line by line
		String strLine;
		DBCollection citiesColl = db.getCollection("cities");
		citiesColl.drop();
		citiesColl = db.getCollection("cities");
		try {
			while ((strLine = br.readLine()) != null) {
				// convert line by line to BSON
				System.out.println(strLine);
				DBObject bson = (DBObject) JSON.parse(strLine);
				// insert BSONs to database
				try {
					citiesColl.insert(bson);
				} catch (MongoException e) {
					// duplicate key
					e.printStackTrace();
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace(); 
			return false;
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
