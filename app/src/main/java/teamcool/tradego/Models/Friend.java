package teamcool.tradego.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lguti on 7/15/16.
 */
@ParseClassName("Friend")
public class Friend extends ParseObject {
    //Friend will be parseObject

    public Friend () {
        super();
    }

    public Friend(String name, String Profile, String ID){
        super();
        setName(name);
        setProfile_url(Profile);
        setUserID(ID);
    }

    // Will be used to access fields from Parse Database
    public String getName (){
        return getString("name");
    }

    public String getProfile_url() {
        return getString("picture");
    }

    public String getUserID(){
        return getString("userID");
    }

    // To modify field values in Parse Database

    public void setName(String name){
        put("name", name);
    }

    public void setProfile_url(String profile_url){
        put("picture", profile_url);
    }

    public void setUserID(String id){
        put("userID", id);
    }


    //TO CONNECT WITH USER OBJECT

    //Get User for this acquaintance
    public ParseUser getUser() {
        return getParseUser("owner");
    }

    //Associate each acquaintance with the user
    public void setOwner(ParseUser user) {
        put("owner", user);
    }

    public static Friend fromAcquaintance (String name, String profile, String ID) {
        Friend friend = new Friend(name, profile, ID);
        friend.setOwner(ParseUser.getCurrentUser());
        friend.saveInBackground();
        return friend;
    }

}
