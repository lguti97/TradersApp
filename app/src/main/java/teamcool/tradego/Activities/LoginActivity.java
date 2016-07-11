package teamcool.tradego.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.interceptors.ParseLogInterceptor;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.FBGraphClient;
import teamcool.tradego.Item;
import teamcool.tradego.R;
import teamcool.tradego.User;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etUserName) EditText etUserName;
    @BindView(R.id.etPassword) EditText etPassword;


    final List<String> permissions = Arrays.asList("public_profile", "email", "user_friends", "user_location", "user_photos");
    static String currentUserFbId;
    
    AccessToken accessToken;
    FBGraphClient fbGraphClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Item.class);

        //Connects to the Parse Server
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("tradego")
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://tradego.herokuapp.com/parse/").build());

        ParseFacebookUtils.initialize(getApplicationContext());

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    //When button is pressed user will login through Facebook
    public void regUser(View view) {
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user == null) {
                    Log.d("MyApp", "User cancelled Facebook login");
                    Toast.makeText(getApplicationContext(), "cancelled login", Toast.LENGTH_SHORT).show();
                    return;
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook");
                    Toast.makeText(getApplicationContext(), "already signed up", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("MyApp", "User logged in through Facebook!!!");
                    Toast.makeText(getApplicationContext(), "thanks for registering", Toast.LENGTH_SHORT).show();
                }

                // some changes made due to code redundancy
                currentUserFbId = AccessToken.getCurrentAccessToken().getUserId();
                accessToken = AccessToken.getCurrentAccessToken();

                // store user info
                //get current user data using FB's Graph API
                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (object != null) {
                            storeUserInDatabase(object);
                        } else {
                            Log.d("DEBUG","Login - object null, response: "+response.toString());
                        }
                    }
                });
                Bundle params = new Bundle();
                params.putString("fields","id,name,location,timezone");
                request.setParameters(params);
                request.executeAsync();
                //DEBUG info: if one of the fields turns out to be empty,
                // it might be because you have no PERMISSION to access it
                // look it up in API and add more permissions to the array

                Intent i = new Intent (LoginActivity.this, FriendImportActivity.class);
                startActivity(i);
            }
        });
    }


    private void storeUserInDatabase(JSONObject object) {
        final User user = User.fromJSON(object);

        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.whereEqualTo("user_id",currentUserFbId);
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if (e == null) {
                    // if no users of this ID exist, then save it;
                    // otherwise, update it;
                    if (objects.size() == 0) {
                        user.saveInBackground();
                    } else {
                        //assume only one object exists, therefore get the 0-th index object
                        // assumption holds true based on invariant: only 1 object of this fb id exists
                        // first time user logs in, 0 of this fbid exists, created
                        // n-th time log in, if user of this fbid exists, updated
                        String objectID = objects.get(0).getObjectId();
                        fbGraphClient.updateUserData(objectID, user.getString("name"),
                                user.getString("location"),
                                user.getString("timezone"),
                                user.getItems());
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

}
