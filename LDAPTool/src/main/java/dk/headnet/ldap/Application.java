package dk.headnet.ldap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

public class Application {
    public static void main( String[] args ) throws NamingException, IOException {
        String syntax =  "Syntax: java -jar jarfile host userid pwd searchbase searchfilter scope pagesize [returnattribute1,returnattribute2...]\nscope is one of [subtree, object, onelevel].";
        
        if (args.length < 7) {
        	System.err.println(syntax);
        	System.exit(0);
        }
        
        
        /*
         * Example arguments for this program:
         HOST=				'hostname' 
         USERID=			'CN=xyz,OU=ou2,OU=ou1,DC=level2,DC=level1' 
         PWD=     			'blabla' 
         SEARCHBASE=		'OU=ou2,OU=ou1,DC=level2,DC=level1' 
         SEARCHFILTER=		'(objectCategory=person)'                 ---> Bredere søgning fås med: (objectClass=*)
         SCOPE=				onelevel
         PAGESIZE=			1000
         RETURNATTRIBUTES=	name givenName sn title department mobile telephoneNumber mail sAMAccountname cn distinguishedName
         * 
         */
        
        String host = args[0];
        String userid = args[1];
        String pwd = args[2];
        String searchbase="";
        String searchfilter=null;
        String scope=null;
        int pageSize;
       	searchbase = args[3];
       	searchfilter = args[4];
       	scope = args[5];
       	pageSize = Integer.parseInt(args[6]);

        List<String> retattribs = new ArrayList<String>();

        if (args.length > 7) {
        	for (int i=7; i<args.length; i++) {
        		retattribs.add(args[i]);
        	}
        }
        
        LDAPTool lt = new LDAPTool(host, userid, pwd, pageSize);
        List<String> res = lt.pagedSearch(searchbase, searchfilter, scope, retattribs);

		System.out.println("<ldap searchbase='" + searchbase + 
				"' searchfilter='" + searchfilter + 
				"' scope='" + scope + 
				"' pagesize='" + pageSize +
				"' retattribs='" + retattribs.toString() + "'>");
		for (String s : res) {
			System.out.println(s);
		}
		System.out.println("<hits>" + res.size() + "</hits></ldap>");
    }
}
