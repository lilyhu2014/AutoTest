package api.autotest.framework.rest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;

public class RestClient {
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String CT_JSON = "application/json";
	private static final String CT_URLENCODED = "application/x-www-form-urlencoded";

	public Optional<Response> get(String url, Map<String, String> headers) {
		try {
			url = URIUtil.encodeQuery(url);
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(url);
			Builder builder = target.request();
			this.parseHeader(builder, headers);
			return Optional.ofNullable(builder.get());
		} catch (URIException e) {
			// throw new RuntimeException(e.getMessage());
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public Optional<Response> post(String url, Map<String, String> headers, String postBody) {
		try {
			url = URIUtil.encodeQuery(url);
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(url);
			Builder builder = target.request();
			this.parseHeader(builder, headers);
			Entity<?> payload = null;
			if (MapUtils.isNotEmpty(headers) && headers.containsKey(CONTENT_TYPE)
					&& headers.get(CONTENT_TYPE).equalsIgnoreCase(CT_URLENCODED)) {
				String[] keyValueArray = postBody.split("&");
				List<String[]> argsArray = Arrays.stream(keyValueArray).map(a -> a.split("="))
						.collect(Collectors.toList());
				Map<String, String> map = new HashMap<>();
				for (String[] arg : argsArray) {
					map.put(arg[0], arg[1]);
				}
				MultivaluedMap<String, String> formData = new MultivaluedHashMap<>(map);
				payload = Entity.form(formData);
			} else if (MapUtils.isNotEmpty(headers) && headers.containsKey(CONTENT_TYPE)
					&& headers.get(CONTENT_TYPE).equalsIgnoreCase(CT_JSON)) {
				payload = Entity.json(postBody);
			} else {
				payload = Entity.text(postBody);
			}
			return Optional.ofNullable(builder.post(payload));
		} catch (URIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	public Optional<Response> put(String url, Map<String, String> headers, String putBody) {
		try {
			url = URIUtil.encodeQuery(url);
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(url);
			Builder builder = target.request();
			this.parseHeader(builder, headers);
			Entity<?> payload = null;
			if(MapUtils.isNotEmpty(headers) && headers.containsKey(CONTENT_TYPE) && headers.get(CONTENT_TYPE).equalsIgnoreCase(CT_JSON)) {
				payload = Entity.json(putBody);
			}else {
				payload = Entity.text(putBody);
			}
			return Optional.ofNullable(builder.put(payload));
		} catch (URIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	public Optional<Response> delete(String url, Map<String, String> headers) {
		try {
			url = URIUtil.encodeQuery(url);
			Client client = ClientBuilder.newClient();
			WebTarget target = client.target(url);
			Builder builder = target.request();
			this.parseHeader(builder, headers);
			return Optional.ofNullable(builder.delete());
		} catch (URIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Optional.empty();
	}

	private void parseHeader(Builder builder, Map<String, String> headers) {
		if (MapUtils.isNotEmpty(headers)) {// Optional.ofNullable(headers).isPresent()
			Set<Entry<String, String>> entrySet = headers.entrySet();
			Iterator<Entry<String, String>> itr = entrySet.iterator();
			while (itr.hasNext()) {
				Entry<String, String> entry = itr.next();
				builder = builder.header(entry.getKey(), entry.getValue());
			}
		}
	}
}
