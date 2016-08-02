package teamcool.tradego.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.Clients.FBGraphClient;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Models.Acquaintance;
import teamcool.tradego.Models.Friend;
import teamcool.tradego.Models.Item;
import teamcool.tradego.Models.User;
import teamcool.tradego.Models.WishItem;
import teamcool.tradego.R;

public class LoginActivity extends AppCompatActivity {

    boolean initialized = false;
    @BindView(R.id.tvLogo)
    TextView tvLogo;

    final List<String> permissions = Arrays.asList("public_profile", "email", "user_friends", "user_location", "user_photos");
    static String currentUserFbId;

    //GraphClient and AccessToken needed to retrieve Facebook data.
    AccessToken accessToken;
    FBGraphClient fbGraphClient;
    ParseClient parseClient;
    User another_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.splashScreenTheme);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        ParseObject.registerSubclass(Item.class);
        ParseObject.registerSubclass(Acquaintance.class);
        ParseObject.registerSubclass(Friend.class);
        ParseObject.registerSubclass(WishItem.class);

        //Parse Initialization done in the ParseApplication Class.

        if (!initialized) {
            FacebookSdk.sdkInitialize(this);
            ParseFacebookUtils.initialize(getApplicationContext());
            initialized = true;
        }

        //Testing purposes


        //Customizing Font
        Typeface font0 = Typeface.createFromAsset(getApplicationContext().getAssets(), "Oranienbaum.ttf");
        tvLogo.setTypeface(font0);

        /*
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        */

    }

    //Required method. No other activity will be launched though
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    //When button is pressed user will login through Facebook
    public void regUser(View view) {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(final ParseUser user, ParseException e) {
                if (e == null) {
                    if (user == null) {
                        Log.d("MyApp", "User cancelled Facebook login");
                        return;
                    } else if (user.isNew()) {
                        Log.d("MyApp", "User signed up and logged in through Facebook");
                    } else {
                        Log.d("MyApp", "User logged in through Facebook!!!");
                    }
                } else {
                    e.printStackTrace();
                }

                fbGraphClient = new FBGraphClient();
                parseClient = new ParseClient();
                currentUserFbId = fbGraphClient.getCurrentUserFbId();
                accessToken = fbGraphClient.getAccessToken();

                //Deserializes the ParseUser in order to have JSONObjects that are retrievable.
                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (object != null) {
                            another_user = User.fromJSON(object, user, accessToken.getUserId());
                            if (user.isNew() || user.getString("global_id")==null || user.getString("global_id").equals("")) {
                                String profile_url = "https://www.facebook.com/app_scoped_user_id/" + user.getString("user_id");
                                final WebView webView = new WebView(getApplicationContext());
                                webView.getSettings().setJavaScriptEnabled(true);
                                webView.loadUrl(profile_url);

                                webView.setWebViewClient(new WebViewClient() {
                                    @Override
                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                        if (!url.contains("app_scoped_user_id")) {
                                            user.put("global_id", extractGlobalId(url));
                                            webView.destroy();
                                            if (user.isNew()) {
                                                Intent i = new Intent(LoginActivity.this, FriendImportActivity.class);
                                                i.putExtra("initial", true);
                                                startActivity(i);
                                            } else {
                                                Intent i = new Intent(LoginActivity.this, NewsFeedActivity.class);
                                                startActivity(i);
                                            }
                                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                        }
                                        return false;
                                    }
                                });
                                setContentView(webView);
                            } else {
                                Intent i = new Intent(LoginActivity.this, NewsFeedActivity.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            }
                        } else {
                        }
                    }
                });

                Bundle params = new Bundle();
                params.putString("fields", "id,link,name,location,timezone,picture.type(large)");
                request.setParameters(params);
                request.executeAsync();
                //DEBUG info: if one of the fields turns out to be empty,
                // it might be because you have3 no PERMISSION to access it
                // look it up in API and add more permissions to the array


            }

        });
    }

    public String extractGlobalId(String url) {
        return url.substring(url.indexOf(".com/") + 5, url.lastIndexOf("?"));
    }

}
