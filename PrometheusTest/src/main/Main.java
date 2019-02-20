package main;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import io.prometheus.client.exporter.HTTPServer;

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
		System.out.println("Simple Test");
		simpleTest();
		
		System.out.println("Up Test");
		queryTest("up");
		
		System.out.println("Scraping Test");
		scrapingTest();
		
	}
	
	private static void simpleTest() {
		try {
			Gauge g = Gauge.build().name("gauge").help("blah").register();
		    Counter c = Counter.build().name("counter").help("meh").register();
		    Summary s = Summary.build().name("summary").help("meh").register();
		    Histogram h = Histogram.build().name("histogram").help("meh").register();
		    Gauge l = Gauge.build().name("labels").help("blah").labelNames("l").register();
			
			HTTPServer server = new HTTPServer(8080);
	        g.set(1);
	        c.inc(2);
	        s.observe(3);
	        h.observe(4);
	        l.labels("foo").inc(5);
	        
	        server.stop();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void queryTest(String query) {
		
		try {
		PrometheusRequest req = new PrometheusRequest(query);
		System.out.println(req);
		
		String rawResult = req.makeRequest();
		System.out.println(rawResult);
		
		JsonParser parser = new JsonParser();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonObject result = parser.parse(rawResult).getAsJsonObject();
		System.out.println(gson.toJson(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void scrapingTest() {
		
		try {
			
			HTTPServer server = new HTTPServer(8080);
			
			ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
			service.scheduleAtFixedRate(new BasicExample(), 1, 5, TimeUnit.SECONDS);
			
			System.out.println("Generating data.");
			Thread.sleep(60000);
			service.shutdown();
			
			System.out.println("Collecting data.");
			System.out.println(BasicExample.COUNTER.collect());
			System.out.println(BasicExample.GAUGE.collect());
			
			server.stop();
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
