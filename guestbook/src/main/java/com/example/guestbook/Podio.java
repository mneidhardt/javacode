package com.example.guestbook;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// import org.apache.commons.lang.StringUtils;




import com.podio.APIFactory;
import com.podio.ResourceFactory;
import com.podio.comment.Comment;
import com.podio.comment.CommentAPI;
import com.podio.comment.CommentCreate;
import com.podio.common.Reference;
import com.podio.common.ReferenceType;
import com.podio.contact.ContactAPI;
import com.podio.contact.Profile;
import com.podio.contact.ProfileField;
import com.podio.contact.ProfileType;
import com.podio.file.FileAPI;
import com.podio.item.FieldValuesUpdate;
import com.podio.item.FieldValuesView;
import com.podio.item.ItemAPI;
import com.podio.item.ItemBadge;
import com.podio.item.ItemCreate;
import com.podio.item.ItemUpdate;
import com.podio.item.ItemsResponse;
import com.podio.oauth.OAuthAppCredentials;
import com.podio.oauth.OAuthClientCredentials;
import com.podio.oauth.OAuthUsernameCredentials;
import com.podio.tag.TagAPI;
import com.podio.user.UserAPI;
import com.podio.app.AppAPI;
import com.podio.app.Application;
import com.podio.app.ApplicationField;
import com.sun.jersey.api.client.UniformInterfaceException;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Podio {
	private Properties properties;
	
	private ResourceFactory podioAPI;

	public Podio(int appId, String appToken) throws IOException {
		super();
		//properties = new Properties();
		//properties.load(new FileInputStream(configFile));

        String cid = "xxyyzz";
        String csecret = "csecret";

		OAuthClientCredentials occ = new OAuthClientCredentials(cid, csecret); // properties.getProperty("podio.clientId"),properties.getProperty("podio.clientSecret"));
		OAuthAppCredentials oac = new OAuthAppCredentials(appId, appToken);

		this.podioAPI = new ResourceFactory(occ, oac);
	}

	
	public List<String> listItems(int appId) {
		APIFactory apiFactory = new APIFactory(this.podioAPI);
		AppAPI appAPI = apiFactory.getAPI(AppAPI.class);
		ItemAPI itemAPI = apiFactory.getAPI(ItemAPI.class);

		Application app = appAPI.getApp(appId);
		List<ApplicationField> appfields = app.getFields();

		List<String> csvdata = new ArrayList<String>();
		
		csvdata.add("HEADER: App: " + app.getConfiguration().getName() + " ID=" + app.getId());

		if (appfields != null) {
			for (ApplicationField appfield : appfields) {
				csvdata.add("FIELDNAMES: "
						+ appfield.getId() + " "
						+ appfield.getExternalId() + " "
						+ appfield.getConfiguration().getLabel() + " "
						+ appfield.getType());
			}
			ItemsResponse itr = itemAPI.getItems(app.getId(), 20, 0, null, null);

			if (itr.getTotal() > 0) {
				List<ItemBadge> itbadges = itr.getItems();
				for (ItemBadge itb : itbadges) {
					List<FieldValuesView> fvalues = itb.getFields();
					String line = "";
					for (FieldValuesView fvalue : fvalues) {
						line += fvalue.getValues().get(0).get("value") + ";";
					}
					
					csvdata.add(line);
				}
			}
		}
		
		return csvdata;
	}

	private String getPodioUsername() {
		APIFactory apiFactory = new APIFactory(this.podioAPI);
		UserAPI userAPI = apiFactory.getAPI(UserAPI.class);
		Profile profile = userAPI.getProfile();

		return profile.getName();
	}
	
	public void listApps(int spaceId) {
		APIFactory apiFactory = new APIFactory(this.podioAPI);
		AppAPI appAPI = apiFactory.getAPI(AppAPI.class);
		ItemAPI itemAPI = apiFactory.getAPI(ItemAPI.class);
		
		List<Application> apps = appAPI.getAppsOnSpace(spaceId);
		
		for (Application app : apps) {
			List<ApplicationField> appfields = app.getFields();
			
			System.out.println("AppId: " + app.getId());

			if (appfields != null) {
				for (ApplicationField appfield : appfields) {
					System.out.println("\t" + appfield.getType());
				}
			} else {
				System.out.println("Sorry, no fields found for app "
						+ app.getId());
			}
		}
		
	}
	 
/*	public static void main(String[] args) throws Exception {

		Podio ps = new Podio(args[0]);
		int spaceId = 3182912;		// ID for space MicWS.
		ps.listApps(spaceId);
	}
    */
}
