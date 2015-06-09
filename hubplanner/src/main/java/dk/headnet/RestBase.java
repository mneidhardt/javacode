package dk.headnet;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

	/**
	 * @author mine@headnet.dk
	 *
	 */
	public class RestBase {

		// Generic GET, which works. Use like so:
	    // doGet("/resource", httpheaders, Resource[].class);
	    // doGet("/project", httpheaders, Project[].class);
	    public <T> T doGet(String endpoint, HttpHeaders headers, Class<T> returntype) throws InterruptedException {
	    	HttpEntity<Map<String,String>> request = new HttpEntity<Map<String,String>>(null, headers);

	    	return(this.doexchange(endpoint, HttpMethod.GET, request, returntype));
	    }
	    
	    /* Generic POST, with sleep time if too many requests.
	    *  Use like so:
	    *  doPost("/resource/search", Resource[].class);
	    *  doPost("/project", Project[].class);
	    */
	    protected <T> T doPost(String endpoint, Map<String,String> body, HttpHeaders headers, Class<T> returntype) throws InterruptedException {
	    	HttpEntity<Map<String,String>> request = new HttpEntity<Map<String,String>>(body, headers);
	    	return(this.doexchange(endpoint, HttpMethod.POST, request, returntype));
	    }

	    
	    /**
	     * This does a generic HTTP(S) call. Included is a wait mechanism, so that if we get 429, which means too many requests,
	     * I wait some seconds, before trying again. This is repeated up to 4 times.
	     * @param url
	     * @param method
	     * @param request
	     * @param returntype
	     * @return
	     * @throws InterruptedException
	     */
	    private <T> T doexchange(String url, HttpMethod method, HttpEntity<Map<String,String>> request, Class<T> returntype) throws InterruptedException {
	    	int attempt=4;

	    	// I use this to be able to handle compressed responses.
	    	HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());	    	
	    	
	    	RestTemplate resttpl = new RestTemplate(clientHttpRequestFactory);
	    	
			do {
				--attempt;

				try {
					ResponseEntity<T> response = resttpl.exchange(url, method, request, returntype);
					return(response.getBody());
				} catch (RestClientException e) {
					// TODO: Find a safer way to check the error code!
					if (e.getMessage().startsWith("429")) {
						Thread.sleep(5000);
					} else {
						attempt = 0;
					}

					System.err.println("Attempt=" + attempt + " >>> " + e.getMessage());
				}
			} while (attempt > 0);
			
			return null;
	    }
	    
		public Map<String,String> getBody(String field, String value) {
	        Map<String, String> body = new HashMap<String, String>();
	        body.put(field, value);
	        
	        return body;
		}
		
		public Map<String,String> getBody() {
	        Map<String, String> body = new HashMap<String, String>();
	        return body;
		}

		/*
	    //Dont know if I'll need this.
	    public Resource createResource(String firstname, String lastname, String email) {
	        Map<String, String> body = new HashMap<String, String>();
	        body.put("firstName", firstname);
	        body.put("lastName", lastname);
	        body.put("email", email);
	        body.put("status", "STATUS_ACTIVE");
	        body.put("role",  "ROLE_TEAM");
	    	
	    	HttpEntity<Map<String,String>> req = new HttpEntity<Map<String,String>>(body, getHeaders());
	    	RestTemplate resttpl = new RestTemplate();
	    	return(resttpl.postForObject(HUBAPIURL+"/resource", req, Resource.class));
	    }
	    */

	
	
	}
