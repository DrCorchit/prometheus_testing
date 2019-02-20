package main;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main {
	
	public static void main(String...args) {
		
		try {
			PrometheusRequest req = new PrometheusRequest("up");
			System.out.println(req);
			
			String rawResult = req.makeRequest();
			System.out.println(rawResult);
			
			JsonParser parser = new JsonParser();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonObject result = parser.parse(rawResult).getAsJsonObject();
			System.out.println(gson.toJson(result));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
