package api.autotest.framework.keywords;

import java.util.Collections;
import java.util.Map;

import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;

import api.autotest.framework.rest.RestClientUtils;

@RobotKeywords
public class RobotRestLibrary {

	@RobotKeyword
	public Object invokeService(String url) {
		return invokeService("GET", Collections.<String, String>emptyMap(), url);
	}
	
	@RobotKeywordOverload
	public Object invokeService(String action, String url) {
		return invokeService(action, Collections.<String, String>emptyMap(), url);
	}
	@RobotKeywordOverload
	public Object invokeService(String action, Map<String, String> requestHeaders, String url) {
		return new RestClientUtils().invoke(action, requestHeaders, url, null);
	}
	
	@RobotKeywordOverload
	public Object invokeService(String action, Map<String, String> requestHeaders, String url, String requestBody) {
		return new RestClientUtils().invoke(action, requestHeaders, url, requestBody);
	}
}
