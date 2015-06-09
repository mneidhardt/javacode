package dk.headnet.Liquidplanner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.xml.bind.DatatypeConverter;
import org.springframework.http.HttpHeaders;

public class LiquidplannerAPI extends dk.headnet.RestBase {

	private final Properties properties;
    private final String USER;
    private final String PASSWORD;
    private final String APIURL;
    private final String ACTIVITYID;
    
    public LiquidplannerAPI(Properties props) throws FileNotFoundException, IOException {
    	this.properties = props;
    	APIURL = properties.getProperty("lp.apiurl");
    	USER = properties.getProperty("lp.user");
    	PASSWORD = properties.getProperty("lp.password");
    	ACTIVITYID = properties.getProperty("lp.activity.id");
    }
    
    public Person[] getPersons() throws UnsupportedEncodingException, IOException, InterruptedException {
    	return(this.doGet(APIURL + "/workspaces/35031/members", getHeaders(), Person[].class));
    }
    
    public Activity[] getActivities()  throws UnsupportedEncodingException, IOException, InterruptedException {
    	// Man ku måske her tilføje et filter så man kun får activities fra dette år, +/- 1.
    	return(this.doGet(APIURL+"/workspaces/35031/treeitems?depth=-1&leaves=true&filter=activity_id="+ACTIVITYID, getHeaders(), Activity[].class));
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
