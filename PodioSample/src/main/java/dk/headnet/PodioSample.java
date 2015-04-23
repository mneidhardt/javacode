package dk.headnet;

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

import org.apache.commons.lang.StringUtils;

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

public class PodioSample {
	private final Properties properties;
	
	private final ResourceFactory podioAPI;
	private APIFactory apiFactory;
	private AppAPI appAPI;
	private ItemAPI itemAPI;

	private PodioSample(String configFile, String type) throws IOException {
		super();
		properties = new Properties();
		properties.load(new FileInputStream(configFile));

		OAuthClientCredentials occ = new OAuthClientCredentials(properties.getProperty("podio.clientId"),properties.getProperty("podio.clientSecret"));
		
		if (type.equals("app")) {
			OAuthAppCredentials oac = new OAuthAppCredentials(Integer.parseInt(properties.getProperty("podio.appId")), properties.getProperty("podio.appToken"));
			this.podioAPI = new ResourceFactory(occ, oac);
			System.out.println("App Auth.");
		} else if (type.equals("user")) {
			OAuthUsernameCredentials ouc = new OAuthUsernameCredentials(properties.getProperty("podio.user.mail"), properties.getProperty("podio.user.password"));
			this.podioAPI = new ResourceFactory(occ, ouc);
			System.out.println("User Auth.");
		} else {
			this.podioAPI=null;
			System.err.println("No known type received.");
			System.exit(1);
		}

		apiFactory = new APIFactory(this.podioAPI);
		this.appAPI = apiFactory.getAPI(AppAPI.class);
		this.itemAPI = apiFactory.getAPI(ItemAPI.class);

	}
	
	private String getPodioUsername() {
		UserAPI userAPI = this.apiFactory.getAPI(UserAPI.class);
		Profile profile = userAPI.getProfile();

		return profile.getName();
	}
	
	private void listApps(int spaceId) {
		List<Application> apps = appAPI.getAppsOnSpace(spaceId);
		
		for (Application app : apps) {
			listItems(app.getId());
		}
		
	}
	
	private void listItems(int appId) {
		Application app = appAPI.getApp(appId);
		List<ApplicationField> appfields = app.getFields();
		//AppMapping am = AppMapping.get(app);

		System.out.println("\n\nApp: " + app.getConfiguration().getName() + " ID="
				+ app.getId());

		if (appfields != null) {
			for (ApplicationField appfield : appfields) {
				System.out.println("\t"
						+ appfield.getId() + " "
						+ appfield.getExternalId() + " "
						+ appfield.getConfiguration().getLabel() + " "
						+ appfield.getType());
			}
			ItemsResponse itr = this.itemAPI.getItems(app.getId(), 20, 0, null, null);

			if (itr.getTotal() > 0) {
				List<ItemBadge> itbadges = itr.getItems();
				for (ItemBadge itb : itbadges) {
					List<FieldValuesView> fvalues = itb.getFields();
					
					for (FieldValuesView fvalue : fvalues) {
						System.out.print(fvalue.getId() + "/" + fvalue.getExternalId() + " " + fvalue.getLabel() + ":" + fvalue.getValues().get(0).get("value") + ", ");
					}
					System.out.println();
				}
			}
		} else {
			System.out.println("Sorry, no fields found for app " + appId);
		}
	}
	
/*	public void addItem() {
		List<FieldValuesUpdate> fields = new ArrayList<FieldValuesUpdate>();
		fields.add(new FieldValuesUpdate(TEXT, "value", status.getText()));
		fields.add(new FieldValuesUpdate(TWEET, "value", getFullText(
				status, printer)));
		fields.add(new FieldValuesUpdate(FROM, "value", getAuthorLink(
				status, printer)));
		if (imageId != null) {
			fields.add(new FieldValuesUpdate(AVATAR, "value", imageId));
		}
		fields.add(new FieldValuesUpdate(FOLLOWERS, "value", status
				.getUser().getFollowersCount()));
		if (status.getPlace() != null) {
			fields.add(new FieldValuesUpdate(LOCATION, "value", status
					.getPlace().getName()));
		}
		if (status.getSource() != null) {
			fields.add(new FieldValuesUpdate(SOURCE, "value", status
					.getSource()));
		}
		fields.add(new FieldValuesUpdate(LINK, "value",
				getTweetLink(status)));
		if (status.getInReplyToStatusId() > 0) {
			Integer replyToItemId = getItemId(status.getInReplyToStatusId());
			if (replyToItemId != null) {
				fields.add(new FieldValuesUpdate(REPLY_TO, "value",
						replyToItemId));
			}
		}

		List<String> tags = new ArrayList<String>();
		if (status.getHashtagEntities() != null) {
			for (HashtagEntity tag : status.getHashtagEntities()) {
				tags.add(tag.getText());
			}
		}

		List<Integer> fileIds = uploadURLs(status);

		apiFactory.getAPI(ItemAPI.class).addItem(
				APP_ID,
				new ItemCreate(Long.toString(status.getId()), fields,
						fileIds, tags), false);

		System.out.println("Added tweet " + status.getText());

		return true;
	}
	}
*/	
	 

/*	private ItemBadge getPodioItem(int ItemId) {
		ItemsResponse response = new ItemAPI(podioAPI).getItemsByExternalId(TICKET_APP_ID, Integer.toString(ItemId));
		if (response.getFiltered() < 1) {
			return null;
		}
		return response.getItems().get(0);
	}

	private ItemBadge getPodioRequester(int zendeskRequesterId) {
		ItemsResponse response = new ItemAPI(podioAPI).getItemsByExternalId(
				REQUESTER_APP_ID, Integer.toString(zendeskRequesterId));
		if (response.getFiltered() < 1) {
			return null;
		}
		return response.getItems().get(0);
	}
*/

	public static void main(String[] args) throws Exception {

		PodioSample ps = new PodioSample(args[0], args[1]);
		
		if (args[1].equals("user")) {
			int spaceId = 3182912;		// ID for space MicWS.
			ps.listApps(spaceId);
		} else if (args[1].equals("app")) {
			ps.listItems(11556910);
		}
		
		//System.out.println(ps.getPodioUsername());
	}
}
