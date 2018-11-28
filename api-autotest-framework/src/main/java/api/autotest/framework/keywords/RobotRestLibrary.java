package api.autotest.framework.keywords;

import static api.autotest.framework.rest.RestClientUtils.REPLACE_WITH;

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

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

	private RestClientUtils restClientUtils;

	public RobotRestLibrary() {
		context = new ClassPathXmlApplicationContext("spring/bean-mappings.xml");
		restClientUtils = (RestClientUtils) context.getBean("restClientUtils");
		LOGGER.info("Init RobotRestLibrary class");
	}

	@Autowired
	public void setRestClientUtils(RestClientUtils restClientUtils) {
		LOGGER.info("Autowired RestClientUtils.");
		this.restClientUtils = restClientUtils;
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
