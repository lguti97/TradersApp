package teamcool.tradego.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
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
    ArrayList<Acquaintance> acquaintances;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_import);


        final ArrayList<Acquaintance> acquaintances = new ArrayList<>();
        GraphRequest request =
                GraphRequest.newMyFriendsRequest(AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONArrayCallback() {
                            @Override
                            public void onCompleted(JSONArray jsonArray, GraphResponse response) {
                                acquaintances.addAll(Acquaintance.fromJSONArray(jsonArray));
                                RecyclerView rvAcquaintances = (RecyclerView) findViewById(R.id.rvAcquaintances);
                                AcquaintanceAdapter adapter = new AcquaintanceAdapter(getApplicationContext(), acquaintances);
                                rvAcquaintances.setAdapter(adapter);
                                //default manager we wanted.
                                rvAcquaintances.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            }
                        });

        Bundle params = new Bundle();
        params.putString("fields", "name, picture.type(large)");
        request.setParameters(params);
        request.executeAsync();

    }


}
