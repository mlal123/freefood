import com.restfb.*;
import com.restfb.types.Event;
import com.restfb.types.Page;
import com.restfb.types.Post;

import java.io.IOException;
import java.util.*;

public class facebook {

    public static FacebookClient getFBClient(String accessToken){
        FacebookClient fbClient = new DefaultFacebookClient(accessToken);
        return fbClient;
    }

    public static Connection<Page> get_my_liked_pages(FacebookClient fbClient){
        Connection<Page> pages = fbClient.fetchConnection("me/likes", Page.class);
        return pages;
    }

    public static List<Page> get_needed_pages(Connection<Page> pages, List<String> page_names){
        List<Page> pageList = new ArrayList<>();

        for (List<Page> feedPage : pages){
            for (Page page : feedPage){
                if (page_names.contains(page.getName())){
                    pageList.add(page);
                }
            }
        }

        return pageList;
    }
    private FacebookClient.AccessToken getFacebookUserToken(String code, String redirectUrl) throws IOException {
        String appId = "YOUR_APP_ID";
        String secretKey = "YOUR_SECRET_KEY";

        WebRequestor wr = new DefaultWebRequestor();
        WebRequestor.Response accessTokenResponse = wr.executeGet(
                "https://graph.facebook.com/oauth/access_token?client_id=" + appId + "&redirect_uri=" + redirectUrl
                        + "&client_secret=" + secretKey + "&code=" + code);

        return DefaultFacebookClient.AccessToken.fromQueryString(accessTokenResponse.getBody());
    }

    public static Map<Date, String> get_FB_posts(FacebookClient fbClient, Page fbPage, String page_name, Date date){
        Map<Date, String> posts = new HashMap<>();
        Connection<Post> postFeed = fbClient.fetchConnection(fbPage.getId()+"/feed", Post.class, Parameter.with("since", date));
        if (postFeed == null){
            return posts;
        }
        for (List<Post> postPage : postFeed){
            for(Post aPost : postPage){
                String post_msg = aPost.getMessage();
                if (FoodHelper.check_for_free_food(post_msg)){
                    posts.put(aPost.getCreatedTime(), post_msg);
                }

            }
        }

        return posts;
    }

    public static void get_FB_Events(FacebookClient fbClient, String page_name){
        Page fbPage = fbClient.fetchObject(page_name, Page.class);
        Connection<Event> eventList = fbClient.fetchConnection("search", Event.class, Parameter.with("q", "york"), Parameter.with("type", "event"));
        for (List<Event> eventPage : eventList){
            System.out.println(eventPage);
            for (Event e : eventPage){
                System.out.println(e.getDescription());
            }
        }
    }

    public static String createDescription(Map<Date, String> feed, Page page){
        if (feed.size() == 0){
            return "empty";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry entry : feed.entrySet()){
            String date = entry.getKey().toString().substring(0,10);
            stringBuilder.append(date).append(": ").append(entry.getValue());
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }


}
