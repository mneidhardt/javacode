package dk.headnet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import dk.headnet.Hubplanner.Booking;
import dk.headnet.Hubplanner.HubplannerAPI;
import dk.headnet.Hubplanner.Resource;
import dk.headnet.Liquidplanner.Activity;
import dk.headnet.Liquidplanner.Assignment;
import dk.headnet.Liquidplanner.LiquidplannerAPI;
import dk.headnet.Liquidplanner.Person;

public class Application {
	private static Properties props;

    public static void main(String args[]) throws FileNotFoundException, IOException, InterruptedException {
    	props = new Properties();
        props.load(new FileInputStream(args[0]));
        
    	LiquidplannerAPI lp = new LiquidplannerAPI(props);
    	/*HubplannerAPI hub = new HubplannerAPI(props);

    	liquidplanner(lp);

    	// For now just use this as test input - this is what I expect from Liquidplanner, for each user.
    	/*String[][] allLPvacation = { { "2015-10-01", "2015-10-01" }, {"2015-10-03", "2015-10-03"}, {"2015-10-05", "2015-10-05"}, {"2015-10-07", "2015-10-18"} };
    	transferVacation(hub, "mine@headnet.dk", allLPvacation);
    	*/
    	
    	
    }

    private static void liquidplanner(LiquidplannerAPI lp) throws FileNotFoundException, IOException, InterruptedException {
  	
    	Person[] lppeople = lp.getPersons();
    	if (lppeople == null) {
    		System.err.println("No persons found in Liquidplanner.");
    		return;
    	}
    	
    	
    	
    	Activity[] acts = lp.getActivities();
    	if (acts != null && acts.length > 0) {
    		for (Activity act : acts) {
    			if (act.getFinish_date() == null) {
    				act.setFinish_date(act.getStart_date());
    			}
    			String email = "NONE";
    			if (act.getAssignments() != null) {
    				Assignment[] assmt = act.getAssignments();
    				for (Person lpp : lppeople) {
    					if (lpp.getId().equalsIgnoreCase(assmt[0].getPerson_id())) {
    						email = lpp.getEmail();
    					}
    				}
    			}
    			if (email.equalsIgnoreCase("anne@headnet.dk")) {
    				System.out.println(act.getActivity_id() + ", " + act.getProject_id() + ". " + act.getStart_date() + " - " + act.getFinish_date() + " for " + email);
    			}
    		}
    	}
    }
    
    /* Book vacation for user with given email.
     * 
     */
    private static void transferVacation(HubplannerAPI hub, String email, String[][] allLPvacation) throws FileNotFoundException, IOException, InterruptedException {
    	Resource[] person = hub.findResources("email", email);
    	if (person != null) {
    		System.out.println("Got person: " + person[0].toString());
    	    Booking[] hubvacations = hub.findBookings(person[0].get_id(), props.getProperty("hub.vacation.eventid"));
    	    
    	    if (hubvacations != null) {
    	    	for (Booking hv : hubvacations) {
    	    		System.out.println("    " + hv.toString());
    	    	}
    	    }
    	    
    	    for (String[] vacation : allLPvacation) {
    	    	if ( ! vacationExists(vacation, hubvacations)) {
    	    		System.out.println("vac not found: " + vacation[0] + "-" + vacation[1]);
    	    		Booking newvac = hub.bookVacation(person[0].get_id(), vacation[0], vacation[1]);
    	    	} else {
    	    		System.out.println("vac found: " + vacation[0] + "-" + vacation[1]);
    	    	}
    	    }
   	
    	}
    }
    
    /* Compare 1 period, vac, with a list of bookings in Hubplanner.
    *  vac contains 2 date strings, each of which is like "2015-02-30".
    */
    private static boolean vacationExists(String[] vac, Booking[] hubvacations) {
    	System.out.println(vac[0] + "-" + vac[1]);
    	
    	for (Booking hv : hubvacations) {
    		if (hv.getStart().startsWith(vac[0]) && hv.getEnd().startsWith(vac[1])) {
    			return true;
    		}
    	}
    	return false;
    }

    /*
    public static void getStuff(HubplannerAPI hub) throws InterruptedException {
    	// Get stuff this way:
    	Resource[] allres = hub.doGet("/resource", Resource[].class);
    	for (Resource r : allres) {
    		System.out.println("Resource: " + r.get_id() + ", " + r.getEmail() + ", " + r.getFirstName() + " " + r.getLastName());
    	}
    	
    	Event[] allevents = hub.doGet("/event", Event[].class);
    	for (Event e : allevents) {
    		System.out.println("Event: " + e.get_id() + ": " + e.getName() + " " + e.getCreatedDate());
    	}
    	
    	Project[] allprojects = hub.doGet("/project", Project[].class);
    	for (Project p : allprojects) {
    		System.out.println("Project: " + p.get_id() + ": " + p.getName() + ", " + p.getCreatedDate());
    	}
    	
    	// Or this way:
    	Project[] projects = hub.findProjects("resource", "some_resource_id");
    }
    */
}
