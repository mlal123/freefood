import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Page;
import com.sun.org.apache.bcel.internal.generic.FADD;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class main {
    public static void main(String[] args){

        String[] recipients = {"mlal123@live.unc.edu"};

        FacebookClient.AccessToken accessToken = new DefaultFacebookClient().obtainAppAccessToken(appID, appSecret);
        String token = accessToken.getAccessToken();
        System.out.println(token);
        DefaultFacebookClient fbClient = new DefaultFacebookClient(token);


        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
        String str_date = "01-07-2018";
        List<String> pageNames = Arrays.asList("Greek Alliance Council (GAC)", "Carolina For The Kids Foundation",
                "UNC Housing", "Carolina Fever", "Carolina Economics Club", "CUAB");

        Date current = new Date();
        Timer timer = new Timer();


        timer.schedule(new TimerTask(){
            public void run(){
                System.out.println("Im Running..."+new Date());
                try {
                    Date date = dateformat.parse(str_date);

                    StringBuilder email_msg = new StringBuilder();
                    String accessToken = "";
                    FacebookClient fbClient = facebook.getFBClient(accessToken);
                    List<Page> pages= facebook.get_needed_pages(facebook.get_my_liked_pages(fbClient), pageNames);

                    for (Page p : pages){
                        String msg = facebook.createDescription(facebook.get_FB_posts(fbClient, p, p.getName(), date), p);
                        if (!msg.equals("empty")){
                            email_msg.append("---> ").append(p.getName()).append(": \n");
                            email_msg.append(msg);
                            email_msg.append("\n");
                        }
                    }

                    String email_body = email_msg.toString();
                    if (!email_body.equals("")) {
                        emailSender.sendMail("", "", "Potential Free Food", email_body, recipients);
                    }
                }catch (ParseException e){
                    e.printStackTrace();
                }
            }
        },current, 24*60*60*1000);
    }

}
