package classes;

import android.app.Application;

/**
 * Created by root on 08.03.18.
 */

public class Constants extends Application{
    public static final String BASE_URL = "https://api.instagram.com/";
    public static final String CLIENT_ID = "0580c5c695b346208b00e6eff8833a1a";
    public static final String REDIRECT_URI = "https://instagram.com/";
    public static final String GOOGLE_API_KEY = "AIzaSyApW_Q2fGmyF-btB2MtaJ-S3j6Zu85bZbg";
    public static final String cx ="000845837705475970075:xmmi7g9uofk";
    public static final String google_api = "https://www.googleapis.com/customsearch/v1?key="+GOOGLE_API_KEY+"&cx="+cx+"&searchType=image&q=tattoo+";
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
