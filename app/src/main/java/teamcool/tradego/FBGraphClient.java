package teamcool.tradego;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

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

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public String getCurrentUserFbId() {
        return currentUserFbId;
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
