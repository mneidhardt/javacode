package dk.headnet;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.Period;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;

/**
 * @author mine
 *
 */
public class CryptoStuff {
	private static Random rn = new Random();
	
	public static int rand(int lo, int hi)
	{
		int n = hi - lo + 1;
		int i = rn.nextInt() % n;
		if (i < 0)
			i = -i;
		return lo + i;
	}

	public static String makeCommonSecret() {
		String result = "";
        char[] allowedChars = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        for (int i = 0; i < 255; i++)
        {
        	result = result + allowedChars[rand(0,allowedChars.length-1)];
        }
		return result;
	}
	
	public static String b64encode(byte[] b) throws MessagingException,
	IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStream b64os = MimeUtility.encode(baos, "base64");
		b64os.write(b);
		b64os.close();
		return new String(baos.toByteArray());
	}
	
	public static byte[] b64decode(String s) throws
	MessagingException, IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes());
		InputStream b64is = MimeUtility.decode(bais, "Base64");
		byte[] tmp = new byte[s.length()];
		int n = b64is.read(tmp);
		byte[] res = new byte[n];
		System.arraycopy(tmp, 0, res, 0, n);
		return res;
	}
	
	public static String getEpochTime() {
		GregorianCalendar calendar = new GregorianCalendar();
		long epochTimeNow = calendar.getTimeInMillis();
		long epochTimeLong = (Math.abs(epochTimeNow/1000));
		String epochTime = Long.toString(epochTimeLong);
		return epochTime;
	}

	public static String cryptParams(String params, int offset, String commonSecret) throws UnsupportedEncodingException, MessagingException, IOException {
		params = addSecret(params, offset, commonSecret);
		String encoded = b64encode(params.getBytes("UTF8"));
		encoded = encoded.replaceAll("\\s", "");
		String md5String = getMD5Hash(encoded);

		return md5String;
	}

	public static int offset(int len) {
		return rand(0, 255-len);
	}
	
	
	public static long durationSinceDate(String datestring) {
		DateTime date = DateTime.parse(datestring, DateTimeFormat.forPattern("yyyyMMddHHmmss").withZoneUTC());
		DateTime now = new DateTime(DateTimeZone.UTC);
		
		Duration dd1 = new Duration(date, now);
		return dd1.getMillis();	
	}
	
	public static String md5fromfile(String filename) {
		try {
			FileInputStream fis = new FileInputStream(new File(filename));
			//String md5 = org.apache.commons.codec.digest.DigestUtils.md5Hex(fis);
			String md5 = DigestUtils.md5Hex(fis);
			fis.close();
			return md5;
		} catch (FileNotFoundException fe) {
			System.err.println("File not found: " + fe.getMessage());
		} catch (IOException ie) {
			System.err.println("IOException: " + ie.getMessage());
		}
		
		return null;
	}

	
	public static String getSHA256(String input) {
		return getMessageDigest(input, "SHA-256");
	}
	
	public static String getMD5Hash(String input) {
		return getMessageDigest(input, "MD5");
	}

	private static String getMessageDigest(String input, String algorithm) {
		StringBuffer result = new StringBuffer(32);
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(input.getBytes());
			Formatter f = new Formatter(result);
			for (byte b : md.digest()) {
				f.format("%02x", b);
			}
			f.close();
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return result.toString();
	}

	private static String addSecret(String input, int offset, String commonSecret) throws UnsupportedEncodingException {
		int inputLength = input.length();
		String comSecretSubstring = commonSecret.substring(offset, inputLength + offset);
		return new String(input + comSecretSubstring);
	}
}
