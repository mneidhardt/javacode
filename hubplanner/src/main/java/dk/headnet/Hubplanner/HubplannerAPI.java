package dk.headnet.Hubplanner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;

/**
 * @author mine
 *
 */
/**
 * @author mine
 *
 */
public class HubplannerAPI extends dk.headnet.RestBase {
    private final String HUBAPIURL;
    private final String HUBAPIKEY;
    
    public HubplannerAPI(String apiurl, String apikey) throws FileNotFoundException, IOException {
        this.HUBAPIURL = apiurl;
        this.HUBAPIKEY = apikey;
    }
    
    /**
     * Returns all the resources that match the search:
     * 
     * @param field Which field you want to search in
     * @param value The value that the field must have
     * @return A list of resources.
     * @throws InterruptedException
     */
    public Resource[] findResources(String field, String value) throws InterruptedException {
    	return(this.doPost(HUBAPIURL+"/resource/search", this.getBody(field, value), this.getHeaders(), Resource[].class));
    }
    
    /**
     * This finds all bookings for a given resource and a given project.
     * 
     * @param resourceid Id for the person whose bookings you want
     * @param projectid Id for the project you want bookings for.
     * @return Array of bookings, or null.
     * @throws InterruptedException
     */
    public Booking[] findBookings(String resourceid, String projectid) throws InterruptedException {
        Map<String, String> body = new HashMap<String, String>();
        body.put("resource", resourceid);
        body.put("project", projectid);
    	return(this.doPost(HUBAPIURL+"/booking/search", body, this.getHeaders(), Booking[].class));
    }
    
    
    /**
     * Returns a list of projects, matching the search criteria.
     * 
     * @param field Field you want to search in.
     * @param value Value of the field
     * @return A list of projects.
     * @throws InterruptedException
     */
    public Project[] findProjects(String field, String value) throws InterruptedException {
    	return(super.doPost(HUBAPIURL+"/project/search", this.getBody(field, value), this.getHeaders(), Project[].class));
    }
    
    public Booking bookVacation(String resourceid, String eventid, String startdate, String enddate) throws InterruptedException {
        Map<String, String> body = new HashMap<String, String>();
        body.put("resource", resourceid);
        body.put("project", eventid);
        body.put("start", startdate);
        body.put("end", enddate);
        body.put("allDay", "true");
        body.put("state", "STATE_PERCENTAGE");
        body.put("stateValue", "100");
        body.put("scale", "SCALE_DAY");
        
        return(this.doPost(HUBAPIURL+"/booking", body, this.getHeaders(), Booking.class));
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Holiday Transfer Agent");
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", HUBAPIKEY);

        return headers;
	}

}
