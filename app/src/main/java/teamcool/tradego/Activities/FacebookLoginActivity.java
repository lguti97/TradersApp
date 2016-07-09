package teamcool.tradego.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;

import teamcool.tradego.R;

public class FacebookLoginActivity extends AppCompatActivity {

    final List<String> permissions = Arrays.asList("public_profile", "email");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);

        ParseFacebookUtils.initialize(getApplicationContext());
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user == null) {
                    Log.d("MyApp", "User cancelled Facebook login");
                    Toast.makeText(getApplicationContext(), "cancelled login", Toast.LENGTH_SHORT).show();
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook");
                    Toast.makeText(getApplicationContext(), "already signed up", Toast.LENGTH_SHORT).show();

                } else {
                    Log.d("MyApp", "User logged in through Facebook!!!");
                    Toast.makeText(getApplicationContext(), "thanks for registering", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent data = new Intent();
        setResult(RESULT_OK, data);
        finish();

    }
}
