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
    String UserName;
    String Password;
    int RESULT_CODE = 20;


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


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "YOU LOGGED IN!", Toast.LENGTH_SHORT).show();
    }
    /*
    //Creates a Temporary User
    public void userCreate (){
        ParseUser user = new ParseUser();
        user.setPassword(Password);
        user.setUsername(UserName);
        user.setEmail("gutierrez.luis97@gmail.com");
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null){
                    //TODO. Create an intent here to go to the friend import activity
                    Toast.makeText(getApplicationContext(), "Does this work", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO. Create another page for error.
                }

            }
        });
    }
    */



    //When button is pressed, user can register. Will go into the User Object class in the parse server
    public void regUser(View view) {
        Intent i = new Intent(LoginActivity.this, FacebookLoginActivity.class);
        startActivityForResult(i, RESULT_CODE);
    }



}
