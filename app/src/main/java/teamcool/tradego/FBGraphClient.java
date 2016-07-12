package teamcool.tradego;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by selinabing on 7/11/16.
 */
public class FBGraphClient {

    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    String currentUserFbId = AccessToken.getCurrentAccessToken().getUserId();

    public ArrayList<User> getFriends() {
        final ArrayList<User> friends = new ArrayList<>();
        //since getting friends accesses significant amount of data
        // the fb api recommended us to use batch request
        GraphRequestBatch batch = new GraphRequestBatch(
                GraphRequest.newMyFriendsRequest(accessToken,
                        new GraphRequest.GraphJSONArrayCallback() {
                            @Override
                            public void onCompleted(JSONArray objects, GraphResponse response) {
                                friends.addAll(User.fromJSONArray(objects));
                            }
                        }));

        return friends;
    }

    //Empty Constructor?
    public FBGraphClient () {
    }

    public void updateUserData(String objectID, final String username, final String location, final String timezone, final List<Item> items) {
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.getInBackground(objectID, new GetCallback<User>() {
            @Override
            public void done(User object, ParseException e) {
                if (e == null) {
                    if (username != null) {
                        object.setUsername(username);
                    }
                    if(location != null) {
                        object.setLocation(location);
                    }
                    if (timezone != null) {
                        object.setTimezone(timezone);
                    }
                    if (items != null) {
                        object.setItems(items);
                    }
                    object.saveInBackground();
                } else {
                    Log.d("DEBUG","updateUserData, ParseException code: "+e.getCode());
                    e.printStackTrace();
                }
            }
        });
    }

}
