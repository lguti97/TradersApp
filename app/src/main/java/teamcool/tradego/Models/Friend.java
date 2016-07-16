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

    public Friend(String name, String profile_url, String id){
        super();
        setName(name);
        setProfile_url(profile_url);
        setUserID(id);
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

    //Deserializes JSON objects into the Acquaintance model
    //How do we do this though?
    public static Friend fromJSON(JSONObject object){
        Friend friend = null;

        return friend;
    }

    //populates ArrayList of type Acquaintance
    //Also goes into the JSONArrays to get JSONObjects. Uses previous method
    public static ArrayList<Friend> fromJSONArray(JSONArray jsonArray){
        ArrayList<Friend> friends = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            try {
                JSONObject jsonFriend = jsonArray.getJSONObject(i);
                Friend friend = Friend.fromJSON(jsonFriend);
                if (friend != null) {
                    friends.add(friend);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return friends;
    }

}
