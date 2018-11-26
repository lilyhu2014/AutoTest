package api.autotest.framework;

import java.util.Optional;

import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import api.autotest.framework.keywords.RobotRestLibrary;
import api.autotest.framework.keywords.JsonLibrary;
import api.autotest.framework.rest.RestClient;
import api.autotest.framework.rest.RestClientUtils;
import api.autotest.framework.rest.RestServiceResponse;

public class TestRestClient {
  @Test
  public void f() {
	  Optional<Response> res = new RestClient().get("https://api.tokenpad.io/platform/v1/ico/categories", null);
	  res.ifPresent(a -> System.out.println(a.toString()));
  }
  
  @Test
  public void testRestClientUtils() {
	 RestServiceResponse res = new RestClientUtils().invoke("GET" , null, "https://www.8btc.com", null);
	 System.out.println(res.getResponse());
  }
  
  @Test
  public void testRobotRestLibrary() {
	  RobotRestLibrary robotLibrary = new RobotRestLibrary();
	  Object res = robotLibrary.invokeService("https://api.tokenpad.io/platform/v1/ico/categories");
	  System.out.println(res);
  }
  
  @Test
  public void testValidationLibrary() {
	  String expected = "{'id':1, 'name':'lily'}";
	  String actual = "{'id':1,'age':10, 'name':'lily'}";
	  
	  JsonLibrary validationLibrary = new JsonLibrary();
	  validationLibrary.validateJsonObject(expected, actual, "strict".toUpperCase());
  }
}
