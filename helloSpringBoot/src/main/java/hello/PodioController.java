package hello;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.podio.item.Item;

@RestController
public class PodioController {
	
	private static final String cid = "xxyyzz";
	private static final String csecret = "csecret";

    @RequestMapping("/")
    public String index() {
        return "Even more greetings from Spring Boot!";
    }

    @RequestMapping("/about")
    public String about() {
        return "This is all about Spring Boot.";
    }

    @RequestMapping("/listItems")
    public List<Item> listItems(@RequestParam(value="appid", required=true, defaultValue="") String appid,
            @RequestParam(value="apptoken", required=true, defaultValue="") String apptoken)
            throws IOException {

        String cid = System.getenv().get("CID");
        String csecret = System.getenv().get("CSECRET");

        /*Podio ps = new Podio(cid, csecret, appid, apptoken);
        if (ps != null) {
            List<String> csv = ps.listItems(appid);
            resp.getWriter().println("Hej - csv er her.");
            for (String s : csv) {
                resp.getWriter().println(s);
            }
        }*/
        
        return null;
    
    }

    class ReturnMsg {
        private final String type;
        private final String code;
        private final String userid;
        private final String fileid;
        private final String msg;

    public ReturnMsg(String type, String code, String userid, String fileid, String msg) {
        this.type=type;
        this.code=code;
        this.userid=userid;
        this.fileid=fileid;
        this.msg=msg;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getUserid() {
        return userid;
    }

    public String getFileid() {
        return fileid;
    }

    public String getMsg() {
        return msg;
    }


}

    class ExceptionMsg {
        private final String type;
        private final String code;
        private final String msg;
   
        public ExceptionMsg(String type, String code, String msg) {
            this.type=type;
            this.code=code;
            this.msg=msg;
        }

        public String getType() {
            return type;
        }

        public String getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

    }

    @ExceptionHandler(IOException.class)
    public ExceptionMsg handleIOException(HttpServletRequest request, HttpServletResponse response, IOException ex) throws IOException {
        System.err.println("Requested URL=" + request.getRequestURL() + "\nException Raised=" + ex);

        return new ExceptionMsg("IOException", "", ex.getMessage());
    }    
    
    @ExceptionHandler(NumberFormatException.class)
    public ExceptionMsg handleNumberFormatException(HttpServletRequest request, HttpServletResponse response, NumberFormatException ex) throws IOException {
        System.err.println("Requested URL=" + request.getRequestURL() + "\nException Raised=" + ex);

        return new ExceptionMsg("IOException", "", ex.getMessage());
    }    
    
}
