package teamcool.tradego.Clients;

import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import teamcool.tradego.Models.Acquaintance;
import teamcool.tradego.Models.User;

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

}
