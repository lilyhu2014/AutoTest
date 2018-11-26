package api.autotest.framework.keywords;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JsonOrgJsonProvider;
import com.jayway.jsonpath.spi.mapper.JsonOrgMappingProvider;

import api.autotest.framework.util.CommonUtil;

@RobotKeywords
public class JsonLibrary {

	@SuppressWarnings("resource")
	@RobotKeyword
	public String buildJsonTemplate(String jsonFilePath, String templateValue) throws Exception {
		String[] templateValues = templateValue.split(";");
		Scanner scanner = null;
		Map<String, Object> templateValuesMap = CommonUtil.getTemplateValuesMap(templateValues,
				CommonUtil.REPLACE_WITH);
		System.out.println("template map --> " + templateValuesMap);
		scanner = new Scanner(new File(jsonFilePath), "UTF-8");
		String text = scanner.useDelimiter("\\A").next(); // ???
		DocumentContext documentContext = JsonPath.using(Configuration.builder()
				.mappingProvider(new JsonOrgMappingProvider()).jsonProvider(new JsonOrgJsonProvider()).build())
				.parse(text);
		Iterator<Entry<String, Object>> itr = templateValuesMap.entrySet().iterator();
		while(itr.hasNext()) {
			Entry<String, Object> entry = itr.next();
			String entryString = entry.getValue().toString();
			if(entry.getValue() == null || entryString.equalsIgnoreCase("None")) {
				documentContext.set(entry.getKey(), JSONObject.NULL);
			}else if(entryString.equalsIgnoreCase("remove from path")) {
				documentContext.delete(entry.getKey());
			}else {
				documentContext.set(entry.getKey(), entry.getValue());
			}
		}
		return documentContext.json().toString();
	}

	@RobotKeyword
	public void validateJsonObject(String expected, String actual, String compareMode) {
		JSONCompareMode jsonCompareMode = JSONCompareMode.LENIENT;

		if (StringUtils.isNotBlank(compareMode)) {
			jsonCompareMode = JSONCompareMode.valueOf(compareMode);
		}

		JsonParser parser = new JsonParser();
		JsonElement expectedJson = parser.parse(expected);
		JsonElement actualJson = parser.parse(actual);

		if (expectedJson.equals(actualJson)) {
			System.out.println("Json objects are equal");
		} else {
			try {
				JSONAssert.assertEquals(expected, actual, jsonCompareMode);
				System.out.println("Json Object are equal");
			} catch (JSONException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
