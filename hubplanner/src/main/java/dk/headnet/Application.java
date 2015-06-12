package dk.headnet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
	private static LiquidplannerAPI lp;
	private static HubplannerAPI hub;

    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        /*
         * Currently, these are in the properties file:
         hub.apikey = 
         hub.apiurl = 
         hub.vacation.eventid = 555dc87ebc8e7a3f06f9765c // The ID for the Hubplanner event Vacation.

         lp.user = 
         lp.password = 
         lp.apiurl = 
         lp.activity.id = 60889 // The id for Liquidplanner event Vacation 020
         lp.vacationwithin.days = 200

         application.name=lphub
        */

        
        
        
        if (args == null || args.length == 0) {
            System.out.println("Syntax: java -jar <jarfilename> <propertiesfile> [-dryrun]");
            System.out.println("If 2nd argument is \"-dryrun\", the program does not book any holidays, but only writes dates/users to stdout.");
            System.exit(0);
        }
        props = new Properties();
        props.load(new FileInputStream(args[0]));

        lp = new LiquidplannerAPI(props.getProperty("lp.apiurl"), props.getProperty("lp.user"), props.getProperty("lp.password"));
        hub = new HubplannerAPI(props.getProperty("hub.apiurl"), props.getProperty("hub.apikey"));
        transferVacations((args.length > 1 && args[1].equalsIgnoreCase("-dryrun")));
    }
   
    /* Book vacation for user with given email.
     * 
     */
    private static void transferVacations(boolean dryrun) throws FileNotFoundException, IOException, InterruptedException {
        if (dryrun) {
            System.out.println("Dryrun, so not booking anything, just printing found vacations to stdout.");
        }
        
        Person[] lppeople = lp.getPersons();
        Activity[] activities = lp.getActivities(Integer.parseInt(props.getProperty("lp.vacationwithin.days", "200")), props.getProperty("lp.activity.id"));
        Map<String,String> hubresources = getHubresources(lppeople);
        Map<String, Booking[]> hubbookings = getHubbookings(lppeople, hubresources);

        for (Activity act : activities) {
            if (act.getStart_date() == null) {
                continue;
            } else if (act.getFinish_date() == null) {
                act.setFinish_date(act.getStart_date());
            }

            String email = getEmailFromAssignment(act.getAssignments(), lppeople);

            if (email != null && hubresources.containsKey(email)) {
                System.out.print("Booking LPVacation in Hubplanner: "
                        + act.getActivity_id() + ": " + act.getStart_date()
                        + " - " + act.getFinish_date() + " for " + email + ": ");

                if (!vacationExists(act.getStart_date(), act.getFinish_date(), hubbookings.get(email))) {
                    if ( ! dryrun) {
                        hub.bookVacation(hubresources.get(email), props.getProperty("hub.vacation.eventid"), act.getStart_date(), act.getFinish_date());
                    }
                    System.out.println("OK. Booked to " + hubresources.get(email));
                } else {
                    System.out.println("Already booked.");
                }

            } else if (email != null) {
                System.out.println("Email " + email + " found in Liquidplanner, but not in Hubplanner.");
            }
        }
    }

    
    /** Returns a map linking email to Hub resource_ID.
     * In Hubplanner a resource is, at least in this context, a person.
     * 
     * @param lppeople
     * @return
     * @throws InterruptedException
     */
    private static Map<String, String> getHubresources(Person[] lppeople) throws InterruptedException {
        Map<String, String> hubresources = new HashMap<String, String>();

        for (Person lpperson : lppeople) {
            Resource[] resource = hub.findResources("email", lpperson.getEmail());
            if (resource != null && resource.length > 0) {
                hubresources.put(lpperson.getEmail(), resource[0].get_id());
            }
        }

        return hubresources;
    }

    /**
     * Returns the existing bookings for every person (i.e. email) in Liquidplanner.
     * This is in the form of a map, key=email, value=list of bookings for this person.
     *  
     * @param lppeople
     * @return
     * @throws InterruptedException
     */
    private static Map<String, Booking[]> getHubbookings(Person[] lppeople, Map<String, String> email2Hubresource) throws InterruptedException {
        Map<String, Booking[]> allbookings = new HashMap<String, Booking[]>();

        for (Person lpperson : lppeople) {
            if (email2Hubresource.containsKey(lpperson.getEmail())) {
                Booking[] hubvacations = hub.findBookings(email2Hubresource.get(lpperson.getEmail()), props.getProperty("hub.vacation.eventid"));

                if (hubvacations != null) {
                    allbookings.put(lpperson.getEmail(), hubvacations);
                }
            }
        }

        return allbookings;
    }

    /**
     * Returns the email for the person_id in this assignment.
     * Liquidplanner activities keeps the person_id in assignments, and I get that
     * and look up the person_id in lppeople, where I then find the email.
     * See Activity class for example of data.
     * @param assmt
     * @param lppeople
     * @return
     */
    private static String getEmailFromAssignment(Assignment[] assmt, Person[] lppeople) {
        if (assmt != null) {
            for (Person lpp : lppeople) {
                if (lpp.getId().equalsIgnoreCase(assmt[0].getPerson_id())) {
                    return lpp.getEmail();
                }
            }
        }

        return null;
    }
	
    /*
     * Compare 1 period, given as (startdate, enddate), with a list of bookings
     * in Hubplanner. startdate and enddate each contains a string such as
     * "2015-02-30".
     */
    private static boolean vacationExists(String startdate, String enddate, Booking[] hubvacations) {

        if (hubvacations == null) {
            return false;
        }

        for (Booking hv : hubvacations) {
            if (hv.getStart().startsWith(startdate) && hv.getEnd().startsWith(enddate)) {
                return true;
            }
        }
        return false;
    }
}
