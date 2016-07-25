package teamcool.tradego.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import teamcool.tradego.R;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.tvLogo) TextView tvLogo;


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

        //Parse Initialization done in the ParseApplication Class.

        FacebookSdk.sdkInitialize(this);
        ParseFacebookUtils.initialize(getApplicationContext());

        //Testing purposes


        //Customizing Font
        Typeface font0 = Typeface.createFromAsset(getApplicationContext().getAssets(), "Oranienbaum.ttf");
        tvLogo.setTypeface(font0);

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
                        } else {
                            Log.d("DEBUG","Login - object null, response: "+response.toString());
                        }
                    }
                });

                Bundle params = new Bundle();
                params.putString("fields","id,name,location,timezone,picture.type(large)");
                request.setParameters(params);
                request.executeAsync();
                //DEBUG info: if one of the fields turns out to be empty,
                // it might be because you have3 no PERMISSION to access it
                // look it up in API and add more permissions to the array

                Intent i = new Intent (LoginActivity.this, FriendImportActivity.class);

                startActivity(i);
            }
        });
    }
}
