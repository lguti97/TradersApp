package teamcool.tradego;

import android.app.Application;

import com.parse.Parse;
import com.parse.interceptors.ParseLogInterceptor;

/**
 * Created by lguti on 7/8/16.
 */
public class LoginApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /*
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("tradego") // should correspond to APP_ID env variable
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://tradego.herokuapp.com/parse/").build()); */


    }


}
