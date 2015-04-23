package dk.headnet.ldap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;

public class LDAPTool {
	private LdapContext ldapContext;
	private String PROVIDERURL;
	private String SECURITYPRINCIPAL;
	private String SECURITYCREDENTIALS;
	private int pageSize;

	/* Second constructor, now using InitialLdapContext, so that I can use PagedResultsControl.
	 * This is required if you want to call pagedSearch() */
	public LDAPTool(String providerurl, String securityprincipal, String securitycredentials, int pageSize) throws IOException {
		try {
			PROVIDERURL = providerurl;
			SECURITYPRINCIPAL = securityprincipal;
			SECURITYCREDENTIALS = securitycredentials;

			Hashtable<String, String> ldapEnv = new Hashtable<String, String>(11);
			ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			ldapEnv.put(Context.PROVIDER_URL, "ldap://" + PROVIDERURL);
			ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
			ldapEnv.put(Context.SECURITY_PRINCIPAL, SECURITYPRINCIPAL);
			ldapEnv.put(Context.SECURITY_CREDENTIALS, SECURITYCREDENTIALS);
			
			// Activate paged results
			this.pageSize = pageSize; // Entries per page
			this.ldapContext = new InitialLdapContext(ldapEnv, null);
			this.ldapContext.setRequestControls(new Control[]{new PagedResultsControl(pageSize, Control.NONCRITICAL) });
		} catch (NamingException e) {
			System.err.println("LDAPTools: LDAP authentication/binding error: " + e.getMessage());
			System.exit(1);
		}
	}

	public String close() {
		try {
			ldapContext.close();
			return null;
		} catch (Throwable t) {
			return "close() failed: " + t.getMessage();
		}
	}

	
	public List<String> pagedSearch(String searchbase, String searchFilter, String scope, List<String> retattribs) throws NamingException, IOException {
	     List<String> searchresult = new ArrayList<String>();
	     
		SearchControls searchCtls = new SearchControls();
		searchCtls.setCountLimit(0);
		boolean dereflinkflag = false;
		searchCtls.setDerefLinkFlag(dereflinkflag);
			
		if (retattribs.isEmpty()) {
			searchCtls.setReturningAttributes(null);
		} else {
			String[] retatt = new String[retattribs.size()];
			retatt = retattribs.toArray(retatt);
			searchCtls.setReturningAttributes(retatt);
		}
			
		if (scope == null || scope.equalsIgnoreCase("subtree")) {
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		} else if (scope.equalsIgnoreCase("object")) {
			searchCtls.setSearchScope(SearchControls.OBJECT_SCOPE);
		} else if (scope.equalsIgnoreCase("onelevel")) {
			searchCtls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		}
			
		if (searchFilter == null) {
			searchFilter = "objectClass=*";
		}
		
	    byte[] pagingcookie = null;
	    int total;
			
	    do {
	         // Perform the search
	         NamingEnumeration<SearchResult> results = ldapContext.search(searchbase, searchFilter, searchCtls);

	         // Iterate over a batch of search results
	         while (results != null && results.hasMore()) {
		         SearchResult entry = results.next();
		         //searchresult.add(attrs2XML(entry.getAttributes()));
		         searchresult.add(attrs2CSV(entry.getAttributes(), retattribs));
	         }

	      // Examine the paged results control response
	         Control[] controls = ldapContext.getResponseControls();
	         if (controls != null) {
	         for (int i = 0; i < controls.length; i++) {
	             if (controls[i] instanceof PagedResultsResponseControl) {
	            	 PagedResultsResponseControl prrc = (PagedResultsResponseControl)controls[i];
	            	 total = prrc.getResultSize();
	            	 pagingcookie = prrc.getCookie();
	             }
	         }
	         }

	         // Re-activate paged results
	         ldapContext.setRequestControls(new Control[]{new PagedResultsControl(pageSize, pagingcookie, Control.CRITICAL) });
	         
	     } while (pagingcookie != null);
	     
	     return searchresult;

	}
	
	
	static String attrs2XML(Attributes attrs) {
		String result="";
		
		if (attrs != null) {
			try {
				result += "<attributes>\n";
				for (NamingEnumeration ae = attrs.getAll(); ae.hasMore();) {
					Attribute attr = (Attribute) ae.next();
					//result += "<attribute id='" + attr.getID() + "'>\n";
					
					String attrID = attr.getID().replaceAll("\\s+", "_");

					/* print each value */
					for (NamingEnumeration e = attr.getAll(); e.hasMore();) {
						String value = e.next().toString().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
						result += "<" + attrID + ">" + value + "</" + attrID + ">\n";
					}
				}
				result += "</attributes>\n";
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	
	/**
	 * Return a comma separated list of the chosen fields.
	 * @param attrs
	 * @param fieldlist The fieldname that the user wants, in correct order.
	 * @return
	 */
	static String attrs2CSV(Attributes attrs, List<String> fieldlist) {
		Map<String,String> map = new HashMap<String,String>();
		
		if (attrs != null) {
			try {
				for (NamingEnumeration ae = attrs.getAll(); ae.hasMore();) {
					Attribute attr = (Attribute) ae.next();

					for (NamingEnumeration e = attr.getAll(); e.hasMore();) {
						map.put(attr.getID(), e.next().toString());
					}
				}
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		
		String result="";
		
		for (int i=0; i< fieldlist.size(); i++) {
			if (map.containsKey(fieldlist.get(i))) {
				 result += "\"" + map.get(fieldlist.get(i)) + "\"";
			}
			 if (i < (fieldlist.size()-1)) {
				 result += ", ";
			 }
		}
		
		return result;
	}

}
