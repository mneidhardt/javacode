package com.example.guestbook;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Enumeration;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GuestbookServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    resp.setContentType("text/plain");
   
    Enumeration<String> parameterNames = req.getParameterNames();
    while (parameterNames.hasMoreElements()) {
   
        String paramName = parameterNames.nextElement();
        resp.getWriter().println(paramName);
   
        String[] paramValues = req.getParameterValues(paramName);
        for (int i = 0; i < paramValues.length; i++) {
            String paramValue = paramValues[i];
            resp.getWriter().println(paramValue);
        }
    }


    if (req.getParameter("testing") != null) {
      resp.setContentType("text/plain");
      resp.getWriter().println("Halløj, this is a testing servlet. \n\n");
      Properties p = System.getProperties();
      p.list(resp.getWriter());

    } else {
        resp.getWriter().println("Halløj, der sker ikke noget her. \n\n");
    	/*
        int appid;
        try {
        	appid = Integer.parseInt(req.getParameter("appid"));
            String apptoken = req.getParameter("apptoken");

            Podio ps = new Podio(appid, apptoken);
            if (ps != null) {
            	List<String> csv = ps.listItems(appid);
            	resp.getWriter().println("Hej - csv er her.");
            	for (String s : csv) {
            		resp.getWriter().println(s);
            	}
            }
        } catch (Exception e) {
        	resp.getWriter().println("der skete en fejl... " + e.getMessage());
        }
        */
    }
  }
}
