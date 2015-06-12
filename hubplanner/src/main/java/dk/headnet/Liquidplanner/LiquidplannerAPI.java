package dk.headnet.Liquidplanner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.DatatypeConverter;
import org.springframework.http.HttpHeaders;

public class LiquidplannerAPI extends dk.headnet.RestBase {
    private final String USER;
    private final String PASSWORD;
    private final String APIURL;
    
    public LiquidplannerAPI(String apiurl, String user, String pwd) throws FileNotFoundException, IOException {
        this.APIURL = apiurl;
    	this.USER = user;
    	this.PASSWORD = pwd;
    }
    
    public Person[] getPersons() throws UnsupportedEncodingException, IOException, InterruptedException {
    	return(this.doGet(APIURL + "/workspaces/35031/members", getHeaders(), Person[].class));
    }
    
    /**
     * Returns LP activities with given ID, e.g. vacations.
     * If days is > 0, only returns those activities that were created within 'days' of now, both in past & future.
     * @param days
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws InterruptedException
     */
    public Activity[] getActivities(int days, String activityid)  throws UnsupportedEncodingException, IOException, InterruptedException {
    	String datefilter="";
    	
    	if (days > 0) {
    		datefilter = "&filter[]=created within " + days;
    	}
    	
    	return(this.doGet(APIURL+"/workspaces/35031/treeitems?depth=-1&leaves=true&filter[]=activity_id="+activityid+datefilter, getHeaders(), Activity[].class));
    }


	private HttpHeaders getHeaders() throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Holiday Transfer Agent");
        headers.set("Accept-Encoding", "gzip, deflate");
        
        byte[] message = (USER + ":" + PASSWORD).getBytes("UTF-8");
        String encodedmsg = DatatypeConverter.printBase64Binary(message);
        headers.set("Authorization", "Basic " + encodedmsg);

        return headers;
	}

}
