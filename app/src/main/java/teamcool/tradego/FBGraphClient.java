package teamcool.tradego;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by selinabing on 7/11/16.
 */
public class FBGraphClient {

    AccessToken accessToken;
    String currentUserFbId;

    public FBGraphClient () {
        accessToken = AccessToken.getCurrentAccessToken();
        currentUserFbId = accessToken.getUserId();
    }

    public ArrayList<User> getFriends() {
        final ArrayList<User> friends = new ArrayList<>();
        //since getting friends accesses significant amount of data
        // the fb api erecommended us to use batch request
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

    // REQUIRES: user's unique FacebookID
    // type: enum{small, normal, album, large, square}
    public String getUserProfilePicURL(String fbid, String type) {
        final StringBuilder pictureUrl = new StringBuilder();
        Bundle params = new Bundle();
        params.putBoolean("redirect",false);
        params.putString("type",type);
        new GraphRequest(accessToken, fbid+"/picture", params, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                try {
                    pictureUrl.append((String) response.getJSONObject().getJSONObject("data").get("url"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).executeAsync();
        return pictureUrl.toString();
    }

}
