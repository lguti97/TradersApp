package teamcool.tradego.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.parse.interceptors.ParseLogInterceptor;
import com.facebook.FacebookSdk;

import org.xml.sax.Parser;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.R;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.etUserName) EditText etUserName;
    @BindView(R.id.etPassword) EditText etPassword;
    final List<String> permissions = Arrays.asList("public_profile", "email");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

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
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook");
                    Toast.makeText(getApplicationContext(), "already signed up", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent (LoginActivity.this, AddItemActivity.class);
                    startActivity(i);

                } else {
                    Log.d("MyApp", "User logged in through Facebook!!!");
                    Toast.makeText(getApplicationContext(), "thanks for registering", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent (LoginActivity.this, AddItemActivity.class);
                    startActivity(i);
                }

            }
        });
    }

}
