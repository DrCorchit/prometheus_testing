package main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.ImmutableMap;

public class HttpRequest {
	public static final String DEFAULT_ENCODING = "UTF-8";
	
	private final RestMethod method;
	private final URL url;
	private final ImmutableMap<String, String> parameters, properties;
	private final String paramString;
	
	public HttpRequest(String url, RestMethod method, Map<String, String> parameters) 
			throws MalformedURLException, UnsupportedEncodingException {
		this(url, method, parameters, new HashMap<>());
	}
	
	public HttpRequest(String url, RestMethod method, Map<String, String> parameters,
			Map<String, String> properties) throws MalformedURLException, UnsupportedEncodingException {
		this.method = method;
		this.url = new URL(url);
		this.parameters = ImmutableMap.copyOf(parameters);
		this.properties = ImmutableMap.copyOf(properties);
		paramString = mapToParamString(parameters);
	}
	
	public RestMethod getMethod() {
		return method;
	}
	
	public String getUrl() {
		return url.toString();
	}
	
	public ImmutableMap<String, String> getParameters() {
		return parameters;
	}
	
	public ImmutableMap<String, String> getProperties() {
		return properties;
	}
	
	public String makeRequest() throws IOException {
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(method.name());
		for (Entry<String, String> entry : properties.entrySet()) {
			connection.addRequestProperty(entry.getKey(), entry.getValue());
		}
		
		connection.setDoOutput(true);
		DataOutputStream output = new DataOutputStream(connection.getOutputStream());
		output.writeBytes(paramString);
		output.flush();
		output.close();
		
		StringWriter writer = new StringWriter();
		IOUtils.copy(connection.getInputStream(), writer, DEFAULT_ENCODING);
		return writer.toString();
	}
	
	public String toString() {
		return url.toString() + "?" + paramString;
	}
	
	public static String mapToParamString(Map<String, String> map) throws UnsupportedEncodingException {
		return mapToParamString(map, DEFAULT_ENCODING);
	}
	
	public static String mapToParamString(Map<String, String> map, String encoding) throws UnsupportedEncodingException {
		StringBuilder builder = new StringBuilder();
		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		Entry<String, String> current;
		
		if (!iter.hasNext()) return "";
		else {
			current = iter.next();
			builder.append(URLEncoder.encode(current.getKey(), encoding));
			builder.append('=');
			builder.append(URLEncoder.encode(current.getValue(), encoding));
		}
		
		while (iter.hasNext()) {
			current = iter.next();
			builder.append('&');
			builder.append(URLEncoder.encode(current.getKey(), encoding));
			builder.append('=');
			builder.append(URLEncoder.encode(current.getValue(), encoding));
		}
		
		return builder.toString();
	}
}
