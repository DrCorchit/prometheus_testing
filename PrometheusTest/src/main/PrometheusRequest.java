package main;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import com.google.common.collect.ImmutableMap;

public class PrometheusRequest extends HttpRequest {
	public static final String BASE_URL = "http://localhost:9090/api/v1/";

	public PrometheusRequest(String query) throws MalformedURLException, UnsupportedEncodingException {
		super(BASE_URL + "query", RestMethod.GET,
				ImmutableMap.of("query", query));
	}

	
	
}
