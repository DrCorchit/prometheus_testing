package main;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
			
			ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
			service.scheduleAtFixedRate(new BasicExample(), 1, 5, TimeUnit.SECONDS);
			
			System.out.println("Generating data.");
			Thread.sleep(30000);
			service.shutdown();
			
			System.out.println("Collecting data.");
			System.out.println(BasicExample.COUNTER.collect());
			System.out.println(BasicExample.GAUGE.collect());
			
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
