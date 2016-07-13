package teamcool.tradego.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONObject;

import java.util.ArrayList;

import teamcool.tradego.Adapters.AcquaintanceAdapter;
import teamcool.tradego.Clients.FBGraphClient;
import teamcool.tradego.Models.Acquaintance;
import teamcool.tradego.R;

/*
ACTIVITY USED TO IMPORT FRIENDS VIA FACEBOOK/EMAIL
 */

public class FriendImportActivity extends AppCompatActivity {
    String currentUserFbId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_import);
        FBGraphClient fbGraphClient = new FBGraphClient();
        currentUserFbId = fbGraphClient.getCurrentUserFbId();



        /*
        //TODO. Populate data into this ArrayList.
        ArrayList<Acquaintance> acquaintances = new ArrayList<>();

        RecyclerView rvAcquaintances = (RecyclerView) findViewById(R.id.rvAcquaintances);
        AcquaintanceAdapter adapter = new AcquaintanceAdapter(FriendImportActivity.this, acquaintances);
        //rvAcquaintances.setAdapter(adapter);
        //default manager we wanted.
        rvAcquaintances.setLayoutManager(new LinearLayoutManager(this)); */




    }

}
