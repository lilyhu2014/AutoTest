package api.autotest.framework.rest;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RestClientUtils {
	public enum Action {
		GET, POST, PUT, DELETE;
	}

	public final static String REPLACE_WITH = " REPLACE WITH ";

	public RestServiceResponse invoke(String action, Map<String, String> requestHeaders, String url,
			String requestBody) {
		RestClient restClient = new RestClient();
		Optional<Response> res;
		switch (Action.valueOf(action)) {
		case GET:
			res = restClient.get(url, requestHeaders);
			break;
		case POST:
			res = restClient.post(url, requestHeaders, requestBody);
			break;
		case PUT:
			res = restClient.put(url, requestHeaders, requestBody);
			break;
		case DELETE:
			res = restClient.delete(url, requestHeaders);
			break;
		default:
			res = Optional.empty();
		}
		if(res.isPresent()) {
			RestServiceResponse response = getRestServiceResponseObject(res.get());
			return response;
		}else {
			return null;
		}
	}

	private RestServiceResponse getRestServiceResponseObject(Response clientResponse) {
		RestServiceResponse response = new RestServiceResponse();
		response.setStatusCode(clientResponse.getStatusInfo().getStatusCode());
		response.setStatusMessage(clientResponse.getStatusInfo().getReasonPhrase());
		response.setHeaders(getResponseHeaders(clientResponse));
		String responseBody = clientResponse.readEntity(String.class);
		if (responseBody.startsWith("<html><body>")) {
			int index = responseBody.indexOf("</div><div>");
			String message = (responseBody.substring(responseBody.indexOf("</div><div>", index + 1),
					responseBody.indexOf("</div></body>"))).replace("</div><div>", "");
			message = StringEscapeUtils.unescapeHtml4(message);
			responseBody = "{\"message\": \"" + message + "\"}";
		}
		
		System.out.println(responseBody);
		Optional<String> newResponseBody = Optional.ofNullable(responseBody).map(a -> a.replace("\\r", "\\\\r"))
				.map(a -> a.replace("\\n", "\\\\n"));

		try {
			if (newResponseBody.isPresent()) {
				responseBody = newResponseBody.get();
				Object responseObject = new JSONParser().parse(responseBody);
				if (responseObject instanceof JSONObject) {
					JSONObject json = (JSONObject) responseObject;
					response.setResponse(json);
				} else if (responseObject instanceof JSONArray) {
					JSONArray json = (JSONArray) responseObject;
					response.setResponse(json);
				} else {
					response.setResponse(responseBody);
				}
			} else {
				response.setResponse(responseBody);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			response.setResponse(responseBody);
			e.printStackTrace();
		}
		return response;
	}

	public Map<String, String> getResponseHeaders(Response clientResponse) {
		Map<String, String> headers = new LinkedHashMap<>();
		MultivaluedMap<String, Object> responseHeaders = clientResponse.getHeaders();
		Set<Entry<String, List<Object>>> entrySet = responseHeaders.entrySet();
		Iterator<Entry<String, List<Object>>> itr = entrySet.iterator();
		while (itr.hasNext()) {
			Entry<String, List<Object>> entry = itr.next();
			headers.put(entry.getKey(), StringUtils.join(entry.getValue(), ";"));
		}
		return headers;
	}
}
