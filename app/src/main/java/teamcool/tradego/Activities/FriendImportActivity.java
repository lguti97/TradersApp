package teamcool.tradego.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.ParseCloud;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import teamcool.tradego.Adapters.AcquaintanceAdapter;
import teamcool.tradego.Clients.FBGraphClient;
import teamcool.tradego.Clients.ParseClient;
import teamcool.tradego.Fragments.AlertSwipeFragment;
import teamcool.tradego.Models.Acquaintance;
import teamcool.tradego.R;
import teamcool.tradego.SimpleItemTouchHelperCallback;

/*
ACTIVITY USED TO IMPORT FRIENDS VIA FACEBOOK/EMAIL
 */

public class FriendImportActivity extends AppCompatActivity {

    @BindView(R.id.skipFriendImport) TextView skipFriendImport;
    boolean initial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_import);

        ButterKnife.bind(this);

        //Doing some channel testing here. Why is this not working?
        HashMap<String, String> test = new HashMap<>();
        test.put("channel", "testing");
        ParseCloud.callFunctionInBackground("pushChannelTest", test);

        //Show Alert Dialog Fragment
        showAlertDialog();


        if (getIntent() != null) {
            initial = getIntent().getBooleanExtra("initial",false);
        }

        if (initial) {
            skipFriendImport.setText(getResources().getString(R.string.skip));
        } else {
            skipFriendImport.setText("Done");
        }

        //This is where I make the acquaintance objects.
        final ArrayList<Acquaintance> acquaintances = new ArrayList<>();
        GraphRequest request =
                GraphRequest.newMyFriendsRequest(AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONArrayCallback() {
                            @Override
                            public void onCompleted(JSONArray jsonArray, GraphResponse response) {
                                acquaintances.addAll(Acquaintance.fromJSONArray(jsonArray));
                                RecyclerView rvAcquaintances = (RecyclerView) findViewById(R.id.rvAcquaintances);
                                AcquaintanceAdapter adapter = new AcquaintanceAdapter(getApplicationContext(), acquaintances, rvAcquaintances);
                                ItemTouchHelper.Callback callback =
                                        new SimpleItemTouchHelperCallback(adapter);
                                ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                                touchHelper.attachToRecyclerView(rvAcquaintances);
                                adapter.notifyDataSetChanged();
                                rvAcquaintances.setAdapter(adapter);
                                rvAcquaintances.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            }
                        });

        Bundle params = new Bundle();
        params.putString("fields", "name, picture.type(square).width(400).height(400), id");
        request.setParameters(params);
        request.executeAsync();
    }

    //To skip the import activity part if you choose to do so.
    public void skipActivity(View view) {
        if (initial) {
            Intent i = new Intent(FriendImportActivity.this, AddItemActivity.class);
            i.putExtra("initial", true);
            startActivity(i);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
        } else {
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    }

    public void showAlertDialog() {
        AlertSwipeFragment alertDialog = AlertSwipeFragment.newInstance("Welcome!");
        alertDialog.show(getFragmentManager(), "fragment_alert");
    }

}
