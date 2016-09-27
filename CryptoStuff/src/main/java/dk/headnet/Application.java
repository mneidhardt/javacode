package dk.headnet;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.NamingException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

public class Application {
	
	public static void main(String[] args) {
		
		System.out.println(CryptoStuff.md5fromfile(args[0]));
	}
	
	public static void oldMain(String[] args) {
		
		String syntax = " Forventer disse argumenter:\n" +
						"date instno callbackurl useremail altkey secret\n" +
						"  date = YYYYMMDDHHMMSS i UTC,\n" +
						"  instno = institutionsnummer,\n" +
						"  callbackurl = callback hostname,\n" +
						"  useremail = user's email,\n" + 
						"  altkey = den altkey brugeren fik ved oprettelse i driveservice,\n" +
						"  secret = den fælles hemmelighed.";
		
		if (args.length < 6) {
			System.err.println(syntax);
			System.exit(1);
		}
		
		String date = args[0];
		String instno = args[1];
		String callbackurl = args[2];
		String useremail= args[3];
		String altkey = args[4];
		String secret = args[5];

		long duration = CryptoStuff.durationSinceDate(date);
		System.out.println("Date > now: " + duration + " ms.");
	
		String oauth2 = date + instno + callbackurl + secret;
		String drive =  date + useremail + altkey + secret;
		String callback = date + instno + altkey + secret;
		String callbackmd5 = "";
		
		String md5hashA = CryptoStuff.getMD5Hash(oauth2);
		System.out.println("OAuth2: " + oauth2 + " >>> " + md5hashA);
		String md5hashB = CryptoStuff.getMD5Hash(drive);
		System.out.println("Drive:  " + drive + " >>> " + md5hashB);
		System.out.println("Callback:\n" + CryptoStuff.getMD5Hash(callback) + "\n" + callbackmd5);
		
	}

}
