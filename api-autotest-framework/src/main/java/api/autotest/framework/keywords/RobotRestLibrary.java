package api.autotest.framework.keywords;

import static api.autotest.framework.rest.RestClientUtils.REPLACE_WITH;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import api.autotest.framework.rest.RestClientUtils;
import api.autotest.framework.util.CommonUtil;

@RobotKeywords
public class RobotRestLibrary {
	private final static Logger LOGGER = Logger.getLogger(RobotRestLibrary.class);
	private static ApplicationContext context;
	private static Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonNodeJsonProvider())
			.mappingProvider(new JacksonMappingProvider()).build();
	public static Map<String, List<Map<String, String>>> tableMap = new HashMap<String, List<Map<String, String>>>();

	private RestClientUtils restClientUtils;

	public RobotRestLibrary() {
		context = new ClassPathXmlApplicationContext("spring/bean-mappings.xml");
		restClientUtils = (RestClientUtils) context.getBean("restClientUtils");
		LOGGER.info("Init RobotRestLibrary class");
	}

	@Autowired
	public void setRestClientUtils(RestClientUtils restClientUtils) {
		this.restClientUtils = restClientUtils;
	}

	@RobotKeyword
	public Object encodeBase64(String value) {
		return new String(Base64.encodeBase64(value.getBytes()));
	}
	
	@RobotKeyword
	public void saveToCache(Object value) {
		restClientUtils.saveToCache(value);
	}
	
	@RobotKeywordOverload
	public void saveToCache(String key, Object value) {
		restClientUtils.saveToCache(key, value, 1000 * 60 * 30);
	}
	
	@RobotKeywordOverload
	public void saveToCache(String key, Object value, long timeToLive) {
		restClientUtils.saveToCache(key, value, timeToLive);
	}
	
	@RobotKeywordOverload
	public void saveToCache(String key, Object value, long timeToLive, long cacheAllowance) {
		restClientUtils.saveToCache(key, value, timeToLive, cacheAllowance);
	}
	
	@RobotKeyword
	public Object getFromCache(String key) {
		return restClientUtils.getFromCache(key);
	}
	
	@RobotKeywordOverload
	public Object getFromCache() {
		return restClientUtils.getFromCache();
	}
	
	@RobotKeyword
	public boolean removeFromCache(String key) {
		return restClientUtils.removeFromCache(key);
	}
	
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
		return invokeService(action, requestHeaders, url, null);
	}

	@RobotKeywordOverload
	public Object invokeService(String action, Map<String, String> requestHeaders, String url, String requestBody) {
		return restClientUtils.invoke(action, requestHeaders, url, requestBody);
	}

	@RobotKeyword
	public Object buildJsonTemplate(String jsonFilePath, String templateValue) {
		String[] templateValues = templateValue.split(";");
		Scanner scanner = null;
		try {
			Map<String, Object> templateValuesMap = CommonUtil.getTemplateValuesMap(templateValues, REPLACE_WITH);
			scanner = new Scanner(new File(jsonFilePath), "UTF-8");
			String text = scanner.useDelimiter("\\A").next();
			DocumentContext documentContext = JsonPath.using(configuration).parse(text);
			Iterator<Entry<String, Object>> itr = templateValuesMap.entrySet().iterator();
			while (itr.hasNext()) {
				Entry<String, Object> entry = itr.next();
				if (entry.getValue() == null) {
					documentContext.delete(entry.getKey());
				} else {
					documentContext.set(entry.getKey(), entry.getValue());
				}
			}
			return documentContext.json();
		} catch (Exception e) {
			LOGGER.error(e);
			throw new RuntimeException(e.getMessage());
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}
}
