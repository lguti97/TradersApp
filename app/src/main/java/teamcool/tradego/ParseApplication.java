package teamcool.tradego;

import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseInstallation;
import com.parse.interceptors.ParseLogInterceptor;

import java.util.HashMap;

/**
 * Created by lguti on 7/15/16.
 */
public class ParseApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Connects to the Parse Server
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("tradego")
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://tradego.herokuapp.com/parse/").build());

        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
