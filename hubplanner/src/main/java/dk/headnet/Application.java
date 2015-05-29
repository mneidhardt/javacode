package dk.headnet;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class Application {
	
    //@Value("${hub.apikey}")
    private static String HUBAPIKEY="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0MzI4MTE3ODMsInNjb3BlIjoiU0NPUEVfUkVBRF9XUklURSIsImlzcyI6IjU1NjZmMDNiNmNlNmZmMGQwN2VmZWZiNiIsInJlc291cmNlIjoiNTU1ZGM4N2ViYzhlN2EzZjA2Zjk3NjU2In0.enPmEa-7Z7rLIcxOC-DyWWemaItjpG8EyE2a3EjTfPs";
    //@Value("${hub.apiurl}")
    private static String HUBAPIURL="https://api.hubplanner.com/v1/";


    public static void main(String args[]) throws ClassNotFoundException {
        doSearch("resource", "firstName", "Thomas");
        System.out.println(".................");
        doGet("project"); // /555dd97cff96f9550db3c086");
    }
    
    public static void doSearch(String type, String field, String value) {
    	HttpEntity<Map<String,String>> req = new HttpEntity<Map<String,String>>(getBody(field, value), getHeaders());
    	RestTemplate resttpl = new RestTemplate();
    	Resource[] res = resttpl.postForObject(HUBAPIURL+"resource/search", req, Resource[].class);
    	for (Resource r : res) {
    		System.out.println(r.get_id() + ", " + r.getEmail() + ", " + r.getFirstName() + " " + r.getLastName());
    	}
    }
    
    public static void failed_getObject(String resid) {
    	// Denne duer ikke, da der mangler headers med bla. Authorization.
    	HttpEntity<Map<String,String>> req = new HttpEntity<Map<String,String>>(getHeaders());
    	RestTemplate resttpl = new RestTemplate();
    	Resource res = resttpl.getForObject(HUBAPIURL+"resource/"+resid, Resource.class);
    	System.out.println(res.toString());
    }

    public static void getTimeEntries(String resid) {
    	RestTemplate restTemplate = new RestTemplate();
    	HttpEntity<String> entity = new HttpEntity<String>(getHeaders());

    	ResponseEntity<String> o = restTemplate.exchange(HUBAPIURL+"resource/"+resid, HttpMethod.GET, entity, String.class);
    	System.out.println(o.getBody());
    }
    
    public static void doGet(String suffix) throws ClassNotFoundException {
    	HttpEntity<Map<String,String>> httpEntity = new HttpEntity<Map<String,String>>(getBody(), getHeaders());
    	RestTemplate restTemplate = new RestTemplate();
    	ResponseEntity<String> res = restTemplate.exchange(HUBAPIURL+suffix, HttpMethod.GET, httpEntity, String.class);
    	System.out.println(res.getBody());
    }
    
	private static Map<String,String> getBody(String field, String value) {
        Map<String, String> body = new HashMap<String, String>();
        body.put(field, value);
        
        return body;
	}
	
	private static Map<String,String> getBody() {
        Map<String, String> body = new HashMap<String, String>();
        return body;
	}

	private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", HUBAPIKEY);

        return headers;
	}
    
    /*    
    public static void doPost() {
    	MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<String, Object>();
        requestBody.add("lastName", "Neidhardt");

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");
        headers.set("Authorization", HUBAPIKEY);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject("https://abc.com/api/request", httpEntity, String.class);
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(response);
            System.out.println(jsonObject.get("status"));
        } catch (ParseException e) {
            e.printStackTrace();
        }    }
*/
	/*
	public static void older_doPost() {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();     
		body.add("lastName", "Benali");
		HttpEntity<?> httpEntity = new HttpEntity<Object>(body, getHeaders());
		
		RestTemplate restTemplate = new RestTemplate();

		HttpEntity<String> response = restTemplate.exchange("/api/url", HttpMethod.POST, httpEntity, String.class);
		

		// MyModel model = restTemplate.exchange("/api/url", HttpMethod.POST, httpEntity, MyModel.class);
	}
*/
}
