package teamcool.tradego;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseObject;

public class MainActivity extends AppCompatActivity {

    public static final String YOUR_APPLICATION_ID = "tradego";
    public static final String YOUR_CLIENT_KEY = "1997";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY); */

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId(YOUR_APPLICATION_ID).clientKey(YOUR_CLIENT_KEY)
                .server("http://tradego.herokuapp:1337/parse").build());

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

    }
}
