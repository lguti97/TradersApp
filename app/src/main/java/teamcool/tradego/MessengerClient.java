package teamcool.tradego;

/**
 * Created by selinabing on 7/8/16.
 */

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FacebookApi;

public class MessengerClient extends OAuthBaseClient {

    public static final Class<? extends Api> REST_API_CLASS = FacebookApi.class;
    public static final String REST_URL = ""; // base API URL
    public static final String REST_CONSUMER_KEY = "528271197361909";
    public static final String REST_CONSUMER_SECRET = "e7f215a374dc63a35019c4914a0b44b7"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://tradegoapp"; // Change this (here and in manifest)

    public MessengerClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    //we might not actually need to authenticate ourselves, but keep that for now (?)

}